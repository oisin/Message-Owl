// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.core.ui.viewer.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.source.SourceViewer;

public class SourceViewerContextMenuProvider {
	private SourceViewer sourceViewer;
	private List<SourceViewerAction> actionList;
	private CopyAction copyAction;
	private PasteAction pasteAction;
	private DeleteAction deleteAction;
	private CutAction cutAction;
	private SourceViewerAction selectAllAction;

	public SourceViewerContextMenuProvider(SourceViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
		if (sourceViewer != null) {
			createSourceActions();
			fillContextMenu();
		}
	}

	/**
	 * Creating Source Viewer Actions
	 */
	private void createSourceActions() {
		actionList = new ArrayList<SourceViewerAction>();
		copyAction = new CopyAction(sourceViewer);
		actionList.add(copyAction);
		pasteAction = new PasteAction(sourceViewer);
		actionList.add(pasteAction);
		deleteAction = new DeleteAction(sourceViewer);
		actionList.add(deleteAction);
		cutAction = new CutAction(sourceViewer);
		actionList.add(cutAction);
		selectAllAction = new SelectAllAction(sourceViewer);
		actionList.add(selectAllAction);

	}

	/**
	 * Adding Context Menu for Query Source Viewer.
	 */
	private void fillContextMenu() {
		MenuManager menuManager = null;
		if (menuManager == null) {
			menuManager = new MenuManager("QuerySourcePopupMenu"); //$NON-NLS-1$
			menuManager.setRemoveAllWhenShown(true);
			menuManager.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {

					updateActions();
					manager.add(cutAction);
					manager.add(copyAction);
					manager.add(pasteAction);
					manager.add(deleteAction);
					manager.add(new Separator());
					manager.add(selectAllAction);
				}

			});
		}
		sourceViewer.getTextWidget().setMenu(menuManager.createContextMenu(sourceViewer.getTextWidget()));
	}

	/**
	 * updating the Actions while popup Context Menu
	 */

	private void updateActions() {
		for (SourceViewerAction action : actionList) {
			action.updateAction();
		}
	}

}
