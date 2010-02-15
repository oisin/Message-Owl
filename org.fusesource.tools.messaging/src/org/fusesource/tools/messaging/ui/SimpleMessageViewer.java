/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.ui;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.forms.IDetailsPage;
import org.fusesource.tools.message.extensions.IMessageViewerExtension;
import org.fusesource.tools.messaging.editors.DefaultMessageDetailPart;


public class SimpleMessageViewer implements IMessageViewerExtension {

	protected DefaultMessageDetailPart detailPart = null;

	public SimpleMessageViewer(){
		detailPart = new DefaultMessageDetailPart();
	}
	
	public Collection<Action> getActions() {
		return detailPart.getActionsList();
	}

	public IDetailsPage getDetailsPage() {
		return detailPart;
	}
}
