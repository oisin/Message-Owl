/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
/** 

 */

package org.fusesource.tools.core.ui.url.urlchooser.workspacechooser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class WorkspaceSelectionContentProvider implements ITreeContentProvider {
    /**
     * Specify whether or not to show closed projects in the tree viewer. Default is to show closed
     * projects.
     * 
     * @param show
     *            boolean if false, do not show closed projects in the tree
     */

    protected boolean showClosedProjects = true;
    protected Object extensionSelected;

    /**
     * Creates a new WorkspaceSelectionContentProvider.
     */
    public WorkspaceSelectionContentProvider(boolean showClosedProjects, String[] filterArray) {
        this.showClosedProjects = showClosedProjects;
    }

    /**
     * The visual part that is using this content provider is about to be disposed. Deallocate all
     * allocated SWT resources.
     */
    public void dispose() {
    }

    /*
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object element) {
        if (element instanceof IWorkspace) {
            // check if closed projects should be shown
            IProject[] allProjects = ((IWorkspace) element).getRoot().getProjects();
            if (showClosedProjects) {
                return allProjects;
            }
            List accessibleProjects = getAccessibleProjects(allProjects);
            if (accessibleProjects != null) {
                return accessibleProjects.toArray();
            } else {
                return new Object[0];
            }
        } else if (element instanceof IContainer) {
            List containerChildren = new ArrayList();
            IContainer currentContainer = (IContainer) element;
            // Check if the current container is accessible before retireiving the memebers
            if (currentContainer.isAccessible()) {
                populateChildren(containerChildren, currentContainer);
            }
            return containerChildren.toArray();
        }
        return new Object[0];
    }

    private void populateChildren(List containerChildren, IContainer currentContainer) {
        try {
            IResource[] members = currentContainer.members();
            for (IResource member : members) {
                containerChildren.add(member);
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private List getAccessibleProjects(IProject[] allProjects) {
        List accessibleProjects = new ArrayList();
        for (IProject allProject : allProjects) {
            if (allProject.isOpen()) {
                accessibleProjects.add(allProject);
            }
        }
        return accessibleProjects;
    }

    /*
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object element) {
        return getChildren(element);
    }

    /*
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        if (element instanceof IResource) {
            return ((IResource) element).getParent();
        }
        return null;
    }

    /*
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
        if (element instanceof IFile) {
            return false;
        }
        return getChildren(element).length > 0;
    }

    /*
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public void setFilterSelected(Object selected) {
        extensionSelected = selected;
    }

}
