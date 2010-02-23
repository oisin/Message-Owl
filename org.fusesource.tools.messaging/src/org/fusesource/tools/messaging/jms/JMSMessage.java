/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.jms;

import javax.jms.Message;
import javax.jms.Session;

import org.fusesource.tools.messaging.MessagingException;

public interface JMSMessage {

    public abstract Message getJMSMessage(JMSSender sender) throws MessagingException;

    public abstract Message getJMSMessage(Session session) throws MessagingException;
}
