package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;

public interface LinkService extends AbstractService<Link, Links> {
    boolean existsUniq(String url);
}
