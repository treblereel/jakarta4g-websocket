package org.treblereel.gwt.websocket.apt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.treblereel.gwt.websocket.apt.mock.MockWebSocketTransport;
import org.treblereel.gwt.websocket.client.WebSocketConfig;

public class WebSocketConfigTest {

    @Test
    public void testBuilderUrl() {
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://example.com/ws")
                .build();
        assertEquals("ws://example.com/ws", config.getUrl());
    }

    @Test
    public void testBuilderSubprotocols() {
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://example.com/ws")
                .subprotocols("chat", "superchat")
                .build();
        assertArrayEquals(new String[]{"chat", "superchat"},
                config.getSubprotocols());
    }

    @Test
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

    @Test
    public void testBuilderTransport() {
        MockWebSocketTransport mock = new MockWebSocketTransport();
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://example.com/ws")
                .transport(mock)
                .build();
        assertEquals(mock, config.getTransport());
    }

    @Test
    public void testDefaultTransport() {
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://example.com/ws")
                .build();
        assertNotNull(config.getTransport());
    }

    @Test
    public void testEmptyDefaults() {
        WebSocketConfig config = WebSocketConfig.builder().build();
        assertEquals("", config.getUrl());
        assertEquals(0, config.getSubprotocols().length);
        assertTrue(config.getHeaders().isEmpty());
    }

    private void assertTrue(boolean value) {
        org.junit.Assert.assertTrue(value);
    }
}
