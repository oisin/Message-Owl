/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.ui;

import org.fusesource.tools.message.extensions.IMessageEditorExtension;

public class TextMessageTypeUI extends SimpleMessageTypeUI {

    protected HeaderPropertyComposite viewHolder = null;

    @Override
    public IMessageEditorExtension getEditorExtension() {
        return new TextMessageEditorExtension();
    }

    @Override
    public boolean canHandle(Object msg) {
        return false;
    }
}
