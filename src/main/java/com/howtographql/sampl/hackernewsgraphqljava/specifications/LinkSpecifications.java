package com.howtographql.sampl.hackernewsgraphqljava.specifications;

import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

import static com.howtographql.sampl.hackernewsgraphqljava.model.QLink.link;
import static com.howtographql.sampl.hackernewsgraphqljava.model.QUser.user;


@UtilityClass
public class LinkSpecifications {

    public static Map<String, Method> predicates() throws NoSuchMethodException {
        return ImmutableMap.of(
                "id", LinkSpecifications.class.getDeclaredMethod("byId", String.class),
                "createdAt", LinkSpecifications.class.getDeclaredMethod("byCreatedAt", String.class),
                "url", LinkSpecifications.class.getDeclaredMethod("linkByUrl", String.class),
                "description", LinkSpecifications.class.getDeclaredMethod("linkByDescription", String.class),
                "userId", LinkSpecifications.class.getDeclaredMethod("linkByUser", Long.class)
        );
    }

    public static BooleanExpression byId(String id) {
        return link.id.eq(Long.parseLong(id));
    }

    // TODO implement date format checking
    public static BooleanExpression byCreatedAt(String dateTime) {
        return link.createdAt.eq(ZonedDateTime.parse(dateTime));
    }

    public static BooleanExpression linkByUrl(String url) {
        return link.url.contains(url);
    }

    public static BooleanExpression linkByDescription(String description) {
        return link.description.contains(description);
    }

    public static BooleanExpression linkByUser(Long userId) {
        return link.userId.eq(userId);
    }

    public static BooleanExpression linkByUsers(Set<Long> userIds) {
        return link.userId.in(userIds);
    }

    public static BooleanExpression linkByUsers(String userName) {
        return link.createdBy.in(JPAExpressions.selectFrom(user).where(user.name.contains(userName)).select(user.id));
    }

    public static BooleanExpression linkByUrlOrDescription(String url, String description) {
        return linkByUrl(url).or(linkByDescription(description));
    }
}
