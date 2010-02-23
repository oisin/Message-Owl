/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.message.editors;

import org.eclipse.swt.widgets.Composite;

public class MessageEditorPageBean {

    private Composite page = null;

    private String label = null;

    public MessageEditorPageBean(String label, Composite composite) {
        this.label = label;
        page = composite;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @param page
     *            the page to set
     */
    public void setPage(Composite page) {
        this.page = page;
    }

    /**
     * @return the page
     */
    public Composite getPage() {
        return page;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }
}
