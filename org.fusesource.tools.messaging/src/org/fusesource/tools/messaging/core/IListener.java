/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.core;

import java.io.Serializable;
import java.util.Map;

import org.fusesource.tools.messaging.DefaultMessagesManager;
import org.fusesource.tools.messaging.MessagingException;

/**
 * This interface represents a Listener instance. An entry point to create a listener is from a
 * Destination @see {@link IDestination#createListener(Map)} Optionally, clients can also implement
 * ILabelProvider to customize text/image displayed for each listener in the Project Explorer
 */
public interface IListener extends Serializable {
    /**
     * Messaging framework calls this method when a new Listener is created/started or when a
     * Connection is re-activated/updated for this listener. Clients can do any required
     * initialization here to setup a listener
     * 
     * @throws MessagingException
     */
    public void start() throws MessagingException;

    /**
     * Messaging framework calls this method when a connection is disconnected, when a listener is
     * deleted/stopped. Clients can release any resource held by this listener here.
     * 
     * @throws MessagingException
     */
    public void stop() throws MessagingException;

    /**
     *Return true if the listener is started
     * 
     * @return
     */
    public boolean isStarted();

    /**
     * Listeners can be explictly started/stopped from UI actions This method returns the current
     * receivable state of the listener
     * 
     * @return
     */
    public boolean canReceive();

    /**
     * Set the receive state for this listener
     * 
     * @param canReceive
     */
    public void setReceive(boolean canReceive);

    /**
     * It is a listener's job to manage the received messages and notify the interested parties
     * about the message events. Clients can just use the default implementation of the
     * IMessagesManager or can provider their own messages manager @see
     * {@link DefaultMessagesManager}
     * 
     * @return IMessagesManager - Default implementation of IMessagesManager
     */
    public IMessagesManager getMessagesManager();

    /**
     * Return the destination object for which this listener has been created
     * 
     * @return
     */
    public IDestination getDestination();

    /**
     * Return the properties associated by the Listener
     * 
     * @return
     */
    public Map<String, Object> getProperties();

}
