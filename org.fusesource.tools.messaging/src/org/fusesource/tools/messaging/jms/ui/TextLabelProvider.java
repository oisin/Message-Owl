/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.ui;

import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.fusesource.tools.messaging.ui.DefaultLabelProvider;


public class TextLabelProvider extends DefaultLabelProvider {

	@Override
	public String getText(Object element) {

		String text = super.getText(element);
		if (element instanceof AnyTypeImpl) {
			text = "Body";
		}
		return text;
	}
}
