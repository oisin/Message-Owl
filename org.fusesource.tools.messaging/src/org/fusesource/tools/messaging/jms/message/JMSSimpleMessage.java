/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.messaging.jms.message;

import javax.jms.Message;
import javax.jms.Session;

import org.fusesource.tools.message.types.SimpleMessage;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.jms.JMSMessage;
import org.fusesource.tools.messaging.jms.JMSSender;

public class JMSSimpleMessage extends SimpleMessage implements JMSMessage {

    public JMSSimpleMessage() {
    }

    public Message getJMSMessage(JMSSender sender) throws MessagingException {
        return getMsgDelegate().getJMSMessage(this, sender);
    }

    public Message getJMSMessage(Session session) throws MessagingException {
        return getMsgDelegate().getJMSMessage(this, session);
    }

    protected JMSSimpleMessageDelegate getMsgDelegate() {
        return new JMSSimpleMessageDelegate();
    }
}
