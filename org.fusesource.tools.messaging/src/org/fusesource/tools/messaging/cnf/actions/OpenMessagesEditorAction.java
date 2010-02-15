package org.fusesource.tools.messaging.cnf.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.fusesource.tools.messaging.cnf.model.BaseGroupComponent;
import org.fusesource.tools.messaging.cnf.model.ListenerComponent;
import org.fusesource.tools.messaging.editors.MessageEditorInput;


public class OpenMessagesEditorAction implements IObjectActionDelegate {

	private ListenerComponent selectedNode;

	public OpenMessagesEditorAction() {
	
	}
	
	public void OpenMessagesEditor(ListenerComponent listenerComp) throws PartInitException {
		if (listenerComp == null)
			return;
		IFile fileToOpen = ((BaseGroupComponent) listenerComp.getParent())
				.getFile();
		if (fileToOpen == null)
			return;
		MessageEditorInput editorInput = new MessageEditorInput(fileToOpen,
				listenerComp.getListener());
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IDE.openEditor(page, editorInput,
				"org.fusesource.tools.messaging.editors.MessagesEditor");
	}

	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
	}

	public void run(IAction arg0) {
		try {
			OpenMessagesEditor(selectedNode);
		} catch (PartInitException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Failed to open messages Editor", e.getMessage());
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		Object firstElement = ((StructuredSelection) arg1).getFirstElement();
		if ((firstElement instanceof ListenerComponent)) {
			selectedNode = (ListenerComponent) firstElement;
		}
	}
}
