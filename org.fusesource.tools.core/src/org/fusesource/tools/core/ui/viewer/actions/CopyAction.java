// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.core.ui.viewer.actions;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.actions.ActionFactory;

public class CopyAction extends SourceViewerAction {

	public CopyAction(SourceViewer sourceViewer) {
		super(sourceViewer, ITextOperationTarget.COPY);
		setProperties();
	}

	public void setProperties() {
		setText(ActionConstants.COPY_EDIT);
		// setActionDefinitionId("org.eclipse.ui.edit.copy");
		setId(ActionFactory.COPY.getId());
	}

}
