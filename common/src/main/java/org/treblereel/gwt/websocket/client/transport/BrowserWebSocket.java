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

package org.treblereel.gwt.websocket.client.transport;

import java.util.Map;

import elemental2.core.ArrayBuffer;
import elemental2.core.Int8Array;
import elemental2.dom.WebSocket;
import jsinterop.base.Js;

public class BrowserWebSocket implements WebSocketTransport {

    private WebSocket ws;
    private boolean open;

    @Override
    public void connect(String url, String[] subprotocols, Map<String, String> headers,
            TransportListener listener) {
        if (subprotocols != null && subprotocols.length > 0) {
            ws = new WebSocket(url, subprotocols);
        } else {
            ws = new WebSocket(url);
        }

        ws.binaryType = "arraybuffer";

        ws.onopen = event -> {
            open = true;
            listener.onOpen();
        };

        ws.onmessage = event -> {
            if ("string".equals(Js.typeof(event.data))) {
                listener.onTextMessage(Js.uncheckedCast(event.data));
            } else {
                ArrayBuffer buffer = Js.uncheckedCast(event.data);
                Int8Array int8Array = new Int8Array(buffer);
                byte[] bytes = new byte[(int) int8Array.length];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = int8Array.getAt(i).byteValue();
                }
                listener.onBinaryMessage(bytes);
            }
        };

        ws.onclose = event -> {
            open = false;
            listener.onClose(
                    (int) event.code,
                    event.reason,
                    event.wasClean);
        };

        ws.onerror = event -> {
            listener.onError(new RuntimeException("WebSocket error"));
        };
    }

    @Override
    public void send(String message) {
        if (ws != null && open) {
            ws.send(message);
        }
    }

    @Override
    public void send(byte[] data) {
        if (ws != null && open) {
            Int8Array int8Array = new Int8Array(data.length);
            for (int i = 0; i < data.length; i++) {
                int8Array.setAt(i, (double) data[i]);
            }
            ws.send((ArrayBuffer) int8Array.buffer);
        }
    }

    @Override
    public void close(int code, String reason) {
        if (ws != null) {
            open = false;
            ws.close(code, reason);
        }
    }

    @Override
    public boolean isOpen() {
        return open;
    }
}
