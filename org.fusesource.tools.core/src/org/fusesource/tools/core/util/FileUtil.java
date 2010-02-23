/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    private FileUtil() {
    }

    public static void copyFolder(String sourceFolder, String targetFolder) throws FileNotFoundException, IOException {
        if (!(new File(sourceFolder)).isDirectory()) {
            throw new AssertionError();
        }
        if (!(new File(targetFolder)).isDirectory()) {
            throw new AssertionError();
        }
        File source = new File(sourceFolder);
        String files[] = source.list();
        for (String file : files) {
            File f = new File((new StringBuilder(String.valueOf(sourceFolder))).append(File.separator).append(file)
                    .toString());
            if (f.isDirectory()) {
                copyFolder(sourceFolder, targetFolder, file);
            } else {
                copyFile(sourceFolder, targetFolder, file);
            }
        }

    }

    public static void copyFile(String sourceFile, String targetFolder) throws FileNotFoundException, IOException {
        if (!(new File(sourceFile)).isFile()) {
            throw new AssertionError();
        }
        if (!(new File(targetFolder)).isDirectory()) {
            throw new AssertionError();
        } else {
            File source = new File(sourceFile);
            copyFile(source.getParent(), targetFolder, source.getName());
            return;
        }
    }

    private static void copyFolder(String sourceFolder, String targetFolder, String name) throws FileNotFoundException,
            IOException {
        File target = new File(getFile(targetFolder, name));
        target.mkdir();
        copyFolder(getFile(sourceFolder, name), getFile(targetFolder, name));
    }

    private static void copyFile(String sourceFolder, String targetFolder, String name) throws FileNotFoundException,
            IOException {
        copyFile(sourceFolder, targetFolder, name, name);
    }

    public static void copyFile(String sourceFolder, String targetFolder, String sourceName, String targetName)
            throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(getFile(sourceFolder, sourceName));
        OutputStream os = new FileOutputStream(getFile(targetFolder, targetName));
        byte buffer[] = new byte[102400];
        do {
            int len = is.read(buffer);
            if (len >= 0) {
                os.write(buffer, 0, len);
            } else {
                is.close();
                os.close();
                return;
            }
        } while (true);

    }

    protected static String getFile(String sourceFolder, String sourceName) {
        return new File(sourceFolder, sourceName).getAbsolutePath();
        // return (new StringBuilder(String.valueOf(sourceFolder))).append(
        // File.separator).append(sourceName).toString();
    }

    public static boolean copyIfNotPresent(String sourceLocation, String targetFolder) throws FileNotFoundException,
            IOException {

        File f = new File(sourceLocation);
        if (f.isDirectory()) {
            throw new AssertionError("Cannot copy folder");
        }

        String sourceFileName = f.getName();
        return copyIfNotPresent(f.getParent(), sourceFileName, targetFolder);
    }

    public static boolean copyIfNotPresent(String sourceFolder, String sourceFileName, String targetFolder)
            throws FileNotFoundException, IOException {

        boolean copied = false;

        File targetFile = new File(targetFolder, sourceFileName);

        if (!targetFile.exists()) {
            copyFile(sourceFolder, targetFolder, sourceFileName, sourceFileName);
            copied = true;
        }

        return copied;
    }

    public static void deleteFolder(File file) {
        if (!file.isDirectory() || (file.isDirectory() && file.list().length == 0)) {
            file.delete();
        } else {
            File[] listFiles = file.listFiles();
            for (File childFile : listFiles) {
                deleteFolder(childFile);
            }
            file.delete();
        }
    }

}
