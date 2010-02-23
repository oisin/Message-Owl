/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.ui.url.urlchooser;

import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class URLChooserFilter extends LinkedHashMap {
    public URLChooserFilter() {
    }

    public URLChooserFilter(String filter) {
        put(filter, filter);
    }

    public URLChooserFilter(String[] filter) {
        for (String element : filter) {
            put(element, element);
        }
    }

    public String[] getFilterNames() {
        return (String[]) values().toArray(new String[0]);
    }

    public String[] getFilterExtensions() {
        return (String[]) keySet().toArray(new String[0]);
    }

    @Override
    public Object put(Object key, Object value) {
        boolean equal = key.equals(value);

        StringTokenizer st = new StringTokenizer((String) key, ";");

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            super.put(token, equal ? token : value);
        }
        return null;
    }
}
