package com.howtographql.sampl.hackernewsgraphqljava.resolvers.modelresolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.service.LinkService;
import com.howtographql.sampl.hackernewsgraphqljava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteResolver implements GraphQLResolver<Vote> {
    @Qualifier("userService")
    private final UserService userService;
    @Qualifier("linkService")
    private final LinkService linkService;

    public User user(Vote vote) {
        if (vote.getUserId() == null) {
            return null;
        }
        return userService.findOne(vote.getUserId());
    }

    public Link link(Vote vote) {
        return linkService.findOne(vote.getLinkId());
    }

    public User createdBy(Vote vote) {
        if (vote.getCreatedBy() == null) {
            return null;
        }
        return userService.findOne(vote.getCreatedBy());
    }

    public User updatedBy(Vote vote) {
        if (vote.getUpdatedBy() == null) {
            return null;
        }
        return userService.findOne(vote.getUpdatedBy());
    }

    public User deletedBy(Vote vote) {
        if (vote.getDeletedBy() == null) {
            return null;
        }
        return userService.findOne(vote.getDeletedBy());
    }
}
