/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.ui.url.editor.resource;

/*

 */
public class URLSpaceConverter {

    /**
     * Converts spaces to platform specific character. For now it works only in windows platform
     * 
     * @param url
     * @return
     */
    public static String encode(String url) {
        return url.replaceAll(" ", "%20");
    }

    /**
     * Converts encode string back space saperated url.
     * 
     * @param url
     * @return
     */
    public static String decode(String url) {
        return url.replaceAll("%20", " ");
    }
}
