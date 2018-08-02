package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.LinkFilter;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
    public List<Link> allLinks(LinkFilter filter) {
        if(filter != null && filter.getUrlContains() != null && filter.getDescriptionContains() != null) {
            return linkRepository.findAllByUrlContainsAndDescriptionContains(filter.getUrlContains(), filter.getDescriptionContains());
        }
        if(filter != null && filter.getUrlContains() != null) {
            // return makeList(linkRepository.findAll(LinkSpecifications.linkByUrl(filter.getUrlContains())));
            return linkRepository.findAllByUrlContains(filter.getUrlContains());
        }
        if(filter != null && filter.getDescriptionContains() != null) {
            // return makeList(linkRepository.findAll(LinkSpecifications.linkByDescription(filter.getDescriptionContains())));
            return linkRepository.findAllByDescriptionContains(filter.getDescriptionContains());
        }
        return linkRepository.findAll();
    }

    public Link link(Long id) {
        return linkRepository.findOne(id);
    }

    // User query resolvers
    public List<User> allUsers(String email) {
        if (!StringUtils.isBlank(email)) {
            // return makeList(userRepository.findAll(UserSpecifications.userByEmail(email)));
            return userRepository.findAllByEmailContains(email);
        }
        return userRepository.findAll();
    }

    public User user(Long id) {
        return userRepository.findOne(id);
    }

    // Vote query resolvers
    public List<Vote> allVotes(Long userId, Long linkId) {
        if (userId != null && linkId != null) {
            return voteRepository.findAllByUserIdAndLinkId(userId, linkId);
        }
        if (userId != null) {
            return voteRepository.findAllByUserId(userId);
        }
        if (linkId != null) {
            return voteRepository.findAllByLinkId(linkId);
        }
        return voteRepository.findAll();
    }

    public Vote vote(Long id) {
        return voteRepository.findOne(id);
    }
}
