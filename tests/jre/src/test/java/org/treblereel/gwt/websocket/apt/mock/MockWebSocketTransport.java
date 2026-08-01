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

package org.treblereel.gwt.websocket.apt.mock;

import java.util.Map;

import org.treblereel.gwt.websocket.client.transport.TransportListener;
import org.treblereel.gwt.websocket.client.transport.WebSocketTransport;

public class MockWebSocketTransport implements WebSocketTransport {

    private TransportListener listener;
    private String lastUrl;
    private String[] lastSubprotocols;
    private Map<String, String> lastHeaders;
    private String lastSentMessage;
    private byte[] lastSentBinaryMessage;
    private int lastCloseCode;
    private String lastCloseReason;
    private boolean open;
    private int sendCount;

    @Override
    public void connect(String url, String[] subprotocols, Map<String, String> headers,
            TransportListener listener) {
        this.lastUrl = url;
        this.lastSubprotocols = subprotocols;
        this.lastHeaders = headers;
        this.listener = listener;
    }

    @Override
    public void send(String message) {
        this.lastSentMessage = message;
        this.sendCount++;
    }

    @Override
    public void send(byte[] data) {
        this.lastSentBinaryMessage = data;
        this.sendCount++;
    }

    @Override
    public void close(int code, String reason) {
        this.lastCloseCode = code;
        this.lastCloseReason = reason;
        this.open = false;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    public void simulateOpen() {
        open = true;
        if (listener != null) {
            listener.onOpen();
        }
    }

    public void simulateTextMessage(String data) {
        if (listener != null) {
            listener.onTextMessage(data);
        }
    }

    public void simulateBinaryMessage(byte[] data) {
        if (listener != null) {
            listener.onBinaryMessage(data);
        }
    }

    public void simulateClose(int code, String reason, boolean wasClean) {
        open = false;
        if (listener != null) {
            listener.onClose(code, reason, wasClean);
        }
    }

    public void simulateError(Throwable error) {
        if (listener != null) {
            listener.onError(error);
        }
    }

    public String getLastUrl() {
        return lastUrl;
    }

    public String[] getLastSubprotocols() {
        return lastSubprotocols;
    }

    public Map<String, String> getLastHeaders() {
        return lastHeaders;
    }

    public String getLastSentMessage() {
        return lastSentMessage;
    }

    public byte[] getLastSentBinaryMessage() {
        return lastSentBinaryMessage;
    }

    public int getLastCloseCode() {
        return lastCloseCode;
    }

    public String getLastCloseReason() {
        return lastCloseReason;
    }

    public int getSendCount() {
        return sendCount;
    }
}
