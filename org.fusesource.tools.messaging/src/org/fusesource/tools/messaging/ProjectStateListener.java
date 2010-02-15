package org.fusesource.tools.messaging;

/**
 * Clients can implement this interface and can add the implementation to
 * MsgProjectStateManager to listen to prpoject state changes
 * 
 * @author kiranb
 * 
 */
public interface ProjectStateListener {
	public void stateChanged(ProjectStateEvent event);
}
