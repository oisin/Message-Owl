/** 
 
 */

package org.fusesource.tools.core.ui.url.urlchooser.workspacechooser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.DrillDownComposite;
import org.fusesource.tools.core.util.ResourceUtil;


public class WorkSpaceSelectionGroup extends Composite {
    // The listener to notify of events
    private Listener listener;

    // Enable user to type in new container name
    private boolean allowNewContainerName = true;

    // show all projects by default
    private boolean showClosedProjects = true;

    // Last selection made by user
    private IResource selectedContainer;

    // handle on parts
    private Text containerNameField;

    protected TreeViewer treeViewer;

	protected Text fileNameField;
	
    // the message to display at the top of this dialog
    private static final String DEFAULT_MSG_NEW_ALLOWED = IDEWorkbenchMessages.ContainerGroup_message;

    private static final String DEFAULT_MSG_SELECT_ONLY = IDEWorkbenchMessages.ContainerGroup_selectFolder;

    // sizing constants
    private static final int SIZING_SELECTION_PANE_WIDTH = 320;

    private static final int SIZING_SELECTION_PANE_HEIGHT = 300;
    
    protected List filtersList;
    /**
     * cutomizable label Provider for the treeViewer
     */
    protected IContentProvider contentProvider;
    protected ILabelProvider labelProvider;

    /**
     * Creates a new instance of the widget.
     *
     * @param parent The parent widget of the group.
     * @param listener A listener to forward events to. Can be null if
     *	 no listener is required.
     * @param allowNewContainerName Enable the user to type in a new container
     *  name instead of just selecting from the existing ones.
     */
    public WorkSpaceSelectionGroup(Composite parent, Listener listener,
            boolean allowNewContainerName,List filters) {
        this(parent, listener, allowNewContainerName, null,filters);
    }

    /**
     * Creates a new instance of the widget.
     *
     * @param parent The parent widget of the group.
     * @param listener A listener to forward events to.  Can be null if
     *	 no listener is required.
     * @param allowNewContainerName Enable the user to type in a new container
     *  name instead of just selecting from the existing ones.
     * @param message The text to present to the user.
     */
    public WorkSpaceSelectionGroup(Composite parent, Listener listener,
            boolean allowNewContainerName, String message,List filters) {
        this(parent, listener, allowNewContainerName, message, true,filters);
    }

    /**
     * Creates a new instance of the widget.
     *
     * @param parent The parent widget of the group.
     * @param listener A listener to forward events to.  Can be null if
     *	 no listener is required.
     * @param allowNewContainerName Enable the user to type in a new container
     *  name instead of just selecting from the existing ones.
     * @param message The text to present to the user.
     * @param showClosedProjects Whether or not to show closed projects.
     */
    public WorkSpaceSelectionGroup(Composite parent, Listener listener,
            boolean allowNewContainerName, String message,
            boolean showClosedProjects,List filters) {
        this(parent, listener, allowNewContainerName, message,
                showClosedProjects, SIZING_SELECTION_PANE_HEIGHT,filters);
    }

    public void setLabelProvider(ILabelProvider lProvider){
    	this.labelProvider = lProvider;
    }
    
    public void setContentProvider(IContentProvider cProvider){
    	this.contentProvider = cProvider;
    }
    /**
     * Creates a new instance of the widget.
     *
     * @param parent The parent widget of the group.
     * @param listener A listener to forward events to.  Can be null if
     *	 no listener is required.
     * @param allowNewContainerName Enable the user to type in a new container
     *  name instead of just selecting from the existing ones.
     * @param message The text to present to the user.
     * @param showClosedProjects Whether or not to show closed projects.
     * @param heightHint height hint for the drill down composite
     */
    public WorkSpaceSelectionGroup(Composite parent, Listener listener,
            boolean allowNewContainerName, String message,
            boolean showClosedProjects, int heightHint,List filters) {
        super(parent, SWT.NONE);
        this.filtersList = filters;
        this.listener = listener;
        this.allowNewContainerName = allowNewContainerName;
        this.showClosedProjects = showClosedProjects;
        String msg = message != null? message : allowNewContainerName? DEFAULT_MSG_NEW_ALLOWED:DEFAULT_MSG_SELECT_ONLY;
        createContents(msg, heightHint);
    }

