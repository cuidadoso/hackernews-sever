package com.howtographql.sampl.hackernewsgraphqljava.repository;

import com.howtographql.sampl.hackernewsgraphqljava.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface BaseRepository<Entity extends BaseEntity> extends JpaRepository<Entity, Long>, QueryDslPredicateExecutor<Entity> {
}
