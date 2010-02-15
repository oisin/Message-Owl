package org.fusesource.tools.messaging.cnf.model;

import org.fusesource.tools.messaging.core.IListener;

/**
 * Represents Listener Component in CNF
 * 
 * @author kiranb
 * 
 */
public class ListenerComponent extends BaseComponent {
	private IListener listener;

	public ListenerComponent(Object parent, IListener theListener) {
		super(parent);
		this.listener = theListener;
	}

	public IListener getListener() {
		return listener;
	}
}
