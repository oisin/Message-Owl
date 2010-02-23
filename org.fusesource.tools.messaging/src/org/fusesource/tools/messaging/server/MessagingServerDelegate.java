/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.server;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.model.ServerDelegate;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.MsgProjectStateManager;
import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.plugin.FuseMessagingPlugin;

public abstract class MessagingServerDelegate extends ServerDelegate {
    private MessagingServerConfiguration serverConfiguration;

    public MessagingServerDelegate() {
        super();
    }

    @Override
    protected void initialize() {
        super.initialize();
        try {
            serverConfiguration = MessagingServerConfigurationFactory.getInstance().getConfiguration(getServer());
            if (serverConfiguration != null && serverConfiguration.getProvider() == null) {
                serverConfiguration.setProvider(createProvider());
                // UI-501 Set default parameters if not loaded from disk
                if (serverConfiguration.getProperties().isEmpty()) {
                    serverConfiguration.setProperties(serverConfiguration.getProvider().getConnectionParams());
                }
                if (serverConfiguration.isAutoConnect()) {
                    MessagingServersUtil.connectToServer(getServer());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfiguration(IProgressMonitor monitor) throws CoreException {
        super.saveConfiguration(monitor);
        try {
            getServerConfiguration().save(monitor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract IProvider createProvider();

    public MessagingServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }

    @Override
    public IStatus canModifyModules(IModule[] add, IModule[] remove) {
        if (add != null) {
            int size = add.length;
            for (int i = 0; i < size; i++) {
                IModule module = add[i];
                if (!IConstants.MSG_PRJ_MODULE_ID.equals(module.getModuleType().getId())) {
                    return new Status(IStatus.ERROR, FuseMessagingPlugin.PLUGIN_ID, 0, "Invalid Module", null);
                }
            }

            boolean isDeployed = isProjectAlreadyDeployed(add);
            if (isDeployed) {
                return new Status(IStatus.ERROR, FuseMessagingPlugin.PLUGIN_ID, 0,
                        "Module already deployed in another server.", null);
            }
        }
        return Status.OK_STATUS;
    }

    @Override
    public IModule[] getChildModules(IModule[] module) {
        return null;
    }

    @Override
    public IModule[] getRootModules(IModule module) throws CoreException {
        return new IModule[] { module };
    }

    @Override
    public void modifyModules(IModule[] add, IModule[] remove, IProgressMonitor monitor) throws CoreException {
        MsgProjectStateManager.getInstance().updateMsgProjectsState(getServer(), add);
        // Force the removed projects to be offline...
        MsgProjectStateManager.getInstance().updateMsgProjectsState(getServer(), remove, false);
    }

    private boolean isProjectAlreadyDeployed(IModule[] add) {
        if (add != null) {
            int size = add.length;
            for (int i = 0; i < size; i++) {
                IServer[] serversByModule = ServerUtil.getServersByModule(add[i], new NullProgressMonitor());
                if (serversByModule.length > 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
