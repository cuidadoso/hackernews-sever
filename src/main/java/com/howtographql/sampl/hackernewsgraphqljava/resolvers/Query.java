package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.LinkSpecifications.*;
import static com.howtographql.sampl.hackernewsgraphqljava.specifications.UserSpecifications.userByEmail;
import static com.howtographql.sampl.hackernewsgraphqljava.specifications.VoteSpecifications.*;

@Log
@Component
@RequiredArgsConstructor
public class Query implements GraphQLQueryResolver {
    @Qualifier("linkService")
    private final AbstractService linkService;
    @Qualifier("userService")
    private final AbstractService userService;
    @Qualifier("voteService")
    private final AbstractService voteService;

    // Link query resolvers
    public Links links(LinkFilter filter, int page, int size, String orderBy) {
        BooleanExpression predicate = null;
        if(filter != null && filter.getUrlContains() != null && filter.getDescriptionContains() != null) {
            predicate = linkByUrlOrDescription(filter.getUrlContains(), filter.getDescriptionContains());
        } else if(filter != null && filter.getUrlContains() != null) {
            predicate = linkByUrl(filter.getUrlContains());
        } else if(filter != null && filter.getDescriptionContains() != null) {
            predicate = linkByDescription(filter.getDescriptionContains());
        }
        return (Links) linkService.findAll(predicate, page, size, orderBy);

    }

    public Link link(Long id) {
        return (Link) linkService.findOne(id);
    }

    // User query resolvers
    public Users users(String email, int page, int size, String orderBy) {
        BooleanExpression predicate = null;
        if (!StringUtils.isBlank(email)) {
            predicate = userByEmail(email);
        }

        return (Users) userService.findAll(predicate, page, size, orderBy);
    }

    public User user(Long id) {
        return (User) userService.findOne(id);
    }

    // Vote query resolvers
    public Votes votes(Long userId, Long linkId, int page, int size, String orderBy) {
        BooleanExpression predicate = null;

        if (userId != null && linkId != null) {
            predicate = voteByLinkAndUser(linkId, userId);
        } else if (userId != null) {
            predicate = voteByUser(userId);
        } else if (linkId != null) {
            predicate = voteByLink(linkId);
        }
        return (Votes) voteService.findAll(predicate, page, size, orderBy);
    }

    public Vote vote(Long id) {
        return (Vote) voteService.findOne(id);
    }
}
