/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.jms.message;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.jms.JMSException;

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.types.AbstractMessageLoader;
import org.fusesource.tools.message.types.TextMessageTypeProvider;
import org.fusesource.tools.messaging.jms.JMSConstants;

public class JMSTextMessageTypeProvider extends TextMessageTypeProvider {

    @Override
    public Message convertMessage(Object msg) throws JMSException {
        javax.jms.TextMessage textMessage = (javax.jms.TextMessage) msg;
        Message messageType = ((JMSTextMessageLoader) getMessageLoader()).getMessageModel(textMessage);
        messageType.setType(getType());
        messageType.setProviderId(getProviderId());
        messageType.setProviderName(getProviderId());
        return messageType;
    }

    @Override
    public boolean canHandle(Object msg) {
        if (msg instanceof javax.jms.TextMessage) {
            return true;
        }
        return false;
    }

    /**
     * Clients can override this method to add JMS provider specific headers
     */
    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new LinkedHashMap<String, String>();
        headers.put(JMSConstants.JMSCORRELATION_ID, "");
        headers.put(JMSConstants.JMSREPLY_TO, "");
        headers.put(JMSConstants.JMSTYPE, "");
        return headers;
    }

    @Override
    protected AbstractMessageLoader getMessageLoader() {
        return new JMSTextMessageLoader();
    }

}
