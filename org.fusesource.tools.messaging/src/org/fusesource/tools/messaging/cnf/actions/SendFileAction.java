// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.cnf.actions;

import java.io.File;
import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserDialog;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserFilter;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.cnf.model.SenderComponent;
import org.fusesource.tools.messaging.utils.MessageDialogUtils;
import org.fusesource.tools.messaging.utils.SenderUtils;


/**
 * @author sgupta
 * 
 */
public class SendFileAction implements IObjectActionDelegate {
	private SenderComponent activeSenderComponent;
	private Shell shell = null;

	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		shell = arg1.getSite().getShell();
	}

	public void run(IAction arg0) {
		String fileToSend = null;
		URLChooserFilter fileFilter = new URLChooserFilter(new String[] {
				"*.message","*.txt", "*.xml"});
		URLChooserDialog dialog = new URLChooserDialog(shell, "Select File",
				fileFilter);
		if (dialog.open() == Dialog.OK) {
			URL fileUrl = dialog.getURL();
			File file = new File(fileUrl.getFile());
			fileToSend = file.getAbsolutePath();
		}
		try {
			if (fileToSend == null)
				return;
			SenderUtils.sendMessage(fileToSend, activeSenderComponent);
		} catch (MessagingException e) {
			e.printStackTrace();
			MessageDialogUtils.showErrorMessage(Display.getDefault().getActiveShell(), "Send operation failed", e
					.getCause() != null ? e.getCause().getMessage() : e.getMessage());
		}
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		Object firstElement = ((StructuredSelection) arg1).getFirstElement();
		if (!(firstElement instanceof SenderComponent))
			return;
		activeSenderComponent = ((SenderComponent) firstElement);
	}
}
