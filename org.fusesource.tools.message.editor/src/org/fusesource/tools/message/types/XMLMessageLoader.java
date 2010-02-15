package org.fusesource.tools.message.types;

import org.fusesource.tools.core.message.Message;

public class XMLMessageLoader extends TextMessageLoader {
	@Override
	protected Message getNewMessage() {
		return new XMLMessage();
	}
}
