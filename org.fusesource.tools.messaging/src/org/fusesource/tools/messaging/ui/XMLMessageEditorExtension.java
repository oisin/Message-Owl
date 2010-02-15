// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.fusesource.tools.core.ui.TextViewerComponent;


/**
 * 
 * @author sgupta
 *
 */
public class XMLMessageEditorExtension extends TextMessageEditorExtension {
	
	private static String[] extensions = new String[]{"*.xml"};
	
	@Override
	protected String[] getExtensions() {		
		return extensions;
	}
	
	@Override
	protected void createViewer(Composite parent) {
		textEditor = TextViewerComponent.createXMLViewer(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	}
}
