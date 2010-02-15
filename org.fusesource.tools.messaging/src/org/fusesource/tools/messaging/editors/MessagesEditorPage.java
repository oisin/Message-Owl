package org.fusesource.tools.messaging.editors;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.ui.ImageConstants;
import org.fusesource.tools.messaging.utils.ImagesUtil;


/**
 * @author sgupta
 */
public class MessagesEditorPage extends FormPage {

	private MessagesMasterBlock block;
	private final FormEditor formEditor;

	public MessagesEditorPage(FormEditor editor) {
		super(editor, "messages", "Messages");
		formEditor = editor;
		block = new MessagesMasterBlock(this);
	}

	protected void createFormContent(final IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		form.setText(getDisplayText());
		form.setBackgroundImage(ImagesUtil.getInstance().getImage(ImageConstants.MESSAGE_FORM_BANNER));
		block.createContent(managedForm);
	}

	private String getDisplayText() {
		MessageEditorInput editorInput = (MessageEditorInput) formEditor.getEditorInput();
		StringBuffer display = new StringBuffer("Received Messages for ");
		display.append(editorInput.getName());

		IDestination destination = editorInput.getListener().getDestination();
		if (destination != null && destination.getDestinationType() != null) {
			String type = destination.getDestinationType().getType();
			if (type != null) {
				display.append(" (");
				display.append(type);
				display.append(")");
			}
		}
		return display.toString();
	}

	/**
	 * @return the block
	 */
	public MessagesMasterBlock getBlock() {
		return block;
	}

	public void clearPage(){
		block.clearBlock();
	}
}