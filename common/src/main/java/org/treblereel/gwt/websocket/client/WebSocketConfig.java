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

package org.treblereel.gwt.websocket.client;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.treblereel.gwt.websocket.client.transport.BrowserWebSocket;
import org.treblereel.gwt.websocket.client.transport.WebSocketTransport;

public class WebSocketConfig {

    private final String url;
    private final String[] subprotocols;
    private final Map<String, String> headers;
    private final WebSocketTransport transport;

    private WebSocketConfig(String url, String[] subprotocols,
            Map<String, String> headers, WebSocketTransport transport) {
        this.url = url;
        this.subprotocols = subprotocols;
        this.headers = Collections.unmodifiableMap(headers);
        this.transport = transport;
    }

    public String getUrl() {
        return url;
    }

    public String[] getSubprotocols() {
        return subprotocols;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public WebSocketTransport getTransport() {
        return transport;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String url = "";
        private String[] subprotocols = new String[0];
        private final Map<String, String> headers = new LinkedHashMap<>();
        private WebSocketTransport transport;

        private Builder() {
        }

        public Builder url(String url) {
            this.url = url != null ? url : "";
            return this;
        }

        public Builder subprotocols(String... subprotocols) {
            this.subprotocols = subprotocols != null ? subprotocols : new String[0];
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder transport(WebSocketTransport transport) {
            this.transport = transport;
            return this;
        }

        public WebSocketConfig build() {
            WebSocketTransport t = transport != null ? transport : new BrowserWebSocket();
            return new WebSocketConfig(url, subprotocols, headers, t);
        }
    }
}
