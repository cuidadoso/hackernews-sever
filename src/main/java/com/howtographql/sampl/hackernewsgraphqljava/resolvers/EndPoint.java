package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.resolvers.exceptions.ErrorHandler;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.GraphQLErrorHandler;
import graphql.servlet.SimpleGraphQLServlet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.USER_ID;

@Component
public class EndPoint extends SimpleGraphQLServlet {
    @Qualifier("userService")
    private final AbstractService userService;
    private final ErrorHandler errorHandler;

    public EndPoint(GraphQLSchema schema,
                    AbstractService userService,
                    ErrorHandler errorHandler) {
        super(schema);
        this.userService = userService;
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
                        return (User) userService.findOne(Long.parseLong(id));
                    }
                    return (User) userService.findOne(USER_ID);
                })
                .orElse((User) userService.findOne(USER_ID));
        return new AuthContext(user, request, response);
    }

    @Override
    protected GraphQLErrorHandler getGraphQLErrorHandler() {
        return errorHandler;
    }
}
