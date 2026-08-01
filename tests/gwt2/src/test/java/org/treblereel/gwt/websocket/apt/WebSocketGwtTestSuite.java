package org.treblereel.gwt.websocket.apt;

import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;

public class WebSocketGwtTestSuite {

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite("Jakarta WebSocket GWT2 Tests");

        suite.addTestSuite(ClientEndpointGwtTest.class);
        suite.addTestSuite(CloseReasonGwtTest.class);
        suite.addTestSuite(WebSocketConfigGwtTest.class);
        suite.addTestSuite(MinimalEndpointGwtTest.class);

        return suite;
    }
}
