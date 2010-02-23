/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.messaging.jms;

import java.io.Serializable;

import org.fusesource.tools.messaging.core.IDestinationType;

public class ReplyToInfo implements Serializable {

    private static final long serialVersionUID = -5489441337689515153L;

    private IDestinationType type = null;

    private String name = null;

    private boolean newDest = false;

    public IDestinationType getType() {
        return type;
    }

    public void setType(IDestinationType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNewDest(boolean newDest) {
        this.newDest = newDest;
    }

    public boolean isNewDest() {
        return newDest;
    }

    @Override
    public String toString() {
        return getType().getType() + ":" + getName();
    }
}
