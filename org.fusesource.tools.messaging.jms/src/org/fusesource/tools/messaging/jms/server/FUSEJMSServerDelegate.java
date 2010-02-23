/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.jms.server;

import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.jms.FuseJMSProvider;
import org.fusesource.tools.messaging.server.MessagingServerDelegate;

public class FUSEJMSServerDelegate extends MessagingServerDelegate {

    public FUSEJMSServerDelegate() {
        super();
    }

    @Override
    public IProvider createProvider() {
        return new FuseJMSProvider();
    }
}
