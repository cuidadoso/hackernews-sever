package com.howtographql.sampl.hackernewsgraphqljava.specifications;

import com.google.common.collect.ImmutableMap;
import com.howtographql.sampl.hackernewsgraphqljava.model.QVote;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.Map;

@UtilityClass
public class VoteSpecifications {
    public static Map<String, Method> predicates() throws NoSuchMethodException {
        return ImmutableMap.of(
                "linkId", VoteSpecifications.class.getDeclaredMethod("voteByLink", Long.class),
                "userId", VoteSpecifications.class.getDeclaredMethod("voteByUser", Long.class));
    }

    public static BooleanExpression voteByLink(Long linkId) {
        return QVote.vote.linkId.eq(linkId);
    }

    public static BooleanExpression voteByUser(Long userId) {
        return QVote.vote.userId.eq(userId);
    }

    public static BooleanExpression voteByLinkAndUser(Long linkId, Long userId) {
        return voteByLink(linkId).and(voteByUser(userId));
    }
}
