package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.BaseEntities;
import com.howtographql.sampl.hackernewsgraphqljava.model.BaseEntity;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface AbstractService<Entity> {
    BaseEntity findOne(Long id);
    BaseEntities findAll(BooleanExpression predicate, int page, int size, String orderBy);
    Entity save(Entity entity);
    void delete(Entity entity);
    void delete(Long id);
    boolean exists(Long id);
}
