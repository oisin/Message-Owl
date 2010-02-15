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
