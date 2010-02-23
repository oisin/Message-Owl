/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.fusesource.tools.messaging.cnf.model.IModelConstants;

/**
 * Delegate class for Messaging Facet
 */
public class MsgFacetInstallDelegate implements IDelegate {
    public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor)
            throws CoreException {
        createProjectStructure(project, monitor);
    }

    private void createProjectStructure(IProject project, IProgressMonitor monitor) throws CoreException {
        String[] filesToCreate = { IModelConstants.SENDERS_FILE_PATH, IModelConstants.LISTENERS_FILE_PATH };
        for (String element : filesToCreate) {
            IFile file = project.getFile(element);
            IFolder settingsFolder = (IFolder) file.getParent();
            if (!settingsFolder.exists()) {
                settingsFolder.create(true, true, monitor);
            }
            file.create(new ByteArrayInputStream(new byte[] {}), true, monitor);
        }
    }
}
