package com.howtographql.sampl.hackernewsgraphqljava.specifications;

import com.howtographql.sampl.hackernewsgraphqljava.model.QVote;
import com.querydsl.core.types.dsl.BooleanExpression;

public class VoteSpecifications {
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
