package org.fusesource.tools.core.ui.url.editor.resource;

import java.net.URL;

import org.eclipse.ui.IStorageEditorInput;

/*
 
 */

public interface URLEditorInput extends IStorageEditorInput {

    public URL getURL();
}
