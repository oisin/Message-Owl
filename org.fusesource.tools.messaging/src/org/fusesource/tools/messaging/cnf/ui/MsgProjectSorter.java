/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.ui;

import java.text.Collator;

import org.eclipse.jface.viewers.ViewerSorter;
import org.fusesource.tools.messaging.cnf.model.ListenersRootComponent;
import org.fusesource.tools.messaging.cnf.model.SendersRootComponent;

/**
 * Sorter for Messaging Project. Sticks the root components as immediate children for the Messaging
 * Project
 */
public class MsgProjectSorter extends ViewerSorter {
    public static final int SENDERS_ROOT_POSITION = -2;
    public static final int LISTENERS_ROOT_POSITION = -1;

    public MsgProjectSorter() {
    }

    public MsgProjectSorter(Collator collator) {
        super(collator);
    }

    @Override
    public int category(Object element) {
        if (element instanceof SendersRootComponent) {
            return SENDERS_ROOT_POSITION;
        } else if (element instanceof ListenersRootComponent) {
            return LISTENERS_ROOT_POSITION;
        }
        return super.category(element);
    }
}
