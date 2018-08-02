package com.howtographql.sampl.hackernewsgraphqljava.repository;

import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {
    List<User> findAllByEmailContains(String email, Pageable pageable);
    User findByEmail(String email);
}
