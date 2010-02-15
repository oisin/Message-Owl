/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.ui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.messaging.IConstants;


public class HeadersLabelProvider implements ITableLabelProvider {

	public String getColumnText(Object element, int col) {
		if (element instanceof Property) {
			Property property = (Property) element;
			if (col == 0)
				return property.getName();
			if (col == 1)
				return property.getValue();
		}
		return IConstants.EMPTY_STRING;
	}

	public org.eclipse.swt.graphics.Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}
}