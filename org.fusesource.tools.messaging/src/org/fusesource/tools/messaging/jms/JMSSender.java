/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.jms;

import java.util.Collections;
import java.util.Map;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.ISender;
import org.fusesource.tools.messaging.jms.ui.JMSLabelProvider;

/**
 * Default implementation of ISender for JMS.
 */
public class JMSSender extends JMSLabelProvider implements ISender {
    private static final long serialVersionUID = -5610983889179726169L;

    private final JMSDestination destination;
    transient private MessageProducer msgProducer;
    private Map<String, Object> senderProps = Collections.emptyMap();
    private boolean isStarted;

    public JMSSender(JMSDestination destination, Map<String, Object> senderProps) {
        this.destination = destination;
        this.senderProps = senderProps;
    }

    protected void createProducer() throws MessagingException, JMSException {
        msgProducer = JMSUtils.getMessageProducer(destination);
        setProducerProperties();
    }

    public void start() throws MessagingException {
        try {
            createProducer();
            if (msgProducer == null) {
                // Allow working in offline mode, connection/session is not yet
                // available
                return;
            }
            isStarted = true;
        } catch (JMSException e) {
            isStarted = false;
            throw new MessagingException("Failed to create a Sender.", e);
        }
    }

    public void stop() throws MessagingException {
        if (msgProducer != null) {
            try {
                msgProducer.close();
                isStarted = false;
            } catch (JMSException e) {
                throw new MessagingException("Failed to stop the sender.", e);
            }
        }
    }

    protected void setProducerProperties() throws JMSException {
        if (msgProducer == null || senderProps == null || senderProps.isEmpty()) {
            return;
        }
        String deliveryMode = (String) senderProps.get(JMSConstants.DELIVERY_MODE);
        String ttl = (String) senderProps.get(JMSConstants.TIME_TO_LIVE);
        String priority = (String) senderProps.get(JMSConstants.PRIORITY);
        if (deliveryMode != null && deliveryMode.trim().length() > 0) {
            if (deliveryMode.equalsIgnoreCase(JMSConstants.NON_PERSISTENT)) {
                msgProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            } else if (deliveryMode.equalsIgnoreCase(JMSConstants.PERSISTENT)) {
                msgProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            }
        }
        if (ttl != null && ttl.trim().length() > 0) {
            try {
                msgProducer.setTimeToLive(Long.parseLong(ttl));
            } catch (NumberFormatException nfe) {
            }// Ignoring
        }
        if (priority != null && priority.trim().length() > 0) {
            try {
                msgProducer.setPriority(Integer.parseInt(priority));
            } catch (NumberFormatException nfe) {
            }// Ignoring
        }
    }

    public void send(org.fusesource.tools.core.message.Message msgToSend) throws MessagingException {
        try {
            if (!(msgToSend instanceof JMSMessage)) {
                throw new MessagingException("File is invalid to send on a JMS destination");
            }
            Message message = ((JMSMessage) msgToSend).getJMSMessage(this);
            // The reply to set on the message will take the preference over the
            // one set on the sender - When there is no ReplyTo set for a
            // message, use the sender's ReplyTo
            if (message.getJMSReplyTo() == null) {
                setReplyToDestination(message);
            }
            msgProducer.send(message);
        } catch (JMSException e) {
            throw new MessagingException("Failed to send the Message.", e);
        }
    }

    protected void setReplyToDestination(Message message) {
        if (senderProps == null || senderProps.isEmpty()) {
            return;
        }
        Object replyTo = senderProps.get(JMSConstants.JMSREPLY_TO);
        Session session = JMSUtils.getJMSSession(destination);
        try {
            Destination replyToDestination = null;
            if (replyTo instanceof String) {
                replyToDestination = JMSUtils.getJMSDestination(session, JMSConstants.TOPIC_TYPE, (String) replyTo);
            } else if (replyTo instanceof ReplyToInfo) {
                ReplyToInfo replyToInfo = (ReplyToInfo) replyTo;
                replyToDestination = JMSUtils.getJMSDestination(session, replyToInfo.getType().getType(), replyToInfo
                        .getName());
            }
            if (replyToDestination != null) {
                message.setJMSReplyTo(replyToDestination);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public IDestination getDestination() {
        return destination;
    }

    protected MessageProducer getJMSProducer() throws MessagingException {
        return msgProducer;
    }

    public Map<String, Object> getProperties() {
        return senderProps;
    }

    @Override
    protected String getDisplayText(Object element) {
        return destination != null ? destination.getDestinationName() : "";
    }

    public boolean isStarted() {
        return isStarted;
    }

}
