package org.fusesource.tools.messaging.cnf.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.server.core.IServer;
import org.fusesource.tools.messaging.cnf.model.IModelConstants;
import org.fusesource.tools.messaging.cnf.model.ListenersRootComponent;
import org.fusesource.tools.messaging.cnf.model.SendersRootComponent;
import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.server.MessagingServersUtil;
import org.fusesource.tools.messaging.ui.dialogs.DestinationDialog;
import org.fusesource.tools.messaging.ui.dialogs.ListenerDestinationDialog;
import org.fusesource.tools.messaging.ui.dialogs.SenderDestinationDialog;
import org.fusesource.tools.messaging.utils.ExtensionsUtil;


/*we need to create an abstract action...*/
public class AddDestinationAction implements IObjectActionDelegate {
	private Shell shell;
	private Object source;

	/**
	 * Constructor for Action1.
	 */
	public AddDestinationAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		try {
			DestinationDialog destinationDialog = null;
			if (isSender()) {
				destinationDialog = ExtensionsUtil.getDestinationUIExtension(DestinationDialog.ISENDER_ATTRIBUTE,
						getProviderID());
				if (destinationDialog == null)
					destinationDialog = new SenderDestinationDialog();
			} else {
				destinationDialog = ExtensionsUtil.getDestinationUIExtension(DestinationDialog.ILISTENER_ATTRIBUTE,
						getProviderID());
				if (destinationDialog == null)
					destinationDialog = new ListenerDestinationDialog();
			}
			destinationDialog.setSource(getSource());
			destinationDialog.open();
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(shell, "Add Destination", e.getMessage());
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection.isEmpty())
			return;
		if (selection instanceof TreeSelection) {
			TreeSelection tSel = (TreeSelection) selection;
			Object firstElement = tSel.getFirstElement();
			IFile modelFile = null;
			if (firstElement instanceof SendersRootComponent) {
				modelFile = ((SendersRootComponent) firstElement).getFile();
			} else if (firstElement instanceof ListenersRootComponent) {
				modelFile = ((ListenersRootComponent) firstElement).getFile();
			}
			setSource(modelFile);
			return;// We are not interested...
		}

	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public boolean isSender() {
		if (IModelConstants.SENDERS_EXT.equals(((IFile) getSource()).getFileExtension()))
			return true;
		return false;
	}

	private String getProviderID() throws Exception {
		String providerId = "";
		IServer deployedServer = MessagingServersUtil.getDeployedServer(((IFile) getSource()).getProject());
		IProvider provider = MessagingServersUtil.getProvider(deployedServer);
		if (provider != null)
			providerId = provider.getId();

		return providerId;
	}
}
