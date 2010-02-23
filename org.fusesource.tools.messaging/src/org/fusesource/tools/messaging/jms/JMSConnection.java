/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.jms;

import java.util.Collections;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IDestinationType;
import org.fusesource.tools.messaging.core.IProvider;

/**
 * Abstract implementation of IConnection for JMS. Clients should provide implementation for
 * initialize method and use the connection properties to create a provider specific JMS Connection.
 * By Default creates a NON- TRANSACTED & AUTO ACK JMS session
 */
public abstract class JMSConnection implements IConnection {
    private final JMSProvider jmsProvider;
    protected Map<String, String> connectionProperties = Collections.emptyMap();

    protected Connection jmsConnection;
    protected Session session;

    public JMSConnection(JMSProvider provider, Map<String, String> properties) {
        this.jmsProvider = provider;
        this.connectionProperties = properties;
        try {
            initialize();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    protected void createSession() throws JMSException {
        if (jmsConnection != null) {
            session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
    }

    public IDestination createDestination(IDestinationType type, String name) throws MessagingException {
        return new JMSDestination(this, type, name);
    }

    public void closeConnection() throws MessagingException {
        if (jmsConnection != null) {
            try {
                if (session != null) {
                    session.close();
                }
                jmsConnection.close();
                System.out.println("[JMSConnection] JMS Connection Closed...");
            } catch (JMSException e) {
                throw new MessagingException(e.getMessage(), e.getCause());
            } finally {
                jmsConnection = null;
                session = null;
            }
        }
    }

    public Connection getJMSConnection() {
        return jmsConnection;
    }

    public Session getJMSSession() {
        return session;
    }

    public IProvider getProvider() {
        return jmsProvider;
    }

    public Map<String, String> getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Map<String, String> properties) {
        this.connectionProperties = properties;
    }

}
