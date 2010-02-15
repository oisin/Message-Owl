/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.ui;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.message.editors.MessageEditorConstants;
import org.fusesource.tools.messaging.IConstants;


public class DefaultLabelProvider implements ILabelProvider {

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		if (element instanceof List) {
			List<Property> list = (List<Property>) element;
			if (list.size() > 0) {
				Property property = list.get(0);
				if (!property.isIsheader())
					return getPropertiesLabel();
				else
					return getHeaderLabel();
			} 
		}
		return IConstants.EMPTY_STRING;
	}

	protected String getHeaderLabel() {
		return MessageEditorConstants.HEADER;
	}

	protected String getPropertiesLabel() {
		return MessageEditorConstants.PROPERTIES;
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