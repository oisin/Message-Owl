/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.ui;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.fusesource.tools.messaging.MessageEvent;


public class TableLabelProvider extends FontBasedLabelProvider implements ITableLabelProvider {

	public TableLabelProvider() {

	}

	public Image getImage(Object element) {
		return super.getImage(element);
	}

	public String getColumnText(Object element, int i) {
		StringBuffer displayString = new StringBuffer();
		if (element instanceof MessageEvent) {
			MessageEvent messageEvent = (MessageEvent) element;
			displayString.append("Message Type:  ");
			Map<String, String> columndata = messageEvent.getMetadata();
			Set<String> keySet = columndata.keySet();
			// TODO: change this bad logic
			ArrayList<String> arrayList = new ArrayList<String>();
			for (String string : keySet) {
				arrayList.add(columndata.get(string));
			}
			return arrayList.get(i);
		}
		return displayString.toString();
	}

	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}
}
