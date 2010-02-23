/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging;

import org.eclipse.core.resources.IProject;

/**
 * An event representing the project state
 */
public class ProjectStateEvent {
    private final int state;
    private final IProject[] project;

    public static final int PROJECT_STATE_ONLINE = 0x001;
    public static final int PROJECT_STATE_OFFLINE = 0x002;

    public ProjectStateEvent(IProject[] project, int state) {
        this.project = project;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public IProject[] getProjects() {
        return project;
    }
}
