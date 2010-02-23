/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.message.extensions;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.fusesource.tools.core.message.Message;

public interface IMessageType {

    public static final String MESSAGE_TYPE_EXT_PT = "org.fusesource.tools.message.editor.MessageType";

    public Message load(String fileUrl);

    public Message load(File file);

    public Message load(IFile ifile);

    public String getType();

    public String getProviderId();

    public void setProviderId(String id);

    public void setType(String type);

    public boolean canHandle(Object msg);

    public Message convertMessage(Object msg) throws Exception;

    public void setSupportedFileExtension(String supportedFileExtension);

    public String getSupportedFileExtension();

    public Map<String, String> getHeaders();

}
