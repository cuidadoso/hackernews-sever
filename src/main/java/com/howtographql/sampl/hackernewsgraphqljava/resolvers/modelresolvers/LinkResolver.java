package com.howtographql.sampl.hackernewsgraphqljava.resolvers.modelresolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.VoteSpecifications.voteByLink;

@Component
@RequiredArgsConstructor
public class LinkResolver implements GraphQLResolver<Link> {
    @Qualifier("userService")
    private final AbstractService userService;
    @Qualifier("voteService")
    private final AbstractService voteService;

    public User postedBy(Link link) {
        if (link.getUserId() == null) {
            return null;
        }
        return (User) userService.findOne(link.getUserId());
    }

    public User createdBy(Link link) {
        if (link.getCreatedBy() == null) {
            return null;
        }
        return (User) userService.findOne(link.getCreatedBy());
    }

    public User updatedBy(Link link) {
        if (link.getUpdatedBy() == null) {
            return null;
        }
        return (User) userService.findOne(link.getUpdatedBy());
    }

    public User deletedBy(Link link) {
        if (link.getDeletedBy() == null) {
            return null;
        }
        return (User) userService.findOne(link.getDeletedBy());
    }

    @SuppressWarnings("unchecked")
    public List<Vote> votes(Link link) {
        return voteService.findAll(voteByLink(link.getId()));
    }
}
