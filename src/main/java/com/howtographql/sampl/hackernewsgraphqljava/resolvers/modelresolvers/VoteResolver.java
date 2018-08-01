package com.howtographql.sampl.hackernewsgraphqljava.resolvers.modelresolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteResolver implements GraphQLResolver<Vote> {
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;

    public User user(Vote vote) {
        return userRepository.findOne(vote.getUserId());
    }

    public Link link(Vote vote) {
        return linkRepository.findOne(vote.getLinkId());
    }
}
