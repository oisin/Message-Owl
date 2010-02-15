// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.core.ui.viewer.actions;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.actions.ActionFactory;

public class PasteAction extends SourceViewerAction {

	public PasteAction(SourceViewer sourceViewer) {
		super(sourceViewer, ITextOperationTarget.PASTE);
		setProperties();
	}

	private void setProperties() {
		setText(ActionConstants.PASTE_EDIT);
		// setActionDefinitionId("org.eclipse.ui.edit.paste");
		setId(ActionFactory.PASTE.getId());
	}
}
