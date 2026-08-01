package org.treblereel.gwt.websocket.apt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.treblereel.gwt.websocket.apt.mock.MockWebSocketTransport;
import org.treblereel.gwt.websocket.apt.service.MinimalEndpoint;
import org.treblereel.gwt.websocket.apt.service.MinimalEndpoint_WebSocketEndpoint;
import org.treblereel.gwt.websocket.client.WebSocketConfig;

public class MinimalEndpointTest {

    private MockWebSocketTransport transport;
    private MinimalEndpoint endpoint;
    private MinimalEndpoint_WebSocketEndpoint proxy;

    @Before
    public void setUp() {
        transport = new MockWebSocketTransport();
        endpoint = new MinimalEndpoint();
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://localhost:8080/ws")
                .transport(transport)
                .build();
        proxy = new MinimalEndpoint_WebSocketEndpoint(endpoint, config);
    }

    @Test
    public void testMinimalOnMessage() {
        proxy.connect();
        transport.simulateOpen();
        transport.simulateTextMessage("test message");
        assertEquals("test message", endpoint.lastMessage);
    }

    @Test
    public void testNoSubprotocols() {
        proxy.connect();
        assertEquals(0, transport.getLastSubprotocols().length);
    }

    @Test
    public void testMessageBeforeOpen() {
        proxy.connect();
        assertNull(endpoint.lastMessage);
    }
}
