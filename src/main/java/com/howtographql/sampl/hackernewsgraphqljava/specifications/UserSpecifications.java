package com.howtographql.sampl.hackernewsgraphqljava.specifications;

import com.howtographql.sampl.hackernewsgraphqljava.model.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;

public class UserSpecifications {
    public static BooleanExpression userByEmail(String email) {
        return QUser.user.email.contains(email);
    }

    public static BooleanExpression userByName(String name) {
        return QUser.user.name.contains(name);
    }
}
