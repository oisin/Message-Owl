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
import org.fusesource.tools.messaging.core.ISender;

public class Senders implements Serializable {

    private static final long serialVersionUID = -6666543246586137732L;

    private List<ISender> senders = Collections.emptyList();

    private transient IFile modelFile;

    public Senders(IFile modelFile) {
        this.modelFile = modelFile;
        senders = new ArrayList<ISender>();
    }

    public Senders(List<ISender> sender) {
        this.senders = sender;
    }

    public List<ISender> getSenders() {
        if (senders == null) {
            senders = new ArrayList<ISender>();
        }
        return senders;
    }

    public void addSender(ISender sender) {
        senders.add(sender);
    }

    public Object[] getChildren() {
        return senders.toArray();
    }

    public void removeSender(ISender sender) {
        senders.remove(sender);
    }

    public boolean hasChildren() {
        return senders.size() > 0;
    }

    public IFile getModelFile() {
        return modelFile;
    }

    public void setModelFile(IFile modelFile) {
        this.modelFile = modelFile;
    }
}
