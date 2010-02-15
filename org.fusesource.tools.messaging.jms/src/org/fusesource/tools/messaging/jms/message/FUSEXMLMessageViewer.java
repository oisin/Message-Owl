/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.message;

import org.fusesource.tools.messaging.jms.ui.JMSSimpleMessageViewer;

public class FUSEXMLMessageViewer extends JMSSimpleMessageViewer {

	public FUSEXMLMessageViewer() {
		detailPart = new FUSEXMLMessageDetailPart();
	}

}
