/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

public class LocalDirectorySystemProvider extends LocalFileSystemProvider {
    private static final String DISPLAY_NAME = "Directory...";
    public static final String ID = "LocalDirectorySystemProvider";
    private static boolean directoryChooserDialog = false;

    public LocalDirectorySystemProvider() {
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public URL[] browse(String initialPath) {
        directoryChooserDialog = true;
        File file = getBestPossibleFile(initialPath);
        DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.OPEN
                | (isSingleSelection ? SWT.SINGLE : SWT.MULTI));
        if (file != null) {
            dialog.setFilterPath(file.getAbsolutePath());
        }

        String s = dialog.open();
        if (s == null) {
            return null;
        }
        URL[] urls = getURLs(dialog.getFilterPath());
        return urls;
    }

    protected URL[] getURLs(String path) {
        List list = new ArrayList();
        try {
            URL url = new File(path).toURL();
            list.add(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return (URL[]) list.toArray(new URL[list.size()]);
    }

    @Override
    public String getID() {
        return ID;
    }

    public static boolean isDirectoryChooserDialogOpen() {
        return directoryChooserDialog;
    }

    public static void setDirectoryChooserDialogOpen(boolean closed) {
        directoryChooserDialog = closed;
    }
}
