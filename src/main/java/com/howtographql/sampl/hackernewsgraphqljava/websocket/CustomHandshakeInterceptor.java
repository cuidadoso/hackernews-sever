package com.howtographql.sampl.hackernewsgraphqljava.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.UID;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logInfo;

public class CustomHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        logInfo("before handshake");
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        String uid = servletRequest.getServletRequest().getRequestedSessionId();
        // Long uid = (Long)session.getAttribute(UID);
        if(uid == null){
            return false;
        }

        //websocket.html?uid=1990:56 WebSocket connection to 'ws://localhost:8080/ws?uid=1990' failed: Error during WebSocket handshake: Unexpected response code: 500
        attributes.put(UID, uid);
        return true;

        // WebSocket connection to 'ws://localhost:8080/ws' failed: Error during WebSocket handshake: Unexpected response code: 200
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        logInfo("after handshake");
    }
}
