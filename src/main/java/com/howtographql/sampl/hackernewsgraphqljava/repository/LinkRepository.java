package com.howtographql.sampl.hackernewsgraphqljava.repository;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface LinkRepository extends JpaRepository<Link, Long>, QueryDslPredicateExecutor<Link> {
}
