package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.publisher.LinkPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Log
@Component
@RequiredArgsConstructor
public class Subscription implements GraphQLSubscriptionResolver {
    private final LinkPublisher linkPublisher;

    /*public Link newLink() {
        log.info("Subscription - newLink");
        return null;
    }*/

    Publisher<Link> newLink() {
        return linkPublisher.getPublisher();
    }
}
