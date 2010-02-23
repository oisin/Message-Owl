/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.server.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.server.core.IServer;
import org.fusesource.tools.messaging.IConstants;

public abstract class AbstractMsgServerAction implements IObjectActionDelegate {

    private IServer selectedServer;

    public AbstractMsgServerAction() {
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {

    }

    public void selectionChanged(IAction action, ISelection selection) {
        Object firstElement = ((StructuredSelection) selection).getFirstElement();
        if (firstElement instanceof IServer) {
            selectedServer = (IServer) firstElement;
        }
        action.setEnabled(canEnable());
    }

    protected abstract boolean canEnable();

    protected IServer getSelectedServer() {
        return selectedServer;
    }

    protected String getServerName() {
        if (getSelectedServer() != null) {
            return getSelectedServer().getName();
        }
        return IConstants.EMPTY_STRING;
    }
}
