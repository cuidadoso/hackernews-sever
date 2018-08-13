package com.howtographql.sampl.hackernewsgraphqljava.specifications;

import com.howtographql.sampl.hackernewsgraphqljava.model.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;

public class UserSpecifications {
    public static BooleanExpression userByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        return QUser.user.email.contains(email);
    }

    public static BooleanExpression userByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return QUser.user.name.contains(name);
    }
}
