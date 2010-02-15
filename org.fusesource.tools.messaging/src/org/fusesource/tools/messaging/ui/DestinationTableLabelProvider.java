package org.fusesource.tools.messaging.ui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.fusesource.tools.messaging.jms.ui.DestinationAdvanceProperty;


public class DestinationTableLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
        case 0:
          return ((DestinationAdvanceProperty) element).getName();
        case 1:
          return ((DestinationAdvanceProperty) element).getValue();
        default:
          return "Invalid column: " + columnIndex;
        }			
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
