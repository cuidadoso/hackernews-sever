package com.howtographql.sampl.hackernewsgraphqljava.service;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class SessionService {
    @Resource(name = "store")
    private Map<String, Object> store;

    public void saveUserId(Long userId) {
        store.put("userId", userId);
    }

    public Long userId() {
        return (Long) store.get("userId");
    }
}
