/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.message.extensions;

public interface IMessageTypeUI {

    public static final String MESSAGE_TYPE_UI_EXT_PT = "org.fusesource.tools.message.editor.MessageTypeUI";

    public IMessageEditorExtension getEditorExtension();

    public IMessageViewerExtension getViewerExtension();

    public boolean canHandle(Object msg);

    public String getProviderId();

    public String getType();

    public void setProviderId(String id);

    public void setType(String type);

}
