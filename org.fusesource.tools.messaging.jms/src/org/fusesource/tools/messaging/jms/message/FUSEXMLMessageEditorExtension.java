// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms.message;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.fusesource.tools.core.ui.TextViewerComponent;
import org.fusesource.tools.messaging.jms.ui.JMSTextMessageEditorExtension;


/**
 * 
 * @author sgupta
 *
 */
public class FUSEXMLMessageEditorExtension extends JMSTextMessageEditorExtension {
	
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
