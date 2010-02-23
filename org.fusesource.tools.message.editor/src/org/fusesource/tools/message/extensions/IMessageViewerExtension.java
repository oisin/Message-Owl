/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.message.extensions;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.forms.IDetailsPage;

public interface IMessageViewerExtension {

    public IDetailsPage getDetailsPage();

    public Collection<Action> getActions();
}
