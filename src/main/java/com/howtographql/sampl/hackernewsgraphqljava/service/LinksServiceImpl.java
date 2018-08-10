package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;
import com.howtographql.sampl.hackernewsgraphqljava.repository.BaseRepository;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("linkService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class LinksServiceImpl extends AbstractServiceHelper<Link, Links> {
    private final LinkRepository linkRepository;

    public LinksServiceImpl(LinkRepository linkRepository) {
        super(Link.class, Links.class);
        this.linkRepository = linkRepository;
    }

    @Override
    protected BaseRepository<Link> repository() {
        return linkRepository;
    }
}
