// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms;

import java.util.LinkedHashMap;
import java.util.Map;

import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.jms.JMSProvider;


public class FuseJMSProvider extends JMSProvider {
	public static String PROVIDER_ID = "FUSE Message Broker";
	public static String PROVIDER_NAME = "FUSE Message Broker";

	public FuseJMSProvider() {
		super(PROVIDER_ID, PROVIDER_NAME);
		connection = new FuseJMSConnection(this, null);
	}

	public Map<String, String> getConnectParameters() {
		return connectParams;
	}

	@Override
	protected void initConnectParams() {
		connectParams = new LinkedHashMap<String, String>();
		// connectParams.put(JMSConstants.CONNECTION_FACTORY_CLASS,
		// "org.apache.activemq.ActiveMQConnectionFactory");
		connectParams.put(JMSConstants.CONNECTION_ID,
				"FuseMsgBrokerConnection");
		connectParams.put(JMSConstants.URL, "tcp://localhost:61616");
		connectParams.put(JMSConstants.USER_NAME, "Administrator");
		connectParams.put(JMSConstants.PASSWORD, "Administrator");
	}

	public IConnection createConnection(Map<String, String> properties)
			throws MessagingException {
		connection.setConnectionProperties(properties);
		connection.initialize();
		return connection;
	}

}
