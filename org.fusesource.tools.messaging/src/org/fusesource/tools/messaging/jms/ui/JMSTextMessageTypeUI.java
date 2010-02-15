package org.fusesource.tools.messaging.jms.ui;

import org.fusesource.tools.message.extensions.IMessageEditorExtension;

public class JMSTextMessageTypeUI extends JMSSimpleMessageTypeUI {

	@Override
	public IMessageEditorExtension getEditorExtension() {
		return new JMSTextMessageEditorExtension();
	}

	public boolean canHandle(Object msg) {
		if (msg instanceof javax.jms.TextMessage)
			return true;
		return false;
	}
}
