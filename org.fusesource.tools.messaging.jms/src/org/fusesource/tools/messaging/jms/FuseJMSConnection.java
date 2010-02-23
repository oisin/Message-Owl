/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.jms;

import java.util.Map;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.fusesource.tools.messaging.MessagingException;

public class FuseJMSConnection extends JMSConnection {

    public FuseJMSConnection(JMSProvider provider, Map<String, String> properties) {
        super(provider, properties);
    }

    public void initialize() throws MessagingException {
        if (connectionProperties == null || connectionProperties.isEmpty()) {
            return;// If no connection properties are there, ignore ?
        }

        String name = connectionProperties.get(JMSConstants.CONNECTION_ID);
        String url = connectionProperties.get(JMSConstants.URL);
        String un = connectionProperties.get(JMSConstants.USER_NAME);
        String pw = connectionProperties.get(JMSConstants.PASSWORD);
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(un, pw, url);
            jmsConnection = connectionFactory.createConnection();
            jmsConnection.setClientID(name);
            jmsConnection.start();
            createSession();
        } catch (JMSException e) {
            throw new MessagingException(e.getMessage(), e.getCause());
        }
    }

    public boolean isActive() {
        boolean isActive = (jmsConnection != null && ((ActiveMQConnection) jmsConnection).isStarted());
        return isActive;
    }

}
