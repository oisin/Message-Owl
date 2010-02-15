package org.fusesource.tools.message.types;

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.utils.EMFUtil;


public abstract class AbstractMessageLoader {

	public AbstractMessageLoader() {
		super();
	}

	public Message getMessageModel(String fileURL) {
		Message message = getNewMessage();
		org.fusesource.tools.core.message.Message messageModel = EMFUtil.getMessageModel(fileURL);
		if (messageModel == null)
			return message;
		loadMessage(message, messageModel);
		return message;
	}

	public void loadMessage(Message toMessage, Message fromMessage) {
		toMessage.setType(fromMessage.getType());
		toMessage.setProviderId(fromMessage.getProviderId());
		toMessage.setProviderName(fromMessage.getProviderName());
		toMessage.setBody(fromMessage.getBody());
		toMessage.setProperties(fromMessage.getProperties());
	}

	protected abstract Message getNewMessage();
}