/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.ui;

import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.fusesource.tools.core.message.Body;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.messaging.ui.DefaultContentProvider;


public class TextContentProvider extends DefaultContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		Object[] objects = super.getChildren(parentElement);
		Object[] finalObjects = objects;
		if (parentElement instanceof Message) {
			Body body = ((Message) parentElement).getBody();
			if (body != null) {
				AnyTypeImpl content = (AnyTypeImpl) body.getContent();
				if (content != null) {
					finalObjects = new Object[objects.length + 1];
					int i = 0;
					for (; i < objects.length; i++) {
						finalObjects[i] = objects[i];
					}
					finalObjects[i] = content;
				}
			}
		}
		return finalObjects;
	}
}
