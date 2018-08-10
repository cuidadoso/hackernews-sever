package com.howtographql.sampl.hackernewsgraphqljava.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UtilityClass
public class Collections {
    public static <E> Collection<E> makeCollection(Iterable<E> iter) {
        Collection<E> list = new ArrayList<>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }

    public static <E> List<E> makeList(Iterable<E> iter) {
        List<E> list = new ArrayList<>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }
}
