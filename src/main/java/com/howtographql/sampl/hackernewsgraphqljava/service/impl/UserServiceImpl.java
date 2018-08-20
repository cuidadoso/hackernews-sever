package com.howtographql.sampl.hackernewsgraphqljava.service.impl;

import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Users;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractServiceHelper;
import com.howtographql.sampl.hackernewsgraphqljava.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.UserSpecifications.userByEmail;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserServiceImpl extends AbstractServiceHelper<User, Users> implements UserService {
    public UserServiceImpl(UserRepository repository) {
        super(User.class, Users.class, repository);
    }

    @Override
    public boolean existsUniq(String email) {
        return !findAll(userByEmail(email)).isEmpty();
    }
}
