/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.ui.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.cnf.model.DataModelManager;
import org.fusesource.tools.messaging.core.IListener;

public class ListenerDestinationDialog extends DestinationDialog {

    public ListenerDestinationDialog() {
        super();
    }

    @Override
    protected String getMessage() {
        return "Enter destination details to create a listener";
    }

    @Override
    protected String getTitle() {
        return "Add Listener";
    }

    @Override
    protected String getDialogTitle() {
        return "Add Listener";
    }

    @Override
    protected String getAdvTitle() {
        return "Listener Properties";
    }

    @Override
    protected boolean hasAdvanceSection() {
        return false;
    }

    @Override
    protected void okPressed() {
        super.okPressed();
        try {
            IListener createListener = getCreatedDestination().createListener(listenerProperties);
            createListener.start();
            DataModelManager.getInstance().addDestination((IFile) getSource(), createListener);
        } catch (MessagingException me) {
            me.printStackTrace();
            MessageDialog.openError(Display.getDefault().getActiveShell(), me.getMessage(), me.getCause() != null ? me
                    .getCause().getMessage() : "");
        }
    }
}
