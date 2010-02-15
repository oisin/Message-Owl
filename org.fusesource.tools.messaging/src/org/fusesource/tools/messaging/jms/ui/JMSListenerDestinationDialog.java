// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms.ui;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.jms.JMSDestination;
import org.fusesource.tools.messaging.ui.DestinationUtil;
import org.fusesource.tools.messaging.ui.dialogs.ListenerDestinationDialog;


/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
public class JMSListenerDestinationDialog extends ListenerDestinationDialog {
	private Button checkButton;
	private Text messageSelectorTxt;
	private Text subscriptionName;
	boolean isUICreated;

	public JMSListenerDestinationDialog() {
		super();
	}

	@Override
	protected String getMessage() {
		return "Enter destination details to create a JMS Listener";
	}

	@Override
	protected String getTitle() {
		return "Add JMS Listener";
	}

	@Override
	protected String getDialogTitle() {
		return "Add JMS Listener";
	}

	@Override
	protected boolean hasAdvanceSection() {
		return true;
	}

	@Override
	protected void createAdvancedUI(Composite composite) {
		populateProperties();
		createPropertiesSection(composite);
		isUICreated = true;
		validate();
	}
	
	@Override
	protected void validate() {
		super.validate();
		if(!isUICreated)
			return;
		String destType = destTypesCombo.getItem(destTypesCombo.getSelectionIndex());
		if (JMSConstants.TOPIC_TYPE.equals(destType)) {
			checkButton.setEnabled(true);
			subscriptionName.setEnabled(checkButton.getSelection());
		} else {
			checkButton.setEnabled(false);
			subscriptionName.setEnabled(checkButton.isEnabled() && checkButton.getSelection());
		}
	}

	@Override
	protected void okPressed() {
		updateData();
		super.okPressed();
	}

	protected void updateData() {
		if (isUICreated) {
			if (checkButton.getSelection())
				listenerProperties.put(JMSConstants.DURABLE_SUBSCRIPTION_NAME, subscriptionName.getText().trim());
			listenerProperties.put(JMSConstants.JMS_MESSAGE_SELECTOR, messageSelectorTxt.getText().trim());
		}
		super.updateData();
		if (getCreatedDestination() != null) {
			((JMSDestination) getCreatedDestination()).setDestinationName(getDestName());
			((JMSDestination) getCreatedDestination()).setDestinationType(getDestinationType());
		}
	}

	private void populateProperties() {
		// TODO - Creation of destination should not be here
		if (createdDestination == null) {
			try {
				IConnection connection = DestinationUtil.getConnection(getProvider());
				if (connection != null)
					createdDestination = connection.createDestination(getDestinationType(), getDestName());
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		if (createdDestination != null) {
			Map<String, Object> propsFromDest = createdDestination.getListenerProperties();
			if (propsFromDest != null && !propsFromDest.isEmpty()) {
				// TODO Not making use of this at present - show this in table
				listenerProperties.putAll(propsFromDest);
			}
		}
	}

	private void createPropertiesSection(Composite composite) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		Group listenerPropsGroup = new Group(composite, SWT.NONE);
		listenerPropsGroup.setText("Listener Properties");
		listenerPropsGroup.setLayout(new GridLayout(2, false));
		listenerPropsGroup.setLayoutData(data);
		createDurablePropertyUI(listenerPropsGroup);
		createMsgSelectorPropertyUI(listenerPropsGroup);
	}

	private void createMsgSelectorPropertyUI(Group listenerPropsGroup) {
		Label label = new Label(listenerPropsGroup, SWT.NONE);
		label.setText("Message Selector: ");
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		messageSelectorTxt = new Text(listenerPropsGroup, SWT.BORDER);
		messageSelectorTxt.setLayoutData(data);
	}

	private void createDurablePropertyUI(Group listenerPropsGroup) {
		Label durableSub = new Label(listenerPropsGroup, SWT.NONE);
		durableSub.setText("Durable Subscriber:");
		Composite durableComp = new Composite(listenerPropsGroup, SWT.NONE);
		durableComp.setLayout(new GridLayout(4, false));
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		durableComp.setLayoutData(data);
		checkButton = new Button(durableComp, SWT.CHECK);
		Label durable = new Label(durableComp, SWT.NONE);
		durable.setText("Durable");
		Label subLabel = new Label(durableComp, SWT.NONE);
		data = new GridData(SWT.CENTER);
		subLabel.setLayoutData(data);
		subLabel.setText("Subscription Name:");
		subscriptionName = new Text(durableComp, SWT.BORDER);
		subscriptionName.setEnabled(false);
		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		subscriptionName.setLayoutData(data);
		checkButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				subscriptionName.setEnabled(checkButton.getSelection());
			}

		});
	}
}
