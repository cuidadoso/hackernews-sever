package com.howtographql.sampl.hackernewsgraphqljava.resolvers.modelresolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.repository.VoteRepository;
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

    private final VoteRepository voteRepository;

    public User postedBy(Link link) {
        if (link.getUserId() == null) {
            return null;
        }
        return (User) userService.findOne(link.getUserId());
    }

    public List<Vote> votes(Link link) {
        return voteService.findAll(voteByLink(link.getId()));
    }
}
