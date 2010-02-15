package org.fusesource.tools.messaging;

import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IDestinationType;

/**
 * Abstract implementation of IDestination
 * 
 * @author kiranb
 * 
 */
public abstract class AbstractDestination implements IDestination {
	private static final long serialVersionUID = -902736454921047639L;
	transient private IConnection con;
	private IDestinationType type;
	private String name;

	public AbstractDestination(IConnection con, IDestinationType type,
			String name) {
		this.con = con;
		this.type = type;
		this.name = name;
	}

	public IConnection getConnection() {
		return con;
	}

	public String getDestinationName() {
		return name;
	}

	public IDestinationType getDestinationType() {
		return type;
	}

	public void setConnection(IConnection con) {
		this.con = con;
	}

	public void setDestinationName(String name) {
		this.name = name;
	}

	public void setDestinationType(IDestinationType type) {
		this.type = type;
	}
}