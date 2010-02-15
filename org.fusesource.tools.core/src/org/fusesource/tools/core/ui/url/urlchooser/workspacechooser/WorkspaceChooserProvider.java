
/** 
 
 */

package org.fusesource.tools.core.ui.url.urlchooser.workspacechooser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ISelectionValidator;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserFilter;
import org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser.LocalFileSystemProvider;




public class WorkspaceChooserProvider extends LocalFileSystemProvider {

	public static String DISPLAY_ID = "Workspace"; 
	public static String ID = "Workspace";
	protected String lastSelectedFolder;
	protected IResource currentSelectedResource;
	protected boolean allowNew;
	protected URLChooserFilter filter;
	protected String title;
	protected String msg;
	protected ISelectionValidator validator;

	/**
	 * The instance can be added to the URLChooser. Brings up the Workspace chooser 
	 * @param selectedResource - the IResource instance to remain initially selected in the tree 
	 * @param filter -  URLChooserFilter instance
	 * @param title - title for the Workspace dialog
	 * @param msg - Message to be set on the Workspace dialog
	 * @param allowNew - if true shows a text field for typing the path for the user, otherwise no text field shows up. 
	 */
	public WorkspaceChooserProvider(IResource selectedResource,URLChooserFilter filter,String title,String msg,boolean allowNew) {
		this.currentSelectedResource = selectedResource;
		this.allowNew = allowNew;
		this.filter = filter;
		this.title = title;
		this.msg = msg;
	}
/**
 * The instance can be added to the URLChooser. Brings up the Workspace chooser 
 * @param selectedResource - the file url to remain initially selected in the tree
 * @param filter -  URLChooserFilter instance
 * @param title - title for the Workspace dialog
 * @param msg - Message to be set on the Workspace dialog
 * @param allowNew - if true shows a text field for typing the path for the user, otherwise no text field shows up. 
 */
	public WorkspaceChooserProvider(String selectedResource,URLChooserFilter filter,String title,String msg,boolean allowNew) {
		this.allowNew = allowNew;
		this.filter = filter;
		this.title = title;
		this.msg = msg;
	}
	 /**
		* @param filter - URLChooserFilter instance
		* @param title - title for the Workspace dialog
		* @param msg - Message to be set on the Workspace dialog
		* @param allowNew - if true shows a text field for typing the path for the user, otherwise no text field shows up.
	*/
	public WorkspaceChooserProvider(URLChooserFilter filter,String title,String msg,boolean allowNew) {
		this(getDefaultProjectRoot(),filter,title,msg,allowNew);
	}
	
	public WorkspaceChooserProvider( ) {
		this( null, "Select Resource", "Select Resource", false );
	}

	public static IWorkspaceRoot getDefaultProjectRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	public String getDisplayName() {
		return DISPLAY_ID;
	}

	public String getID() {
		return ID;
	}

	public void setFilters(URLChooserFilter filter) {
		this.filter = filter;
	}

	public URLChooserFilter getFilters() {
		return filter;
	}

	public URL[] browse(String initialPath) {
		WorkSpaceChooserDialog dialog = getWorkSpaceChooserDialogInstance();
		List l = new ArrayList();
		if (currentSelectedResource != null)
			l.add(currentSelectedResource);
		dialog.setInitialElementSelections(l);
		if (Window.OK == dialog.open()) {
			if (dialog.getResult().length < 1)
				return null;
			String dd = (String) dialog.getResult()[0].toString();
			URL[] urls = getURLs(dd);
			return urls;
		}
		return null;
	}
	
	protected WorkSpaceChooserDialog getWorkSpaceChooserDialogInstance() {
		WorkSpaceChooserDialog  dialog = new WorkSpaceChooserDialog(Display.getCurrent().getActiveShell(),currentSelectedResource,filter,title,allowNew,msg);
		dialog.setValidator(validator);
		return dialog;
	}
	
	private URL[] getURLs(String fileNames) {
		List list = new ArrayList();
		try {
			URL url = new File(fileNames).toURL();
			list.add(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return (URL[]) list.toArray(new URL[list.size()]);
	}

	/**
	 * @param ISelectionValidator instance that validates the selection and 
	 * shows returned error message,if any, on isValid() call for selections.
	 * If string returned is null then then it is considered valid, 
	 * Otherwise considered error and message shows up  in the dialog
	 */
	public void setSelectionValidator(ISelectionValidator validator) {
		this.validator = validator;
	}
}