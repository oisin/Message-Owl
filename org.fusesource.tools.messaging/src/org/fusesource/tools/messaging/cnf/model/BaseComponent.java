/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.model;

/**
 * Represents the base component in CNF
 * 
 */
public class BaseComponent {
    Object parent;

    public BaseComponent(Object parent) {
        this.parent = parent;
    }

    public Object getParent() {
        return parent;
    }

}
