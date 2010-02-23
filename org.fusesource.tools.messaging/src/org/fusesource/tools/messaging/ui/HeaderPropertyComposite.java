/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.ui;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.fusesource.tools.messaging.IConstants;

public class HeaderPropertyComposite {
    protected Composite parentComposite;
    public static final String HEADER_NAME = "Name";
    public static final String HEADER_VALUE = "Value";
    protected String[] columnNames = new String[] { HEADER_NAME, HEADER_VALUE };
    protected Table table;
    protected TableViewer tableViewer;

    public HeaderPropertyComposite(Composite parentComposite) {
        this.parentComposite = parentComposite;
        createUI();
    }

    public HeaderPropertyComposite(Composite parentComposite, Object model) {
        this(parentComposite);
        setModel(model);
    }

    public void setModel(Object model) {
        if (model != null) {
            tableViewer.setInput(model);
        }
    }

    public void createUI() {
        tableViewer = new DefaultMessageTableViewer(parentComposite, SWT.FULL_SELECTION, IConstants.EMPTY_STRING);
        tableViewer.setUseHashlookup(true);
        tableViewer.setColumnProperties(columnNames);
        table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        ((DefaultMessageTableViewer) tableViewer).refreshTable("Name;Value");

        initializeTableViewer();

    }

    protected void initializeTableViewer() {
        tableViewer.setContentProvider(new HeadersViewerContentProvider());
        tableViewer.setLabelProvider(new HeadersLabelProvider());
    }

    public Control getControl() {
        return tableViewer.getTable();
    }

    @SuppressWarnings("unchecked")
    protected class HeadersViewerContentProvider extends HeadersContentProvider {

        public HeadersViewerContentProvider() {
        }

        @Override
        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof List) {
                List input = (List) inputElement;
                return input.toArray(new Object[input.size()]);
            }
            return null;
        }
    }
}
