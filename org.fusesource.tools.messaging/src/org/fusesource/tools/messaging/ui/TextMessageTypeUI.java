package org.fusesource.tools.messaging.ui;

import org.fusesource.tools.message.extensions.IMessageEditorExtension;

public class TextMessageTypeUI extends SimpleMessageTypeUI {

	protected HeaderPropertyComposite viewHolder = null;

	@Override
	public IMessageEditorExtension getEditorExtension() {
		return new TextMessageEditorExtension();
	}
	
	public boolean canHandle(Object msg) {
		return false;
	}
}
