package com.howtographql.sampl.hackernewsgraphqljava.repository;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long>, QueryDslPredicateExecutor<Link> {
    List<Link> findAllByUrlContains(String url, Pageable pageable);
    List<Link> findAllByDescriptionContains(String description, Pageable pageable);
    List<Link> findAllByUrlContainsOrDescriptionContains(String url, String description, Pageable pageable);
}
