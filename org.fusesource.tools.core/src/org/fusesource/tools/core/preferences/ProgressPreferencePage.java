package org.fusesource.tools.core.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ProgressPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public ProgressPreferencePage() {
	}

	public ProgressPreferencePage(String title) {
		super(title);
	}

	public ProgressPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		return new Composite(parent,SWT.NONE);
	}

	public void init(IWorkbench workbench) {
	}

}
