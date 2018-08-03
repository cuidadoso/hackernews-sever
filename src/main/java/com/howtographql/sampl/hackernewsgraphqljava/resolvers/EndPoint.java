package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import com.howtographql.sampl.hackernewsgraphqljava.resolvers.exceptions.ErrorHandler;
import graphql.schema.GraphQLSchema;
import graphql.servlet.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class EndPoint extends SimpleGraphQLServlet {
    private final UserRepository userRepository;
    private final ErrorHandler errorHandler;

    public EndPoint(GraphQLSchema schema,
                    UserRepository userRepository,
                    ErrorHandler errorHandler) {
        super(schema);
        this.userRepository = userRepository;
        this.errorHandler = errorHandler;
    }

    @Override
    protected GraphQLContext createContext(Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        User user = request
                .map(req -> req.getHeader("Authorization"))
                .filter(id -> !id.isEmpty())
                .map(id -> id.replace("Bearer ", ""))
                .map(id -> {
                    if (StringUtils.isNumeric(id)) {
                        return userRepository.findOne(Long.parseLong(id));
                    }
                    return userRepository.findOne(1L);
                })
                .orElse(userRepository.findOne(1L));
        return new AuthContext(user, request, response);
    }

    @Override
    protected GraphQLErrorHandler getGraphQLErrorHandler() {
        return errorHandler;
    }
}
