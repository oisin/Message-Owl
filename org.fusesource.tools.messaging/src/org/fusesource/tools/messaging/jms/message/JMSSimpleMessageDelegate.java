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

import org.eclipse.emf.common.util.EList;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.jms.JMSDestination;
import org.fusesource.tools.messaging.jms.JMSSender;
import org.fusesource.tools.messaging.jms.JMSUtils;

public class JMSSimpleMessageDelegate {

    public Message getJMSMessage(org.fusesource.tools.core.message.Message simpleMessage, JMSSender sender)
            throws MessagingException {
        JMSDestination destination = (JMSDestination) sender.getDestination();
        Session session = JMSUtils.getJMSSession(destination);
        return getJMSMessage(simpleMessage, session);
    }

    public Message getJMSMessage(org.fusesource.tools.core.message.Message simpleMessage, Session session)
            throws MessagingException {
        Message jmsMessage;
        try {
            jmsMessage = createJMSMessage(session);
            populateHeadersAndProperties(simpleMessage, jmsMessage, session);
        } catch (Exception e) {
            throw new MessagingException("Failed to compose JMS Message", e);
        }
        return jmsMessage;
    }

    protected Message createJMSMessage(Session session) throws JMSException {
        return session.createMessage();
    }

    /**
     * Gets all headers and properties as Properties By default sets all properties as
     * StringProperties and required headers as JMS Headers.
     * 
     * @param simpleMessage
     * @param jmsMessage
     * @param session
     *            TODO
     * @throws JMSException
     */
    protected void populateHeadersAndProperties(org.fusesource.tools.core.message.Message simpleMessage,
            Message jmsMessage, Session session) throws JMSException {
        Properties properties = simpleMessage.getProperties();
        EList<Property> propertyList = properties.getProperty();
        for (Property property : propertyList) {
            if (!property.isIsheader()) {
                jmsMessage.setStringProperty(property.getName(), property.getValue());
            } else {
                if (JMSConstants.JMSCORRELATION_ID.equals(property.getName())) {
                    jmsMessage.setJMSCorrelationID(property.getValue());
                } else if (JMSConstants.JMSTYPE.equals(property.getName())) {
                    jmsMessage.setJMSType(property.getValue());
                } else if (JMSConstants.JMSREPLY_TO.equals(property.getName())) {
                    if (property.getValue() != null && property.getValue().trim().length() != 0) {
                        String value = property.getValue();
                        if (value.contains(":")) {
                            String[] strings = value.split(":");
                            if (strings.length > 0 && JMSConstants.QUEUE_TYPE.equals(strings[0])) {
                                jmsMessage.setJMSReplyTo(session.createQueue(strings[1]));
                                continue;
                            } else if (strings.length > 0 && JMSConstants.TOPIC_TYPE.equals(strings[0])) {
                                jmsMessage.setJMSReplyTo(session.createTopic(strings[1]));
                                continue;
                            }
                            jmsMessage.setJMSReplyTo(session.createTopic(value));
                        }
                    }
                }
            }
        }
    }
}
