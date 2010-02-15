package org.fusesource.tools.core.ui.url.urlchooser.workspacechooser;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Display;
import org.fusesource.tools.core.util.ResourceUtil;


public class ResourceChooserProvider extends WorkspaceChooserProvider{
	public static String DISPLAY_ID = "Resource"; 
	public static String ID = "Resource";
	private IFolder rootFolder;
		
	public ResourceChooserProvider(){
		super(null, "Test Select Resource", "Test Select Resource", false);
		String currentProjectName = ResourceUtil.getCurrentProjectName();
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
		.getProject(currentProjectName);
		rootFolder=project.getFolder("/src/main/resources");
	}
	
	protected WorkSpaceChooserDialog getWorkSpaceChooserDialogInstance() {
		WorkSpaceChooserDialog  dialog = new WorkSpaceChooserDialog(Display.getCurrent().getActiveShell(),currentSelectedResource,filter,title,allowNew,msg,rootFolder);
		dialog.setValidator(validator);
		return dialog;
	}
	
	public String getDisplayName() {
		return DISPLAY_ID;
	}

	public String getID() {
		return ID;
	}

}
