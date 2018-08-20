package com.howtographql.sampl.hackernewsgraphqljava.loader;

import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.configurations.SpringBeanUtils.session;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.USER_ID;

@Component
@Order(0)
@RequiredArgsConstructor
public class DataLoader0000User implements ApplicationRunner {
    private final static List<User> USER_LIST = new ArrayList<>();
    @Qualifier("userService")
    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        session().saveUserId(USER_ID);

        createUser("name", "email", "pass");
        createUser("name2", "email2", "pass");
        createUser("name3", "email3", "pass");

        userService.save(USER_LIST);
    }

    private void createUser(String name, String email, String pass) {
        if (userService.existsUniq(email)) {
            return;
        }
        USER_LIST.add(User.builder()
                .name(name)
                .email(email)
                .password(pass)
                .build());
    }
}
