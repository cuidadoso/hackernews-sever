package com.howtographql.sampl.hackernewsgraphqljava.resolvers.modelresolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;
import com.howtographql.sampl.hackernewsgraphqljava.model.PageInfo;

import java.util.List;

public class LinksResolver implements GraphQLResolver<Links> {
    public List<Link> items(Links links) {
        return links.getItems();
    }

    public PageInfo pageInfo(Links links) {
        return links.getPageInfo();
    }
}
