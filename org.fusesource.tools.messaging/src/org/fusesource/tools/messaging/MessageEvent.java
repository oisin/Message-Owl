/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging;

import java.util.Collections;
import java.util.Map;

import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.IMessageChangeListener;
import org.fusesource.tools.messaging.core.IMessagesManager;

/**
 * This class wraps the received message as an Object in it. The flag of the message event is
 * updated by the MessagesManager associated for this message event. Clients can extend this class
 * to create customized message events
 * 
 * @see IMessagesManager#resetFlag(MessageEvent, int)
 */
public class MessageEvent {
    protected Object receivedMessage;
    protected IListener source;
    /**
     * Each key from a metadata map becomes a column in MessagesEditor UI
     */
    protected Map<String, String> msgMetadata = Collections.emptyMap();

    protected int flag;

    public MessageEvent(Object message, IListener src) {
        this.receivedMessage = message;
        source = src;
        flag = IMessageChangeListener.MESSAGE_UNREAD;
    }

    public IListener getSource() {
        return source;
    }

    public Map<String, String> getMetadata() {
        return msgMetadata;
    }

    public Object getMessage() {
        return receivedMessage;
    }

    public void setFlag(int newFlag) {
        this.flag = newFlag;
    }

    public int getFlag() {
        return flag;
    }
}
