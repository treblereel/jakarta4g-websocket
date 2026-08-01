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

package org.treblereel.gwt.websocket.client.proxy;

import java.util.HashMap;
import java.util.Map;

import jakarta.websocket.CloseReason;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;

import org.treblereel.gwt.websocket.client.transport.WebSocketTransport;

public class DefaultWebSocketSession implements Session {

    private final WebSocketTransport transport;
    private final String id;
    private final String uri;
    private final String negotiatedSubprotocol;
    private final BasicRemote basicRemote;
    private final Map<String, Object> userProperties = new HashMap<>();

    public DefaultWebSocketSession(WebSocketTransport transport, String id,
            String uri, String negotiatedSubprotocol) {
        this.transport = transport;
        this.id = id;
        this.uri = uri;
        this.negotiatedSubprotocol = negotiatedSubprotocol != null
                ? negotiatedSubprotocol : "";
        this.basicRemote = new BasicRemote();
    }

    @Override
    public RemoteEndpoint.Basic getBasicRemote() {
        return basicRemote;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isOpen() {
        return transport.isOpen();
    }

    @Override
    public void close() {
        transport.close(
                CloseReason.CloseCodes.NORMAL_CLOSURE.getCode(),
                "Normal closure");
    }

    @Override
    public void close(CloseReason closeReason) {
        transport.close(
                closeReason.getCloseCode().getCode(),
                closeReason.getReasonPhrase());
    }

    @Override
    public String getRequestURI() {
        return uri;
    }

    @Override
    public String getNegotiatedSubprotocol() {
        return negotiatedSubprotocol;
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return userProperties;
    }

    private class BasicRemote implements RemoteEndpoint.Basic {

        @Override
        public void sendText(String text) {
            transport.send(text);
        }

        @Override
        public void sendBinary(byte[] data) {
            transport.send(data);
        }
    }
}
