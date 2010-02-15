package org.fusesource.tools.messaging.utils;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.MessageConstants;
import org.fusesource.tools.message.editors.MessageEditorConstants;
import org.fusesource.tools.message.extensions.IMessageType;
import org.fusesource.tools.message.utils.MessageExtensionsMgr;
import org.fusesource.tools.message.utils.MessageManager;
import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.core.ISender;


/**
 * 
 * @since
 * @author sgupta
 * @version 1.0
 */

public class MessageLoader extends MessageManager {

	public static Message getLoadedMessage(IFile ifile, ISender sender) {
		if (ifile.getFileExtension().equalsIgnoreCase(MessageEditorConstants.MESSAGE_FILE_EXTENSION)) {
			return loadMessage(ifile);
		} else {
			IMessageType messageType = findHandler(sender, ifile);
			if (messageType == null) {
				return null;
			}
			return messageType.load(ifile);
		}
	}

	public static Message getLoadedMessage(String fileUrl, ISender sender) {
		if (fileUrl.endsWith(MessageEditorConstants.MESSAGE_FILE_EXTENSION)) {
			return loadMessage(fileUrl);
		} else {
			IMessageType messageType = findHandler(sender, fileUrl);
			if (messageType == null) {
				return null;
			}
			return messageType.load(fileUrl);
		}
	}

	public static IMessageType findHandler(ISender sender, Object object) {
		String providerId = null;
		if (sender != null) {
			IProvider provider = sender.getDestination().getConnection().getProvider();
			providerId = provider.getId();
		}
		IMessageType messageType = providerSpecificMessageType(object, providerId);
		return messageType;
	}

	private static IMessageType providerSpecificMessageType(Object object, String providerId) {
		IMessageType supportedMessageType = null;
		List<IMessageType> messageTypeExtensions = MessageExtensionsMgr.getInstance().getMessageTypeExtensions();
		String extension = getExtension(object);
		for (IMessageType messageType : messageTypeExtensions) {
			if (doMatch(messageType, extension)) {
				supportedMessageType = messageType;
				if (!messageType.getProviderId().equalsIgnoreCase(MessageConstants.DEFAULT_PROVIDER)) {
					break;
				}
			}
		}
		return supportedMessageType;
	}
}
