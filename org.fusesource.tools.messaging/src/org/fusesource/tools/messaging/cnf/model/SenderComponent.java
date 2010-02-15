package org.fusesource.tools.messaging.cnf.model;

import org.fusesource.tools.messaging.core.ISender;

/**
 * Represents Sender Root Component in CNF
 * 
 * @author kiranb
 * 
 */
public class SenderComponent extends BaseComponent {
	private ISender sender;

	public SenderComponent(Object parent, ISender theSender) {
		super(parent);
		this.sender = theSender;
	}

	public ISender getSender() {
		return sender;
	}
}
