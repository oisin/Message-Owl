package org.fusesource.tools.messaging.cnf.model;

/**
 * Represents the base component in CNF
 * @author kiranb
 * 
 */
public class BaseComponent {
	Object parent;

	public BaseComponent(Object parent) {
		this.parent = parent;
	}

	public Object getParent() {
		return parent;
	}

}
