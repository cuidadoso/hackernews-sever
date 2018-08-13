package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.repository.*;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.LinkSpecifications.linkByDescription;
import static com.howtographql.sampl.hackernewsgraphqljava.specifications.LinkSpecifications.linkByUrl;
import static com.howtographql.sampl.hackernewsgraphqljava.specifications.UserSpecifications.userByEmail;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Log
@Component
@RequiredArgsConstructor
public class Query implements GraphQLQueryResolver {
    @Qualifier("linkService")
    private final AbstractService linkService;
    @Qualifier("userService")
    private final AbstractService userService;
    private final VoteRepository voteRepository;

    // Link query resolvers
    public Links links(LinkFilter filter, int page, int size, String orderBy) {
        BooleanExpression predicate = null;
        if(filter != null && filter.getUrlContains() != null && filter.getDescriptionContains() != null) {
            predicate = linkByUrl(filter.getUrlContains()).or(linkByDescription(filter.getUrlContains()));
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
