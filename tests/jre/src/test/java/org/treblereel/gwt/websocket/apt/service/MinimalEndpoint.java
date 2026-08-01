package org.treblereel.gwt.websocket.apt.service;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;

@ClientEndpoint
public class MinimalEndpoint {

    public String lastMessage;

    @OnMessage
    public void onMessage(String message) {
        this.lastMessage = message;
    }
}
