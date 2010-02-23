/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.editors;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.fusesource.tools.messaging.core.IListener;

public class MessageEditorStorage implements IStorage {

    private IListener listener = null;

    private IFile file = null;

    public MessageEditorStorage(IListener listener, IFile file) {
        this.listener = listener;
        this.file = file;
    }

    /**
     * @return the listeners
     */
    public IListener getListener() {
        return listener;
    }

    public InputStream getContents() throws CoreException {
        return null;
    }

    public IPath getFullPath() {
        return file.getFullPath();
    }

    public String getName() {
        return listener.getDestination() != null ? listener.getDestination().getDestinationName() : "";
    }

    public boolean isReadOnly() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public Object getAdapter(Class arg0) {
        return null;
    }
}
