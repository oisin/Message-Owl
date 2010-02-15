// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
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
 * @author kiranb
 * 
 */
public class MsgFacetUninstallDelegate implements IDelegate {
	public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor)
			throws CoreException {
		cleanProject(project, monitor);
	}

	private void cleanProject(IProject project, IProgressMonitor monitor) throws CoreException {
		String[] filesToClean = { IModelConstants.SENDERS_FILE_PATH, IModelConstants.LISTENERS_FILE_PATH };
		for (int i = 0; i < filesToClean.length; i++) {
			IFile file = project.getFile(filesToClean[i]);
			file.delete(true, new NullProgressMonitor());
		}
	}
}