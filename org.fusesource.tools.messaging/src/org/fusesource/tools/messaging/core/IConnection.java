/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.core;

import org.fusesource.tools.messaging.MessagingException;

/**
 * This interface represents a connection to a server. An entry point to create a connection is
 * through the Provider {@link IProvider}
 * 
 */
public interface IConnection {

    /**
     * Returns the provider associated with the connection
     * 
     * @return IProvider instance
     */
    public IProvider getProvider();

    /**
     * Clients should return true if the connection is active
     * 
     * @return true if connection is live
     */
    public boolean isActive();

    /**
     * Clients should do the required initialization here to connect to the server
     * 
     * @throws MessagingException
     */
    public void initialize() throws MessagingException;

    /**
     * Closes a connection - Clients can free the held resources here
     * 
     * @throws MessagingException
     *             if connection can not be closed.
     */
    public void closeConnection() throws MessagingException;

    /**
     * Entry point to create a destination in a server
     * 
     * @param connection
     * @param type
     * @param name
     * @return
     */
    public IDestination createDestination(IDestinationType type, String destinationName) throws MessagingException;

}