    public WorkSpaceSelectionGroup(Composite area, Listener listener2,
			boolean allowNewContainerName2, String message,
			boolean showClosedProjects2, List filters,
			IFolder rootFolder) {
    	this(area,listener2,allowNewContainerName2,message,showClosedProjects2,filters);
		if (rootFolder != null) {
			setInputForTreeViewer(rootFolder);
			treeViewer.refresh();
		}
		// TODO Auto-generated constructor stub
	}

	

	/**
     * The container selection has changed in the
     * tree view. Update the container name field
     * value and notify all listeners.
     */
    public void containerSelectionChanged(IResource container) {
        selectedContainer = container;
        if (allowNewContainerName) {
        	if( container == null ) {
        		fileNameField.setText( "" );
                containerNameField.setText("");
        	}
        	else if( ( selectedContainer.getType() & IResource.FILE ) > 0 ) {
        		containerNameField.setText( selectedContainer.getParent().getFullPath( ).makeRelative().toString() );
        		fileNameField.setText( selectedContainer.getName() );
        	}
            else
                containerNameField.setText(container.getFullPath()
                        .makeRelative().toString());
        }

        // fire an event so the parent can update its controls
        if (listener != null) {
            Event changeEvent = new Event();
            changeEvent.type = SWT.Selection;
            changeEvent.widget = this;
            listener.handleEvent(changeEvent);
        }
    }

    /**
     * Creates the contents of the composite.
     */
    public void createContents(String message) {
        createContents(message, SIZING_SELECTION_PANE_HEIGHT);
    }

    /**
     * Creates the contents of the composite.
     * 
     * @param heightHint height hint for the drill down composite
     */
    public void createContents(String message, int heightHint) {
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Label label = new Label(this, SWT.WRAP);
        label.setText(message);
        label.setFont(this.getFont());

        if (allowNewContainerName) {
            containerNameField = new Text(this, SWT.SINGLE | SWT.BORDER);
            containerNameField.setLayoutData(new GridData(
                    GridData.FILL_HORIZONTAL));
            containerNameField.addListener(SWT.Modify, listener);
            containerNameField.setFont(this.getFont());
        } else {
            // filler...
            new Label(this, SWT.NONE);
        }

        createTreeViewer(heightHint);
        if( allowNewContainerName) {
	        Composite cmpFileName = new Composite( this, SWT.NONE );
	        GridLayout layFileName = new GridLayout( 2, false );
	        layFileName.marginWidth = 0;
	        cmpFileName.setLayout( layFileName );
	        cmpFileName.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	        
	        Label fileNameMsg = new Label( cmpFileName, SWT.NONE );
	        fileNameMsg.setText( "File Name : " );
	        fileNameMsg.setFont( this.getFont() );
	        
	        fileNameField = new Text( cmpFileName, SWT.SINGLE | SWT.BORDER );
	        fileNameField.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	        fileNameField.addListener( SWT.Modify, listener );
	        fileNameField.setFont( this.getFont() );
	        fileNameField.setFocus();
        }
        
        Dialog.applyDialogFont(this);
    }

