/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.core.ui.viewer.actions;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.actions.ActionFactory;

public class PasteAction extends SourceViewerAction {

    public PasteAction(SourceViewer sourceViewer) {
        super(sourceViewer, ITextOperationTarget.PASTE);
        setProperties();
    }

    private void setProperties() {
        setText(ActionConstants.PASTE_EDIT);
        // setActionDefinitionId("org.eclipse.ui.edit.paste");
        setId(ActionFactory.PASTE.getId());
    }
}
