package org.fusesource.tools.messaging.cnf.model;

import org.eclipse.core.resources.IFile;

/**
 * Represents Listeners Root Component in CNF
 * 
 * @author kiranb
 * 
 */
public class ListenersRootComponent extends BaseGroupComponent {

	private Object[] children;

	public ListenersRootComponent(String name, IFile file1) {
		super(name, file1);
	}

	public Object[] getChildren() {
		return children;
	}

	public void setChildren(Object[] children) {
		this.children = children;
	}

}
