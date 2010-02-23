/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.jms.message;

import javax.jms.JMSException;

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.MessageFactory;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.message.types.SimpleMessageLoader;
import org.fusesource.tools.messaging.jms.JMSUtils;

public class JMSSimpleMessageLoader extends SimpleMessageLoader {

    public Message getMessageModel(javax.jms.Message message) throws JMSException {
        Message simpleMessage = getNewMessage();
        simpleMessage.setProperties(loadProperties(message));
        return simpleMessage;
    }

    public Properties loadProperties(javax.jms.Message message) throws JMSException {
        Properties properties = MessageFactory.eINSTANCE.createProperties();
        JMSUtils.populateHeaders(message, properties);
        JMSUtils.populateProperties(message, properties);
        return properties;
    }

    @Override
    protected Message getNewMessage() {
        return new JMSSimpleMessage();
    }
}
