package com.howtographql.sampl.hackernewsgraphqljava.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

@Configuration
@EnableWebSocket
public class AppBeans {
    @Bean
    @Scope(value = "singleton", proxyMode = TARGET_CLASS)
    public Map<String, Object> store() {
        return new HashMap<>();
    }
}
