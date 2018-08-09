package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
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

@Log
@Component
@RequiredArgsConstructor
public class Query implements GraphQLQueryResolver {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    // Link query resolvers
    public Links links(LinkFilter filter, int page, int size, String orderBy) {
        log.info("Query - links");
        Pageable pageable = new PageRequest(page, size, orders(orderBy, Link.class));
        Page<Link> links;

        if(filter != null && filter.getUrlContains() != null && filter.getDescriptionContains() != null) {
            links = linkRepository.findAllByUrlContainsOrDescriptionContains(filter.getUrlContains(), filter.getDescriptionContains(), pageable);
        } else if(filter != null && filter.getUrlContains() != null) {
            // return makeList(linkRepository.findAll(LinkSpecifications.linkByUrl(filter.getUrlContains())));
            links = linkRepository.findAllByUrlContains(filter.getUrlContains(), pageable);
        } else if(filter != null && filter.getDescriptionContains() != null) {
            // return makeList(linkRepository.findAll(LinkSpecifications.linkByDescription(filter.getDescriptionContains())));
            links = linkRepository.findAllByDescriptionContains(filter.getDescriptionContains(), pageable);
        } else {
            links = linkRepository.findAll(pageable);
        }

        int totalPages = links.getTotalPages();
        int pageNumber = links.getNumber() + 1;

        PageInfo pageInfo = PageInfo.builder()
                .hasNextPage(pageNumber < totalPages)
                .hasPreviousPage(pageNumber > 1)
                .build();

        return Links.builder()
                .items(links.getContent())
                .pageInfo(pageInfo)
                .build();

    }

    public Link link(Long id) {
        log.info("Query - link");
        return linkRepository.findOne(id);
    }

    // User query resolvers
    public List<User> users(String email, int page, int size, String orderBy) {
        log.info("Query - users");
        Pageable pageable = new PageRequest(page, size, orders(orderBy, User.class));
        if (!StringUtils.isBlank(email)) {
            // return makeList(userRepository.findAll(UserSpecifications.userByEmail(email)));
            return userRepository.findAllByEmailContains(email, pageable);
        }
        return userRepository.findAll(pageable).getContent();
    }

    public User user(Long id) {
        log.info("Query - user");
        return userRepository.findOne(id);
    }

    // Vote query resolvers
    public List<Vote> votes(Long userId, Long linkId, int page, int size, String orderBy) {
        log.info("Query - votes");
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
        log.info("Query - vote");
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
