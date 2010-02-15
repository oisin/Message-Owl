// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.ui;

import org.fusesource.tools.message.extensions.IMessageEditorExtension;

public class XMLMessageTypeUI extends TextMessageTypeUI {

	@Override
	public IMessageEditorExtension getEditorExtension() {
		return new XMLMessageEditorExtension();
	}

	@Override
	public boolean canHandle(Object msg) {
		return false;
	}

}
