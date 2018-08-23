package com.howtographql.sampl.hackernewsgraphqljava.service.impl;

import com.google.common.collect.ImmutableMap;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Users;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractServiceHelper;
import com.howtographql.sampl.hackernewsgraphqljava.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.UserSpecifications.userByEmail;
import static com.howtographql.sampl.hackernewsgraphqljava.util.ObjectType.*;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserServiceImpl extends AbstractServiceHelper<User, Users> implements UserService {
    public UserServiceImpl(UserRepository repository) {
        super(ImmutableMap.of(
                ENTITY, "model.User",
                PAGEABLE, "model.Users",
                SPEC, "specifications.UserSpecifications"
        ), repository);
    }

    @Override
    public boolean existsUniq(String email) {
        return !findAll(userByEmail(email)).isEmpty();
    }
}
