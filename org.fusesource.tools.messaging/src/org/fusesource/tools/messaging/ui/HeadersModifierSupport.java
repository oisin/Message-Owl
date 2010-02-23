/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.messaging.ui;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;
import org.fusesource.tools.core.message.Property;

public class HeadersModifierSupport implements ICellModifier {

    protected Viewer viewer;

    public HeadersModifierSupport(Viewer viewer) {
        this.viewer = viewer;
    }

    /**
     * Returns whether the property can be modified
     * 
     * @param element
     *            the element
     * @param property
     *            the property
     * @return boolean
     */
    public boolean canModify(Object element, String property) {
        // Allow editing of all values
        return true;
    }

    /**
     * Returns the value for the property
     * 
     * @param element
     *            the element
     * @param propertyStr
     *            the property
     * @return Object
     */
    public Object getValue(Object element, String propertyStr) {
        Property property = (Property) element;
        if ("Name".equalsIgnoreCase(propertyStr)) {
            return property.getName();
        } else if ("Value".equalsIgnoreCase(propertyStr)) {
            return property.getValue();
        }
        return null;
    }

    /**
     * Modifies the element
     * 
     * @param element
     *            the element
     * @param propertyStr
     *            the property
     * @param value
     *            the value
     */
    public void modify(Object element, String propertyStr, Object value) {
        if (element instanceof Item) {
            element = ((Item) element).getData();
        }

        Property property = (Property) element;
        if ("Name".equalsIgnoreCase(propertyStr)) {
            property.setName((String) value);
        } else if ("Value".equalsIgnoreCase(propertyStr)) {
            property.setValue((String) value);
        }

        // Force the viewer to refresh
        viewer.refresh();
    }
}
