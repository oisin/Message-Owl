/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
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
        if (MessagingServersUtil.isMsgServer(getSelectedServer())) {
            return !MessagingServersUtil.isMsgServerOnline(getSelectedServer());
        }
        return false;
    }

}
