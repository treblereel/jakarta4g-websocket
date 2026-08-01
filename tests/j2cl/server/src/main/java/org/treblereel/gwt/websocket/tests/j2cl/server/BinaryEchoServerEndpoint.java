package org.treblereel.gwt.websocket.tests.j2cl.server;

import jakarta.websocket.OnMessage;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo-binary")
public class BinaryEchoServerEndpoint {

    @OnMessage
    public byte[] onMessage(byte[] data) {
        return data;
    }
}
