package com.howtographql.sampl.hackernewsgraphqljava.util;

import graphql.GraphQLException;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

@Log
@UtilityClass
public class Logging {
    public static void logInfo(String pattern, Object ... args) {
        log.info(String.format(pattern, args));
    }

    public static void logWarning(String pattern, Object ... args) {
        log.warning(String.format(pattern, args));
    }

    public static void logError(String pattern, Object ... args) throws GraphQLException {
        log.severe(String.format(pattern, args));
    }
}
