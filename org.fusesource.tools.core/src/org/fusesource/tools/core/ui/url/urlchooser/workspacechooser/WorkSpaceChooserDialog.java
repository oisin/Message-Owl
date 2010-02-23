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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionValidator;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserFilter;

public class WorkSpaceChooserDialog extends SelectionDialog {
    private static final String ALL_DIRECTORIES = "All Directories";
    public static final String DIRECTORY_FILTER_ID = ".";
    // the widget group;
    protected WorkSpaceSelectionGroup group;
    // protected String [] extensions;
    protected IContentProvider cp;
    protected ILabelProvider lp;
    // the root resource to populate the viewer with
    private IResource initialSelection;

    // allow the user to type in a new container name
    private boolean allowNewContainerName = true;

    // the validation message
    protected Label statusMessage;

    protected String selectedFilter;
    protected Text pathText;
    protected Combo extensionsCombo;
    protected List selectionList = new ArrayList();
    // for validating the selection
    protected ISelectionValidator validator;
    protected CustomViewFilter viewFilter = new CustomViewFilter();
    // show closed projects by default
    private boolean showClosedProjects = true;
    protected Map filter;
    private IFolder rootFolder;

    /**
     * Creates a resource container selection dialog rooted at the given resource. All selections
     * are considered valid.
     * 
     * @param parentShell
     *            the parent shell
     * @param initialRoot
     *            the initial selection in the tree
     * @param allowNewContainerName
     *            <code>true</code> to enable the user to type in a new container name, and
     *            <code>false</code> to restrict the user to just selecting from existing ones
     * @param message
     *            the message to be displayed at the top of this dialog, or
     * @param cp
     *            Add your own ContentProvider.
     * @param lp
     *            Add your own LabelProvider <code>null</code> to display a default message
     */
    public WorkSpaceChooserDialog(Shell parentShell, IResource initialRoot, String title,
            boolean allowNewContainerName, String message, IContentProvider cp, ILabelProvider lp) {
        this(parentShell, initialRoot, null, title, allowNewContainerName, message);
        this.cp = cp;
        this.lp = lp;
    }

    public WorkSpaceChooserDialog(Shell parentShell, IResource initialSelection, Map filter, String title,
            boolean allowNewContainerName, String message) {
        super(parentShell);
        if (title == null) {
            setTitle("Choose from Workspace");
        } else {
            setTitle(title);
        }
        this.initialSelection = initialSelection;
        this.allowNewContainerName = allowNewContainerName;
        this.filter = filter;
        if (message != null) {
            setMessage(message);
        } else {
            setMessage("Select the location from the workspace");
        }
        setShellStyle(getShellStyle() | SWT.RESIZE);
        setFilter(filter);
    }

    public WorkSpaceChooserDialog(Shell activeShell, IResource currentSelectedResource, URLChooserFilter filter2,
            String title, boolean allowNew, String msg, IFolder rootFolder) {
        this(activeShell, currentSelectedResource, filter2, title, allowNew, msg);
        this.rootFolder = rootFolder;
        // TODO Auto-generated constructor stub
    }

    public void setFilter(Map filter) {
        this.filter = filter;
    }

