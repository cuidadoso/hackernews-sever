package com.howtographql.sampl.hackernewsgraphqljava.util;

import com.howtographql.sampl.hackernewsgraphqljava.resolvers.exceptions.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum ObjectType {
    ENTITY("Model of entity"),
    PAGEABLE("Model of pageable entity list"),
    SPEC("Filter specifications"),
    PROJECT_PATH("Project path");

    private final String description;

    public Class getClass(Map<ObjectType, String> classes) {
        String classPath = classes.get(PROJECT_PATH) + "." + classes.get(this);
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            throw new CustomException(String.format("Class [%s] not found. (type %s)", classPath, this.getDescription()));
        }
    }
}
