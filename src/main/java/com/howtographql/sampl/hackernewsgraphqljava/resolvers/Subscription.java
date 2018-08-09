package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Log
@Component
@RequiredArgsConstructor
public class Subscription implements GraphQLSubscriptionResolver {
    private final LinkRepository linkRepository;

    public Link newLink() {
        log.info("Subscription - newLink");
        return null;
    }
}
