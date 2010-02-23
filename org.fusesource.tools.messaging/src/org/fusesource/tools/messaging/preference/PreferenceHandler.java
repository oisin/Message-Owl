/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.preference;

public interface PreferenceHandler {
    public void addPreferenceChangedListener(PreferenceChangedListener listener);

    public void setPreferenceValue(String key, String value);

    public String getPreferenceValue(String key);

    public void removePreferenceChangedListener(PreferenceChangedListener listener);

}
