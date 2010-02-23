/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

/**
 * Defines a Messaging Perspective
 * 
 */
public class MessagingPerspective implements IPerspectiveFactory {

    private static final String MSG_PERSPECTIVE_ID = "org.fusesource.tools.messaging.FuseMessagingPerspective";
    private static final String SERVERS_VIEW_ID = "org.eclipse.wst.server.ui.ServersView";
    private static final String PROBLEMS_VIEW_ID = "org.eclipse.ui.views.ProblemView";

    public void createInitialLayout(IPageLayout layout) {
        defineActions(layout);
        defineLayout(layout);
    }

    protected void defineActions(IPageLayout layout) {
        // Adding new wizard
        layout.addNewWizardShortcut("org.fusesource.tools.messaging.MsgFacetedProjectWizard");
        layout.addNewWizardShortcut("org.fusesource.tools.message.presentation.MessageModelWizardID");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");

        // layout.addPerspectiveShortcut(MSG_PERSPECTIVE_ID);
        // ombh 02feb10 removed these to decouple from other code
        // layout.addPerspectiveShortcut(PerspectiveConstants.ID_FID_PERSPECTIVE);
        // layout.addPerspectiveShortcut(PerspectiveConstants.ID_DEBUG_PERSPECTIVE);

        layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
        layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
        layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
        layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");
    }

    protected void defineLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.25F, editorArea);
        left.addView(ProjectExplorer.VIEW_ID);

        IFolderLayout bottomfolderlayout = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.75F, editorArea);
        bottomfolderlayout.addView(SERVERS_VIEW_ID);
        bottomfolderlayout.addView(PROBLEMS_VIEW_ID);
    }
}
