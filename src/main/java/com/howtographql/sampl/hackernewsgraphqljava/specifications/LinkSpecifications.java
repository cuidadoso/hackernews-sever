package com.howtographql.sampl.hackernewsgraphqljava.specifications;

import com.howtographql.sampl.hackernewsgraphqljava.model.QLink;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class LinkSpecifications {
    public static BooleanExpression linkByUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        return QLink.link.url.contains(url);
    }

    public static BooleanExpression linkByDescription(String description) {
        if (StringUtils.isBlank(description)) {
            return null;
        }
        return QLink.link.description.contains(description);
    }
}
