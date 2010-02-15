/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.message.extensions;

import java.util.Collection;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.Composite;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.editors.MessageEditorPageBean;


public interface IMessageEditorExtension {

	public Collection<MessageEditorPageBean> getEditorPages(Composite container, EditingDomain editingDomain, Message messageModel);

	public void createBody(Composite parent, EditingDomain editingDomain, Message messageModel);

}
