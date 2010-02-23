/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.model;

import java.io.File;

public interface IModelConstants {
    final String SENDERS_EXT = "senders";
    final String META_FOLDER = ".settings";
    final String SENDERS_FILE_NAME = "Senders" + "." + SENDERS_EXT;
    final String LISTENERS_EXT = "listeners";
    final String LISTENERS_FILE_NAME = "Listeners" + "." + LISTENERS_EXT;
    final String SENDERS_FILE_PATH = META_FOLDER + File.separator + SENDERS_FILE_NAME;
    final String LISTENERS_FILE_PATH = META_FOLDER + File.separator + LISTENERS_FILE_NAME;
}
