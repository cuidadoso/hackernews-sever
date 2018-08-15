package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService;
import com.howtographql.sampl.hackernewsgraphqljava.service.SessionService;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.UserSpecifications.userByEmail;
import static com.howtographql.sampl.hackernewsgraphqljava.specifications.VoteSpecifications.voteByLink;

@Log
@Component
@RequiredArgsConstructor
public class Mutation implements GraphQLMutationResolver {
    @Qualifier("linkService")
    private final AbstractService<Link, Links> linkService;
    @Qualifier("userService")
    private final AbstractService<User, Users> userService;
    @Qualifier("voteService")
    private final AbstractService<Vote, Votes> voteService;
    private final SessionService sessionService;

    // Link mutation resolvers
    public Link createLink(String url, String description, DataFetchingEnvironment env) {
        AuthContext context = env.getContext();
        return linkService.save(Link
                .builder()
                .url(url)
                .description(description)
                .userId(sessionService.userId())
                //.userId(context.getUser().getId())
                .build());
    }

    public boolean deleteLink(Long id) {
        if (linkService.exists(id)) {
            linkService.delete(id);
            return true;
        }
        throw new GraphQLException("Link not exists");
    }

    // User mutation resolvers
    public User createUser(String name, String email, String password) {
        return userService.save(User
                .builder()
                .name(name)
                .email(email)
                .password(password)
                .build());
    }

    public boolean deleteUset(Long id) {
        if (userService.exists(id)) {
            userService.delete(id);
            return true;
        }
        throw new GraphQLException("User not exists");
    }

    public User createUserAuth(String name, AuthData authData) {
        return userService.save(User
                .builder()
                .name(name)
                .email(authData.getEmail())
                .password(authData.getPassword())
                .build());
    }

    public SigninPayload signUp(String name, String email, String password) {
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
        return voteService.save(Vote
                .builder()
                .userId(userId)
                .linkId(linkId)
                .build());
    }

    public Vote vote(Long linkId) {
        List<Vote> all = voteService.findAll(voteByLink(linkId));
        List<Vote> votes = all
                .stream()
                .filter(vote -> vote.getUserId().equals(sessionService.userId()))
                .collect(Collectors.toList());
        if (!votes.isEmpty()) {
            return null;
        }
        return voteService.save(Vote
                .builder()
                .userId(sessionService.userId())
                .linkId(linkId)
                .build());
    }

    public boolean deleteVote(Long id) {
        if (voteService.exists(id)) {
            voteService.delete(id);
            return true;
        }
        throw new GraphQLException("Vote not exists");
    }
}
