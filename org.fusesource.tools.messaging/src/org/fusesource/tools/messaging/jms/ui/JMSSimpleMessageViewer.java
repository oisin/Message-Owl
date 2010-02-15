/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.ui;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.forms.IDetailsPage;
import org.fusesource.tools.message.extensions.IMessageViewerExtension;


public class JMSSimpleMessageViewer implements IMessageViewerExtension {

	protected JMSMessageDetailPart detailPart = null;

	public JMSSimpleMessageViewer() {
		detailPart = new JMSMessageDetailPart();
	}

	public Collection<Action> getActions() {
		return detailPart.getActionsList();
	}

	public IDetailsPage getDetailsPage() {
		return detailPart;
	}
}
