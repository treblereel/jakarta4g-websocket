package org.treblereel.gwt.websocket.apt;

import com.google.gwt.junit.client.GWTTestCase;
import jakarta.websocket.CloseReason;
import org.treblereel.gwt.websocket.apt.mock.MockWebSocketTransport;
import org.treblereel.gwt.websocket.apt.service.ChatEndpoint;
import org.treblereel.gwt.websocket.apt.service.ChatEndpoint_WebSocketEndpoint;
import org.treblereel.gwt.websocket.client.WebSocketConfig;

public class ClientEndpointGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.treblereel.gwt.websocket.WebSocketGwtTest";
    }

    private MockWebSocketTransport transport;
    private ChatEndpoint endpoint;
    private ChatEndpoint_WebSocketEndpoint proxy;

    @Override
    protected void gwtSetUp() {
        transport = new MockWebSocketTransport();
        endpoint = new ChatEndpoint();
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://localhost:8080/chat")
                .subprotocols("chat")
                .transport(transport)
                .build();
        proxy = new ChatEndpoint_WebSocketEndpoint(endpoint, config);
    }

    public void testConnect() {
        proxy.connect();
        assertEquals("ws://localhost:8080/chat", transport.getLastUrl());
        assertEquals("chat", transport.getLastSubprotocols()[0]);
    }

    public void testOnOpen() {
        proxy.connect();
        transport.simulateOpen();
        assertTrue(endpoint.opened);
        assertNotNull(endpoint.session);
    }

    public void testOnMessage() {
        proxy.connect();
        transport.simulateOpen();
        transport.simulateTextMessage("Hello World");
        assertEquals("Hello World", endpoint.lastMessage);
    }

    public void testOnClose() {
        proxy.connect();
        transport.simulateOpen();
        transport.simulateClose(1000, "Normal closure", true);
        assertNotNull(endpoint.lastCloseReason);
        assertEquals(1000, endpoint.lastCloseReason.getCloseCode().getCode());
        assertEquals("Normal closure", endpoint.lastCloseReason.getReasonPhrase());
    }

    public void testOnError() {
        proxy.connect();
        transport.simulateOpen();
        RuntimeException error = new RuntimeException("Connection lost");
        transport.simulateError(error);
        assertNotNull(endpoint.lastError);
        assertEquals("Connection lost", endpoint.lastError.getMessage());
    }

    public void testMultipleMessages() {
        proxy.connect();
        transport.simulateOpen();
        transport.simulateTextMessage("first");
        assertEquals("first", endpoint.lastMessage);
        transport.simulateTextMessage("second");
        assertEquals("second", endpoint.lastMessage);
        transport.simulateTextMessage("third");
        assertEquals("third", endpoint.lastMessage);
    }

    public void testFullLifecycle() {
        proxy.connect();
        assertNull(endpoint.lastMessage);
        transport.simulateOpen();
        assertTrue(endpoint.opened);
        transport.simulateTextMessage("hello");
        assertEquals("hello", endpoint.lastMessage);
        transport.simulateClose(1000, "Done", true);
        assertEquals(1000, endpoint.lastCloseReason.getCloseCode().getCode());
    }

    public void testSendTextViaSession() {
        proxy.connect();
        transport.simulateOpen();
        endpoint.session.getBasicRemote().sendText("outgoing message");
        assertEquals("outgoing message", transport.getLastSentMessage());
    }

    public void testCloseViaSession() {
        proxy.connect();
        transport.simulateOpen();
        endpoint.session.close();
        assertEquals(1000, transport.getLastCloseCode());
    }

    public void testCloseWithReason() {
        proxy.connect();
        transport.simulateOpen();
        CloseReason reason = new CloseReason(
                CloseReason.CloseCodes.GOING_AWAY, "Navigating away");
        endpoint.session.close(reason);
        assertEquals(1001, transport.getLastCloseCode());
        assertEquals("Navigating away", transport.getLastCloseReason());
    }

    public void testSessionUri() {
        proxy.connect();
        assertNotNull(proxy.getSession());
        assertEquals("ws://localhost:8080/chat", proxy.getSession().getRequestURI());
    }

    public void testSessionSubprotocol() {
        proxy.connect();
        assertEquals("chat", proxy.getSession().getNegotiatedSubprotocol());
    }
}
