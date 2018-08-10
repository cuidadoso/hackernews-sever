package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;
import com.howtographql.sampl.hackernewsgraphqljava.model.PageInfo;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Service;

@Service("linkService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class LinkServiceImpl extends AbstractService<Link> implements LinkService {
    private final LinkRepository linkRepository;

    public LinkServiceImpl(LinkRepository linkRepository) {
        super(Link.class);
        this.linkRepository = linkRepository;
    }

    @Override
    public Links findAll(BooleanExpression predicate, int page, int size, String orderBy) {
        Page<Link> links = pageable(predicate, page, size, orderBy);
        PageInfo pageInfo = pageInfo(links);

        return Links.builder()
                .items(links.getContent())
                .pageInfo(pageInfo)
                .build();
    }

    @Override
    public Link findOne(Long id) {
        return linkRepository.findOne(id);
    }

    @Override
    protected QueryDslPredicateExecutor<Link> repository() {
        return linkRepository;
    }
}
