// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.message.types;

import java.io.File;

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.util.ReaderUtils;
import org.fusesource.tools.message.editors.MessageEditorConstants;
import org.fusesource.tools.message.utils.EMFUtil;
import org.fusesource.tools.message.utils.MessageManager;


/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
public class TextMessageLoader extends SimpleMessageLoader {

	@Override
	public Message getMessageModel(String fileURL) {
		String extension = MessageManager.getExtension(fileURL);
		if (extension.equalsIgnoreCase(MessageEditorConstants.MESSAGE_FILE_EXTENSION)) {
			return super.getMessageModel(fileURL);
		} else {
			return loadFile(fileURL);
		}
	}

	public Message loadFile(String fileUrl) {
		Message message = getNewMessage();
		try {
			String loadTextFromFile = ReaderUtils.getContentAsString(new File(fileUrl));
			Message strToMessage = EMFUtil.strToMessage(loadTextFromFile);
			loadMessage(message, strToMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	protected Message getNewMessage() {
		return new TextMessage();
	}

}
