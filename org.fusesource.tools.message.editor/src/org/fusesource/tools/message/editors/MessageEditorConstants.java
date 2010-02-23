/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.message.editors;

import org.fusesource.tools.message.MessageConstants;

public interface MessageEditorConstants {
    final String EMPTY_STRING = "";
    public static final String MESSAGE_BODY_PAGE_NAME = "Message";
    public static final String DEFAULT_TYPE = MessageConstants.TEXT_MESSAGE_TYPE;
    public static final String PROPERTIES = "Properties";
    public static final String VARIABLES = "Variables";
    public static final String PART_HEADER = "Part Headers";
    public static final String HEADER = "Headers";
    public static final String MESSAGE_FILE_EXTENSION = "message";

    public static final String DISPLAY_TYPE_PREFIX = " (";
    public static final String DISPLAY_TYPE_SUFFIX = ")";
}
