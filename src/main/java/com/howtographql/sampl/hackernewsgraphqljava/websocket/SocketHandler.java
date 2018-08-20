package com.howtographql.sampl.hackernewsgraphqljava.websocket;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.howtographql.sampl.hackernewsgraphqljava.publisher.LinkPublisher;
import com.howtographql.sampl.hackernewsgraphqljava.publisher.VotePublosher;
import com.howtographql.sampl.hackernewsgraphqljava.resolvers.scalars.DateTime;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.UID;
import static com.howtographql.sampl.hackernewsgraphqljava.util.JsonKit.*;
import static com.howtographql.sampl.hackernewsgraphqljava.websocket.OperationMessage.*;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler implements SubProtocolCapable {
    private final LinkPublisher linkPublisher;
    private final VotePublosher votePublosher;

    private static final Map<String, WebSocketSession> USER_SOCKET_SESSION_MAP = Maps.newConcurrentMap();
    private HashMap<WebSocketSession, List<String>> subscriptionMap = new HashMap<>();
    private final AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();
    private final String[] subProtocols = {"graphql-ws"};

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        OperationMessage operationMessage = fromJson(message.getPayload(), OperationMessage.class);
        switch (operationMessage.getType()) {
            case GQL_CONNECTION_TERMINATE:
                unsubscribe(session, operationMessage.getId());
                session.close();
                break;
            case GQL_START:
                subscribe(session, operationMessage.getId());
                GraphQLSchema schema = getGraphQLSchema();
                GraphQL graphql = GraphQL.newGraphQL(schema).build();
                ExecutionInput executionInput = fromJson(operationMessage.getPayload(), ExecutionInput.class);
                ExecutionResult result = graphql.execute(executionInput);
                if (result.getData() instanceof Publisher) handlePublisher(session, result, operationMessage.getId());
                break;
            case GQL_STOP:
                unsubscribe(session, operationMessage.getId());
                break;
            case GQL_CONNECTION_INIT:
                session.sendMessage(new TextMessage(toJsonString(new OperationMessage(GQL_CONNECTION_ACK))));
                break;
        }
    }

    private void subscribe(WebSocketSession session, String id) {
        List<String> ids = subscriptionMap.get(session);
        if (ids != null && !ids.contains(id)) {
            ids.add(id);
        } else {
            subscriptionMap.put(session, Lists.newArrayList(id));
        }
    }

    private void unsubscribe(WebSocketSession session, String id) {
        List<String> ids = subscriptionMap.get(session);
        if (ids != null) {
            ids.remove(id);
        }
    }

    private void handlePublisher(WebSocketSession session, ExecutionResult result, String id) {
        Publisher<ExecutionResult> stream = result.getData();
        Subscriber<ExecutionResult> subscriber = new Subscriber<ExecutionResult>() {
            @Override
            public void onSubscribe(Subscription s) {
                List<String> subscribingIds = subscriptionMap.get(session);
                if (subscribingIds != null && subscribingIds.contains(id)) {
                    subscriptionRef.set(s);
                    try {
                        session.sendMessage(new TextMessage(dataJsonString(id)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    request(1);
                }
            }

            @Override
            public void onNext(ExecutionResult executionResult) {
                List<String> subscribingIds = subscriptionMap.get(session);
                if (subscribingIds != null && subscribingIds.contains(id)) {
                    try {
                        if (executionResult.getErrors().isEmpty())
                            session.sendMessage(new TextMessage(toJsonString(new OperationMessage(toJsonTree(executionResult), id, GQL_DATA))));
                        else
                            session.sendMessage(new TextMessage(toJsonString(new OperationMessage(toJsonTree(executionResult), id, GQL_ERROR))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    request(1);
                }
            }

            @Override
            public void onError(Throwable t) {
                try {
                    session.sendMessage(new TextMessage(toJsonString(new OperationMessage(toJsonTree(t), id, GQL_ERROR))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {
                try {
                    session.sendMessage(new TextMessage(toJsonString(new OperationMessage(id, GQL_COMPLETE))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        stream.subscribe(subscriber);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uid = (String) session.getAttributes().get(UID);
        USER_SOCKET_SESSION_MAP.putIfAbsent(uid, session);
    }

    private GraphQLSchema getGraphQLSchema() {
        //
        // reads a file that provides the schema types
        //
        Reader streamReader = loadSchemaFile();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(streamReader);

        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .scalar(new DateTime())
                .type(newTypeWiring("Subscription")
                        .dataFetcher(OPERATION_NEW_LINK, subscriptionFetcher(OPERATION_NEW_LINK)))
                .type(newTypeWiring("Subscription")
                        .dataFetcher(OPERATION_NEW_VOTE, subscriptionFetcher(OPERATION_NEW_VOTE)))
                .build();

        return new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
    }

    private DataFetcher subscriptionFetcher(String subscription) {
        return environment -> {
            switch (subscription) {
                case OPERATION_NEW_LINK:
                    return linkPublisher.getPublisher();
                case OPERATION_NEW_VOTE:
                    return votePublosher.getPublisher();
                default:
                    return null;
            }
        };
    }


    private Reader loadSchemaFile() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("graphql/schema.graphqls");
        return new InputStreamReader(stream);
    }

    private void request(int n) {
        Subscription subscription = subscriptionRef.get();
        if (subscription != null) {
            subscription.request(n);
        }
    }

    @Override
    public List<String> getSubProtocols() {
        return Arrays.asList(subProtocols);
    }
}
