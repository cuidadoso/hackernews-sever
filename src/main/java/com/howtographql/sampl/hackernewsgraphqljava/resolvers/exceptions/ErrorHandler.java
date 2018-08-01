package com.howtographql.sampl.hackernewsgraphqljava.resolvers.exceptions;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.execution.ExecutionPath;
import graphql.servlet.DefaultGraphQLErrorHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ErrorHandler extends DefaultGraphQLErrorHandler  {
    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        return errors.stream()
                .filter(e -> e instanceof ExceptionWhileDataFetching || super.isClientError(e))
                .map(e -> e instanceof ExceptionWhileDataFetching ?
                        new SanitizedError(ExecutionPath.fromList(e.getPath()), ((ExceptionWhileDataFetching) e).getException(), e.getLocations().get(0)) :
                        e)
                .collect(Collectors.toList());
    }
}
