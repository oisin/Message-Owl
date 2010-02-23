/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.messaging.ui;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.core.message.Property;

public class HeadersContentProvider implements IStructuredContentProvider {

    public HeadersContentProvider() {
    }

    public Object[] getElements(Object element) {
        if (element instanceof Properties) {
            Properties properties = (Properties) element;
            EList<Property> propertyList = properties.getProperty();
            return propertyList.toArray();
        } else {
            return new Object[0];
        }
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
