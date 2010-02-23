/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.jms.ui;

import java.io.File;
import java.net.URL;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
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
import org.fusesource.tools.messaging.utils.ImagesUtil;
import org.fusesource.tools.messaging.utils.MessageLoader;

public class JMSMessageDetailPart extends DefaultMessageDetailPart {

    Action replyToActionMenu = null;

    public JMSMessageDetailPart() {
        addToolBarActions();
    }

    public void addToolBarActions() {
        createReplyToAction();
        actionsList.add(replyToActionMenu);
    }

    private void createReplyToAction() {
        replyToActionMenu = new Action("JMSReplyTo", IAction.AS_PUSH_BUTTON) {
            @Override
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
        URLChooserFilter fileFilter = new URLChooserFilter(new String[] { "*.txt", "*.xml", "*.message" });
        URLChooserDialog dialog = new URLChooserDialog(Display.getDefault().getActiveShell(), "Select File", fileFilter);
        if (dialog.open() == Window.OK) {
            URL fileUrl = dialog.getURL();
            File file = new File(fileUrl.getFile());
            fileToSend = file.getAbsolutePath();
        }
        try {
            if (fileToSend == null) {
                return;
            }
            IListener source = currentMessage.getSource();
            javax.jms.Message receivedJmsMessage = (javax.jms.Message) currentMessage.getMessage();
            Destination replyTo = receivedJmsMessage.getJMSReplyTo();
            Session session = JMSUtils.getJMSSession((JMSDestination) source.getDestination());
            MessageProducer producer = session.createProducer(replyTo);
            org.fusesource.tools.core.message.Message loadedMsg = MessageLoader.getLoadedMessage(fileToSend, null);
            javax.jms.Message composedMsg = null;
            if (loadedMsg instanceof JMSMessage) {
                composedMsg = ((JMSMessage) loadedMsg).getJMSMessage(session);
                // set the required
                String correlationID = receivedJmsMessage.getJMSCorrelationID();
                composedMsg.setJMSCorrelationID(correlationID);
            }
            if (composedMsg != null) {
                producer.send(composedMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog
                    .openError(Display.getCurrent().getActiveShell(), "Failed to send the message", e.getMessage());
        }
    }

    @Override
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

    @Override
    protected Message getConvertedMessage(MessageEvent messageEvent) {
        Message message = super.getConvertedMessage(messageEvent);
        if (message != null) {
            return message;
        }
        IMessageType messageType = MessageExtensionsMgr.getInstance().getMessageTypeExtension(
                messageEvent.getMessage(), JMSConstants.DEFAULT_JMS_PROVIDER);
        try {
            message = messageType.convertMessage(messageEvent.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
