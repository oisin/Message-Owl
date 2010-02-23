/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.cnf.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.cnf.model.SenderComponent;
import org.fusesource.tools.messaging.utils.MessageDialogUtils;
import org.fusesource.tools.messaging.utils.SenderUtils;

/**
 * Handles the drag and drop of files on Senders
 * 
 */
public class SendFileDropAdaptorAssistant extends CommonDropAdapterAssistant {

    @Override
    public IStatus handleDrop(CommonDropAdapter arg0, DropTargetEvent dropEvent, Object dropTarget) {
        IFile fileToSend = null;
        if (dropEvent.data instanceof ISelection
                && ((StructuredSelection) dropEvent.data).getFirstElement() instanceof IFile) {
            fileToSend = (IFile) ((StructuredSelection) dropEvent.data).getFirstElement();
        }
        if (fileToSend == null) {
            return Status.CANCEL_STATUS;
        }

        try {
            SenderUtils.sendMessage(fileToSend, ((SenderComponent) dropTarget));
        } catch (MessagingException e) {
            e.printStackTrace();
            MessageDialogUtils.showErrorMessage(Display.getDefault().getActiveShell(), "Send operation failed", e
                    .getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
        return Status.OK_STATUS;
    }

    // TODO Add required validations here - provider based?
    @Override
    public IStatus validateDrop(Object target, int arg1, TransferData arg2) {
        if (!(target instanceof SenderComponent)) {
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }
}
