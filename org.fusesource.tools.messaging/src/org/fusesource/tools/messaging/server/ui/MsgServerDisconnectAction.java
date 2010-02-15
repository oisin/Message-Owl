// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.server.ui;

import org.eclipse.jface.action.IAction;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.MsgProjectStateManager;
import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.server.MessagingServersUtil;
import org.fusesource.tools.messaging.utils.MessageDialogUtils;


/**
 * 
 * @author kiranb
 * 
 */
public class MsgServerDisconnectAction extends AbstractMsgServerAction {

	public MsgServerDisconnectAction() {
		super();
	}

	public void run(IAction action) {
		try {
			IProvider provider = MessagingServersUtil.getProvider(getSelectedServer());
			if (provider == null) {
				MessageDialogUtils.showErrorMessage("Provider unavailable",
						"No messaging provider associated with the server. " + getServerName());
				return;
			}
			provider.getConnection().closeConnection();
		} catch (MessagingException msgEx) {
			MessageDialogUtils.showErrorMessage("Failed to disconnect", "Failed to disconnect from the server "
					+ getServerName() + " " + msgEx.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialogUtils.showErrorMessage("Failed to disconnect", "Failed to disconnect from the server "
					+ getServerName());
		} finally {
			MsgProjectStateManager.getInstance().updateMsgProjectsState(getSelectedServer(),
					getSelectedServer().getModules());
		}
	}

	@Override
	protected boolean canEnable() {
		if(MessagingServersUtil.isMsgServer(getSelectedServer()))
			return MessagingServersUtil.isMsgServerOnline(getSelectedServer());
		return false;
	}

}
