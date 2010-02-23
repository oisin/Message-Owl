/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.utils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class to show messages to the user
 */
public class MessageDialogUtils {
    public static void showErrorMessage(String title, String message) {
        showErrorMessage(Display.getDefault().getActiveShell(), title, message);
    }

    public static void showErrorMessage(Shell shell, String title, String message) {
        MessageDialog.openError(Display.getDefault().getActiveShell(), title, message);
    }
}
