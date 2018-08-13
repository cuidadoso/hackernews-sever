package com.howtographql.sampl.hackernewsgraphqljava.specifications;

import com.howtographql.sampl.hackernewsgraphqljava.model.QVote;
import com.querydsl.core.types.dsl.BooleanExpression;

public class VoteSpecifications {
    public static BooleanExpression voteByLink(Long linkId) {
        if (linkId == null) {
            return null;
        }
        return QVote.vote.linkId.eq(linkId);
    }

    public static BooleanExpression voteByUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return QVote.vote.userId.eq(userId);
    }
}
