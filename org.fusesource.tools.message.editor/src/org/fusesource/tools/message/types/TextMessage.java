package org.fusesource.tools.message.types;

/**
 * 
 * @author sgupta
 * 
 */
public class TextMessage extends SimpleMessage {

	public TextMessage() {
	}

	public String getMessageBody() {
		return getBody().getMessageContent();
	}
}