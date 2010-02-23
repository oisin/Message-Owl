/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.message.types;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.extensions.IMessageType;

public class SimpleMessageTypeProvider implements IMessageType {
    private String providerId;

    private String type;

    protected String supportedFileExtension;

    public boolean canHandle(Object msg) {
        return false;
    }

    public Message convertMessage(Object msg) throws Exception {
        return null;
    }

    public String getType() {
        return type;
    }

    public String getProviderId() {
        return providerId;
    }

    public Message load(String fileURL) {
        return getMessageLoader().getMessageModel(fileURL);
    }

    public Message load(File file) {
        return load(file.getAbsolutePath());
    }

    public Message load(IFile ifile) {
        return load(ifile.getLocation().toOSString());
    }

    public void setProviderId(String id) {
        this.providerId = id;

    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSupportedFileExtension(String supportedFileExtn) {
        supportedFileExtension = supportedFileExtn;
    }

    public String getSupportedFileExtension() {
        return supportedFileExtension;
    }

    /**
     * Clients can override this method to add JMS provider specific headers
     */
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    protected AbstractMessageLoader getMessageLoader() {
        return new SimpleMessageLoader();
    }

}
