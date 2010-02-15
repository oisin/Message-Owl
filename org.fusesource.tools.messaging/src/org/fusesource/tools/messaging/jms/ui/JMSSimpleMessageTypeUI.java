/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.ui;

import org.fusesource.tools.message.extensions.IMessageEditorExtension;
import org.fusesource.tools.message.extensions.IMessageViewerExtension;
import org.fusesource.tools.messaging.ui.SimpleMessageTypeUI;


public class JMSSimpleMessageTypeUI extends SimpleMessageTypeUI {

	public IMessageEditorExtension getEditorExtension() {
		return new JMSSimpleMessageEditorExtension();
	}

	public IMessageViewerExtension getViewerExtension() {
		return new JMSSimpleMessageViewer();
	}

	public boolean canHandle(Object msg) {
		if (msg instanceof javax.jms.TextMessage)
			return false;
		if (msg instanceof javax.jms.Message)
			return true;
		return false;
	}
}
