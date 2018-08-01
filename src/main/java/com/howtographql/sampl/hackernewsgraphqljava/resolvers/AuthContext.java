package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import graphql.servlet.GraphQLContext;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Getter
public class AuthContext extends GraphQLContext {
    private final User user;

    public AuthContext(User user, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        super(request, response);
        this.user = user;
    }
}
