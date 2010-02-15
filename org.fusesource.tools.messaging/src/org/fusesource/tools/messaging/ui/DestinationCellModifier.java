package org.fusesource.tools.messaging.ui;

import java.util.Map;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;
import org.fusesource.tools.messaging.jms.ui.DestinationAdvanceProperty;


public class DestinationCellModifier implements ICellModifier {

	private static String NAME_PROPERTY = "name";
	private static String VALUE_PROPERTY = "value";
	
	private Map<String, Object> senderProperties;
	private TableViewer viewer;

	public DestinationCellModifier(TableViewer viewer, Map<String, Object> senderProperties) {
		this.viewer = viewer;
		this.senderProperties = senderProperties;
	}

	public boolean canModify(Object element, String property) {
		if (VALUE_PROPERTY.equals(property))
			return true;
		else
			return false;
	}

	public Object getValue(Object element, String property) {
		if (NAME_PROPERTY.equals(property))
          return ((DestinationAdvanceProperty) element).getName();
        else
          return ((DestinationAdvanceProperty) element).getValue();
	}

	public void modify(Object element, String property, Object value) {
		TableItem tableItem = (TableItem) element;
        element = tableItem.getData();
        DestinationAdvanceProperty data = (DestinationAdvanceProperty) element;
        if (VALUE_PROPERTY.equals(property)){
        	data.setValue(value.toString());
        	senderProperties.put(data.getName(),value.toString());
        }
		viewer.refresh(data);
	}


}