    /*
     * (non-Javadoc) Method declared in Window.
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(shell, IIDEHelpContextIds.CONTAINER_SELECTION_DIALOG);
    }

    private String getSelectedFilterExtension() {
        String selectedExtension = selectedFilter.substring(selectedFilter.indexOf('.') + 1, selectedFilter.length());
        return selectedExtension;
    }

    protected void validate() {
        IPath fullPath = getContainerFullPath();
        if (fullPath == null) {
            getOkButton().setEnabled(false);
            return;
        }
        String errorMsg;
        if (ALL_DIRECTORIES.equals(extensionsCombo.getItem(extensionsCombo.getSelectionIndex()))) {
            errorMsg = null;
        } else {
            errorMsg = validator.isValid(fullPath);
        }
        if (errorMsg == null || errorMsg.equals("")) {
            statusMessage.setText("");
            getOkButton().setEnabled(true);
        } else {
            statusMessage.setForeground(JFaceColors.getErrorText(statusMessage.getDisplay()));
            statusMessage.setText(errorMsg);
            getOkButton().setEnabled(false);
        }
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        // create composite
        Composite area = (Composite) super.createDialogArea(parent);

        Listener listener = new Listener() {
            public void handleEvent(Event event) {
                if (statusMessage != null && validator != null) {
                    validate();
                }
            }

        };

        // create resource selection group
        group = createWorkspaceGroup(area, listener);
        group.treeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    Object item = ((IStructuredSelection) selection).getFirstElement();
                    if (item instanceof IFile) {
                        okPressed();
                    }
                }
            }
        });
        if (initialSelection != null) {
            group.setSelectedLocation(initialSelection);
        } else {
            TreeViewer treeViewer = group.getTreeViewer();

            treeViewer.expandAll();

            TreeItem[] treeItems = treeViewer.getTree().getItems();

            if (treeItems != null && treeItems.length > 0) {
                TreeItem treeItem = treeItems[0];
                treeViewer.getTree().setSelection(treeItem);
            }
        }
        if (!allowNewContainerName) {
            createExtensionsComposite(area);
        } else {
            if (filter != null && !filter.isEmpty()) {
                selectedFilter = (String) filter.keySet().toArray()[0];
            } else {
                selectedFilter = "*.*";
            }
            viewFilter.selectFilter(selectedFilter);
        }
        statusMessage = new Label(parent, SWT.NONE);
        statusMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        statusMessage.setFont(parent.getFont());
        return dialogArea;
    }

    private void createExtensionsComposite(Composite area) {
        Composite cc = new Composite(area, SWT.FILL);
        cc.setLayout(new GridLayout(1, true));
        final GridData gridData = new GridData(SWT.FILL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.minimumWidth = 0;
        gridData.horizontalAlignment = SWT.FILL;
        cc.setLayoutData(gridData);
        final Label extensionsLabel = new Label(cc, SWT.LEFT);
        extensionsLabel.setLayoutData(gridData);
        extensionsLabel.setText("Filter Extensions: ");
        extensionsCombo = new Combo(cc, SWT.LEFT | SWT.READ_ONLY);
        extensionsCombo.setLayoutData(gridData);
        populateCombo();
    }

    protected void populateCombo() {
        if (filter == null) {
            createDefaultChooser();
        }
        Set s = filter.keySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            String element = (String) it.next();
            String extension = (String) filter.get(element);
            extensionsCombo.add(extension);
            selectionList.add(element);
        }
        selectFilterOnCombo();
        ComboListener comboListener = createComboListener();
        extensionsCombo.addSelectionListener(comboListener);
    }

    protected ComboListener createComboListener() {
        return new ComboListener(this);
    }

    protected void createDefaultChooser() {
        filter = new HashMap();
    }

    private void selectFilterOnCombo() {
        int init = 0;
        extensionsCombo.select(init);
        repaintGroup(init);
    }

    protected WorkSpaceSelectionGroup createWorkspaceGroup(Composite area, Listener listener) {
        List filters = getFiltersOnPath();
        return new WorkSpaceSelectionGroup(area, listener, allowNewContainerName, getMessage(), showClosedProjects,
                filters, rootFolder);
    }

    protected List getFiltersOnPath() {
        List l = new ArrayList();
        l.add(viewFilter);
        return l;
    }

    protected IPath getContainerFullPath() {
        IPath fullPath = group.getContainerFullPath();
        String extension = getSelectedFilterExtension();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IContainer[] folders = null;
        if (fullPath != null) {
            folders = workspace.getRoot().findContainersForLocation(fullPath);
            if (folders == null || (folders.length > 0 && !folders[0].exists())) {
                if (fullPath != null
                        && !extension.equals("*")
                        && (fullPath.getFileExtension() == null || !(fullPath.getFileExtension()
                                .equalsIgnoreCase(extension)))) {
                    String path = fullPath.toString();
                    fullPath = new Path(path + "." + extension);
                }
            }
        }

        return fullPath;
    }

    protected IPath getContainerRelativePath() {
        IPath path = group.getContainerRelativePath();
        return path;
    }

    protected IPath getWorkspaceRelativePath() {
        IPath relativePath = group.getRelativePath();
        return relativePath;
    }

    @Override
    protected Control createButtonBar(Composite parent) {
        Control buttonBar = super.createButtonBar(parent);
        validate();
        return buttonBar;
    }

    /**
     * The <code>ContainerSelectionDialog</code> implementation of this <code>Dialog</code> method
     * builds a list of the selected resource containers for later retrieval by the client and
     * closes this dialog.
     */
    @Override
    protected void okPressed() {
        List chosenContainerPathList = new ArrayList();
        IPath returnValue = null;
        returnValue = getContainerFullPath();

        if (returnValue == null) {
            return;
        }
        String pathForSelectedFile = getPathForSelectedFile(returnValue);
        String location = pathForSelectedFile;// getPathValue(pathForSelectedFile);
        if (location != null) {
            chosenContainerPathList.add(location);
        }
        setResult(chosenContainerPathList);
        super.okPressed();
    }

