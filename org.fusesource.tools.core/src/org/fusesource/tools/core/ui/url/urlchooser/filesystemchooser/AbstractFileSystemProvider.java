package org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser;

import java.net.URL;

import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserFilter;


/**

 */
public abstract class AbstractFileSystemProvider implements FileSystemProvider {
    protected static URLOpener opener = null;

    protected URLChooserFilter filter;
    protected boolean isSingleSelection;

	private URL[] selection;

    public void setSelectionType(boolean isSingleOrMulti) {
        this.isSingleSelection = isSingleOrMulti;
    }

    public void setFilters(URLChooserFilter filter)
    {
        this.filter = filter;
    }
    
    public URLChooserFilter getFilters()
    {
        return filter;
    }

    public URL[] acceptDroppedData(DropTargetEvent event) {
        return null;
    }
    
    public Object[] acceptDrop( DropTargetEvent event ) {
    	return acceptDroppedData( event );
    }

    public boolean supportsDnd() {
        return false;
    }

    public Transfer[] getTransferTypes() {
        return new Transfer[0];
    }

    public void open(URL selectedURL) {
        if (validate(selectedURL)) {
            if (opener != null) {
                opener.open(selectedURL, this);
            }
        }
    }

    protected boolean isSingleSelection() {
        return isSingleSelection;
    }

    public static void setURLOpener(URLOpener o) {
        opener = o;
    }

    public static interface URLOpener {
        void open(URL url, FileSystemProvider provider);
    }
    
    public URL convertToURL(String path) {
        return null;
    }
    
    public void run( Object[] initParams ) {
    	selection = browse( (initParams != null && initParams.length > 0 && initParams[0] != null) ? 
    			initParams[0].toString() : null );
    }
    
    public Object[] getSelection( ) {
    	return selection;
    }
    
    public boolean isSelectionSupported( ) {
    	return true;
    }
}