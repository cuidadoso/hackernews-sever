package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.AuthData;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mutation implements GraphQLMutationResolver {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    // Link mutation resolvers
    public Link createLink(String url, String description) {

        return linkRepository.save(Link
                .builder()
                .url(url)
                .description(description)
                .build());
    }

    // User mutation resolvers
    public User createUser(String name, String email, String password) {
        return userRepository.save(User
                .builder()
                .name(name)
                .email(email)
                .password(password)
                .build());
    }

    public User createUserAuth(String name, AuthData authData) {
        return userRepository.save(User
                .builder()
                .name(name)
                .email(authData.getEmail())
                .password(authData.getPassword())
                .build());
    }
}
