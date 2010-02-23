/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging;

import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IDestinationType;

/**
 * Abstract implementation of IDestination
 */
public abstract class AbstractDestination implements IDestination {
    private static final long serialVersionUID = -902736454921047639L;
    transient private IConnection con;
    private IDestinationType type;
    private String name;

    public AbstractDestination(IConnection con, IDestinationType type, String name) {
        this.con = con;
        this.type = type;
        this.name = name;
    }

    public IConnection getConnection() {
        return con;
    }

    public String getDestinationName() {
        return name;
    }

    public IDestinationType getDestinationType() {
        return type;
    }

    public void setConnection(IConnection con) {
        this.con = con;
    }

    public void setDestinationName(String name) {
        this.name = name;
    }

    public void setDestinationType(IDestinationType type) {
        this.type = type;
    }
}
