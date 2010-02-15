// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms.message;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.jms.JMSException;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.types.AbstractMessageLoader;
import org.fusesource.tools.message.types.XMLMessageTypeProvider;
import org.fusesource.tools.messaging.jms.JMSConstants;


/**
 * 
 * @author sgupta
 *
 */
public class FUSEXMLMessageTypeProvider extends XMLMessageTypeProvider {

	public Message convertMessage(Object msg) throws JMSException {
		javax.jms.TextMessage message = (javax.jms.TextMessage) msg;
		Message messageType = ((FUSEXMLMessageLoader) getMessageLoader()).getMessageModel(message);
		messageType.setType(getType());
		messageType.setProviderId(getProviderId());
		messageType.setProviderName(getProviderId());
		return messageType;
	}

	public boolean canHandle(Object message) {
		// FUSE XML does not have separate XML message type extensions
		// Uses ActiveMQTextMessage to send XML content also.
		// We do a primitive check here to figure whether it is XML content
		if (message instanceof ActiveMQTextMessage) {
			ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;
			try {
				String text = textMessage.getText();
				if (text == null)
					return false;
				text = text.trim();
				if ((text.startsWith("<")) && (text.endsWith(">")))
					return true;
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Clients can override this method to add JMS provider specific headers
	 */
	public Map<String, String> getHeaders() {
		HashMap<String, String> headers = new LinkedHashMap<String, String>();
		headers.put(JMSConstants.JMSCORRELATION_ID, "");
		headers.put(JMSConstants.JMSREPLY_TO, "");
		headers.put(JMSConstants.JMSTYPE, "");
		return headers;
	}

	@Override
	protected AbstractMessageLoader getMessageLoader() {
		return new FUSEXMLMessageLoader();
	}
}
