package com.howtographql.sampl.hackernewsgraphqljava.repository;

import com.howtographql.sampl.hackernewsgraphqljava.model.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;

public class UserSpecifications {
    public static BooleanExpression userByEmail(String email) {
        return QUser.user.email.contains(email);
    }
}