    /**
     * Returns a new drill down viewer for this dialog.
     *
     * @param heightHint height hint for the drill down composite
     * @return a new drill down viewer
     */
    protected void createTreeViewer(int heightHint) {
        // Create drill down.
        DrillDownComposite drillDown = new DrillDownComposite(this, SWT.BORDER);
        GridData spec = new GridData(SWT.FILL, SWT.FILL, true, true);
        spec.widthHint = SIZING_SELECTION_PANE_WIDTH;
        spec.heightHint = heightHint;
        drillDown.setLayoutData(spec);

        // Create tree viewer inside drill down.
        treeViewer = new TreeViewer(drillDown, SWT.NONE);
        addFiltersOnTreeViewer();
        drillDown.setChildTree(treeViewer);
        IContentProvider cp = getContentProvider(showClosedProjects);
        treeViewer.setContentProvider(cp);
        ILabelProvider labelProvider = getLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
        treeViewer.setSorter(new ViewerSorter());
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event
                        .getSelection();
                containerSelectionChanged((IResource) selection.getFirstElement()); // allow null
            }
        });
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    Object item = ((IStructuredSelection) selection)
                            .getFirstElement();
                    if (treeViewer.getExpandedState(item))
                        treeViewer.collapseToLevel(item, 1);
                    else
                        treeViewer.expandToLevel(item, 1);
                }
            }
        });

        // This has to be done after the viewer has been laid out
        treeViewer.setInput(ResourcesPlugin.getWorkspace());
    }
	
	private void setInputForTreeViewer(IFolder rootFolder) {
		treeViewer.setInput(rootFolder);
		
	}

	private void addFiltersOnTreeViewer() {
		if(filtersList==null)
			return;
		for (Iterator iter = filtersList.iterator(); iter.hasNext();) {
			Object element =  iter.next();
			if(element instanceof ViewerFilter) {
				ViewerFilter filter = (ViewerFilter) element;
				treeViewer.addFilter(filter);
			}
		}
	}

	protected ILabelProvider getLabelProvider() {
		return WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider();
	}

	protected IContentProvider getContentProvider(boolean showClosedProjects) {
			return contentProvider==null ? getDefaultContentProvider(showClosedProjects):contentProvider;
	}

	private WorkspaceSelectionContentProvider getDefaultContentProvider(boolean showClosedProjects) {
		return new WorkspaceSelectionContentProvider(showClosedProjects,null);
	}

    /**
     * Returns the currently entered resource name.
     * Null if the field is empty. Note that the
     * resource may not exist yet if the user
     * entered a new resource name in the field.
     */
    public IPath getContainerFullPath() {
        if (allowNewContainerName) {
            String folderName = containerNameField.getText();
            String fileName = fileNameField.getText( );
            if (folderName == null || folderName.length() < 1 ||
            		fileName == null || fileName.length() < 1 )
                return null;
            else{
            	String pathName = getFullPathForFile( folderName + "/" + fileName );
            	if(pathName==null)
            		return null;
            	return new Path(pathName);
            }
        } else {
            if (selectedContainer == null)
                return null;
            else
                return selectedContainer.getLocation();
        }
    }
    // when user types the full file name
	private String getFullPathForFile(String pathName) {
		String workspacePath = null;
		String path = removePathCharsInFront(pathName);
		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < allProjects.length; i++) {
			final String name = allProjects[i].getName();
			if(path.startsWith(name)) {
				String projectPath = allProjects[i].getProject().getLocation().makeAbsolute().toPortableString();
				workspacePath = path.replaceFirst(name,projectPath);
			}
		}
		if(workspacePath==null)
			return null;
		return workspacePath;
	}

    private String removePathCharsInFront(String pathName) {
    	int i=0;
		for(;i<pathName.length();i++){
			if(pathName.charAt(i)=='/' ||pathName.charAt(i)=='\\')
				continue;
			break;
		}
		if(i<pathName.length())
			return pathName.substring(i);
		return pathName;
	}
    
    /**
     * @author masif
     * Returns the path relative to the workspace
     * MAY BE NULL
     * @return path String
     */
    public IPath getRelativePath() {
		if (selectedContainer == null)
			return null;
		else
			return selectedContainer.getFullPath();
	}
    

    public IPath getContainerRelativePath(){
    	String folderName = containerNameField.getText();
        String fileName = fileNameField.getText( );
        if (folderName == null || folderName.length() < 1 ||
        		fileName == null || fileName.length() < 1 ){
                  	return null;
        }
        return (new Path(folderName + "/" + fileName));
    }
    
  
	/**
     * Gives focus to one of the widgets in the group, as determined by the group.
     */
    public void setInitialFocus() {
        if (allowNewContainerName)
            containerNameField.setFocus();
        else{
            treeViewer.getTree().setFocus();
        }
    }

    /**
     * Sets the selected existing container.
     */
    public void setSelectedLocation(IResource container) {
        selectedContainer = container;
        //expand to and select the specified container
        List itemsToExpand = new ArrayList();
        IContainer parent = container.getParent();
        while (parent != null) {
            itemsToExpand.add(0, parent);
            parent = parent.getParent();
        }
        treeViewer.setExpandedElements(itemsToExpand.toArray());
        if( container.exists() )
        	treeViewer.setSelection(new StructuredSelection(container), true);
        else if( container.getType() == IResource.FILE ){
        	treeViewer.setSelection(new StructuredSelection(container.getParent()), true);
        	if( allowNewContainerName )
        		fileNameField.setText( container.getName() );
        }
    }

	public void refreshView() {
		treeViewer.refresh();
	}
	public TreeViewer getTreeViewer(){
		return treeViewer;
	}

}