/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.MsgProjectStateManager;
import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.utils.MessageDialogUtils;

/**
 * Provides Helper methods
 */
public class MessagingServersUtil {

    public static List<IServer> getMessagingServers() {
        List<IServer> msgServers = new ArrayList<IServer>();
        IServer[] servers = ServerCore.getServers();
        for (IServer server : servers) {
            if (isMsgServer(server)) {
                msgServers.add(server);
            }
        }
        return msgServers;
    }

    public static String[] getMessagingServerNames() {
        List<String> msgServerNames = new ArrayList<String>();
        List<IServer> messagingServers = getMessagingServers();
        for (IServer server : messagingServers) {
            msgServerNames.add(server.getName());
        }
        return msgServerNames.toArray(new String[] {});
    }

    public static Map<String, String> getMsgServersNameIdMap() {
        Map<String, String> nameServerMap = new HashMap<String, String>();
        IServer[] servers = ServerCore.getServers();
        for (IServer server : servers) {
            Object adapter = server.loadAdapter(MessagingServerDelegate.class, new NullProgressMonitor());
            if (adapter != null) {
                nameServerMap.put(server.getName(), server.getId());
            }
        }
        return nameServerMap;
    }

    public static boolean isMsgServer(IServer server) {
        if (server == null) {
            return false;
        }
        Object adapter = server.loadAdapter(MessagingServerDelegate.class, new NullProgressMonitor());
        return adapter != null ? true : false;
    }

    public static boolean isMsgServerOnline(IServer server) {
        if (server == null) {
            return false;
        }

        try {
            IProvider provider = getProvider(server);
            if (provider == null) {
                return false;
            }
            IConnection connection = provider.getConnection();
            if (connection == null) {
                return false;
            }
            return connection.isActive();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isMsgProjectOnline(IProject project) {
        return isMsgServerOnline(getDeployedServer(project));
    }

    public static void deployModule(IProject project, IServer server) throws CoreException {
        if (server == null || project == null) {
            return;
        }
        IServerWorkingCopy wc = server.createWorkingCopy();
        IModule msgModule = findMessagingModule(project);
        ServerUtil.modifyModules(wc, new IModule[] { msgModule }, null, null);
        wc.save(true, new NullProgressMonitor());
    }

    public static void deployModule(IProject project, String serverId) throws CoreException {
        if (serverId == null || project == null || serverId.trim().length() == 0) {
            return;
        }
        IServer server = ServerCore.findServer(serverId);
        deployModule(project, server);
    }

    public static IServer getDeployedServer(IProject project) {
        IModule msgModule = findMessagingModule(project);
        IServer[] serversByModule = ServerUtil.getServersByModule(msgModule, null);
        if (serversByModule == null || serversByModule.length == 0) {
            return null;
        }
        if (serversByModule.length > 1) {
            // TODO log a warning...error?
        }
        return serversByModule[0];
    }

    private static IModule findMessagingModule(IProject project) {
        IModule[] modules = ServerUtil.getModules(project);
        for (IModule eachModule : modules) {
            if (IConstants.MSG_PRJ_MODULE_ID.equals(eachModule.getModuleType().getId())) {
                return eachModule;
            }
        }
        return null;
    }

    public static IModule[] getMsgModules(IServer server) {
        return ServerUtil.getModules(IConstants.MSG_PRJ_MODULE_ID);
    }

    public static IProvider getProvider(IServer server) {
        if (server == null) {
            return null;
        }
        MessagingServerDelegate serverDelegate = (MessagingServerDelegate) server.loadAdapter(
                MessagingServerDelegate.class, new NullProgressMonitor());
        if (serverDelegate == null) {
            return null;
        }
        return serverDelegate.getServerConfiguration().getProvider();
    }

    public static boolean isMsgFacetedProject(IProject project) {
        try {
            IFacetedProject facetedProject = ProjectFacetsManager.create(project);
            if (facetedProject == null) {
                return false;
            }
            IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(IConstants.MSG_PRJ_FACET);
            return facetedProject.hasProjectFacet(projectFacet);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void connectToServer(IServer server) throws MessagingException {

        MessagingServerDelegate serverDelegate = (MessagingServerDelegate) server.loadAdapter(
                MessagingServerDelegate.class, new NullProgressMonitor());
        IProvider provider = serverDelegate.getServerConfiguration().getProvider();
        if (provider == null) {
            MessageDialogUtils.showErrorMessage("Provider unavailable",
                    "No messaging provider associated with the server. " + server.getName());
        }
        MessagingServerConfiguration serverConfiguration = serverDelegate.getServerConfiguration();
        provider.createConnection(serverConfiguration.getProperties());
        MsgProjectStateManager.getInstance().updateMsgProjectsState(server, server.getModules());

    }
}
