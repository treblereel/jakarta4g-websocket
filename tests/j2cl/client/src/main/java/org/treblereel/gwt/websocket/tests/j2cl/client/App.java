package org.treblereel.gwt.websocket.tests.j2cl.client;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import org.treblereel.gwt.websocket.client.WebSocketConfig;
import org.treblereel.j2cl.processors.annotations.GWT3EntryPoint;

public class App {

    @GWT3EntryPoint
    public void onModuleLoad() {
        HTMLDivElement statusDiv = (HTMLDivElement) DomGlobal.document.getElementById("statusDiv");
        HTMLDivElement messagesDiv = (HTMLDivElement) DomGlobal.document.getElementById("messagesDiv");

        EchoEndpoint endpoint = new EchoEndpoint(statusDiv, messagesDiv);

        String wsUrl = "ws://" + DomGlobal.window.location.hostname + ":"
                + DomGlobal.window.location.port + "/echo";

        WebSocketConfig config = WebSocketConfig.builder()
                .url(wsUrl)
                .build();

        EchoEndpoint_WebSocketEndpoint wsEndpoint =
                new EchoEndpoint_WebSocketEndpoint(endpoint, config);
        wsEndpoint.connect();
    }
}
