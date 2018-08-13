package com.howtographql.sampl.hackernewsgraphqljava.model;

import java.util.List;

public class Votes extends BaseEntities<Vote>{
    public Votes(List<Vote> items, PageInfo pageInfo) {
        super(items, pageInfo);
    }
}
