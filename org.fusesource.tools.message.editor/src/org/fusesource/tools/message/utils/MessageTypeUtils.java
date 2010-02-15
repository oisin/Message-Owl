package org.fusesource.tools.message.utils;

import java.util.List;

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.MessageConstants;
import org.fusesource.tools.message.extensions.IMessageType;


/**
 * 
 * @author kiranb
 * 
 */
public class MessageTypeUtils {

	public static Message getConvertedMessage(Object message) {
		if (message == null)
			return null;
		IMessageType messageTypeCon = null;
		List<IMessageType> messageTypeExtensions = MessageExtensionsMgr.getInstance().getMessageTypeExtensions();
		for (IMessageType messageType : messageTypeExtensions) {
			if (messageType.canHandle(message)) {
				messageTypeCon = messageType;
				if (messageType.getProviderId().equalsIgnoreCase(MessageConstants.DEFAULT_PROVIDER)) {
					continue;
				} else {
					break;
				}
			}
		}
		if (messageTypeCon != null)
			try {
				return messageTypeCon.convertMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
}
