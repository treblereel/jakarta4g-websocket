package org.treblereel.gwt.websocket.apt;

import static org.junit.Assert.assertEquals;

import jakarta.websocket.CloseReason;

import org.junit.Test;

public class CloseReasonTest {

    @Test
    public void testNormalClosure() {
        CloseReason reason = new CloseReason(
                CloseReason.CloseCodes.NORMAL_CLOSURE, "Done");
        assertEquals(1000, reason.getCloseCode().getCode());
        assertEquals("Done", reason.getReasonPhrase());
    }

    @Test
    public void testGoingAway() {
        CloseReason reason = new CloseReason(
                CloseReason.CloseCodes.GOING_AWAY, null);
        assertEquals(1001, reason.getCloseCode().getCode());
        assertEquals("", reason.getReasonPhrase());
    }

    @Test
    public void testAllStandardCodes() {
        assertEquals(1000,
                CloseReason.CloseCodes.NORMAL_CLOSURE.getCode());
        assertEquals(1001,
                CloseReason.CloseCodes.GOING_AWAY.getCode());
        assertEquals(1002,
                CloseReason.CloseCodes.PROTOCOL_ERROR.getCode());
        assertEquals(1003,
                CloseReason.CloseCodes.CANNOT_ACCEPT.getCode());
        assertEquals(1004,
                CloseReason.CloseCodes.RESERVED.getCode());
        assertEquals(1005,
                CloseReason.CloseCodes.NO_STATUS_CODE.getCode());
        assertEquals(1006,
                CloseReason.CloseCodes.CLOSED_ABNORMALLY.getCode());
        assertEquals(1007,
                CloseReason.CloseCodes.NOT_CONSISTENT.getCode());
        assertEquals(1008,
                CloseReason.CloseCodes.VIOLATED_POLICY.getCode());
        assertEquals(1009,
                CloseReason.CloseCodes.TOO_BIG.getCode());
        assertEquals(1010,
                CloseReason.CloseCodes.NO_EXTENSION.getCode());
        assertEquals(1011,
                CloseReason.CloseCodes.UNEXPECTED_CONDITION.getCode());
        assertEquals(1012,
                CloseReason.CloseCodes.SERVICE_RESTART.getCode());
        assertEquals(1013,
                CloseReason.CloseCodes.TRY_AGAIN_LATER.getCode());
        assertEquals(1015,
                CloseReason.CloseCodes.TLS_HANDSHAKE_FAILURE.getCode());
    }

    @Test
    public void testGetCloseCodeKnown() {
        CloseReason.CloseCode code = CloseReason.CloseCodes.getCloseCode(1000);
        assertEquals(1000, code.getCode());
        assertEquals(CloseReason.CloseCodes.NORMAL_CLOSURE, code);
    }

    @Test
    public void testGetCloseCodeCustom() {
        CloseReason.CloseCode code = CloseReason.CloseCodes.getCloseCode(4000);
        assertEquals(4000, code.getCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCloseCodeInvalid() {
        CloseReason.CloseCodes.getCloseCode(999);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCloseCode() {
        new CloseReason(null, "test");
    }

    @Test
    public void testToString() {
        CloseReason reason = new CloseReason(
                CloseReason.CloseCodes.NORMAL_CLOSURE, "bye");
        assertEquals("CloseReason[1000,bye]", reason.toString());
    }

    @Test
    public void testToStringNoReason() {
        CloseReason reason = new CloseReason(
                CloseReason.CloseCodes.NORMAL_CLOSURE, null);
        assertEquals("CloseReason[1000]", reason.toString());
    }
}
