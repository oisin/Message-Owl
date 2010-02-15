package org.fusesource.tools.messaging.editors;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;
import org.fusesource.tools.messaging.core.IListener;


/**
 * 
 * @since
 * @author sgupta
 * @version
 */

public class MessageEditorInput implements IStorageEditorInput {

	private MessageEditorStorage storage = null;

	private IListener listener = null;

	private IFile file = null;

	private String name;
	
	private static String strConstant = "Listeners";

	public MessageEditorInput(IFile file, IListener listener) {
		storage = new MessageEditorStorage(listener, file);
		this.listener = listener;
		this.file = file;
		name = listener.getDestination() != null ? listener.getDestination().getDestinationName() : "Messages Editor";
	}

	/**
	 * @return the listener
	 */
	public IListener getListener() {
		return listener;
	}

	public IStorage getStorage() throws CoreException {
		return storage;
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return name;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		String type = listener.getDestination().getDestinationType().getType();
		StringBuilder editorTitle = new StringBuilder();
		IProject project = file.getProject();
		editorTitle.append(project.getName());
		editorTitle.append(File.separator);
		editorTitle.append(strConstant);
		editorTitle.append(File.separator);
		if (listener instanceof ILabelProvider)
			editorTitle.append(((ILabelProvider) listener).getText(listener));
		else
			editorTitle.append(getName());
		editorTitle.append(" (");
		editorTitle.append(type);
		editorTitle.append(")");
		return editorTitle.toString();
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class arg0) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MessageEditorInput)) {
			return false;
		}
		MessageEditorInput editorInput = (MessageEditorInput) obj;
		if (this.file.equals(editorInput.file) && this.listener.equals(editorInput.listener)) {
			return true;
		}
		return super.equals(obj);
	}
}