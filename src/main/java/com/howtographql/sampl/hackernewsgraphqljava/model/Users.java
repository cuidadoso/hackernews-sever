package com.howtographql.sampl.hackernewsgraphqljava.model;

import java.util.List;

public class Users extends BaseEntities<User>{
    public Users(List<User> items, PageInfo pageInfo) {
        super(items, pageInfo);
    }
}
