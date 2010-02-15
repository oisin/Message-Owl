package org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser;

import java.net.URL;

import org.eclipse.swt.dnd.DropTargetEvent;
import org.fusesource.tools.core.ui.url.urlchooser.ActionProvider;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserFilter;


/**
 
 */
public interface FileSystemProvider extends ActionProvider {

    /**
     * @param url
     * @return true if this url is a valid url of this provider
     */
    boolean validate(URL url);

    /**
     * Set the filters
     */
    void setFilters(URLChooserFilter filter);
    
    /**
     * get the current filter settings
     * @return
     */
    URLChooserFilter getFilters();

    /**
     * Bring up an UI that allows the user to browse this file system and return an array of selected URLs
     * @param object the entry that a user may have typed or previously selected that exists in the textfield MAY BE NULL
     * @return
     */
    URL[] browse(String object);

    /**
     * Converts a path to the FileSystemProvider implementations filesystem specifc URL format.
     * @param path
     * @return
     */
    URL convertToURL(String path);

    /**
     * @param selectedURL Open this URL in its associated editor
     */
    void open(URL selectedURL);
    
    URL[] acceptDroppedData( DropTargetEvent event );
}