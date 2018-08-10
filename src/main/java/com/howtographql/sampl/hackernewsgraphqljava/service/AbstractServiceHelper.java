package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.repository.BaseRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import graphql.GraphQLException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logError;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logInfo;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Getter
@RequiredArgsConstructor
public abstract class AbstractServiceHelper<Entity extends BaseEntity, Entities extends BaseEntities> implements AbstractService {
    private final Class<Entity> classOfEntity;
    private final Class<Entities> classOfEntities;

    protected abstract BaseRepository<Entity> repository();

    @Override
    public Entity findOne(Long id) {
        return repository().findOne(id);
    }

    public Entities findAll(BooleanExpression predicate, int page, int size, String orderBy) {
        Page<Entity> entityPage = pageable(predicate, page, size, orderBy);
        return entities(entityPage);
    }

    private Page<Entity> pageable(BooleanExpression predicate, int page, int size, String orderBy) {
        Pageable pageable = new PageRequest(page, size, orders(orderBy));
        return repository().findAll(predicate, pageable);
    }

    private Entities entities(Page<Entity> entityPage) {
        try {
            return classOfEntities.getDeclaredConstructor(List.class, PageInfo.class)
                    .newInstance(entityPage.getContent(), pageInfo(entityPage));
        } catch (Exception e) {
            logError("Error: %s", e.getMessage());
            throw new GraphQLException("Server error");
        }
    }

    private Sort orders(String orderBy) {
        if (!StringUtils.isBlank(orderBy) && isBaseEntity()) {
            String[] orderParams = orderBy.split("_");
            String fieldName = orderParams[0];
            Direction direction = Direction.fromStringOrNull(orderParams[1]);
            switch (fieldName) {
                case "id":
                case "createdAt":
                    return new Sort(new Order(direction == null ? ASC : direction, fieldName));
                default:
                    try {
                        // Just to check if classOfEntity contains field with name = fieldName
                        classOfEntity.getDeclaredField(fieldName);
                        return new Sort(new Order(direction == null ? ASC : direction, fieldName));
                    } catch (NoSuchFieldException e) {
                        logInfo("Field not exists.");
                        // Default sort by id
                        return new Sort(new Order(ASC, "id"));
                    }
            }
        }
        return null;
    }

    private PageInfo pageInfo(Page<Entity> entities) {
        int totalPages = entities.getTotalPages();
        int pageNumber = entities.getNumber() + 1;
        long total = entities.getTotalElements();
        return PageInfo.builder()
                .hasNextPage(pageNumber < totalPages)
                .hasPreviousPage(pageNumber > 1)
                .total(total)
                .build();
    }

    private boolean isBaseEntity() {
        return BaseEntity.class.equals(classOfEntity.getSuperclass());
    }
}
