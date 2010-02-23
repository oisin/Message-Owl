/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.server;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerLifecycleListener;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.ServerWorkingCopy;

/**
 * 
 * Maintains one Messaging Server Configurations per server
 */
public class MessagingServerConfigurationFactory {
    private Map<IServer, MessagingServerConfiguration> msgServerConfigs;
    private static MessagingServerConfigurationFactory instance;

    private MessagingServerConfigurationFactory() {
        msgServerConfigs = new HashMap<IServer, MessagingServerConfiguration>();
        ServerCore.addServerLifecycleListener(new IServerLifecycleListener() {
            public void serverAdded(IServer server) {
                // UI - 402 When the server is actually added, replace the key with
                // actual server instance...
                synchronized (msgServerConfigs) {
                    IServer foundServer = null;
                    for (Object element : msgServerConfigs.keySet()) {
                        IServer storedServer = (IServer) element;
                        if (storedServer.getId().equals(server.getId())) {
                            foundServer = storedServer;
                            break;
                        }
                    }
                    if (foundServer != null) {
                        MessagingServerConfiguration messagingServerConfiguration = msgServerConfigs.get(foundServer);
                        msgServerConfigs.remove(foundServer);
                        msgServerConfigs.put(server, messagingServerConfiguration);
                    }
                }
            }

            public void serverChanged(IServer server) {
            }

            public void serverRemoved(IServer server) {
                synchronized (msgServerConfigs) {
                    msgServerConfigs.remove(server);
                }
            }
        });
    }

    public static MessagingServerConfigurationFactory getInstance() {
        if (instance == null) {
            instance = new MessagingServerConfigurationFactory();
        }
        return instance;
    }

    public synchronized MessagingServerConfiguration getConfiguration(IServer server) throws Exception {
        // UI - 402
        // Get original is null when server is being created - so store config against working copy.
        // Later when server is added, replace the working copy key with the actual server
        // This logic is implemented to avoid redundant ServerWorkingCopy instance storage in the
        // map
        // for a single instance of IServer
        if (server instanceof ServerWorkingCopy && ((ServerWorkingCopy) server).getOriginal() != null) {
            server = ((ServerWorkingCopy) server).getOriginal();
        }
        MessagingServerConfiguration serverConfiguration = msgServerConfigs.get(server);
        if (serverConfiguration == null) {
            serverConfiguration = new MessagingServerConfiguration(server);
            msgServerConfigs.put(server, serverConfiguration);
        }
        return serverConfiguration;
    }

}
