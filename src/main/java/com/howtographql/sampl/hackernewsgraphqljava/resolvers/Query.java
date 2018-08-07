package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.LinkFilter;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Collections.makeList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Component
@RequiredArgsConstructor
public class Query implements GraphQLQueryResolver {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    // Link query resolvers
    public List<Link> links(LinkFilter filter, int page, int size, String orderBy) {
        Pageable pageable = new PageRequest(page, size, orders(orderBy, Link.class));

        if(filter != null && filter.getUrlContains() != null && filter.getDescriptionContains() != null) {
            return linkRepository.findAllByUrlContainsOrDescriptionContains(filter.getUrlContains(), filter.getDescriptionContains(), pageable);
        }
        if(filter != null && filter.getUrlContains() != null) {
            // return makeList(linkRepository.findAll(LinkSpecifications.linkByUrl(filter.getUrlContains())));
            return linkRepository.findAllByUrlContains(filter.getUrlContains(), pageable);
        }
        if(filter != null && filter.getDescriptionContains() != null) {
            // return makeList(linkRepository.findAll(LinkSpecifications.linkByDescription(filter.getDescriptionContains())));
            return linkRepository.findAllByDescriptionContains(filter.getDescriptionContains(), pageable);
        }
        return linkRepository.findAll(pageable).getContent();
    }

    public Link link(Long id) {
        return linkRepository.findOne(id);
    }

    // User query resolvers
    public List<User> users(String email, int page, int size, String orderBy) {
        Pageable pageable = new PageRequest(page, size, orders(orderBy, User.class));
        if (!StringUtils.isBlank(email)) {
            // return makeList(userRepository.findAll(UserSpecifications.userByEmail(email)));
            return userRepository.findAllByEmailContains(email, pageable);
        }
        return userRepository.findAll(pageable).getContent();
    }

    public User user(Long id) {
        return userRepository.findOne(id);
    }

    // Vote query resolvers
    public List<Vote> votes(Long userId, Long linkId, int page, int size, String orderBy) {
        Pageable pageable = new PageRequest(page, size, orders(orderBy, Vote.class));
        if (userId != null && linkId != null) {
            return voteRepository.findAllByUserIdAndLinkId(userId, linkId, pageable);
        }
        if (userId != null) {
            return voteRepository.findAllByUserId(userId, pageable);
        }
        if (linkId != null) {
            return voteRepository.findAllByLinkId(linkId, pageable);
        }
        return voteRepository.findAll(pageable).getContent();
    }

    public Vote vote(Long id) {
        return voteRepository.findOne(id);
    }

    private Sort orders(String orderBy, Class clazz) {
        if (orderBy != null) {
            String[] orderParams = orderBy.split("_");
            Direction direction = Direction.fromStringOrNull(orderParams[1]);
            try {
                Field field = clazz.getField(orderParams[0]);
                return new Sort(new Sort.Order(direction == null ? ASC : DESC, field.getName()));
            } catch (NoSuchFieldException e) {
                // TODO logging
                return new Sort(new Sort.Order(direction == null ? ASC : DESC, "id"));
            }
        } else {
            return null;
        }
    }
}
