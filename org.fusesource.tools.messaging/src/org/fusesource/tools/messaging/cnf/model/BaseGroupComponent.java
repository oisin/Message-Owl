/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.model;

import org.eclipse.core.resources.IFile;

/**
 * Represents base group component in CNF
 * 
 */
public class BaseGroupComponent {
    private final String name;
    private final IFile file;

    public BaseGroupComponent(String nameParam, IFile fileParam) {
        this.name = nameParam;
        this.file = fileParam;
    }

    public String getName() {
        return name;
    }

    public IFile getFile() {
        return file;
    }
}
