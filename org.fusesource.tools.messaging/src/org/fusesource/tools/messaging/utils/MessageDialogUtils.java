package org.fusesource.tools.messaging.utils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class to show messages to the user
 * 
 * @author kiranb
 * 
 */
public class MessageDialogUtils {
	public static void showErrorMessage(String title, String message) {
		showErrorMessage(Display.getDefault().getActiveShell(), title, message);
	}

	public static void showErrorMessage(Shell shell, String title, String message) {
		MessageDialog.openError(Display.getDefault().getActiveShell(), title, message);
	}
}
