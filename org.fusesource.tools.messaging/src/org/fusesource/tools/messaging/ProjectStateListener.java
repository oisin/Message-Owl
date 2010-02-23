/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging;

/**
 * Clients can implement this interface and can add the implementation to MsgProjectStateManager to
 * listen to prpoject state changes
 */
public interface ProjectStateListener {
    public void stateChanged(ProjectStateEvent event);
}
