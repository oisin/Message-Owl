/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.server.core.ServerCore;
import org.fusesource.tools.messaging.MsgProjectStateManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FuseMessagingPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.fusesource.tools.messaging";

    private static FuseMessagingPlugin plugin;

    public FuseMessagingPlugin() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        addServersLifeCycleChangeListener();
    }

    private void addServersLifeCycleChangeListener() {
        ServerCore.addServerLifecycleListener(MsgProjectStateManager.getInstance().getServersChangeListener());
    }

    private void removeServerLifeCycleListener() {
        ServerCore.removeServerLifecycleListener(MsgProjectStateManager.getInstance().getServersChangeListener());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
        removeServerLifeCycleListener();
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static FuseMessagingPlugin getDefault() {
        return plugin;
    }

}
