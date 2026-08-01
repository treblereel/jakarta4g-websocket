/*
 * Copyright © 2024 Treblereel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treblereel.gwt.websocket.apt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jakarta.websocket.CloseReason;

import org.junit.Before;
import org.junit.Test;
import org.treblereel.gwt.websocket.apt.mock.MockWebSocketTransport;
import org.treblereel.gwt.websocket.apt.service.ChatEndpoint;
import org.treblereel.gwt.websocket.apt.service.ChatEndpoint_WebSocketEndpoint;
import org.treblereel.gwt.websocket.client.WebSocketConfig;

public class ClientEndpointTest {

    private MockWebSocketTransport transport;
    private ChatEndpoint endpoint;
    private ChatEndpoint_WebSocketEndpoint proxy;

    @Before
    public void setUp() {
        transport = new MockWebSocketTransport();
        endpoint = new ChatEndpoint();
        WebSocketConfig config = WebSocketConfig.builder()
                .url("ws://localhost:8080/chat")
                .subprotocols("chat")
                .transport(transport)
                .build();
        proxy = new ChatEndpoint_WebSocketEndpoint(endpoint, config);
    }

    @Test
    public void testConnect() {
        proxy.connect();
        assertEquals("ws://localhost:8080/chat", transport.getLastUrl());
        assertEquals("chat", transport.getLastSubprotocols()[0]);
    }

    @Test
    public void testOnOpen() {
        proxy.connect();
        transport.simulateOpen();
        assertTrue(endpoint.opened);
        assertNotNull(endpoint.session);
    }

    @Test
    public void testOnMessage() {
        proxy.connect();
        transport.simulateOpen();
        transport.simulateTextMessage("Hello World");
        assertEquals("Hello World", endpoint.lastMessage);
    }

    @Test
    public void testOnClose() {
        proxy.connect();
        transport.simulateOpen();
        transport.simulateClose(1000, "Normal closure", true);
        assertNotNull(endpoint.lastCloseReason);
        assertEquals(1000, endpoint.lastCloseReason.getCloseCode().getCode());
        assertEquals("Normal closure", endpoint.lastCloseReason.getReasonPhrase());
    }

    @Test
    public void testOnError() {
        proxy.connect();
        transport.simulateOpen();
        RuntimeException error = new RuntimeException("Connection lost");
        transport.simulateError(error);
        assertNotNull(endpoint.lastError);
        assertEquals("Connection lost", endpoint.lastError.getMessage());
    }

    @Test
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

    @Test
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

    @Test
    public void testSendTextViaSession() {
        proxy.connect();
        transport.simulateOpen();
        endpoint.session.getBasicRemote().sendText("outgoing message");
        assertEquals("outgoing message", transport.getLastSentMessage());
    }

    @Test
    public void testCloseViaSession() {
        proxy.connect();
        transport.simulateOpen();
        endpoint.session.close();
        assertEquals(1000, transport.getLastCloseCode());
    }

    @Test
    public void testCloseWithReason() {
        proxy.connect();
        transport.simulateOpen();
        CloseReason reason = new CloseReason(
                CloseReason.CloseCodes.GOING_AWAY, "Navigating away");
        endpoint.session.close(reason);
        assertEquals(1001, transport.getLastCloseCode());
        assertEquals("Navigating away", transport.getLastCloseReason());
    }

    @Test
    public void testSessionUri() {
        proxy.connect();
        assertNotNull(proxy.getSession());
        assertEquals("ws://localhost:8080/chat", proxy.getSession().getRequestURI());
    }

    @Test
    public void testSessionSubprotocol() {
        proxy.connect();
        assertEquals("chat", proxy.getSession().getNegotiatedSubprotocol());
    }
}
