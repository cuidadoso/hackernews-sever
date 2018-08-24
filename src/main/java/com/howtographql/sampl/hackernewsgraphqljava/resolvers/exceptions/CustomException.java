package com.howtographql.sampl.hackernewsgraphqljava.resolvers.exceptions;

import graphql.GraphQLException;

public class CustomException extends GraphQLException {
    public CustomException(String message) {
        super(message);
    }
}
