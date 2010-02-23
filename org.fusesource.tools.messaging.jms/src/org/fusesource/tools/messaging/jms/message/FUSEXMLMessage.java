/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.messaging.jms.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.fusesource.tools.message.types.XMLMessage;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.jms.JMSMessage;
import org.fusesource.tools.messaging.jms.JMSSender;

public class FUSEXMLMessage extends XMLMessage implements JMSMessage {

    public FUSEXMLMessage() {
        super();
    }

    public Message getJMSMessage(JMSSender sender) throws MessagingException {
        Message message = getMsgDelegate().getJMSMessage(this, sender);
        setContent(message);
        return message;
    }

    public Message getJMSMessage(Session session) throws MessagingException {
        Message message = getMsgDelegate().getJMSMessage(this, session);
        setContent(message);
        return message;
    }

    protected JMSSimpleMessageDelegate getMsgDelegate() {
        return new JMSSimpleMessageDelegate() {
            @Override
            protected Message createJMSMessage(Session session) throws JMSException {
                return session.createTextMessage();
            }
        };
    }

    private void setContent(Message message) throws MessagingException {
        try {
            ((javax.jms.TextMessage) message).setText(getMessageBody());
        } catch (JMSException e) {
            e.printStackTrace();
            throw new MessagingException("Failed to compose JMS Message");
        }
    }

}
