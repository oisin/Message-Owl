package org.fusesource.tools.messaging;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerLifecycleListener;
import org.eclipse.wst.server.core.internal.DeletedModule;
import org.fusesource.tools.messaging.cnf.model.DataModelManager;
import org.fusesource.tools.messaging.cnf.model.IModelConstants;
import org.fusesource.tools.messaging.cnf.model.Listeners;
import org.fusesource.tools.messaging.cnf.model.Senders;
import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.core.ISender;
import org.fusesource.tools.messaging.server.MessagingServersUtil;


/**
 * Handles the state updates for Messaging Projects. Clients can listen to the
 * project state changes by adding a listener
 * 
 * @author kiranb
 * 
 */
@SuppressWarnings("restriction")
public class MsgProjectStateManager {

	public static MsgProjectStateManager stateManager;

	private ServersChangeListener serversChangeListener;

	private List<ProjectStateListener> prjStateListeners = new ArrayList<ProjectStateListener>();;

	private MsgProjectStateManager() {
		serversChangeListener = new ServersChangeListener();
	}

	public static MsgProjectStateManager getInstance() {
		if (stateManager == null) {
			stateManager = new MsgProjectStateManager();
		}
		return stateManager;
	}

	// Listens to server add/change/remove actions
	// For now, handling remove action - Closes the connection, forces the deployed
	// projects to be offline when the server is removed
	class ServersChangeListener implements IServerLifecycleListener {

		public void serverAdded(IServer server) {
		}

		public void serverChanged(IServer server) {
		}

