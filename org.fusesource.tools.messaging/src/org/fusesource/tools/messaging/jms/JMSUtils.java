/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.jms;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.eclipse.emf.common.util.EList;
import org.fusesource.tools.core.message.MessageFactory;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.core.IListener;

/**
 * Utility methods to provide functionality for JMS layer
 */
public class JMSUtils {

    public static Session getJMSSession(JMSDestination dest) {
        Session session = null;
        if (dest.getConnection() instanceof JMSConnection && dest.getConnection().isActive()) {
            session = ((JMSConnection) dest.getConnection()).getJMSSession();
        }
        return session;
    }

    // TODO add other types of jms destinations when required
    public static Destination getJMSDestination(JMSDestination destination) throws JMSException {
        Session session = JMSUtils.getJMSSession(destination);
        String type = destination.getDestinationType().getType();
        String name = destination.getDestinationName();
        return getJMSDestination(session, type, name);
    }

    public static Destination getJMSDestination(Session session, String type, String name) throws JMSException {
        Destination jmsDestination = null;
        if (session == null) {
            return null;
        }
        if (JMSConstants.QUEUE_TYPE.equals(type)) {
            jmsDestination = session.createQueue(name);
        } else if (JMSConstants.TOPIC_TYPE.equals(type)) {
            jmsDestination = session.createTopic(name);
        }
        return jmsDestination;
    }

    public static MessageProducer getMessageProducer(JMSDestination destination) throws JMSException {
        Session session = JMSUtils.getJMSSession(destination);
        if (session == null) {
            return null;
        }
        Destination jmsDestination = JMSUtils.getJMSDestination(destination);
        return session.createProducer(jmsDestination);
    }

    public static MessageConsumer getMessageConsumer(JMSDestination destination) throws JMSException {
        Session session = JMSUtils.getJMSSession(destination);
        if (session == null) {
            return null;
        }
        Destination jmsDestination = JMSUtils.getJMSDestination(destination);
        return session.createConsumer(jmsDestination);
    }

    public static MessageConsumer getMessageConsumer(JMSDestination destination, String msgSelector)
            throws JMSException {
        if (msgSelector == null) {
            return getMessageConsumer(destination);
        }
        Session session = JMSUtils.getJMSSession(destination);
        if (session == null) {
            return null;
        }
        Destination jmsDestination = JMSUtils.getJMSDestination(destination);
        return session.createConsumer(jmsDestination, msgSelector);
    }

    public static MessageConsumer getDurableSubscriber(JMSDestination destination, String subName, String msgSelector)
            throws JMSException {
        Session session = JMSUtils.getJMSSession(destination);
        if (session == null) {
            return null;
        }
        Destination jmsDestination = JMSUtils.getJMSDestination(destination);
        if (msgSelector != null) {
            return session.createDurableSubscriber((Topic) jmsDestination, subName, msgSelector, false);
        } else {
            return session.createDurableSubscriber((Topic) jmsDestination, subName);
        }
    }

    public static String getDestinationName(Destination destination) throws JMSException {
        if (destination instanceof Queue) {
            Queue qDest = (Queue) destination;
            return qDest.getQueueName();
        } else if (destination instanceof Topic) {
            Topic tDest = (Topic) destination;
            return tDest.getTopicName();
        }
        return null;
    }

    public static void populateHeaders(javax.jms.Message message, Properties properties) throws JMSException {
        MessageFactory messageFactory = MessageFactory.eINSTANCE;

        Property property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSCORRELATION_ID);
        property.setValue(message.getJMSCorrelationID());
        EList<Property> propertyList = properties.getProperty();
        propertyList.add(property);

        property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSPRIORITY);
        property.setValue(String.valueOf(message.getJMSPriority()));
        propertyList.add(property);

        property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSTYPE);
        property.setValue(message.getJMSType());
        propertyList.add(property);

        property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSDELIVERYMODE);
        property.setValue(String.valueOf(message.getJMSDeliveryMode()));
        propertyList.add(property);

        property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSMESSAGEID);
        property.setValue(message.getJMSMessageID());
        propertyList.add(property);

        property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSDESTINATION);
        property.setValue(JMSUtils.getDestinationName(message.getJMSDestination()));
        propertyList.add(property);

        property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSREPLY_TO);
        String replyTo = JMSUtils.getDestinationName(message.getJMSReplyTo());
        property.setValue(replyTo != null ? replyTo : IConstants.EMPTY_STRING);
        propertyList.add(property);

        property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSTIMESTAMP);
        setTimeStamp(message, property);
        propertyList.add(property);

        property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSREDELIVERED);
        property.setValue(String.valueOf(message.getJMSRedelivered()));
        propertyList.add(property);

        property = messageFactory.createProperty();
        property.setName(JMSConstants.JMSEXPIRATION);
        property.setValue(String.valueOf(message.getJMSExpiration()));
        propertyList.add(property);
    }

    private static void setTimeStamp(javax.jms.Message message, Property property) throws JMSException {
        long timeStamp = message.getJMSTimestamp();
        if (timeStamp != -1) {
            Date date = new Date(timeStamp);
            property.setValue(date.toString());
        } else {
            property.setValue(String.valueOf(timeStamp));
        }
    }

    public static boolean isDurableSubscriber(JMSListener listener) {
        // Can not use jmsListener.msgConsumer instanceof TopicSubscriber since
        // when no connection/session is available msgConsumer will be null
        // Assuming the listener is Durable Subscriber if the Durabale
        // Subscription name is present
        Map<String, Object> properties = listener.getProperties();
        if (properties.get(JMSConstants.DURABLE_SUBSCRIPTION_NAME) != null) {
            return true;
        }
        return false;
    }

    public static boolean hasActiveConnection(IListener listener) {
        IConnection connection = listener.getDestination() != null ? listener.getDestination().getConnection() : null;
        return connection != null && connection.isActive();
    }

    public static void unsubscribe(JMSListener listener) throws MessagingException, JMSException {
        if (listener.getProperties() == null && listener.getProperties().isEmpty()) {
            throw new MessagingException("Could not find subscribtion name");
        }
        String subName = (String) listener.getProperties().get(JMSConstants.DURABLE_SUBSCRIPTION_NAME);
        Session session = getJMSSession((JMSDestination) listener.getDestination());
        if (session == null) {
            throw new MessagingException("Could not unsubscribe the durable topic, session unavilable.");
        }
        session.unsubscribe(subName);
    }

    /**
     * @param message
     * @param properties
     * @throws JMSException
     */
    @SuppressWarnings("unchecked")
    public static void populateProperties(Message message, Properties properties) throws JMSException {
        EList<Property> propertyList = properties.getProperty();
        MessageFactory messageFactory = MessageFactory.eINSTANCE;
        Enumeration propertyNames = message.getPropertyNames();
        Property property = null;
        while (propertyNames.hasMoreElements()) {
            String propertyName = (String) propertyNames.nextElement();
            property = messageFactory.createProperty();
            property.setIsheader(false);
            property.setName(propertyName);
            property.setValue(message.getStringProperty(propertyName));
            propertyList.add(property);
        }
    }
}
