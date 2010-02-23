/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.message.utils;

import org.fusesource.tools.message.MessageConstants;
import org.fusesource.tools.message.editors.MessageEditorConstants;

public interface IEMFConstant {

    final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<message:message xmlns:message=\"http://fuse.com/tools/message\" \nproviderName=\""
            + MessageConstants.DEFAULT_PROVIDER + "\" providerId=\"" + MessageConstants.DEFAULT_PROVIDER + "\" type=\""
            + MessageEditorConstants.DEFAULT_TYPE + "\">" + "<message:properties/><message:body><message:content>";

    final String XML_FOOTER = "</message:content></message:body> </message:message>";
}
