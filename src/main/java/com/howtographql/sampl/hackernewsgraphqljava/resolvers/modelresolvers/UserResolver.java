package com.howtographql.sampl.hackernewsgraphqljava.resolvers.modelresolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResolver implements GraphQLResolver<User> {
    @Qualifier("userService")
    private final UserService userService;

    public User createdBy(User user) {
        if (user.getCreatedBy() == null) {
            return null;
        }
        return userService.findOne(user.getCreatedBy());
    }

    public User updatedBy(User user) {
        if (user.getUpdatedBy() == null) {
            return null;
        }
        return userService.findOne(user.getUpdatedBy());
    }

    public User deletedBy(User user) {
        if (user.getDeletedBy() == null) {
            return null;
        }
        return userService.findOne(user.getDeletedBy());
    }
}
