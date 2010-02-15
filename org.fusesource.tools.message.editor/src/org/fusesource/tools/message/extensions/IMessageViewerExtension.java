/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.message.extensions;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.forms.IDetailsPage;

public interface IMessageViewerExtension {

	public IDetailsPage getDetailsPage();
	
	public Collection<Action> getActions();
}
