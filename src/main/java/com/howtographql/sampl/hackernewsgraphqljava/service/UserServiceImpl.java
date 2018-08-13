package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Users;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserServiceImpl extends AbstractServiceHelper<User, Users> {
    public UserServiceImpl(UserRepository repository) {
        super(User.class, Users.class, repository);
    }
}
