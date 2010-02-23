/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.ui.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.cnf.model.DataModelManager;
import org.fusesource.tools.messaging.core.ISender;

/**
 * 
 * 
 */
public class SenderDestinationDialog extends DestinationDialog {

    public SenderDestinationDialog() {
        super();
    }

    @Override
    protected String getMessage() {
        return "Enter destination details to create a sender";
    }

    @Override
    protected String getDialogTitle() {
        return "Add Sender";
    }

    @Override
    protected String getTitle() {
        return "Add Sender";
    }

    @Override
    protected String getAdvTitle() {
        return "Sender Properties";
    }

    @Override
    protected boolean hasAdvanceSection() {
        return false;
    }

    @Override
    protected void okPressed() {
        super.okPressed();
        try {
            ISender createSender = getCreatedDestination().createSender(senderProperties);
            createSender.start();
            DataModelManager.getInstance().addDestination((IFile) getSource(), createSender);
        } catch (MessagingException me) {
            me.printStackTrace();
            MessageDialog.openError(Display.getDefault().getActiveShell(), me.getMessage(), me.getCause() != null ? me
                    .getCause().getMessage() : "");
        }
    }
}
