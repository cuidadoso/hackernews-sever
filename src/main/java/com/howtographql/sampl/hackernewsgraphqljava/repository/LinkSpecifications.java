package com.howtographql.sampl.hackernewsgraphqljava.repository;

import com.howtographql.sampl.hackernewsgraphqljava.model.QLink;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkSpecifications {
    public static BooleanExpression linkByUrl(String url) {
        return QLink.link.url.contains(url);
    }

    public static BooleanExpression linkByDescription(String description) {
        return QLink.link.description.contains(description);
    }
}
