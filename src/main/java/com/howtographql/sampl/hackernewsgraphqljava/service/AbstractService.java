package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.Filter;
import com.howtographql.sampl.hackernewsgraphqljava.model.OrderBy;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

public interface AbstractService<Entity, Entities> {
    Entity findOne(Long id);
    List<Entity> findAll();
    List<Entity> findAll(List<Filter> filter);
    Entities findAll(List<Filter> filter, int page, int size, List<OrderBy> orderBy);
    List<Entity> findAll(BooleanExpression predicate);
    Entities findAll(BooleanExpression predicate, int page, int size, List<OrderBy> orderBy);
    Entity save(Entity entity);
    List<Entity> save(List<Entity> entity);
    boolean delete(Entity entity);
    boolean delete(Long id);
    boolean delete(Iterable<Entity> entities);
    boolean exists(Long id);
}
