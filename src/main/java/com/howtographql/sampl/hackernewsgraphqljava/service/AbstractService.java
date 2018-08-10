package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface AbstractService {
    Links findAll(BooleanExpression predicate, int page, int size, String orderBy);
    Link findOne(Long id);
}
