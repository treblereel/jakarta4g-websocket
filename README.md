# Jakarta WebSocket for GWT / J2CL

Client-side [Jakarta WebSocket](https://jakarta.ee/specifications/websocket/) implementation for [J2CL](https://github.com/nicoleengineer/nicoleengineer.github.io/blob/main/nicoleengineer.md) and [GWT](http://www.gwtproject.org/) projects. Uses the browser's native `WebSocket` API under the hood while exposing the standard Jakarta WebSocket annotation-driven programming model.

## Features

- Standard Jakarta WebSocket annotations: `@ClientEndpoint`, `@OnOpen`, `@OnMessage`, `@OnClose`, `@OnError`
- `Session` API for sending text and binary messages
- `CloseReason` with all standard close codes
- Annotation processor generates endpoint proxies at compile time
- Works with both J2CL and GWT 2 compilers
- Pluggable transport layer (defaults to browser `WebSocket`)

## Maven coordinates

```xml
<dependency>
    <groupId>org.treblereel.gwt.jakarta.websocket</groupId>
    <artifactId>common</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```

Add the annotation processor to your compiler configuration:

```xml
<annotationProcessorPaths>
    <path>
        <groupId>org.treblereel.gwt.jakarta.websocket</groupId>
        <artifactId>processor</artifactId>
        <version>0.1-SNAPSHOT</version>
    </path>
</annotationProcessorPaths>
```

## Usage

### 1. Define a client endpoint

```java
import jakarta.websocket.*;

@ClientEndpoint
public class ChatEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        session.getBasicRemote().sendText("hello");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // handle incoming message
    }

    @OnClose
    public void onClose(CloseReason reason) {
        // handle close
    }

    @OnError
    public void onError(Throwable error) {
        // handle error
    }
}
```

The annotation processor generates a `ChatEndpoint_WebSocketEndpoint` proxy class at compile time.

### 2. Connect

```java
ChatEndpoint endpoint = new ChatEndpoint();

WebSocketConfig config = WebSocketConfig.builder()
        .url("ws://localhost:8080/chat")
        .subprotocols("chat")
        .build();

ChatEndpoint_WebSocketEndpoint proxy =
        new ChatEndpoint_WebSocketEndpoint(endpoint, config);
proxy.connect();
```

### 3. GWT module descriptor

For GWT 2 projects, inherit the module in your `.gwt.xml`:

```xml
<inherits name="org.treblereel.gwt.websocket.Common"/>
```

## Project structure

```
jakarta4g-websocket/
├── jakarta/      - Jakarta WebSocket API annotations and interfaces
├── common/       - Runtime library (transport, config, session, endpoint base)
├── processor/    - Annotation processor (generates _WebSocketEndpoint proxies)
└── tests/
    ├── jre/      - JUnit 4 unit tests (mock transport, no browser required)
    ├── j2cl/     - J2CL integration tests
    │   ├── client/   - J2CL client app compiled to JavaScript
    │   └── server/   - Quarkus WebSocket server + Selenium tests
    └── gwt2/     - GWT 2 tests (GWTTestCase)
```

## Supported API

| Jakarta WebSocket | Status |
|---|---|
| `@ClientEndpoint` | Supported |
| `@OnOpen` | Supported |
| `@OnMessage` (text) | Supported |
| `@OnMessage` (binary) | Supported |
| `@OnClose` | Supported |
| `@OnError` | Supported |
| `Session` | Subset (send, close, properties, id, uri, subprotocol) |
| `RemoteEndpoint.Basic` | `sendText`, `sendBinary` |
| `CloseReason` / `CloseCodes` | Full |

Server-side annotations (`@ServerEndpoint`) are not supported — this is a client-only library.

## Building

```bash
mvn clean install
```

To skip tests:

```bash
mvn clean install -DskipTests
```

## License

Apache License 2.0
