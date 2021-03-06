/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.fusesource.tools.messaging.core.IListener;

public class Listeners implements Serializable {

    private static final long serialVersionUID = -6210776651408932192L;

    private List<IListener> listeners = Collections.emptyList();

    private transient IFile modelFile;

    public Listeners(IFile file) {
        this.modelFile = file;
        listeners = new ArrayList<IListener>();
    }

    public Listeners(List<IListener> listener) {
        this.listeners = listener;
    }

    public List<IListener> getListeners() {
        if (listeners == null) {
            listeners = new ArrayList<IListener>();
        }
        return listeners;
    }

    public void addListener(IListener listener) {
        listeners.add(listener);
    }

    public Object[] getChildren() {
        return listeners.toArray();
    }

    public void removeListener(IListener listener) {
        listeners.remove(listener);
    }

    public boolean hasChildren() {
        return listeners.size() > 0;
    }

    public IFile getModelFile() {
        return modelFile;
    }

    public void setModelFile(IFile modelFile) {
        this.modelFile = modelFile;
    }

}
