package org.fusesource.tools.core.ui;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;


public class XMLViewerComponentEditor extends FieldEditor {
	
	protected SourceViewer xmlViewer;

	protected String defaultString;
	

	public XMLViewerComponentEditor(Composite parent, String tracerPreference, String defaultString) {
		createControl(parent);
		this.defaultString = defaultString;
		setPreferenceName(tracerPreference);
	}
	
	@Override
	protected void adjustForNumColumns(int numColumns) {
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = numColumns;
		composite.setLayoutData(gridData);
		composite.setLayout(new FillLayout());
		
		xmlViewer = TextViewerComponent.createXMLViewer(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		xmlViewer.getDocument().set(defaultString);
	}

	@Override
	protected void doLoad() {
		String tracerBean = getPreferenceStore().getString(getPreferenceName());
		if(tracerBean == null){
			xmlViewer.getDocument().set(defaultString);
		}
		xmlViewer.getDocument().set(tracerBean);
	}

	@Override
	protected void doLoadDefault() {
		xmlViewer.getDocument().set(defaultString);
	}

	@Override
	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), xmlViewer.getDocument().get());
	}

	@Override
	public int getNumberOfControls() {
		return 1;
	}
}
