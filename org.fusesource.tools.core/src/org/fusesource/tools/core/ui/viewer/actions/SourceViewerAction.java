// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.core.ui.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.source.SourceViewer;

public abstract class SourceViewerAction extends Action {

	protected SourceViewer sourceViewer;
	protected int type;

	public SourceViewerAction(SourceViewer sourceViewer, int type) {
		this.sourceViewer = sourceViewer;
		this.type = type;
	}

	public void updateAction() {
		setEnabled(sourceViewer.canDoOperation(type));
	}

	public void run() {
		sourceViewer.doOperation(type);
	}
}
