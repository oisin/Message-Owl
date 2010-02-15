// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.message.utils;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.MessageConstants;
import org.fusesource.tools.message.extensions.IMessageType;


public class MessageManager {
	// singleton??
	public MessageManager() {
		super();
	}

	public static Message loadMessage(String fileToSend) {
		MessageSAXParser parser = new MessageSAXParser();
		MessageMetaInfo metaData = parser.parse(fileToSend);
		IMessageType msgProvider = MessageExtensionsMgr.getInstance().getMessageTypeExtension(metaData.getType(), metaData.getProviderId());
		return msgProvider.load(fileToSend);
	}

	public static Message loadMessage(File file) {
		MessageSAXParser parser = new MessageSAXParser();
		MessageMetaInfo metaData = parser.parse(file);
		IMessageType msgProvider = MessageExtensionsMgr.getInstance().getMessageTypeExtension(metaData.getType(), metaData.getProviderId());
		return msgProvider.load(file);
	}

	public static Message loadMessage(IFile ifile) {
		MessageSAXParser parser = new MessageSAXParser();
		MessageMetaInfo metaData = parser.parse(ifile);
		IMessageType msgProvider = MessageExtensionsMgr.getInstance().getMessageTypeExtension(metaData.getType(), metaData.getProviderId());
		return msgProvider.load(ifile);
	}

	public static void save(final IFile file, final Message msgToSave, IProgressMonitor monitor) throws Exception {
		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
			// This is the method that gets invoked when the operation runs.
			@Override
			public void execute(IProgressMonitor monitor) {
				try {
					EMFUtil.saveMessageToDisk(file, msgToSave);
					file.refreshLocal(1, monitor);
				} catch (IOException e) {
					e.printStackTrace();
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "Failed to save the Message.", e.getMessage());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		};
		new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, operation);
	}

	public static boolean doMatch(IMessageType messageType, String extension) {
		String extensionType = messageType.getSupportedFileExtension();
		if (extensionType != null && extensionType.trim().length() > 0) {
			extensionType = extensionType.trim();
			if (extensionType.contains(MessageConstants.SUPPORTED_EXTENSION_TYPE_DELIM)) {
				StringTokenizer stringTokenizer = new StringTokenizer(extensionType,
						MessageConstants.SUPPORTED_EXTENSION_TYPE_DELIM);
				while (stringTokenizer.hasMoreElements()) {
					String token = (String) stringTokenizer.nextElement();
					if (token.equalsIgnoreCase(extension)) {
						return true;
					}
				}
			} else {
				if (extensionType.equalsIgnoreCase(extension)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getExtension(Object object) {
		String extension = null;
		if (object instanceof IFile)
			extension = ((IFile) object).getFileExtension();
		else if (object instanceof String) {
			String fileUrl = ((String) object);
			StringTokenizer stringTokenizer = new StringTokenizer(fileUrl, ".");
			while (stringTokenizer.hasMoreElements()) {
				String token = (String) stringTokenizer.nextElement();
				extension = token;
			}
		}
		return extension;
	}
}