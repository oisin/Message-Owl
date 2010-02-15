package org.fusesource.tools.messaging;

import org.fusesource.tools.messaging.core.IDestinationType;

/**
 * Default implementation of IDestinationType
 * @author kiranb
 * 
 */
public class DestinationType implements IDestinationType {
	private static final long serialVersionUID = 4139155062747397377L;
	private String type;
	private boolean canSend;
	private boolean canReceive;

	public DestinationType(String type) {
		this(type, true, true);
	}

	public DestinationType(String type, boolean send, boolean receive) {
		this.type = type;
		canSend = send;
		canReceive = receive;
	}

	public boolean canReceive() {
		return canReceive;
	}

	public boolean canSend() {
		return canSend;
	}

	public String getType() {
		return type;
	}
}