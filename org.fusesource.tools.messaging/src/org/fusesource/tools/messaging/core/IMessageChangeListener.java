/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.core;

import java.util.List;

import org.fusesource.tools.messaging.MessageEvent;

/**
 * This interface can be implemented by clients to subscribe to message change events of a listener @see
 * {@link IMessagesManager#addMessageChangeListener(IMessageChangeListener)} The IMessagesManager
 * associated by the listener can be obtained from @see {@link IListener#getMessagesManager()}
 */
public interface IMessageChangeListener {
    /**
     * Event kind constants
     */
    public static final int MESSAGE_ADDED = 0x10;

    public static final int MESSAGE_REMOVED = 0x20;

    public static final int MESSAGE_UNREAD = 0x30;

    public static final int MESSAGE_READ = 0x40;

    /**
     * Fired when there is any change in the list of messages received by the listener
     * 
     * @param me
     *            - Represents the MessageEvent - this event wraps the received message in it
     * @param kind
     *            - Kind of the event
     */
    public void messageChangeEvent(MessageEvent me, int kind);

    /**
     * Fired when all the queued messages of the listener is purged
     * 
     * @param clearedMsg
     *            - The list of message events whose messages are purged
     */
    public void messagesClearedEvent(List<MessageEvent> clearedMsgs);

}
