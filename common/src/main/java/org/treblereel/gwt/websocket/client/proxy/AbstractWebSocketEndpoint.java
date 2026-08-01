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

import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;

import org.treblereel.gwt.websocket.client.WebSocketConfig;
import org.treblereel.gwt.websocket.client.transport.TransportListener;
import org.treblereel.gwt.websocket.client.transport.WebSocketTransport;

public abstract class AbstractWebSocketEndpoint {

    private final WebSocketConfig config;
    private DefaultWebSocketSession session;
    private static int sessionCounter = 0;

    protected AbstractWebSocketEndpoint(WebSocketConfig config) {
        this.config = config;
    }

    public void connect() {
        WebSocketTransport transport = config.getTransport();
        String sessionId = "ws-session-" + (++sessionCounter);
        String[] subs = config.getSubprotocols();
        String subprotocol = (subs != null && subs.length > 0) ? subs[0] : "";
        session = new DefaultWebSocketSession(transport, sessionId,
                config.getUrl(), subprotocol);

        transport.connect(config.getUrl(), config.getSubprotocols(),
                config.getHeaders(), new TransportListener() {

                    @Override
                    public void onOpen() {
                        handleOpen(session);
                    }

                    @Override
                    public void onTextMessage(String data) {
                        handleTextMessage(data, session);
                    }

                    @Override
                    public void onBinaryMessage(byte[] data) {
                        handleBinaryMessage(data, session);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean wasClean) {
                        CloseReason.CloseCode closeCode =
                                CloseReason.CloseCodes.getCloseCode(code);
                        CloseReason closeReason = new CloseReason(closeCode, reason);
                        handleClose(closeReason, session);
                    }

                    @Override
                    public void onError(Throwable error) {
                        handleError(error, session);
                    }
                });
    }

    public Session getSession() {
        return session;
    }

    protected void handleOpen(Session session) {
    }

    protected void handleTextMessage(String message, Session session) {
    }

    protected void handleBinaryMessage(byte[] data, Session session) {
    }

    protected void handleClose(CloseReason closeReason, Session session) {
    }

    protected void handleError(Throwable error, Session session) {
    }
}
