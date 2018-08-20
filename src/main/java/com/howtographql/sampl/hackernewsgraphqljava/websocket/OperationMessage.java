package com.howtographql.sampl.hackernewsgraphqljava.websocket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.howtographql.sampl.hackernewsgraphqljava.util.JsonKit.toJsonString;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OperationMessage {
    private JsonElement payload;
    private String id;
    private String type;

    public static final String GQL_CONNECTION_INIT = "connection_init"; // client->server
    public static final String GQL_CONNECTION_ACK = "connection_ack"; // server->client
    public static final String GQL_CONNECTION_ERROR = "connection_error"; // server->client
    public static final String GQL_CONNECTION_KEEP_ALIVE = "ka"; // server->client
    public static final String GQL_CONNECTION_TERMINATE = "connection_terminate"; // client->server


    public static final String GQL_START = "start";
    public static final String GQL_DATA = "data";
    public static final String GQL_ERROR = "error";
    public static final String GQL_COMPLETE = "complete";
    public static final String GQL_STOP = "stop";

    public static final String OPERATION_NEW_LINK = "newLink";
    public static final String OPERATION_NEW_VOTE = "newVote";

    public OperationMessage(String type) {
        this.type = type;
    }

    public OperationMessage(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public static String dataJsonString(String id) {
        OperationMessage dataMessage = OperationMessage.builder()
                .payload(new JsonObject())
                .id(id)
                .type(GQL_DATA)
                .build();
        return toJsonString(dataMessage);
    }
}
