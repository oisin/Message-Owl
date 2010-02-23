/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import org.fusesource.tools.messaging.DefaultMessagesManager;
import org.fusesource.tools.messaging.MessageEvent;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.IMessagesManager;
import org.fusesource.tools.messaging.jms.ui.JMSLabelProvider;

/**
 * Default implementation of IListener for JMS Creates MessageConsumers and provides functionality
 * for start()/stop() methods. When a message arrives to onMessage() - the message will be added to
 * the MessagesManager and thus it notifies all interested parties about the new message arrival
 * event
 * 
 * In addition to the basic functionality, this class extends the JMSLabelProvider to customize the
 * display of a listener in the Project Explorer Eg. Shows a message selector in brackets when a
 * message selector is set on a listener
 */
public class JMSListener extends JMSLabelProvider implements IListener, MessageListener {
    private static final long serialVersionUID = 5184365198739458997L;
    private final JMSDestination destination;
    transient protected MessageConsumer msgConsumer;
    transient private IMessagesManager msgMgr;
    private Map<String, Object> listenerProps = new HashMap<String, Object>();
    private boolean isStarted;
    private boolean canReceive = true;

    public JMSListener(JMSDestination dest, Map<String, Object> props) {
        super();
        destination = dest;
        listenerProps = props;
    }

    public void start() throws MessagingException {
        try {
            createConsumer();
            if (msgConsumer == null) {
                // Allow working in offline mode, connection/session is not yet
                // available
                return;
            }
            initMessagesManager();
            msgConsumer.setMessageListener(this);
            isStarted = true;
        } catch (JMSException e) {
            isStarted = false;
            throw new MessagingException("Failed to create the Listener", e);
        }
    }

    protected void createConsumer() throws JMSException {
        String messageSelector = (String) listenerProps.get(JMSConstants.JMS_MESSAGE_SELECTOR);
        String durableSubName = (String) listenerProps.get(JMSConstants.DURABLE_SUBSCRIPTION_NAME);
        if (durableSubName != null) {
            msgConsumer = JMSUtils.getDurableSubscriber(destination, durableSubName, messageSelector);
        } else {
            msgConsumer = JMSUtils.getMessageConsumer(destination, messageSelector);
        }
    }

    protected void initMessagesManager() {
        if (msgMgr == null) {
            msgMgr = new DefaultMessagesManager(this);
        }
    }

    public void stop() throws MessagingException {
        try {
            if (msgConsumer != null) {
                msgConsumer.close();
            }
            isStarted = false;
        } catch (JMSException e) {
            throw new MessagingException("Failed to close Message Consumer", e);
        }
    }

    public void onMessage(Message msg) {
        MessageEvent event;
        try {
            event = new JMSMessageEvent(msg, this);
            getMessagesManager().addNewMessage(event);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public IDestination getDestination() {
        return destination;
    }

    public Map<String, Object> getProperties() {
        return listenerProps;
    }

    public IMessagesManager getMessagesManager() {
        initMessagesManager();
        return msgMgr;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean canReceive() {
        return canReceive;
    }

    public void setReceive(boolean receive) {
        canReceive = receive;
    }

    /**
     * For a listener, appends the Message Selector if specified
     */
    @Override
    protected String getDisplayText(Object element) {
        StringBuffer displayText = new StringBuffer();
        displayText.append(destination != null ? destination.getDestinationName() : "");
        // Append MessageSelector if specified
        if (listenerProps != null && !listenerProps.isEmpty()) {
            String selector = (String) listenerProps.get(JMSConstants.JMS_MESSAGE_SELECTOR);
            if (selector != null && selector.trim().length() > 0) {
                displayText.append("[" + selector + "]");
            }
        }
        return displayText.toString();
    }
}
