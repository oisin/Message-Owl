// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms.message;

import javax.jms.JMSException;

import org.fusesource.tools.core.message.Body;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.MessageFactory;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.message.types.XMLMessageLoader;
import org.fusesource.tools.message.utils.EMFUtil;
import org.fusesource.tools.messaging.jms.JMSUtils;


/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
public class FUSEXMLMessageLoader extends XMLMessageLoader {

	@Override
	protected Message getNewMessage() {
		return new FUSEXMLMessage();
	}

	public Message getMessageModel(javax.jms.Message message) throws JMSException {
		Message messageModel = getNewMessage();
		messageModel.setProperties(loadProperties(message));
		messageModel.setBody(loadBody((javax.jms.TextMessage) message));
		return messageModel;
	}

	public Properties loadProperties(javax.jms.Message message) throws JMSException {
		Properties properties = MessageFactory.eINSTANCE.createProperties();
		JMSUtils.populateHeaders(message, properties);
		JMSUtils.populateProperties(message, properties);
		return properties;
	}

	public Body loadBody(javax.jms.TextMessage textMessage) {
		Body bodyType = MessageFactory.eINSTANCE.createBody();
		try {
			String text = (textMessage.getText());
			bodyType.setContent(EMFUtil.strToAnyType(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bodyType;
	}
}
