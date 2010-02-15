/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.ui;

import org.fusesource.tools.message.extensions.IMessageEditorExtension;
import org.fusesource.tools.message.extensions.IMessageTypeUI;
import org.fusesource.tools.message.extensions.IMessageViewerExtension;

public class SimpleMessageTypeUI implements IMessageTypeUI {

	private String providerId;

	private String type;

	public IMessageEditorExtension getEditorExtension() {
		return new SimpleMessageEditorExtension();
	}

	public String getProviderId() {
		return providerId;
	}

	public String getType() {
		return type;
	}

	public void setProviderId(String id) {
		this.providerId = id;

	}

	public void setType(String type) {
		this.type = type;

	}

	public IMessageViewerExtension getViewerExtension() {
		return new SimpleMessageViewer();
	}

	public boolean canHandle(Object msg) {
		return false;
	}
}
