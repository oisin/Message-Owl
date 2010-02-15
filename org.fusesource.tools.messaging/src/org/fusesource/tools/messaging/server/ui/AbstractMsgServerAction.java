package org.fusesource.tools.messaging.server.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.server.core.IServer;
import org.fusesource.tools.messaging.IConstants;


/**
 * 
 * @author kiranb
 * 
 */
public abstract class AbstractMsgServerAction implements IObjectActionDelegate {

	private IServer selectedServer;

	public AbstractMsgServerAction() {
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public void selectionChanged(IAction action, ISelection selection) {
		Object firstElement = ((StructuredSelection) selection).getFirstElement();
		if (firstElement instanceof IServer) {
			selectedServer = (IServer) firstElement;
		}
		action.setEnabled(canEnable());
	}

	protected abstract boolean canEnable();

	protected IServer getSelectedServer() {
		return selectedServer;
	}

	protected String getServerName() {
		if (getSelectedServer() != null)
			return getSelectedServer().getName();
		return IConstants.EMPTY_STRING;
	}
}
