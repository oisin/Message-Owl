package org.fusesource.tools.core.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.fusesource.tools.core.Activator;
import org.fusesource.tools.core.ui.StringFieldEditor;


public class FusePreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public FusePreferencePage() {
	}

	public FusePreferencePage(String title) {
		super(title);
	}

	public FusePreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite arg0) {
		return new Composite(arg0, SWT.NONE);
	}

	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub

	}

}
