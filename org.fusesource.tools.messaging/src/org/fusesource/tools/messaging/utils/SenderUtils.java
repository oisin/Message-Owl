// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.server.core.IServer;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.cnf.model.BaseGroupComponent;
import org.fusesource.tools.messaging.cnf.model.SenderComponent;
import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.ISender;
import org.fusesource.tools.messaging.server.MessagingServersUtil;


public class SenderUtils {

	public static void sendMessage(IFile fileToSend, SenderComponent senderComponent) throws MessagingException {
		if (!validateSend(senderComponent))
			return;
		Message loadedMsg = MessageLoader.getLoadedMessage(fileToSend, senderComponent.getSender());
		sendLoadedMessage(loadedMsg, senderComponent.getSender());
	}

	public static void sendMessage(String fileToSend, SenderComponent senderComponent) throws MessagingException {
		if (!validateSend(senderComponent))
			return;
		Message loadedMsg = MessageLoader.getLoadedMessage(fileToSend, senderComponent.getSender());
		sendLoadedMessage(loadedMsg, senderComponent.getSender());
	}

	private static void sendLoadedMessage(Message loadedMsg, ISender sender) throws MessagingException {
		if (loadedMsg != null) {
			sender.send(loadedMsg);
		} else {
			throw new MessagingException("Failed to send the Message. The Message file may be corrupted or the file type not supported.");
		}
	}

	private static boolean validateSend(SenderComponent senderComponent) {
		IProject sendingFromProject = ((BaseGroupComponent) senderComponent.getParent()).getFile().getProject();
		IServer deployedServer = MessagingServersUtil.getDeployedServer(sendingFromProject);
		if (deployedServer == null) {
			MessageDialog
					.openWarning(Display.getCurrent().getActiveShell(), "Warning",
							"Project is not deployed on any messaging server. Ensure to add the project and try again.");
			return false;
		} else if (!hasActiveConnection(senderComponent.getSender())) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Warning",
					"Connection is unavailable. Please connect to the server and try again.");
			return false;
		}
		return true;
	}

	public static boolean hasActiveConnection(ISender sender) {
		IDestination destination = sender.getDestination();
		if (destination != null) {
			IConnection connection = destination.getConnection();
			if (connection != null && connection.isActive())
				return true;
		}
		return false;
	}

}
