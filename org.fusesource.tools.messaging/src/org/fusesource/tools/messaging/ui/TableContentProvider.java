/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.ui;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.messaging.IConstants;


public class TableContentProvider implements IStructuredContentProvider {
	
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			List queuedMsgs = (List) inputElement;
			return queuedMsgs.size() > 0 ? queuedMsgs.toArray() : IConstants.NO_CHILDREN;
		} else if (inputElement instanceof Message) {
			return new Object[] { inputElement };
		}
		return IConstants.NO_CHILDREN;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
