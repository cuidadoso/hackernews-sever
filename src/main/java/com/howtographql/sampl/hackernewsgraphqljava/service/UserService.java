package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.model.Users;

public interface UserService extends AbstractService<User, Users> {
    boolean existsUniq(String email);
}
