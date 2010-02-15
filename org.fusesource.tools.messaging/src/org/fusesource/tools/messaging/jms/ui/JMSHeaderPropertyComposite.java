package org.fusesource.tools.messaging.jms.ui;

import java.util.Date;

import org.eclipse.swt.widgets.Composite;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.ui.HeaderPropertyComposite;
import org.fusesource.tools.messaging.ui.HeadersLabelProvider;


public class JMSHeaderPropertyComposite extends HeaderPropertyComposite {

	public JMSHeaderPropertyComposite(Composite parentComposite) {
		super(parentComposite);
	}

	public JMSHeaderPropertyComposite(Composite parentComposite, Object model) {
		super(parentComposite, model);
	}

	protected void initializeTableViewer() {
		tableViewer.setContentProvider(new HeadersViewerContentProvider());
		tableViewer.setLabelProvider(new HeadersLabelProvider() {
			@Override
			public String getColumnText(Object element, int col) {
				if (element instanceof Property && col == 1) {
					Property property = (Property) element;
					if (JMSConstants.JMSTIMESTAMP.equalsIgnoreCase(property.getName())) {
						try {
							return new Date(Long.valueOf(property.getValue())).toString();
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
				return super.getColumnText(element, col);
			}
		});
	}
}
