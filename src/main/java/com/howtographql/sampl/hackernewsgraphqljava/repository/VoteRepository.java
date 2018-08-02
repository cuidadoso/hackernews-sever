package com.howtographql.sampl.hackernewsgraphqljava.repository;

import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long>, QueryDslPredicateExecutor<Vote> {
    List<Vote> findAllByUserId(Long userId);
    List<Vote> findAllByLinkId(Long linkId);
    List<Vote> findAllByUserIdAndLinkId(Long userId, Long linkId);
}
