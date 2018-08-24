package com.howtographql.sampl.hackernewsgraphqljava.service.impl;

import com.google.common.collect.ImmutableMap;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractServiceHelper;
import com.howtographql.sampl.hackernewsgraphqljava.service.LinkService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.LinkSpecifications.*;
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
    protected Order orderByService(String id, boolean desc) {
        if ("postedBy".equals(id)) {
            return new Order(desc ? DESC : ASC, "createdBy");
        }
        return null;
    }

    @Override
    protected BooleanExpression predicateByService(String id, String value) {
        switch (id) {
            case "url_or_description":
                return linkByUrlOrDescription(value, value);
            case "postedBy":
                return linkByUsers(value);
             default:
                 return null;
         }
    }
}
