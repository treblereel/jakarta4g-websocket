package org.treblereel.gwt.websocket.apt.service;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ClientEndpoint
public class ChatEndpoint {

    public boolean opened;
    public Session session;
    public String lastMessage;
    public CloseReason lastCloseReason;
    public Throwable lastError;

    @OnOpen
    public void onOpen(Session session) {
        this.opened = true;
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        this.lastMessage = message;
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        this.lastCloseReason = closeReason;
    }

    @OnError
    public void onError(Throwable error) {
        this.lastError = error;
    }
}
