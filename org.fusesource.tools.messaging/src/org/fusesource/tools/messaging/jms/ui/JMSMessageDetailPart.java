// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms.ui;

import java.io.File;
import java.net.URL;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserDialog;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserFilter;
import org.fusesource.tools.message.extensions.IMessageType;
import org.fusesource.tools.message.utils.MessageExtensionsMgr;
import org.fusesource.tools.messaging.MessageEvent;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.editors.DefaultMessageDetailPart;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.jms.JMSDestination;
import org.fusesource.tools.messaging.jms.JMSMessage;
import org.fusesource.tools.messaging.jms.JMSMessageEvent;
import org.fusesource.tools.messaging.jms.JMSUtils;
import org.fusesource.tools.messaging.jms.ui.JMSImageConstants;
import org.fusesource.tools.messaging.utils.ImagesUtil;
import org.fusesource.tools.messaging.utils.MessageLoader;


/**
 * 
 * @author sgupta
 *
 */
public class JMSMessageDetailPart extends DefaultMessageDetailPart  {

	Action replyToActionMenu = null;

	public JMSMessageDetailPart() {
		addToolBarActions();
	}

	public void addToolBarActions() {
		createReplyToAction();
		actionsList.add(replyToActionMenu);
	}

	private void createReplyToAction() {
		replyToActionMenu = new Action("JMSReplyTo", Action.AS_PUSH_BUTTON) {
			public void run() {
				handleChooseReply();
			}
		};
		replyToActionMenu.setToolTipText("Send JMSReply");
		replyToActionMenu.setEnabled(false);
		replyToActionMenu.setImageDescriptor(ImagesUtil.getInstance().getImageDescriptor(JMSImageConstants.REPLY_TO));
	}

	private void handleChooseReply() {
		String fileToSend = null;
		URLChooserFilter fileFilter = new URLChooserFilter(
				new String[] { "*.txt","*.xml","*.message" });
		URLChooserDialog dialog = new URLChooserDialog(Display.getDefault()
				.getActiveShell(), "Select File", fileFilter);
		if (dialog.open() == Dialog.OK) {
			URL fileUrl = dialog.getURL();
			File file = new File(fileUrl.getFile());
			fileToSend = file.getAbsolutePath();
		}
		try {
			if (fileToSend == null)
				return;
			IListener source = currentMessage.getSource();
			javax.jms.Message receivedJmsMessage = (javax.jms.Message) currentMessage
					.getMessage();
			Destination replyTo = receivedJmsMessage.getJMSReplyTo();
			Session session = JMSUtils.getJMSSession((JMSDestination) source
					.getDestination());
			MessageProducer producer = session.createProducer(replyTo);
			org.fusesource.tools.core.message.Message loadedMsg = MessageLoader
					.getLoadedMessage(fileToSend, null);
			javax.jms.Message composedMsg = null;
			if (loadedMsg instanceof JMSMessage) {
				composedMsg = ((JMSMessage) loadedMsg).getJMSMessage(session);
				// set the required
				String correlationID = receivedJmsMessage.getJMSCorrelationID();
				composedMsg.setJMSCorrelationID(correlationID);
			}
			if (composedMsg != null)
				producer.send(composedMsg);
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Failed to send the message", e.getMessage());
		}
	}
	
	protected void updateActions(MessageEvent currentMsg) {
		JMSMessageEvent event = (JMSMessageEvent) currentMsg;
		if (event != null) {
			javax.jms.Message message = (javax.jms.Message) event.getMessage();
			try {
				replyToActionMenu.setEnabled((message.getJMSReplyTo() != null));
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected Message getConvertedMessage(MessageEvent messageEvent) {
		Message message = super.getConvertedMessage(messageEvent);
		if (message != null)
			return message;
		IMessageType messageType = MessageExtensionsMgr.getInstance().getMessageTypeExtension(messageEvent.getMessage(), JMSConstants.DEFAULT_JMS_PROVIDER);
		try {
			message = messageType.convertMessage(messageEvent.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
}
