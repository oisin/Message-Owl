/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.messaging.jms.ui;

import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.fusesource.tools.messaging.ui.DefaultLabelProvider;

public class TextLabelProvider extends DefaultLabelProvider {

    @Override
    public String getText(Object element) {

        String text = super.getText(element);
        if (element instanceof AnyTypeImpl) {
            text = "Body";
        }
        return text;
    }
}
