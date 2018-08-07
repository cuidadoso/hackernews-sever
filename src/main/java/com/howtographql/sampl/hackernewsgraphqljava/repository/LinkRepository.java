package com.howtographql.sampl.hackernewsgraphqljava.repository;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface LinkRepository extends JpaRepository<Link, Long>, QueryDslPredicateExecutor<Link> {
    Page<Link> findAllByUrlContains(String url, Pageable pageable);
    Page<Link> findAllByDescriptionContains(String description, Pageable pageable);
    Page<Link> findAllByUrlContainsOrDescriptionContains(String url, String description, Pageable pageable);
}
