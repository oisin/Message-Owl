/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.ui;

import org.fusesource.tools.messaging.IConstants;

public class Column {
    private String columnName = IConstants.EMPTY_STRING;
    private float widthValue;

    public Column(String columnName) {
        this(columnName, 0.1f);
        this.columnName = columnName;
    }

    public Column(String columnName, float widthValue) {
        this.columnName = columnName;
        this.widthValue = widthValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public float getWidthValue() {
        return widthValue;
    }
}
