package org.fusesource.tools.messaging;

import org.eclipse.core.resources.IProject;

/**
 * An event representing the project state
 * 
 * @author kiranb
 * 
 */
public class ProjectStateEvent {
	private int state;
	private IProject[] project;

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
