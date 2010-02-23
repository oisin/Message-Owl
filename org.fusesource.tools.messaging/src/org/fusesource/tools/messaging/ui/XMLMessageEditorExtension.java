/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.fusesource.tools.core.ui.TextViewerComponent;

public class XMLMessageEditorExtension extends TextMessageEditorExtension {

    private static String[] extensions = new String[] { "*.xml" };

    @Override
    protected String[] getExtensions() {
        return extensions;
    }

    @Override
    protected void createViewer(Composite parent) {
        textEditor = TextViewerComponent.createXMLViewer(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    }
}
