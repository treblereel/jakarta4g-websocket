package org.treblereel.gwt.websocket.apt;

import com.google.gwt.junit.client.GWTTestCase;
import org.treblereel.gwt.websocket.apt.mock.MockWebSocketTransport;
import org.treblereel.gwt.websocket.apt.service.MinimalEndpoint;
import org.treblereel.gwt.websocket.apt.service.MinimalEndpoint_WebSocketEndpoint;
import org.treblereel.gwt.websocket.client.WebSocketConfig;

public class MinimalEndpointGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.treblereel.gwt.websocket.WebSocketGwtTest";
    }

    private MockWebSocketTransport transport;
    private MinimalEndpoint endpoint;
    private MinimalEndpoint_WebSocketEndpoint proxy;

    @Override
    protected void gwtSetUp() {
        transport = new MockWebSocketTransport();
        endpoint = new MinimalEndpoint();
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://localhost:8080/ws")
                .transport(transport)
                .build();
        proxy = new MinimalEndpoint_WebSocketEndpoint(endpoint, config);
    }

    public void testMinimalOnMessage() {
        proxy.connect();
        transport.simulateOpen();
        transport.simulateTextMessage("test message");
        assertEquals("test message", endpoint.lastMessage);
    }

    public void testNoSubprotocols() {
        proxy.connect();
        assertEquals(0, transport.getLastSubprotocols().length);
    }

    public void testMessageBeforeOpen() {
        proxy.connect();
        assertNull(endpoint.lastMessage);
    }
}
