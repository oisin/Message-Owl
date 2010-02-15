package org.fusesource.tools.core.ui.url.urlchooser;

import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

public interface ActionProvider {

	/**
	 * @return Name to be displayed in the menu-item
	 */
	public String getDisplayName();
	/**
	 * @return Unique ID of the provider
	 */
	public String getID( );
	/**
	 * Method which gets call back when the menu-item is clicked
	 * @param initParameters selection in the chooser when menu item is clicked
	 */
	public void run( Object[] initParameters );
	/**
	 * @return true if the action supports selection, false otherwise
	 */
	public boolean isSelectionSupported( );
	/**
	 * Returns the selected values.
	 * @return null if the selection dialog was cancelled
	 * empty array if the selection is empty
	 * array of the selection otherwise
	 */
	public Object[] getSelection( );
	/**
	 * Method which gets call back when an object is dropped on the control
	 * @param data Drop data
	 * @return objects selected
	 */
	public Object[] acceptDrop(DropTargetEvent data);
	/**
	 * set the selection type of the provider
	 * @param isSingleSelection true if control supports single selection, false otherwise
	 */
	public void setSelectionType(boolean isSingleSelection);
	/**
	 * @return true if provider supports drag and drop
	 */
	public boolean supportsDnd();
	public Transfer[] getTransferTypes();

}
