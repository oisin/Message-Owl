// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.core.ui.viewer.actions;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.actions.ActionFactory;

public class CutAction extends SourceViewerAction {

	public CutAction(SourceViewer sourceViewer) {
		super(sourceViewer, ITextOperationTarget.CUT);
		setProperties();
	}

	private void setProperties() {
		setText(ActionConstants.CUT_EDIT);
		// setActionDefinitionId("org.eclipse.ui.edit.cut");
		setId(ActionFactory.CUT.getId());
	}
}
