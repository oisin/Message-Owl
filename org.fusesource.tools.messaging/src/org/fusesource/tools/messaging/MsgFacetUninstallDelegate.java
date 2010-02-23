/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.fusesource.tools.messaging.cnf.model.IModelConstants;

/**
 * Delegate class for Un-installing Messaging Facet
 */
public class MsgFacetUninstallDelegate implements IDelegate {
    public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor)
            throws CoreException {
        cleanProject(project, monitor);
    }

    private void cleanProject(IProject project, IProgressMonitor monitor) throws CoreException {
        String[] filesToClean = { IModelConstants.SENDERS_FILE_PATH, IModelConstants.LISTENERS_FILE_PATH };
        for (String element : filesToClean) {
            IFile file = project.getFile(element);
            file.delete(true, new NullProgressMonitor());
        }
    }
}
