package com.howtographql.sampl.hackernewsgraphqljava.model;

import java.util.List;

public class Links extends BaseEntities<Link>{
    public Links(List<Link> items, PageInfo pageInfo) {
        super(items, pageInfo);
    }
}
