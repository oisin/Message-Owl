/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.messaging.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.messaging.IConstants;

public class DefaultContentProvider implements ITreeContentProvider {

    private final List<Property> headerList = new ArrayList<Property>();
    private final List<Property> propertiesList = new ArrayList<Property>();;

    public Object[] getChildren(Object parentElement) {
        // TODO Read message from MessageType

        if (parentElement instanceof Message) {
            Properties properties = ((Message) parentElement).getProperties();
            if (properties != null) {
                populateList(properties);
                if (propertiesList.size() > 0) {
                    return new Object[] { headerList, propertiesList };
                } else {
                    return new Object[] { headerList };
                }
            }
        }
        return IConstants.NO_CHILDREN;
    }

    public Object getParent(Object element) {
        return null;
    }

    public boolean hasChildren(Object element) {
        return false;
    }

    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    /**
	 * 
	 */
    protected void populateList(Properties properties) {
        for (Property property : properties.getProperty()) {
            if (property.isIsheader()) {
                headerList.add(property);
            } else {
                propertiesList.add(property);
            }
        }
    }
}
