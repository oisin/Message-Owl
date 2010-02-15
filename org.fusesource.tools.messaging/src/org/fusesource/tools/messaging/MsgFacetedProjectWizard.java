package org.fusesource.tools.messaging;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.ui.ModifyFacetedProjectWizard;
import org.fusesource.tools.messaging.server.MessagingServersUtil;


/**
 * Customized wizard to create a Messaging Project
 * 
 * @author kiranb
 * 
 */
public class MsgFacetedProjectWizard extends ModifyFacetedProjectWizard
		implements INewWizard, IExecutableExtension {
	private MsgProjectCreationPage fusePrjCreationPage;
	private HashSet<IProjectFacet> fixedFacets = null;
	private IWorkbench workbench;
	private IConfigurationElement configElement;

	public MsgFacetedProjectWizard() {
		setWindowTitle("New Messaging Project");
	}

	protected void createProjectCreationPage() {
		fusePrjCreationPage = new MsgProjectCreationPage("fuse.project.create");
	}

	public IWizardPage getProjectCreationPage() {
		return fusePrjCreationPage;
	}

	protected String getProjectName() {
		return fusePrjCreationPage.getProjectName();
	}

	protected IPath getProjectLocation() {
		return fusePrjCreationPage.getLocationPath();
	}

	@Override
	public void addPages() {
		setShowFacetsSelectionPage(false);
		if (this.fixedFacets == null) {
			final IFacetedProjectWorkingCopy fpjwc = getFacetedProjectWorkingCopy();

			// set the list of facets that are fixed and cannot be removed from
			// the project
			Map<IProjectFacet, SortedSet<IProjectFacetVersion>> facets = fpjwc
					.getAvailableFacets();
			this.fixedFacets = new HashSet<IProjectFacet>();
			for (Iterator<IProjectFacet> iterator = facets.keySet().iterator(); iterator
					.hasNext();) {
				IProjectFacet type = iterator.next();
				if (type.getId().equals("fuse.messaging")) {
					this.fixedFacets.add(type);
				}
			}
			fpjwc.setFixedProjectFacets(fixedFacets);
		}

		createProjectCreationPage();
		addPage(fusePrjCreationPage);
		super.addPages();
	}

	@Override
	public IWizardPage[] getPages() {
		final IWizardPage[] base = super.getPages();
		final IWizardPage[] pages = new IWizardPage[base.length + 1];

		pages[0] = fusePrjCreationPage;
		System.arraycopy(base, 0, pages, 1, base.length);

		return pages;
	}

	@Override
	public IWizardPage getNextPage(final IWizardPage page) {
		if (page == fusePrjCreationPage) {
			final IFacetedProjectWorkingCopy fpjwc = getFacetedProjectWorkingCopy();
			fpjwc.setProjectName(getProjectName());
			if (((WizardNewProjectCreationPage) page).useDefaults()) {
				fpjwc.setProjectLocation(null);
			} else {
				fpjwc.setProjectLocation(getProjectLocation());
			}
		}

		return super.getNextPage(page);
	}

	@Override
	public boolean canFinish() {
		return fusePrjCreationPage.validate() && super.canFinish();
	}

	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		workbench = arg0;
	}

	public void setInitializationData(IConfigurationElement arg0, String arg1,
			Object arg2) throws CoreException {
		configElement = arg0;
	}

	public String getSelectedServerId() {
		return fusePrjCreationPage.getSelectedServerId();
	}

	@Override
	public boolean performFinish() {
		super.performFinish();
		IProject project = getFacetedProjectWorkingCopy().getProject();
		try {
			MessagingServersUtil.deployModule(project, getSelectedServerId());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		BasicNewProjectResourceWizard.updatePerspective(configElement);
        BasicNewResourceWizard.selectAndReveal(project, workbench.getActiveWorkbenchWindow());
        
		return true;
	}
}
