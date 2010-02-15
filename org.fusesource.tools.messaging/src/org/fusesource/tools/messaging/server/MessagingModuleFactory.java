// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.internal.FacetedProjectNature;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.model.ModuleDelegate;
import org.eclipse.wst.server.core.util.ProjectModuleFactoryDelegate;
import org.fusesource.tools.messaging.IConstants;


@SuppressWarnings("restriction")
public class MessagingModuleFactory extends ProjectModuleFactoryDelegate {
	protected Map<IModule, MessagingProjectModule> moduleDelegates = new HashMap<IModule, MessagingProjectModule>();

	@Override
	public ModuleDelegate getModuleDelegate(IModule module) {
		return (ModuleDelegate) moduleDelegates.get(module);
	}

	@Override
	protected IModule[] createModules(IProject project) {
		try {
			FacetedProjectNature nature = (FacetedProjectNature) project.getNature(FacetedProjectNature.NATURE_ID);
			if (nature != null)
				return createModules(nature);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected IModule[] createModules(FacetedProjectNature nature) {
		IProject project = nature.getProject();
		try {
			IFacetedProject comp = ProjectFacetsManager.create(project);
			return createModuleDelegates(comp.getProject());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected IModule[] createModuleDelegates(IProject component) {
		List<IModule> projectModules = new ArrayList<IModule>();
		try {
			if (MessagingServersUtil.isMsgFacetedProject(component.getProject())) {
				IVirtualComponent vComp = ComponentCore.createComponent(component.getProject());
				MessagingProjectModule delegate = new MessagingProjectModule(component.getProject(), component
						.getFullPath(), vComp);
				IModule module = createModule(component.getName(), component.getName(), IConstants.FUSE_PRJ_MODULE_ID,
						delegate.getVersion(), component.getProject());
				moduleDelegates.put(module, delegate);
				projectModules.add(module);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectModules.toArray(new IModule[projectModules.size()]);
	}

	protected IPath[] getListenerPaths() {
		return new IPath[] { new Path(".project"), // nature
				new Path(".settings/org.eclipse.wst.common.project.facet.core.xml") // facets
		};
	}
	
}
