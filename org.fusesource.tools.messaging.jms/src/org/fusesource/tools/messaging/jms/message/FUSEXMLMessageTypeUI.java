// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms.message;

import javax.jms.JMSException;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.fusesource.tools.message.extensions.IMessageEditorExtension;
import org.fusesource.tools.message.extensions.IMessageViewerExtension;
import org.fusesource.tools.messaging.jms.ui.JMSTextMessageTypeUI;


/**
 * 
 * @author sgupta
 *
 */
public class FUSEXMLMessageTypeUI extends JMSTextMessageTypeUI {

	
	@Override
	public IMessageEditorExtension getEditorExtension() {
		return new FUSEXMLMessageEditorExtension();
	}
	
	public boolean canHandle(Object message) {
		// FUSE XML does not have separate XML message type extensions
		// Uses ActiveMQTextMessage to send XML content also.
		// We do a primitive check here, to plug-in XML viewer control, for
		// better display of XML messages
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
	
	@Override
	public IMessageViewerExtension getViewerExtension() {
		return new FUSEXMLMessageViewer();
	}
}
