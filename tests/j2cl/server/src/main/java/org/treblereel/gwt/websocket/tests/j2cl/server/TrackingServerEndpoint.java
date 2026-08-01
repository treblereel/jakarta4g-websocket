package org.treblereel.gwt.websocket.tests.j2cl.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/tracking")
public class TrackingServerEndpoint {

    private static final List<String> events = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(Session session) {
        events.add("OPEN");
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        events.add("TEXT:" + message);
        if ("TRIGGER_ERROR".equals(message)) {
            throw new RuntimeException("Intentional test error");
        }
        return "ECHO:" + message;
    }

    @OnClose
    public void onClose(CloseReason reason) {
        events.add("CLOSE:" + reason.getCloseCode().getCode());
    }

    @OnError
    public void onError(Throwable error) {
        events.add("ERROR:" + error.getMessage());
    }

    public static List<String> getEvents() {
        return new ArrayList<>(events);
    }

    public static void clearEvents() {
        events.clear();
    }
}
