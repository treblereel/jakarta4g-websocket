package org.treblereel.gwt.websocket.tests.j2cl.client;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ClientEndpoint
public class EchoEndpoint {

    private final HTMLDivElement statusDiv;
    private final HTMLDivElement messagesDiv;

    public EchoEndpoint(HTMLDivElement statusDiv, HTMLDivElement messagesDiv) {
        this.statusDiv = statusDiv;
        this.messagesDiv = messagesDiv;
    }

    @OnOpen
    public void onOpen(Session session) {
        statusDiv.textContent = "CONNECTED";
        session.getBasicRemote().sendText("hello");
    }

    @OnMessage
    public void onMessage(String message) {
        HTMLDivElement msgDiv = (HTMLDivElement) DomGlobal.document.createElement("div");
        msgDiv.className = "message";
        msgDiv.textContent = message;
        messagesDiv.appendChild(msgDiv);
    }

    @OnClose
    public void onClose(CloseReason reason) {
        statusDiv.textContent = "CLOSED:" + reason.getCloseCode().getCode();
    }

    @OnError
    public void onError(Throwable error) {
        statusDiv.textContent = "ERROR:" + error.getMessage();
    }
}
