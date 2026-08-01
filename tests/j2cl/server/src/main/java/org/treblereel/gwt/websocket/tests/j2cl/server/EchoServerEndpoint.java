package org.treblereel.gwt.websocket.tests.j2cl.server;

import jakarta.websocket.OnMessage;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class EchoServerEndpoint {

    @OnMessage
    public String onMessage(String message) {
        return message;
    }
}
