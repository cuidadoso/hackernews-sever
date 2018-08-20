package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.service.LinkService;
import com.howtographql.sampl.hackernewsgraphqljava.service.SessionService;
import com.howtographql.sampl.hackernewsgraphqljava.service.UserService;
import com.howtographql.sampl.hackernewsgraphqljava.service.VoteService;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.UserSpecifications.userByEmail;

@Log
@Component
@RequiredArgsConstructor
public class Mutation implements GraphQLMutationResolver {
    @Qualifier("linkService")
    private final LinkService linkService;
    @Qualifier("userService")
    private final UserService userService;
    @Qualifier("voteService")
    private final VoteService voteService;
    private final SessionService sessionService;

    // Link mutation resolvers
    public Link createLink(String url, String description, DataFetchingEnvironment env) {
        AuthContext context = env.getContext();
        if (linkService.existsUniq(url)) {
            throw new GraphQLException(String.format("Link [%s] already exists", url));
        }
        return linkService.save(Link
                .builder()
                .url(url)
                .description(description)
                .userId(sessionService.userId())
                //.userId(context.getUser().getId())
                .build());
    }

    public boolean deleteLink(Long id) {
            return linkService.delete(id);
    }

    // User mutation resolvers
    public User createUser(String name, String email, String password) {
        if (userService.existsUniq(email)) {
            throw new GraphQLException(String.format("User with email [%s] already exists", email));
        }
        return userService.save(User
                .builder()
                .name(name)
                .email(email)
                .password(password)
                .build());
    }

    public boolean deleteUset(Long id) {
            return userService.delete(id);
    }

    public User createUserAuth(String name, AuthData authData) {
        String email = authData.getEmail();
        if (userService.existsUniq(email)) {
            throw new GraphQLException(String.format("User with email [%s] already exists", email));
        }
        return userService.save(User
                .builder()
                .name(name)
                .email(email)
                .password(authData.getPassword())
                .build());
    }

    public SigninPayload signUp(String name, String email, String password) {
        if (userService.existsUniq(email)) {
            throw new GraphQLException(String.format("User with email [%s] already exists", email));
        }
        User user = userService.save(User
                .builder()
                .name(name)
                .email(email)
                .password(password)
                .build());
        sessionService.saveUserId(user.getId());
        return new SigninPayload(user.getId(), user);
    }
    // TODO replace with OAht2 + JWT implementation
    public SigninPayload signIn(AuthData auth) {
        List<User> users = userService.findAll(userByEmail(auth.getEmail()));
        if (!users.isEmpty() && users.get(0).getPassword().equals(auth.getPassword())) {
            User user = users.get(0);
            sessionService.saveUserId(user.getId());
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException("Invalid credentials");
    }
    // TODO replace with OAht2 + JWT implementation
    public SigninPayload login(String email, String password) {
        List<User> users = userService.findAll(userByEmail(email));
        if (!users.isEmpty() && users.get(0).getPassword().equals(password)) {
            User user = users.get(0);
            sessionService.saveUserId(user.getId());
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException("Invalid credentials");
    }

    // Vote mutation resolvers
    public Vote createVote(Long linkId, Long userId) {
        if (voteService.existsUniq(linkId, userId)) {
            throw new GraphQLException(String.format("Vote with link [%d] and user [%d] already exists", linkId, userId));
        }
        return voteService.save(Vote
                .builder()
                .userId(userId)
                .linkId(linkId)
                .build());
    }

    public Vote vote(Long linkId) {
        Long userId = sessionService.userId();
        if (voteService.existsUniq(linkId, userId)) {
            throw new GraphQLException(String.format("Vote with link [%d] and user [%d] already exists", linkId, userId));
        }
        return voteService.save(Vote
                .builder()
                .userId(sessionService.userId())
                .linkId(linkId)
                .build());
    }

    public boolean deleteVote(Long id) {
            return voteService.delete(id);
    }
}
