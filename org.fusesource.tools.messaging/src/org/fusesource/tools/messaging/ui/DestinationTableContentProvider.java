package org.fusesource.tools.messaging.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.fusesource.tools.messaging.jms.ui.DestinationAdvanceProperty;


public class DestinationTableContentProvider implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof HashMap) {
			List list = new ArrayList();
			Iterator keys = ((HashMap) inputElement).keySet().iterator();
			DestinationAdvanceProperty sProperty = null;
			while(keys.hasNext()){
				String key = keys.next().toString();
				String value = ((HashMap)inputElement).get(key).toString();
				sProperty = new DestinationAdvanceProperty(key,value);
				list.add(sProperty);
			}
			return list.toArray();
		}
		return null;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
