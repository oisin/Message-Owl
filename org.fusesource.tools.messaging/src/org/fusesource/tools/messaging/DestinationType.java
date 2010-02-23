/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging;

import org.fusesource.tools.messaging.core.IDestinationType;

/**
 * Default implementation of IDestinationType
 */
public class DestinationType implements IDestinationType {
    private static final long serialVersionUID = 4139155062747397377L;
    private final String type;
    private final boolean canSend;
    private final boolean canReceive;

    public DestinationType(String type) {
        this(type, true, true);
    }

    public DestinationType(String type, boolean send, boolean receive) {
        this.type = type;
        canSend = send;
        canReceive = receive;
    }

    public boolean canReceive() {
        return canReceive;
    }

    public boolean canSend() {
        return canSend;
    }

    public String getType() {
        return type;
    }
}
