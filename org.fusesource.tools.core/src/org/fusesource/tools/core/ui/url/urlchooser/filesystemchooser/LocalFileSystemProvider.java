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
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

/**
 
 */
public class LocalFileSystemProvider extends AbstractFileSystemProvider {
    private static final String DISPLAY_NAME = "Local File System";
    public static final String ID = "LocalFileSystemProvider";
    private static boolean fileChooserDialog = false;

    public LocalFileSystemProvider() {
    }

    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public boolean validate(URL url) {
        if (!url.getProtocol().equals("file")) {
            return false;
        }
        String path = url.getPath();
        try {
            File file = new File(path);
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public URL[] browse(String initialPath) {
        fileChooserDialog = true;
        File file = getBestPossibleFile(initialPath);
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN
                | (isSingleSelection ? SWT.SINGLE : SWT.MULTI));
        if (file != null) {
            dialog.setFilterPath(file.getAbsolutePath());
        }

        if (filter != null) {
            dialog.setFilterNames(filter.getFilterNames());
            dialog.setFilterExtensions(filter.getFilterExtensions());
        }

        String s = dialog.open();
        if (s == null) {
            return null;
        }
        URL[] urls = getURLs(dialog.getFilterPath(), dialog.getFileNames());
        return urls;
    }

    protected File getBestPossibleFile(String initialPath) {
        if (initialPath == null) {
            return null;
        }
        try {
            if (initialPath.startsWith("file:")) {
                initialPath = initialPath.substring("file:".length());
            }
            File file = new File(initialPath);
            while (file != null) {
                if (file.exists() && file.isDirectory()) {
                    return file;
                }
                file = file.getParentFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected URL[] getURLs(String path, String[] fileNames) {
        List list = new ArrayList();
        for (String fileName : fileNames) {
            try {
                URL url = new File(path, fileName).toURL();
                list.add(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return (URL[]) list.toArray(new URL[list.size()]);
    }

    public String getID() {
        return ID;
    }

    @Override
    public URL[] acceptDroppedData(DropTargetEvent event) {
        if (!FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
            return super.acceptDroppedData(event);
        }

        Object transferData = event.data;
        if (transferData instanceof String[]) {
            String[] files = (String[]) transferData;
            List urls = new ArrayList(files.length);
            for (String _file : files) {
                File file = new File(_file);
                try {
                    if (file.exists()) {
                        urls.add(file.toURL());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return (URL[]) urls.toArray(new URL[urls.size()]);
        }
        return super.acceptDroppedData(event);
    }

    @Override
    public boolean supportsDnd() {
        return true;
    }

    @Override
    public Transfer[] getTransferTypes() {
        return new Transfer[] { FileTransfer.getInstance() };
    }

    public static boolean isFileChooserDialogOpen() {
        return fileChooserDialog;
    }

    public static void setFileChooserDialogOpen(boolean closed) {
        fileChooserDialog = closed;
    }
}
