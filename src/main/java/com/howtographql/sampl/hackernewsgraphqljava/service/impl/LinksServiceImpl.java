package com.howtographql.sampl.hackernewsgraphqljava.service.impl;

import com.google.common.collect.ImmutableMap;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractServiceHelper;
import com.howtographql.sampl.hackernewsgraphqljava.service.LinkService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.LinkSpecifications.linkByUrl;
import static com.howtographql.sampl.hackernewsgraphqljava.util.ObjectType.*;

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
}
