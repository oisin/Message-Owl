/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.cnf.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.fusesource.tools.messaging.MsgProjectStateManager;
import org.fusesource.tools.messaging.ProjectStateEvent;
import org.fusesource.tools.messaging.ProjectStateListener;
import org.fusesource.tools.messaging.cnf.actions.OpenMessagesEditorAction;
import org.fusesource.tools.messaging.cnf.model.BaseComponent;
import org.fusesource.tools.messaging.cnf.model.BaseGroupComponent;
import org.fusesource.tools.messaging.cnf.model.IModelConstants;
import org.fusesource.tools.messaging.cnf.model.ListenerComponent;
import org.fusesource.tools.messaging.cnf.model.ListenersRootComponent;
import org.fusesource.tools.messaging.core.IListener;

/**
 * Handles UI changes to the Messaging Projects in CNF
 */
public class MsgProjectChangeHandler implements IResourceChangeListener, IResourceDeltaVisitor {
    private StructuredViewer viewer;
    private final MsgProjectStateListener prjStateListener;
    public static Font DEFAULT_FONT, BOLD_FONT;

    public MsgProjectChangeHandler() {
        prjStateListener = new MsgProjectStateListener();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
        MsgProjectStateManager.getInstance().addProjectStateListener(prjStateListener);
    }

    public void resourceChanged(IResourceChangeEvent event) {
        IResourceDelta delta = event.getDelta();
        try {
            // Interested only in the content change event...
            delta.accept(this, IResourceDelta.CONTENT);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    public boolean visit(IResourceDelta delta) {
        IResource source = delta.getResource();
        switch (source.getType()) {
            case IResource.FILE:
                final IFile file = (IFile) source;
                Object element = ((StructuredSelection) viewer.getSelection()).getFirstElement();
                // There is one case where we have to handle refresh for the
                // Listeners node, we auto create listener for reply to destination,
                // but that doen't show up until we explicitly refresh the project
                // See notes in UI-365 for more details;
                if (IModelConstants.SENDERS_FILE_NAME.equals(file.getName())) {
                    updateUI(element);
                } else if (IModelConstants.LISTENERS_FILE_NAME.equals(file.getName())) {
                    if (element instanceof ListenersRootComponent || element instanceof ListenerComponent) {
                        updateUI(element);
                    } else {
                        updateUI(findListenersRootComponent(file));
                    }
                }
                return false;
        }
        return true;
    }

    public void updateUI(final Object element) {
        if (viewer == null || element == null) {
            return;
        }
        Runnable runnable = new Runnable() {
            public void run() {
                Object elementToRefresh = element;
                if (element instanceof IListener) {
                    elementToRefresh = findListenerComponent((IListener) element);
                    // TODO could not refresh the collapsed listener components
                } else if (element instanceof BaseComponent) {
                    elementToRefresh = ((BaseComponent) element).getParent();
                }
                if (elementToRefresh == null) {
                    return;
                }
                viewer.refresh(elementToRefresh, true);
                // If we are creating Senders/Listeners we want to show
                // expand and show the complete subtree
                if (elementToRefresh instanceof BaseGroupComponent) {
                    ((TreeViewer) viewer).expandToLevel(elementToRefresh, AbstractTreeViewer.ALL_LEVELS);
                }
            }

        };
        Display.getDefault().asyncExec(runnable);
    }

    /**
     * Find the corresponding ListenerComponent to refresh the tree...
     * 
     * @param element
     * @return
     */
    private ListenerComponent findListenerComponent(IListener element) {
        TreeItem[] items = ((TreeViewer) viewer).getTree().getItems();
        for (int i = 0; i < items.length; i++) {
            if (!(items[i].getData() instanceof IProject)) {
                continue;
            }
            // Get Project's children
            TreeItem[] projectChildren = items[i].getItems();
            for (TreeItem element2 : projectChildren) {
                if (element2.getData() instanceof ListenersRootComponent) {
                    ListenersRootComponent listenersRoot = (ListenersRootComponent) element2.getData();
                    Object[] listenerComponents = listenersRoot.getChildren();
                    if (listenerComponents == null) {
                        break;// Only one listeners root component is expected in a project
                    }
                    // Iterate over each ListenerComponent
                    for (Object listenerComponent2 : listenerComponents) {
                        if (listenerComponent2 instanceof ListenerComponent) {
                            ListenerComponent listenerComponent = (ListenerComponent) listenerComponent2;
                            if (element.equals(listenerComponent.getListener())) {
                                return listenerComponent;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find the corresponding ListenersRootComponent to refresh the tree...
     * 
     * @param modelFile
     * @return
     */
    private ListenersRootComponent findListenersRootComponent(final IFile modelFile) {
        if (viewer == null) {
            return null;
        }
        final ListenersRootComponent[] root = new ListenersRootComponent[] { null };
        Runnable runnable = new Runnable() {
            public void run() {
                TreeItem[] items = ((TreeViewer) viewer).getTree().getItems();
                // Iterate over the correct project item
                for (TreeItem item : items) {
                    if (item.getData().equals(modelFile.getProject())) {
                        root[0] = searchListenersNode(modelFile, item.getItems());
                        return;
                    }
                }
                return;
            }
        };
        Display.getDefault().syncExec(runnable);
        return root[0];
    }

    private ListenersRootComponent searchListenersNode(IFile modelFile, TreeItem[] items) {
        for (TreeItem item : items) {
            if (item.getData() instanceof ListenersRootComponent) {
                ListenersRootComponent root = (ListenersRootComponent) item.getData();
                if (root.getFile().equals(modelFile)) {
                    return root;
                }
            }
        }
        return null;
    }

    /**
     * The class will be notified when a server state is changed Based on the event, we can refresh
     * any UI decorations if required
     */
    class MsgProjectStateListener implements ProjectStateListener {
        public void stateChanged(ProjectStateEvent event) {
            IProject[] projects = event.getProjects();
            for (IProject project : projects) {
                updateUI(project);
            }
        }
    }

    // TODO move this to some UI utils...
    private void initFonts() {
        DEFAULT_FONT = ((TreeViewer) viewer).getTree().getFont();
        if (BOLD_FONT == null) {
            FontData[] fontData = DEFAULT_FONT.getFontData();
            for (FontData fdata : fontData) {
                fdata.setStyle(fdata.getStyle() | SWT.BOLD);
            }
            BOLD_FONT = new Font(((TreeViewer) viewer).getTree().getDisplay(), fontData);
        }
    }

    public void cleanUp() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        MsgProjectStateManager.getInstance().removeProjectStateListener(prjStateListener);
    }

    public void initViewer(StructuredViewer viewer2) {
        this.viewer = viewer2;
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                Object firstElement = ((StructuredSelection) event.getSelection()).getFirstElement();
                if (!(firstElement instanceof ListenerComponent)) {
                    return;
                }
                try {
                    new OpenMessagesEditorAction().OpenMessagesEditor((ListenerComponent) firstElement);
                } catch (Exception e) {
                    e.printStackTrace();
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", "Failed To Open Editor");
                }
            }
        });
        initFonts();
    }
}
