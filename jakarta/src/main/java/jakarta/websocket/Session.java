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

import java.util.Map;

/**
 * J2CL-compatible subset of the Jakarta WebSocket Session API.
 *
 * <p>Represents a conversation between two web socket endpoints.
 * Methods that are not supported in the browser environment are omitted.
 */
public interface Session {

    /**
     * Return a reference to the {@link RemoteEndpoint.Basic} object representing the peer of this conversation
     * that is able to send messages synchronously to the peer.
     *
     * @return the remote endpoint
     */
    RemoteEndpoint.Basic getBasicRemote();

    /**
     * Return a string containing the unique identifier assigned to this session.
     *
     * @return the unique identifier
     */
    String getId();

    /**
     * Return true if and only if the underlying socket is open.
     *
     * @return whether the session is active
     */
    boolean isOpen();

    /**
     * Close the current conversation with a normal status code and no reason phrase.
     */
    void close();

    /**
     * Close the current conversation, giving a reason for the closure.
     *
     * @param closeReason the reason for the closure
     */
    void close(CloseReason closeReason);

    /**
     * Return the URI under which this session was opened, including the query string if there is one.
     *
     * @return the request URI as a string
     */
    String getRequestURI();

    /**
     * Return the sub protocol agreed during the websocket handshake for this conversation.
     *
     * @return the negotiated subprotocol, or the empty string if there isn't one
     */
    String getNegotiatedSubprotocol();

    /**
     * While the session is open, this method returns a {@link Map} that the developer may use to store application
     * specific information relating to this session instance. The developer may retrieve information from this
     * Map at any time between the opening and during the closure of the session.
     *
     * @return an editable Map of application data
     */
    Map<String, Object> getUserProperties();
}
