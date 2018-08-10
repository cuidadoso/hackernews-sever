package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.BaseEntity;
import com.howtographql.sampl.hackernewsgraphqljava.model.PageInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logInfo;
import static org.springframework.data.domain.Sort.Direction.ASC;

@RequiredArgsConstructor
public abstract class AbstractServiceHelper<E extends BaseEntity> {
    private final Class<E> classOfEntity;

    protected abstract QueryDslPredicateExecutor<E> repository();

    protected Page<E> pageable(BooleanExpression predicate, int page, int size, String orderBy) {
        Pageable pageable = new PageRequest(page, size, orders(orderBy));
        return repository().findAll(predicate, pageable);
    }

    protected Sort orders(String orderBy) {
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
                        classOfEntity. getDeclaredField(fieldName);
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

    protected PageInfo pageInfo(Page<E> entities) {
        int totalPages = entities.getTotalPages();
        int pageNumber = entities.getNumber() + 1;
        return PageInfo.builder()
                .hasNextPage(pageNumber < totalPages)
                .hasPreviousPage(pageNumber > 1)
                .build();
    }

    private boolean isBaseEntity() {
        return BaseEntity.class.equals(classOfEntity.getSuperclass());
    }
}
