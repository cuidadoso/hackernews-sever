package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

public interface AbstractService<Entity, Entities> {
    Entity findOne(Long id);
    List<Entity> findAll();
    List<Entity> findAll(BooleanExpression predicate);
    Entities findAll(BooleanExpression predicate, int page, int size, String orderBy);
    Entity save(Entity entity);
    void delete(Entity entity);
    void delete(Long id);
    void delete(Iterable<Entity> entities);
    boolean exists(Long id);
}
