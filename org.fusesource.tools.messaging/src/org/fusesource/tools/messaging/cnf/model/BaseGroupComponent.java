package org.fusesource.tools.messaging.cnf.model;

import org.eclipse.core.resources.IFile;

/**
 * Represents base group component in CNF
 * 
 * @author kiranb
 * 
 */
public class BaseGroupComponent {
	private String name;
	private IFile file;

	public BaseGroupComponent(String nameParam, IFile fileParam) {
		this.name = nameParam;
		this.file = fileParam;
	}

	public String getName() {
		return name;
	}

	public IFile getFile() {
		return file;
	}
}
