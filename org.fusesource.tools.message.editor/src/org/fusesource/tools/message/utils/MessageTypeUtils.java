/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.message.utils;

import java.util.List;

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.MessageConstants;
import org.fusesource.tools.message.extensions.IMessageType;

public class MessageTypeUtils {

    public static Message getConvertedMessage(Object message) {
        if (message == null) {
            return null;
        }
        IMessageType messageTypeCon = null;
        List<IMessageType> messageTypeExtensions = MessageExtensionsMgr.getInstance().getMessageTypeExtensions();
        for (IMessageType messageType : messageTypeExtensions) {
            if (messageType.canHandle(message)) {
                messageTypeCon = messageType;
                if (messageType.getProviderId().equalsIgnoreCase(MessageConstants.DEFAULT_PROVIDER)) {
                    continue;
                } else {
                    break;
                }
            }
        }
        if (messageTypeCon != null) {
            try {
                return messageTypeCon.convertMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
