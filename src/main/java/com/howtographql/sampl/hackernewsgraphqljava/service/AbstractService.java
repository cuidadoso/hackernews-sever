package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.BaseEntity;
import com.howtographql.sampl.hackernewsgraphqljava.model.PageInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RequiredArgsConstructor
public abstract class AbstractService<E extends BaseEntity> {
    private final Class<E> classOfEntity;

    protected abstract QueryDslPredicateExecutor<E> repository();

    protected Page<E> pageable(BooleanExpression predicate, int page, int size, String orderBy) {
        Pageable pageable = new PageRequest(page, size, orders(orderBy));
        return repository().findAll(predicate, pageable);
    }

    protected Sort orders(String orderBy) {
        if (orderBy != null) {
            String[] orderParams = orderBy.split("_");
            Direction direction = Direction.fromStringOrNull(orderParams[1]);
            /*TODO validate sort field
            try {
                Field field = classOfEntity.getField(orderParams[0]);
                return new Sort(new Order(direction == null ? ASC : direction, field.getName()));
            } catch (NoSuchFieldException e) {
                logInfo("Field not exists.");
                return new Sort(new Order(direction == null ? ASC : direction, "id"));
            }*/
            return new Sort(new Order(direction == null ? ASC : direction, orderParams[0]));
        } else {
            return null;
        }
    }

    protected PageInfo pageInfo(Page<E> entities) {
        int totalPages = entities.getTotalPages();
        int pageNumber = entities.getNumber() + 1;
        return PageInfo.builder()
                .hasNextPage(pageNumber < totalPages)
                .hasPreviousPage(pageNumber > 1)
                .build();
    }
}
