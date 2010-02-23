/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class MessageUtil {

    private static final String TOOLID = "JMS Tool";

    /**
     * standard alert box will be run in ui thread with the specified message
     * 
     * @param text
     * @param title
     */
    public static void message(final String title, final String text) {
        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        Display display = shell.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                MessageDialog.openWarning(shell, title, text);
            }
        });

    }

    /**
     * Standard Alert box will be run in the UI thread regardless of where it's called from
     */
    public static void message(String text) {
        message(TOOLID, text);
    }

    /**
     * 
     * Standard Alert box with info icon will be run in the UI thread regardless of where it's
     * called from
     */
    public static void messageInfo(String text) {
        messageInfo(TOOLID, text);
    }

    /**
     * 
     * Standard Alert box with info icon will be run in the UI thread regardless of where it's
     * called from
     */
    public static void messageInfo(final String title, final String text) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                MessageDialog.openInformation(shell, title, text);
            }
        });
    }

    /**
     * 
     * Standard Alert box with question icon will be run in the UI thread regardless of where it's
     * called from
     */
    public static boolean messageQuestion(String text) {
        return messageQuestion(TOOLID, text);
    }

    /**
     * 
     * Standard Alert box with info icon will be run in the UI thread regardless of where it's
     * called from
     */
    public static boolean messageQuestion(final String title, final String text) {
        RunnableWithResult task = new RunnableWithResult() {
            @Override
            public void run() {
                Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                bResult = MessageDialog.openQuestion(shell, title, text);
            }
        };
        Display.getDefault().syncExec(task);
        return task.bResult;
    }

    /**
     * 
     * Standard Alert box with warning icon will be run in the UI thread regardless of where it's
     * called from
     */
    public static void messageWarning(String text) {
        messageWarning(TOOLID, text);
    }

    /**
     * 
     * Standard Alert box with info icon will be run in the UI thread regardless of where it's
     * called from
     */
    public static void messageWarning(final String title, final String text) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                MessageDialog.openWarning(shell, title, text);
            }
        });
    }

    public static void messageError(String text) {
        messageError(TOOLID, text);
    }

    /**
     * Standard Alert box with error will be run in the UI thread regardless of where it's called
     * from
     */
    public static void messageError(final String title, final String text) {
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

                MessageDialog.openError(shell, title, text);
            }
        });
    }

    private static class RunnableWithResult implements Runnable {
        protected boolean bResult;

        public void run() {
        }

    }
}
