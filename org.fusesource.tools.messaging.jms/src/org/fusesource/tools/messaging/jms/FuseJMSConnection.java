// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms;

import java.util.Map;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.jms.JMSConnection;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.jms.JMSProvider;


public class FuseJMSConnection extends JMSConnection {

	public FuseJMSConnection(JMSProvider provider, Map<String, String> properties) {
		super(provider, properties);
	}

	public void initialize() throws MessagingException {
		if (connectionProperties == null || connectionProperties.isEmpty())
			return;// If no connection properties are there, ignore ?

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
			System.out.println("FUSE JMS Connection, Session created successfully.");
		} catch (JMSException e) {
			throw new MessagingException(e.getMessage(), e.getCause());
		}
	}

	public boolean isActive() {
		boolean isActive = (jmsConnection != null && ((ActiveMQConnection) jmsConnection).isStarted());
		return isActive;
	}

}
