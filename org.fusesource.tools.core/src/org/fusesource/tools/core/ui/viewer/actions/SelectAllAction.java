// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.core.ui.viewer.actions;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.actions.ActionFactory;

public class SelectAllAction extends SourceViewerAction {

	public SelectAllAction(SourceViewer sourceViewer) {
		super(sourceViewer, ITextOperationTarget.SELECT_ALL);
		setProperties();
	}

	public void setProperties() {
		// setActionDefinitionId("org.eclipse.ui.edit.selectAll");
		setId(ActionFactory.SELECT_ALL.getId());
		setText(ActionConstants.SELECT_ALL);
	}
}
