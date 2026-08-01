/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package jakarta.websocket;

/**
 * J2CL-compatible subset of the Jakarta WebSocket RemoteEndpoint API.
 *
 * <p>Only {@link Basic} is supported in the browser environment.
 */
public interface RemoteEndpoint {

    /**
     * Sends messages synchronously to the peer.
     */
    interface Basic extends RemoteEndpoint {

        /**
         * Send a text message, blocking until all of the message has been transmitted.
         *
         * @param text the text to send
         */
        void sendText(String text);

        /**
         * Send a binary message, blocking until all of the message has been transmitted.
         *
         * @param data the binary data to send
         */
        void sendBinary(byte[] data);
    }
}
