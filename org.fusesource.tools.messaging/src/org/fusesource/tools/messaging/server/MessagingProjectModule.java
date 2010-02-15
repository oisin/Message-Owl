package org.fusesource.tools.messaging.server;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.util.ProjectModule;
import org.fusesource.tools.messaging.IConstants;


public class MessagingProjectModule extends ProjectModule {
	private final IPath moduleRoot;
	protected IVirtualComponent component = null;
	public MessagingProjectModule(IProject project, IPath moduleRoot,IVirtualComponent component) {
		super(project);
		this.moduleRoot = moduleRoot;
		this.component=component;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.server.core.util.ProjectModule#members()
	 */
	@Override
	public IModuleResource[] members() throws CoreException {
		IProject project = getProject();
		return getModuleResources(new Path(""), project);	
	}
	
	public IPath getModuleRoot() {
		return moduleRoot;
	}
	
	public String getVersion() {
		IFacetedProject facetedProject = null;
		try {
			facetedProject = ProjectFacetsManager.create(component.getProject());
			if (facetedProject !=null && ProjectFacetsManager.isProjectFacetDefined(IConstants.FUSE_MSG_PRJ_FACET)) {
				IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(IConstants.FUSE_MSG_PRJ_FACET);
				return facetedProject.getInstalledVersion(projectFacet).getVersionString();
			}
		} catch (Exception e) {
			//Ignore
		}
		return "1.0"; 
	}
	
}
