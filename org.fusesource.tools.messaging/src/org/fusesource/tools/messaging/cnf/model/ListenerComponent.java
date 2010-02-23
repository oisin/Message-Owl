/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.model;

import org.fusesource.tools.messaging.core.IListener;

/**
 * Represents Listener Component in CNF
 */
public class ListenerComponent extends BaseComponent {
    private final IListener listener;

    public ListenerComponent(Object parent, IListener theListener) {
        super(parent);
        this.listener = theListener;
    }

    public IListener getListener() {
        return listener;
    }
}
