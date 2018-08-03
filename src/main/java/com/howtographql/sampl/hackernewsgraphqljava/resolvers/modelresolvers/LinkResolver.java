package com.howtographql.sampl.hackernewsgraphqljava.resolvers.modelresolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import com.howtographql.sampl.hackernewsgraphqljava.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkResolver implements GraphQLResolver<Link> {
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public User postedBy(Link link) {
        if (link.getUserId() == null) {
            return null;
        }
        return userRepository.findOne(link.getUserId());
    }

    public List<Vote> votes(Link link) {
        return voteRepository.findAllByLinkId(link.getId());
    }
}
