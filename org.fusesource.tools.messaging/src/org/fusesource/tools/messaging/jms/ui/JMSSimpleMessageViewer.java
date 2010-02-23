/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.jms.ui;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.forms.IDetailsPage;
import org.fusesource.tools.message.extensions.IMessageViewerExtension;

public class JMSSimpleMessageViewer implements IMessageViewerExtension {

    protected JMSMessageDetailPart detailPart = null;

    public JMSSimpleMessageViewer() {
        detailPart = new JMSMessageDetailPart();
    }

    public Collection<Action> getActions() {
        return detailPart.getActionsList();
    }

    public IDetailsPage getDetailsPage() {
        return detailPart;
    }
}
