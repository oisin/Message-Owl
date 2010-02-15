package org.fusesource.tools.core.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

public class ProjectUtil {
	public static boolean isMavenProject(IProject project){
		IFacetedProject facetedProject;
		try {
			facetedProject = ProjectFacetsManager.create(project, true, null);
			IProjectFacet projectFacet = ProjectFacetsManager
					.getProjectFacet("fuse.maven");
			return !facetedProject.hasProjectFacet(projectFacet);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return true;
	}
}
