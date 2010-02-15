/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.message;

import org.eclipse.swt.SWT;
import org.fusesource.tools.core.ui.TextViewerComponent;
import org.fusesource.tools.messaging.jms.ui.JMSMessageDetailPart;


public class FUSEXMLMessageDetailPart extends JMSMessageDetailPart {

	protected void getRightCompositeForBody(Object data) {
		textViewer = TextViewerComponent.createXMLViewer(rightCompositeHolder, data, SWT.MULTI | SWT.WRAP
				| SWT.V_SCROLL);
		textViewer.setEditable(false);
	}
}
