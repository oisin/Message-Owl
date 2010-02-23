/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.messaging.cnf.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.cnf.model.BaseComponent;
import org.fusesource.tools.messaging.cnf.model.BaseGroupComponent;
import org.fusesource.tools.messaging.cnf.model.DataModelManager;
import org.fusesource.tools.messaging.cnf.model.ListenerComponent;
import org.fusesource.tools.messaging.cnf.model.SenderComponent;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.ISender;
import org.fusesource.tools.messaging.editors.MessageEditorInput;

public class DeleteDestinationAction implements IObjectActionDelegate {
    private BaseComponent activeSelection;

    public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
    }

    public void run(IAction arg0) {
        try {
            BaseGroupComponent baseGroupComponent = (BaseGroupComponent) activeSelection.getParent();
            IFile modelFile = baseGroupComponent.getFile();
            if (activeSelection instanceof SenderComponent && modelFile != null) {
                deleteSender(modelFile);
            } else if (activeSelection instanceof ListenerComponent && modelFile != null) {
                deleteListener(modelFile);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method closed the editor window opened for the listener
     * 
     * @param listener
     * @param listenerComponent
     */
    protected void closeEditorWindow(IListener listener, ListenerComponent listenerComponent) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
        IFile fileToOpen = ((BaseGroupComponent) listenerComponent.getParent()).getFile();
        MessageEditorInput messageEditorInput = new MessageEditorInput(fileToOpen, listener);
        for (IWorkbenchWindow workbenchWindow : workbenchWindows) {
            IWorkbenchPage page = workbenchWindow.getActivePage();
            IEditorPart findEditor = page.findEditor(messageEditorInput);
            if (findEditor instanceof FormEditor) {
                ((FormEditor) findEditor).close(false);
                break;
            }
        }
    }

    protected void deleteSender(IFile modelFile) throws MessagingException {
        boolean openConfirm = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "Confirm",
                "Are you sure you want to delete the Sender?");
        if (!openConfirm) {
            return;
        }
        ISender sender = ((SenderComponent) activeSelection).getSender();
        sender.stop();
        DataModelManager.getInstance().removeDestination(modelFile, sender);
    }

    protected void deleteListener(IFile modelFile) throws MessagingException {
        boolean openConfirm = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "Confirm",
                "Are you sure you want to delete the Listener?");
        if (!openConfirm) {
            return;
        }
        ListenerComponent listenerComponent = (ListenerComponent) activeSelection;
        IListener listener = listenerComponent.getListener();
        listener.stop();
        DataModelManager.getInstance().removeDestination(modelFile, listener);
        closeEditorWindow(listener, listenerComponent);
    }

    public void selectionChanged(IAction arg0, ISelection arg1) {
        Object firstElement = ((StructuredSelection) arg1).getFirstElement();
        if ((firstElement instanceof BaseComponent)) {
            activeSelection = (BaseComponent) firstElement;
        }
    }
}
