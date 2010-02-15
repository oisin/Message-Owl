/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.ui;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.core.message.Property;


public class HeadersContentProvider implements IStructuredContentProvider{
	
	public HeadersContentProvider(){
	}

	public Object[] getElements(Object element) {
		if (element instanceof Properties) {
			Properties properties = (Properties) element;
			EList<Property> propertyList = properties.getProperty();
			return propertyList.toArray();
		} else
			return new Object[0];
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
