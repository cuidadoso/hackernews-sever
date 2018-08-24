package com.howtographql.sampl.hackernewsgraphqljava.specifications;

import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.Map;

import static com.howtographql.sampl.hackernewsgraphqljava.model.QUser.user;

@UtilityClass
public class UserSpecifications {
    public static Map<String, Method> predicates() throws NoSuchMethodException {
        return ImmutableMap.of(
                "email", UserSpecifications.class.getDeclaredMethod("userByEmail", String.class),
                "name", UserSpecifications.class.getDeclaredMethod("userByName", String.class));
    }

    public static BooleanExpression userByEmail(String email) {
        return user.email.contains(email);
    }

    public static BooleanExpression userByName(String name) {
        return user.name.contains(name);
    }
}
