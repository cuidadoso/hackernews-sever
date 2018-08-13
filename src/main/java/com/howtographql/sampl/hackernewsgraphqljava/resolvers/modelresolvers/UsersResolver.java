package com.howtographql.sampl.hackernewsgraphqljava.resolvers.modelresolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.PageInfo;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Users;

import java.util.List;

public class UsersResolver implements GraphQLResolver<Users> {
    public List<User> items(Users users) {
        return users.getItems();
    }

    public PageInfo pageInfo(Users users) {
        return users.getPageInfo();
    }
}
