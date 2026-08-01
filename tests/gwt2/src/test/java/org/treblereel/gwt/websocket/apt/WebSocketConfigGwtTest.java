package org.treblereel.gwt.websocket.apt;

import com.google.gwt.junit.client.GWTTestCase;
import org.treblereel.gwt.websocket.apt.mock.MockWebSocketTransport;
import org.treblereel.gwt.websocket.client.WebSocketConfig;

public class WebSocketConfigGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.treblereel.gwt.websocket.WebSocketGwtTest";
    }

    public void testBuilderUrl() {
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://example.com/ws")
                .build();
        assertEquals("ws://example.com/ws", config.getUrl());
    }

    public void testBuilderSubprotocols() {
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://example.com/ws")
                .subprotocols("chat", "superchat")
                .build();
        assertEquals(2, config.getSubprotocols().length);
        assertEquals("chat", config.getSubprotocols()[0]);
        assertEquals("superchat", config.getSubprotocols()[1]);
    }

    public void testBuilderHeaders() {
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://example.com/ws")
                .header("Authorization", "Bearer token123")
                .header("X-Custom", "value")
                .build();
        assertEquals("Bearer token123",
                config.getHeaders().get("Authorization"));
        assertEquals("value", config.getHeaders().get("X-Custom"));
    }

    public void testBuilderTransport() {
        MockWebSocketTransport mock = new MockWebSocketTransport();
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://example.com/ws")
                .transport(mock)
                .build();
        assertEquals(mock, config.getTransport());
    }

    public void testDefaultTransport() {
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://example.com/ws")
                .build();
        assertNotNull(config.getTransport());
    }

    public void testEmptyDefaults() {
        WebSocketConfig config = WebSocketConfig.builder().build();
        assertEquals("", config.getUrl());
        assertEquals(0, config.getSubprotocols().length);
        assertTrue(config.getHeaders().isEmpty());
    }
}
