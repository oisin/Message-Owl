// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.core.ui.viewer.actions;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.actions.ActionFactory;

public class DeleteAction extends SourceViewerAction {

	public DeleteAction(SourceViewer sourceViewer) {
		super(sourceViewer, ITextOperationTarget.DELETE);
		setProperties();
	}

	private void setProperties() {
		setText(ActionConstants.DELETE_EDIT);
		// setActionDefinitionId("org.eclipse.ui.edit.delete");
		setId(ActionFactory.DELETE.getId());
	}
}
