package com.howtographql.sampl.hackernewsgraphqljava.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

@Configuration
public class AppStore {
    @Bean
    @Scope(value = "singleton", proxyMode = TARGET_CLASS)
    public Map<String, Object> store() {
        return new HashMap<>();
    }
}
