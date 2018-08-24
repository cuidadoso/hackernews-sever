package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.service.LinkService;
import com.howtographql.sampl.hackernewsgraphqljava.service.UserService;
import com.howtographql.sampl.hackernewsgraphqljava.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Log
@Component
@RequiredArgsConstructor
public class Query implements GraphQLQueryResolver {
    @Qualifier("linkService")
    private final LinkService linkService;
    @Qualifier("userService")
    private final UserService userService;
    @Qualifier("voteService")
    private final VoteService voteService;

    // Link query resolvers
    public Links links(List<Filter> filter, int page, int size, List<OrderBy> orderBy) {
        return linkService.findAll(filter, page, size, orderBy);
    }

    public Link link(Long id) {
        return linkService.findOne(id);
    }

    // User query resolvers
    public Users users(List<Filter> filter, int page, int size, List<OrderBy> orderBy) {
        return userService.findAll(filter, page, size, orderBy);
    }

    public User user(Long id) {
        return userService.findOne(id);
    }

    // Vote query resolvers
    public Votes votes(List<Filter> filter, int page, int size, List<OrderBy> orderBy) {
        return voteService.findAll(filter, page, size, orderBy);
    }

    public Vote vote(Long id) {
        return voteService.findOne(id);
    }
}