		public void serverRemoved(IServer server) {
			if (!MessagingServersUtil.isMsgServer(server))
				return;
			IProvider provider = MessagingServersUtil.getProvider(server);
			if (provider != null) {
				try {
					provider.getConnection().closeConnection();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			// Forcing the projects to be in offline state as we know no
			// connection is available for the project(s) and fire project
			// state change event
			makeMsgProjectsOffline(server.getModules());
			fireProjectStateChanges(server.getModules(), false);
		}
	}

	public ServersChangeListener getServersChangeListener() {
		return serversChangeListener;
	}

	/**
	 * Given IServer - retrieves the Messaging Modules and forces the projects
	 * to be online or offline based on the server's connection status
	 * 
	 * @param server
	 */
	public void updateMsgProjectsState(IServer server, IModule[] msgModules) {
		boolean isOnline = MessagingServersUtil.isMsgServerOnline(server);
		updateMsgProjectsState(server, msgModules, isOnline);
	}

	public void updateMsgProjectsState(IServer server, IModule[] msgModules,
			boolean isOnline) {
		if (!canProceed(msgModules))
			return;
		if (isOnline)
			makeMsgProjectsOnline(server, msgModules);
		else
			makeMsgProjectsOffline(msgModules);

		// Notify Listeners about the Project state change
		fireProjectStateChanges(msgModules, isOnline);
	}

	private void makeMsgProjectsOnline(IServer server, IModule[] modules) {
		if (!canProceed(modules))
			return;

		IConnection activeConnection = null;
		try {
			activeConnection = getActiveConnection(server);
		} catch (Exception e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Failed to activate the projects. You may not be able to send or receive messages.", e.getMessage());
			e.printStackTrace();
			return;
		}

		if (activeConnection != null) {
			makeListenersOnline(activeConnection, modules);
			makeSendersOnline(activeConnection, modules);
		}
	}

	private IConnection getActiveConnection(IServer server) throws Exception {
		IProvider provider = MessagingServersUtil.getProvider(server);
		if (provider == null)
			throw new MessagingException(
					"Provider is not associated with the server "
							+ server.getName() + ".");
		IConnection connection = provider.getConnection();
		if (connection == null)
			throw new MessagingException(
					"Connection is not established with the server "
							+ server.getName() + ".");
		return connection;
	}

	private void makeListenersOnline(IConnection activeConnection,
			IModule[] modules) {
		for (IModule module : modules) {
			Object object = getDataModelObject(module,
					IModelConstants.LISTENERS_FILE_PATH);
			if (object instanceof Listeners) {
				Listeners listenersModel = (Listeners) object;
				List<IListener> listenersList = listenersModel.getListeners();
				for (IListener listener : listenersList) {
					try {
						listener.getDestination().setConnection(
								activeConnection);
						if (listener.canReceive())
							listener.start();
					} catch (MessagingException e) {
						e.printStackTrace();
						// TODO Collect and show after operation
					}
				}
			}
		}
	}

	private void makeSendersOnline(IConnection activeConnection,
			IModule[] modules) {
		for (IModule module : modules) {
			Object object = getDataModelObject(module,
					IModelConstants.SENDERS_FILE_PATH);
			if (object instanceof Senders) {
				Senders sendersModel = (Senders) object;
				List<ISender> sendersList = sendersModel.getSenders();
				for (ISender sender : sendersList) {
					try {
						sender.getDestination().setConnection(activeConnection);
						sender.start();
					} catch (MessagingException e) {
						e.printStackTrace();
						// TODO Collect and show after operation
					}
				}
			}
		}
	}

	private void makeMsgProjectsOffline(IModule[] modules) {
		if (!canProceed(modules))
			return;

		makeListenersOffline(modules);
		makeSendersOffline(modules);
	}

	private void makeSendersOffline(IModule[] modules) {
		for (IModule module : modules) {
			Object object = getDataModelObject(module,
					IModelConstants.SENDERS_FILE_PATH);
			if (object instanceof Senders) {
				Senders sendersModel = (Senders) object;
				List<ISender> sendersList = sendersModel.getSenders();
				for (ISender sender : sendersList) {
					try {
						sender.stop();
					} catch (MessagingException e) {
						e.printStackTrace();
						// TODO Collect and show after operation
					}
				}
			}
		}
	}

	private void makeListenersOffline(IModule[] modules) {
		for (IModule module : modules) {
			Object object = getDataModelObject(module,
					IModelConstants.LISTENERS_FILE_PATH);
			if (object instanceof Listeners) {
				Listeners listenersModel = (Listeners) object;
				List<IListener> listenersList = listenersModel.getListeners();
				for (IListener listener : listenersList) {
					try {
						listener.stop();
					} catch (MessagingException e) {
						e.printStackTrace();
						// TODO Collect and show after operation
					}
				}
			}
		}
	}

	private boolean canProceed(IModule[] modules) {
		if (modules == null || modules.length == 0)
			return false;
		return true;
	}

	private Object getDataModelObject(IModule module, String filePath) {
		if (module instanceof DeletedModule)
			return null;
		IProject project = module.getProject();
		IFile modelFile = project.getFile(filePath);
		return DataModelManager.getInstance().getModel(modelFile);
	}

	public synchronized void addProjectStateListener(
			ProjectStateListener stateListener) {
		if (stateListener == null)
			return;
		prjStateListeners.add(stateListener);
	}

	public synchronized void removeProjectStateListener(
			ProjectStateListener stateListener) {
		if (stateListener == null)
			return;
		prjStateListeners.remove(stateListener);
	}

	private void fireProjectStateChanges(IModule[] msgModules, boolean isOnline) {
		ProjectStateEvent event = createEvent(msgModules, isOnline);
		List<ProjectStateListener> listeners = new ArrayList<ProjectStateListener>(
				prjStateListeners);
		for (ProjectStateListener listener : listeners) {
			listener.stateChanged(event);
		}
	}

	private ProjectStateEvent createEvent(IModule[] msgModules, boolean isOnline) {
		int state = getStateConstant(isOnline);
		List<IProject> projectsList = new ArrayList<IProject>();
		for (IModule module : msgModules) {
			projectsList.add(module.getProject());
		}
		IProject[] projectsArray = projectsList.toArray(new IProject[] {});
		return new ProjectStateEvent(projectsArray, state);
	}

	private int getStateConstant(boolean isOnline) {
		if (isOnline)
			return ProjectStateEvent.PROJECT_STATE_ONLINE;
		return ProjectStateEvent.PROJECT_STATE_OFFLINE;
	}

}
