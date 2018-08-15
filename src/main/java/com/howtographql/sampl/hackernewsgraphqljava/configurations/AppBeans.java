package com.howtographql.sampl.hackernewsgraphqljava.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

@Configuration
public class AppBeans {
    @Bean
    @Scope(value = "singleton", proxyMode = TARGET_CLASS)
    public Map<String, Object> store() {
        return new HashMap<>();
    }

    @Bean
    @Scope(value = "singleton", proxyMode = TARGET_CLASS)
    public AbstractWebSocketMessageBrokerConfigurer webSocket() {
        return new AbstractWebSocketMessageBrokerConfigurer() {
            @Override
            public void registerStompEndpoints(StompEndpointRegistry registry) {
                registry.addEndpoint("/subscriptions").withSockJS();
                registry.addEndpoint("/color").withSockJS();
            }
        };
    }
}
