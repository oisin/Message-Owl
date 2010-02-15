// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.server.ui;

import org.eclipse.jface.action.IAction;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.server.MessagingServersUtil;
import org.fusesource.tools.messaging.utils.MessageDialogUtils;


public class MsgServerConnectAction extends AbstractMsgServerAction {

	public MsgServerConnectAction() {
		super();
	}

	public void run(IAction action) {
		try {
			MessagingServersUtil.connectToServer(getSelectedServer());
		} catch (MessagingException msgEx) {
			msgEx.printStackTrace();
			MessageDialogUtils.showErrorMessage("Failed to Connect", "Failed to connect to the server "
					+ getServerName() + " " + msgEx.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialogUtils.showErrorMessage("Failed to Connect", "Failed to connect to the server "
					+ getServerName() + " " + e.getMessage());
		}
	}

	@Override
	protected boolean canEnable() {
		if (MessagingServersUtil.isMsgServer(getSelectedServer()))
			return !MessagingServersUtil.isMsgServerOnline(getSelectedServer());
		return false;
	}

}
