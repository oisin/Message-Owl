/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.messaging.jms.ui;

import org.fusesource.tools.message.extensions.IMessageEditorExtension;
import org.fusesource.tools.message.extensions.IMessageViewerExtension;
import org.fusesource.tools.messaging.ui.SimpleMessageTypeUI;

public class JMSSimpleMessageTypeUI extends SimpleMessageTypeUI {

    @Override
    public IMessageEditorExtension getEditorExtension() {
        return new JMSSimpleMessageEditorExtension();
    }

    @Override
    public IMessageViewerExtension getViewerExtension() {
        return new JMSSimpleMessageViewer();
    }

    @Override
    public boolean canHandle(Object msg) {
        if (msg instanceof javax.jms.TextMessage) {
            return false;
        }
        if (msg instanceof javax.jms.Message) {
            return true;
        }
        return false;
    }
}
