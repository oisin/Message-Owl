/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.ui.url.urlchooser.workspacechooser;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Display;
import org.fusesource.tools.core.util.ResourceUtil;

public class ResourceChooserProvider extends WorkspaceChooserProvider {
    public static String DISPLAY_ID = "Resource";
    public static String ID = "Resource";
    private IFolder rootFolder;

    public ResourceChooserProvider() {
        super(null, "Test Select Resource", "Test Select Resource", false);
        String currentProjectName = ResourceUtil.getCurrentProjectName();
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(currentProjectName);
        rootFolder = project.getFolder("/src/main/resources");
    }

    @Override
    protected WorkSpaceChooserDialog getWorkSpaceChooserDialogInstance() {
        WorkSpaceChooserDialog dialog = new WorkSpaceChooserDialog(Display.getCurrent().getActiveShell(),
                currentSelectedResource, filter, title, allowNew, msg, rootFolder);
        dialog.setValidator(validator);
        return dialog;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_ID;
    }

    @Override
    public String getID() {
        return ID;
    }

}
