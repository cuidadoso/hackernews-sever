package com.howtographql.sampl.hackernewsgraphqljava.specifications;

import com.google.common.collect.ImmutableMap;
import com.howtographql.sampl.hackernewsgraphqljava.model.QLink;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.Map;


@UtilityClass
public class LinkSpecifications {

    public static Map<String, Method> predicates() throws NoSuchMethodException {
        return ImmutableMap.of(
                "url", LinkSpecifications.class.getDeclaredMethod("linkByUrl", String.class),
                "description", LinkSpecifications.class.getDeclaredMethod("linkByDescription", String.class),
                "url_or_description", LinkSpecifications.class.getDeclaredMethod("linkByUrlOrDescription", String.class, String.class)
        );
    }

    public static BooleanExpression linkByUrl(String url) {
        return QLink.link.url.contains(url);
    }

    public static BooleanExpression linkByDescription(String description) {
        return QLink.link.description.contains(description);
    }

    public static BooleanExpression linkByUrlOrDescription(String url, String description) {
        return linkByUrl(url).or(linkByDescription(description));
    }
}
