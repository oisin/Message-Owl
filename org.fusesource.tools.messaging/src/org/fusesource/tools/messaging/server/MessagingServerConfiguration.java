/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.server.core.IServer;
import org.fusesource.tools.messaging.core.IProvider;

/**
 * Stores the Connection properties to this file
 * 
 */
public class MessagingServerConfiguration {
    private static final String MSG_SERVER_PROPERTIES = "MsgServer.props";
    private Map<String, String> properties;
    private IServer server;
    private IProvider provider;

    public static String AUTO_CONNECT_KEY = "MessagingServerConfiguration.autoConnect";

    public MessagingServerConfiguration(IServer server) throws Exception {
        this.server = server;
        this.properties = new HashMap<String, String>();
        load();
    }

    public void save(IProgressMonitor monitor) throws CoreException {
        IFolder serverConfiguration = server.getServerConfiguration();
        if (serverConfiguration == null) {
            return;
        }
        IFile file = serverConfiguration.getFile(MSG_SERVER_PROPERTIES);
        ObjectOutputStream obj = null;
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(file.getLocation().toOSString());
            obj = new ObjectOutputStream(fileOutputStream);
            obj.writeObject(getProperties());
            obj.flush();
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (obj != null) {
                try {
                    obj.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load() throws Exception {
        IFolder serverConfiguration = server.getServerConfiguration();
        if (serverConfiguration == null) {
            return;
        }
        IFile serverPropsFile = serverConfiguration.getFile(MSG_SERVER_PROPERTIES);
        if (!serverPropsFile.exists()) {
            return;
        }
        FileInputStream inputStream = null;
        ObjectInputStream objInputStream = null;
        HashMap<String, String> propMap = null;
        try {
            inputStream = new FileInputStream(serverPropsFile.getLocation().toOSString());
            objInputStream = new ObjectInputStream(inputStream);
            propMap = (HashMap<String, String>) objInputStream.readObject();
            setProperties(propMap);
        } finally {
            if (objInputStream != null) {
                objInputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public void setProperties(Map<String, String> serverProperties) {
        this.properties = serverProperties;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public IProvider getProvider() {
        return provider;
    }

    public void setProvider(IProvider provider) {
        this.provider = provider;
    }

    public void setAutoConnect(boolean autoConnect) {
        properties.put(AUTO_CONNECT_KEY, String.valueOf(autoConnect));
    }

    public boolean isAutoConnect() {
        return Boolean.valueOf(properties.get(AUTO_CONNECT_KEY));
    }
}
