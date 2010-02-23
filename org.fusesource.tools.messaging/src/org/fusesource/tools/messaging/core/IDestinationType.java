/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.core;

import java.io.Serializable;

/**
 * This interface represents a destination type Each destination type can support sending or
 * listening or both
 * 
 */
public interface IDestinationType extends Serializable {

    /**
     * Return type name of the destination type
     * 
     * @return
     */
    public String getType();

    /**
     * Return true if this type can participate as Sender destination
     * 
     * @return
     */
    public boolean canSend();

    /**
     * Return true if this type can participate as Listener destination
     * 
     * @return
     */
    public boolean canReceive();
}
