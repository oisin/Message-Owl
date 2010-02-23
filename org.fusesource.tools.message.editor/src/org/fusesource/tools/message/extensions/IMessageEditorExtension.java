/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.message.extensions;

import java.util.Collection;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.Composite;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.editors.MessageEditorPageBean;

public interface IMessageEditorExtension {

    public Collection<MessageEditorPageBean> getEditorPages(Composite container, EditingDomain editingDomain,
            Message messageModel);

    public void createBody(Composite parent, EditingDomain editingDomain, Message messageModel);

}
