/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.message.types;

import java.io.File;

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.util.ReaderUtils;
import org.fusesource.tools.message.editors.MessageEditorConstants;
import org.fusesource.tools.message.utils.EMFUtil;
import org.fusesource.tools.message.utils.MessageManager;

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

    @Override
    protected Message getNewMessage() {
        return new TextMessage();
    }

}
