/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class ResourceUtil {
    private static String projectName;

    public static URL convertWorkspacePathToURL(String workspacePath) throws MalformedURLException {
        IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(workspacePath);
        URI uri = ((resource == null) ? null : resource.getLocationURI());

        if (uri == null) {
            return null;
        } else if (!uri.getScheme().equals("file")) {
            return uri.toURL();
        } else {
            return convertFilePathToURL(uri.getRawPath());
        }
    }

    public static URL convertFilePathToURL(String filePath) throws MalformedURLException {
        File file = new File(filePath);
        String path = file.getAbsolutePath().replace(File.separatorChar, '/');

        if (path.indexOf(':') > 0) {
            path = "///" + path;
        }

        return new URL("file", null, path);
    }

    public static String getRelativeToProject(String[] strValue) {
        String fullPath = strValue[0];
        String workSpacePath = getWorkSpacePath();
        String projectPath = getCurrentProjectPath();
        if (fullPath.startsWith(workSpacePath)) {
            if (fullPath.startsWith(projectPath)) {
                int length = projectPath.length();
                String relativeProjectPath = fullPath.substring(length);
                return relativeProjectPath;
            } else {
                int lengthWorkspace = workSpacePath.length();
                String relativeProjectPath = fullPath.substring(lengthWorkspace - 1);
                return ".." + relativeProjectPath;
            }
        }
        return fullPath;
    }

    public static String getRelativeToResource(String[] strValue) {
        String fullPath = strValue[0];
        String workSpacePath = getWorkSpacePath();
        String projectPath = getCurrentProjectPath();
        String resourcePath = projectPath + "src/main/resources";
        if (fullPath.startsWith(workSpacePath)) {
            if (fullPath.startsWith(resourcePath)) {
                int length = resourcePath.length();
                String relativeResourcePath = fullPath.substring(length);
                return relativeResourcePath;
            }
        }
        return fullPath;
    }

    public static String getWorkSpacePath() {
        String rPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toPortableString();
        File file = new File(rPath);
        try {
            URL fileUrl = file.toURL();
            String workSpacePath = fileUrl.toString();
            return workSpacePath;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentActiveFile() {
        FileEditorInput fileEditor = getActiveFileEditorInput();
        if (fileEditor == null) {
            return null;
        }
        IPath path = fileEditor.getPath();
        File file = new File(path.toString());
        try {
            URL fileUrl = file.toURL();
            String filePath = fileUrl.toString();
            return filePath;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentProjectPath() {
        String workSpacePath = getWorkSpacePath();
        return workSpacePath + getCurrentProjectName() + "/";
    }

    public static String getCurrentProjectName() {
        String currentActiveFile = getCurrentActiveFile();
        String workSpacePath = getWorkSpacePath();
        int workSpacePathLength = workSpacePath.length();
        String relative = currentActiveFile.substring(workSpacePathLength);
        int index = relative.indexOf('/');
        projectName = relative.substring(0, index);
        return projectName;
    }

    private static FileEditorInput getActiveFileEditorInput() {
        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
        IWorkbenchPage page = win.getActivePage();
        IEditorPart activeEditor = page.getActiveEditor();
        if (activeEditor == null) {
            return null;
        }
        IEditorInput editorInput = activeEditor.getEditorInput();
        FileEditorInput fileEditor = (FileEditorInput) editorInput;
        return fileEditor;
    }

    public static IFile getCurrentIFile() {
        FileEditorInput activeFileEditorInput = getActiveFileEditorInput();
        if (activeFileEditorInput == null) {
            return null;
        }
        return activeFileEditorInput.getFile();
    }
}
