package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Collections.makeList;

@Component
@RequiredArgsConstructor
public class Query implements GraphQLQueryResolver {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    // Link query resolvers
    public List<Link> allLinks() {
        return linkRepository.findAll();
    }

    public Link link(Long id) {
        return linkRepository.findOne(id);
    }

    public List<Link> linksByUrl(String url) {
        // return makeList(linkRepository.findAll(LinkSpecifications.linkByUrl(url)));
        return linkRepository.findAllByUrlContains(url);
    }

    // User query resolvers
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User user(Long id) {
        return userRepository.findOne(id);
    }

    public List<User> usersByEmail(String email) {
        // return makeList(userRepository.findAll(UserSpecifications.userByEmail(email)));
        return userRepository.findAllByEmailContains(email);
    }

    // Vote query resolvers
    public List<Vote> allVotes() {
        return voteRepository.findAll();
    }

    public Vote vote(Long id) {
        return voteRepository.findOne(id);
    }

    public List<Vote> votesByUser(Long userId) {
        return voteRepository.findAllByUserId(userId);
    }

    public List<Vote> votesByLink(Long linkId) {
        return voteRepository.findAllByLinkId(linkId);
    }

    public Vote voteByUserAndLink(Long userId, Long linkId) {
        return voteRepository.findByUserIdAndLinkId(userId, linkId);
    }
}
