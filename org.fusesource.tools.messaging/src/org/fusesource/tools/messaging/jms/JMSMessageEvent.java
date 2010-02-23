/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.jms;

import static org.fusesource.tools.messaging.jms.JMSConstants.JMSCORRELATION_ID;
import static org.fusesource.tools.messaging.jms.JMSConstants.JMSMESSAGE_ID;
import static org.fusesource.tools.messaging.jms.JMSConstants.JMSREPLY_TO;
import static org.fusesource.tools.messaging.jms.JMSConstants.JMSTIMESTAMP;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.jms.JMSException;
import javax.jms.Message;

import org.fusesource.tools.messaging.MessageEvent;
import org.fusesource.tools.messaging.core.IListener;

/**
 * Creates a required meta data object to show the received message in the UI
 */
public class JMSMessageEvent extends MessageEvent {

    public JMSMessageEvent(Object message, IListener src) throws JMSException {
        super(message, src);
        msgMetadata = new LinkedHashMap<String, String>();
        populateMetaData();
    }

    protected void populateMetaData() throws JMSException {
        Message receivedMsg = (Message) getMessage();
        msgMetadata.put(JMSMESSAGE_ID, receivedMsg.getJMSMessageID());
        msgMetadata.put(JMSCORRELATION_ID, receivedMsg.getJMSCorrelationID());
        msgMetadata.put(JMSTIMESTAMP, "" + new Date(receivedMsg.getJMSTimestamp()).toString());
        String name = JMSUtils.getDestinationName(receivedMsg.getJMSDestination());
        name = name != null ? name : "";

        // msgMetadata.put(JMSDESTINATION, name);
        name = JMSUtils.getDestinationName(receivedMsg.getJMSReplyTo());
        name = name != null ? name : "";
        msgMetadata.put(JMSREPLY_TO, name);
    }
}
