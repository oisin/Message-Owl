/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.jms.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.fusesource.tools.messaging.cnf.model.BaseGroupComponent;
import org.fusesource.tools.messaging.cnf.model.DataModelManager;
import org.fusesource.tools.messaging.cnf.model.ListenerComponent;
import org.fusesource.tools.messaging.jms.JMSListener;
import org.fusesource.tools.messaging.jms.JMSUtils;

/**
 * Action implementation for Unsubscribe and Close action on JMS Listeners
 */
public class UnsubscribeAndCloseAction implements IObjectActionDelegate {

    private ListenerComponent activeSelection;

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public void run(IAction action) {
        if (!(activeSelection.getParent() instanceof BaseGroupComponent)) {
            return;
        }
        IFile modelFile = ((BaseGroupComponent) activeSelection.getParent()).getFile();
        JMSListener listener = (JMSListener) activeSelection.getListener();
        try {
            listener.stop();
            JMSUtils.unsubscribe(listener);
            DataModelManager.getInstance().removeDestination(modelFile, activeSelection.getListener());
        } catch (Exception e) {
            MessageDialog.openError(Display.getDefault().getActiveShell(), "Failed to Unsubscribe the Listener.", e
                    .getMessage());
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        Object firstElement = ((StructuredSelection) selection).getFirstElement();
        if ((firstElement instanceof ListenerComponent)) {
            activeSelection = (ListenerComponent) firstElement;
            action.setEnabled(isDurableSubscriber());
        }
    }

    private boolean isDurableSubscriber() {
        if (activeSelection instanceof ListenerComponent) {
            return JMSUtils.isDurableSubscriber((JMSListener) activeSelection.getListener());
        }
        return false;
    }
}
