package com.howtographql.sampl.hackernewsgraphqljava.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logError;

@Getter
@RequiredArgsConstructor
public enum ObjectType {
    ENTITY("Model of entity"),
    PAGEABLE("Model of pageable entity list"),
    SPEC("Filter specifications");

    public static final String PROJECT_PATH = "com.howtographql.sampl.hackernewsgraphqljava.";

    private final String description;

    public Class getClass(Map<ObjectType, String> classes) {
        String classPath = PROJECT_PATH + classes.get(this);
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            logError("Class [%s] not found. (type %s)", classPath, this.getDescription());
            return null;
        }
    }
}
