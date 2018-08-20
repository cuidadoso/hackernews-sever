package com.howtographql.sampl.hackernewsgraphqljava.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.Map;

public class JsonKit {
    private static final Gson GSON = new GsonBuilder()
            // This is important because the graphql spec says that null values should be present
            .serializeNulls()
            .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
            .create();

    public static String toJsonString(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return GSON.fromJson(jsonString, clazz);
    }

    public static <T> T fromJson(JsonElement jsonElement, Class<T> clazz) {
        return GSON.fromJson(jsonElement, clazz);
    }

    public static JsonElement toJsonTree(Object src) {
        return GSON.toJsonTree(src);
    }

    public static Map<String, Object> toMap(String jsonStr) {
        if (jsonStr == null || jsonStr.trim().length() == 0) {
            return java.util.Collections.emptyMap();
        }
        // gson uses type tokens for generic input like Map<String,Object>
        TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {};
        Map<String, Object> map = GSON.fromJson(jsonStr, typeToken.getType());
        return map == null ? Collections.emptyMap() : map;
    }
}
