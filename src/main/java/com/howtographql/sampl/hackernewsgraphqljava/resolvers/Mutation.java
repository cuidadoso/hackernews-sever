package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import com.howtographql.sampl.hackernewsgraphqljava.repository.VoteRepository;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;

import static java.time.ZoneOffset.UTC;

@Component
@RequiredArgsConstructor
public class Mutation implements GraphQLMutationResolver {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    @Resource(name = "store")
    private Map<String, Object> store;

    // Link mutation resolvers
    public Link createLink(String url, String description, DataFetchingEnvironment env) {
        return linkRepository.save(Link
                .builder()
                .url(url)
                .description(description)
                .userId((Long) store.get("userId"))
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

    public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
        User user = userRepository.findByEmail(auth.getEmail());
        if (user.getPassword().equals(auth.getPassword())) {
            store.put("userId", user.getId());
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException("Invalid credentials");
    }

    // Vote mutation resolvers
    public Vote createVote(Long linkId, Long userId) {
        ZonedDateTime now = Instant.now().atZone(UTC);
        return voteRepository.save(Vote
                .builder()
                .createdAt(now)
                .userId(userId)
                .linkId(linkId)
                .build());
    }
}
