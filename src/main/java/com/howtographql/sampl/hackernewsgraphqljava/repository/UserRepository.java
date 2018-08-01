package com.howtographql.sampl.hackernewsgraphqljava.repository;

import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {

}
