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

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.types.AbstractMessageLoader;
import org.fusesource.tools.message.types.SimpleMessageTypeProvider;
import org.fusesource.tools.messaging.jms.JMSConstants;

public class JMSSimpleMessageTypeProvider extends SimpleMessageTypeProvider {

    @Override
    public boolean canHandle(Object msg) {
        // TODO: needs to be changed based on the way we find the provider
        // extensions
        if (msg instanceof javax.jms.TextMessage) {
            return false;
        }
        if (msg instanceof javax.jms.Message) {
            return true;
        }
        return false;
    }

    @Override
    public Message convertMessage(Object msg) throws Exception {
        javax.jms.Message message = (javax.jms.Message) msg;
        Message simpleMessage = ((JMSSimpleMessageLoader) getMessageLoader()).getMessageModel(message);
        simpleMessage.setType(getType());
        simpleMessage.setProviderId(getProviderId());
        simpleMessage.setProviderName(getProviderId());
        return simpleMessage;
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
        return new JMSSimpleMessageLoader();
    }
}
