/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging;

/**
 * Common Messaging exception
 */
public class MessagingException extends Exception {
    private static final long serialVersionUID = 6200932667257058364L;

    public MessagingException() {
    }

    public MessagingException(String msg) {
        super(msg);
    }

    public MessagingException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
