/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.ui.url.editor.resource;

import java.net.URL;

import org.eclipse.ui.IStorageEditorInput;

/*

 */

public interface URLEditorInput extends IStorageEditorInput {

    public URL getURL();
}
