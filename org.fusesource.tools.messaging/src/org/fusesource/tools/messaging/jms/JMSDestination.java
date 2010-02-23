/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.jms;

import java.util.Collections;
import java.util.Map;

import org.fusesource.tools.messaging.AbstractDestination;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.core.IDestinationType;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.ISender;

/**
 * Default implementation of IDestination for JMS
 */
public class JMSDestination extends AbstractDestination {

    private static final long serialVersionUID = -1333007801275177388L;

    public JMSDestination(JMSConnection jmsCon, IDestinationType type, String name) {
        super(jmsCon, type, name);
    }

    public IListener createListener(Map<String, Object> listenerProps) {
        return new JMSListener(this, listenerProps);
    }

    public ISender createSender(Map<String, Object> senderProps) throws MessagingException {
        return new JMSSender(this, senderProps);
    }

    public Map<String, Object> getListenerProperties() {
        return Collections.emptyMap();
    }

    public Map<String, Object> getSenderProperties() {
        return Collections.emptyMap();
    }
}
