package com.howtographql.sampl.hackernewsgraphqljava.service.impl;

import com.google.common.collect.ImmutableMap;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import com.howtographql.sampl.hackernewsgraphqljava.resolvers.exceptions.CustomException;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractServiceHelper;
import com.howtographql.sampl.hackernewsgraphqljava.service.LinkService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.LinkSpecifications.linkByUrl;
import static com.howtographql.sampl.hackernewsgraphqljava.util.ObjectType.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service("linkService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class LinksServiceImpl extends AbstractServiceHelper<Link, Links> implements LinkService {
    public LinksServiceImpl(LinkRepository repository) {
        super(ImmutableMap.of(
                ENTITY, "model.Link",
                PAGEABLE, "model.Links",
                SPEC, "specifications.LinkSpecifications"
                ), repository);
    }

    @Override
    public boolean existsUniq(String url) {
        return !findAll(linkByUrl(url)).isEmpty();
    }

    @Override
    protected Sort.Order createOrder(String id, boolean desc) {
        switch (id) {
            case "id":
            case "createdAt":
                return new Sort.Order(desc ? DESC : ASC, id);
            case "postedBy":
                return new Sort.Order(desc ? DESC : ASC, "createdBy");
            default:
                try {
                    // Just to check if classOfEntity contains field with name = fieldName
                    entityClass.getDeclaredField(id);
                    return new Sort.Order(desc ? DESC : ASC, id);
                } catch (NoSuchFieldException e) {
                    throw new CustomException(String.format("Entity [%s] doesn't have field [%s].", entityClass.getSimpleName(), id));
                }
        }
    }

    @Override
    protected BooleanExpression createPredicate(String id, String value) {
        try {
            Method method = predicates().get(id);
            switch (id) {
                case "id":
                case "createdAt":
                    return (BooleanExpression) method.invoke(specClass, value);
                case "url_or_description":
                    return (BooleanExpression) method.invoke(specClass, value, value);
                default:
                    // Just to check if classOfEntity contains field with name = id
                    entityClass.getDeclaredField(id);
                    return (BooleanExpression) method.invoke(specClass, value);
            }
        } catch (Exception e) {
            throw new CustomException(String.format("Entity [%s] doesn't have predicates.", entityClass.getSimpleName()));
        }
    }
}
