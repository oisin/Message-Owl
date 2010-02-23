/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.ui.url.urlchooser;

import java.util.Map;

/**

 */
public interface NewWizardProvider {
    /**
     * The URL of the newly created resource. It is expected that most implementations will have a
     * scheme of 'sonicfs'.
     * 
     * @return
     */
    String getPrimaryPath();

    /**
     * An array of all the resource extension-types supported by this provider. Normally this method
     * will return an array with a single element.
     * 
     * @return
     */
    String[] getSupportedExtensions();

    String getDefaultExtension();

    void setNewParameterMap(Map parameters);

    void setUrlChooser(URLChooser urlChooser);
}
