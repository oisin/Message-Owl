/**
 * 
 */
package org.fusesource.tools.messaging.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.fusesource.tools.messaging.core.IListener;


/**
 * @author
 * 
 */
public class MessagesEditor extends FormEditor {
	private IListener listener;
	MessagesEditorPage msgEditorPage;
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		if (input instanceof MessageEditorInput) {
			MessageEditorInput editorInput = (MessageEditorInput) input;
			this.listener = editorInput.getListener();
			this.setPartName(editorInput.getName());
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}
	
	@Override
	public void doSaveAs() {
	}
	
	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public IListener getListener() {
		return listener;
	}

	@Override
	public void dispose() {
		if(msgEditorPage!=null)
			msgEditorPage.clearPage();
		super.dispose();
	}

	@Override
	protected void addPages() {
		try {
			msgEditorPage = new MessagesEditorPage(this);
			addPage(msgEditorPage);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
