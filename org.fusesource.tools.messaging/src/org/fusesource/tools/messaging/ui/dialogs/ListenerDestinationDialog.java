// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.ui.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.cnf.model.DataModelManager;
import org.fusesource.tools.messaging.core.IListener;


/**
 * 
 * @author kiranb
 *
 */
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