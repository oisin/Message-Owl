/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.model;

import org.fusesource.tools.messaging.core.ISender;

/**
 * Represents Sender Root Component in CNF
 */
public class SenderComponent extends BaseComponent {
    private final ISender sender;

    public SenderComponent(Object parent, ISender theSender) {
        super(parent);
        this.sender = theSender;
    }

    public ISender getSender() {
        return sender;
    }
}