    private String getPathForSelectedFile(IPath returnValue) {
        return returnValue.toPortableString();
    }

    /**
     * Sets the validator to use.
     * 
     * @param validator
     *            A selection validator
     */
    public void setValidator(ISelectionValidator validator) {
        this.validator = validator;
    }

    /**
     * Set whether or not closed projects should be shown in the selection dialog.
     * 
     * @param show
     *            Whether or not to show closed projects.
     */
    public void showClosedProjects(boolean show) {
        this.showClosedProjects = show;
    }

    public void refreshTree() {
        String item = extensionsCombo.getItem(extensionsCombo.getSelectionIndex());

        validate();

        if (item.equals(selectedFilter)) {
            return;
        }
        selectedFilter = item;
        repaintGroup(extensionsCombo.getSelectionIndex());
    }

    protected void repaintGroup(int selected) {
        if (group != null) {
            if (selected < selectionList.size()) {
                selectedFilter = (String) selectionList.get(selected);
            } else {
                selectedFilter = "*.*";
            }
            viewFilter.selectFilter(selectedFilter);
            group.refreshView();
        }
    }

    protected static class CustomViewFilter extends ViewerFilter {

        private String extensionSelected;

        public CustomViewFilter() {

        }

        public CustomViewFilter(String selected) {
            extensionSelected = selected;
        }

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            if (element instanceof IFile) {
                boolean b = qualifiesFilter((IFile) element);
                return b;
            }
            return true;
        }

        protected boolean qualifiesFilter(IFile f) {
            if (extensionSelected != null) {
                if (extensionSelected.equals(DIRECTORY_FILTER_ID)) {
                    return false;
                }
                int dot = extensionSelected.lastIndexOf(".");
                if (dot < 0) {
                    return true;
                }
                String extension = extensionSelected.substring(dot);
                // Filter out those whose extension does not qualify
                if (!extension.equals(".*") && !f.getName().endsWith(extension)) {
                    return false;
                }
            }
            return true;
        }

        public void selectFilter(String s) {
            extensionSelected = s;
        }
    } // end of CustomViewFilter class

    protected static class ComboListener extends SelectionAdapter {
        private WorkSpaceChooserDialog dialog;

        public ComboListener(WorkSpaceChooserDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            dialog.refreshTree();
            super.widgetDefaultSelected(e);
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            dialog.refreshTree();
            super.widgetSelected(e);
        }
    }// end of class ComboListener
}
