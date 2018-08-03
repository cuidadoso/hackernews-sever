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

    private static final ZonedDateTime NOW = Instant.now().atZone(UTC);

    // Link mutation resolvers
    public Link createLink(String url, String description, DataFetchingEnvironment env) {
        AuthContext context = env.getContext();
        return linkRepository.save(Link
                .builder()
                .createdAt(NOW)
                .url(url)
                .description(description)
                .userId((Long) store.get("userId"))
                //.userId(context.getUser().getId())
                .build());
    }

    public boolean deleteLink(Long id) {
        if (linkRepository.exists(id)) {
            linkRepository.delete(id);
            return true;
        }
        throw new GraphQLException("Link not exists");
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

    public boolean deleteUset(Long id) {
        if (userRepository.exists(id)) {
            userRepository.delete(id);
            return true;
        }
        throw new GraphQLException("User not exists");
    }

    public User createUserAuth(String name, AuthData authData) {
        return userRepository.save(User
                .builder()
                .name(name)
                .email(authData.getEmail())
                .password(authData.getPassword())
                .build());
    }

    public SigninPayload signUp(String name, String email, String password) {
        User user = userRepository.save(User
                .builder()
                .name(name)
                .email(email)
                .password(password)
                .build());
        return new SigninPayload(user.getId(), user);
    }

    public SigninPayload signIn(AuthData auth) throws IllegalAccessException {
        User user = userRepository.findByEmail(auth.getEmail());
        if (user.getPassword().equals(auth.getPassword())) {
            // TODO replace with OAht2 + JWT implementation
            store.put("userId", user.getId());
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException("Invalid credentials");
    }

    public SigninPayload login(String email, String password) throws IllegalAccessException {
        User user = userRepository.findByEmail(email);
        if (user.getPassword().equals(password)) {
            // TODO replace with OAht2 + JWT implementation
            store.put("userId", user.getId());
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException("Invalid credentials");
    }

    // Vote mutation resolvers
    public Vote createVote(Long linkId, Long userId) {
        return voteRepository.save(Vote
                .builder()
                .createdAt(NOW)
                .userId(userId)
                .linkId(linkId)
                .build());
    }

    public Vote vote(Long linkId) {
        return voteRepository.save(Vote
                .builder()
                .createdAt(NOW)
                .userId((Long) store.get("userId"))
                .linkId(linkId)
                .build());
    }

    public boolean deleteVote(Long id) {
        if (voteRepository.exists(id)) {
            voteRepository.delete(id);
            return true;
        }
        throw new GraphQLException("Vote not exists");
    }
}
