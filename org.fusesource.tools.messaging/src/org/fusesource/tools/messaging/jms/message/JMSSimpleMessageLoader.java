/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.message;

import javax.jms.JMSException;

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.MessageFactory;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.message.types.SimpleMessageLoader;
import org.fusesource.tools.messaging.jms.JMSUtils;


public class JMSSimpleMessageLoader extends SimpleMessageLoader{

	public Message getMessageModel(javax.jms.Message message) throws JMSException {
		Message simpleMessage = getNewMessage();
		simpleMessage.setProperties(loadProperties(message));
		return simpleMessage;
	}

	public Properties loadProperties(javax.jms.Message message) throws JMSException {
		Properties properties = MessageFactory.eINSTANCE.createProperties();
		JMSUtils.populateHeaders(message, properties);
		JMSUtils.populateProperties(message, properties);
		return properties;
	}

	protected Message getNewMessage() {
		return new JMSSimpleMessage();
	}
}
