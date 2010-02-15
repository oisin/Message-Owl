/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.cnf.model.DataModelManager;
import org.fusesource.tools.messaging.cnf.model.IModelConstants;
import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.jms.JMSDestination;
import org.fusesource.tools.messaging.jms.ReplyToInfo;
import org.fusesource.tools.messaging.ui.DefaultMessageTableViewer;
import org.fusesource.tools.messaging.ui.DestinationCellModifier;
import org.fusesource.tools.messaging.ui.DestinationTableContentProvider;
import org.fusesource.tools.messaging.ui.DestinationTableLabelProvider;
import org.fusesource.tools.messaging.ui.DestinationUtil;
import org.fusesource.tools.messaging.ui.dialogs.SenderDestinationDialog;


public class JMSSenderDestinationDialog extends SenderDestinationDialog {

	private TableViewer tableViewer = null;
	private static String COLUMN_HEADERS_STRING = "Name;Value";
	private static String NAME_PROPERTY = "name";
	private static String VALUE_PROPERTY = "value";

	protected JMSReplyToDestinationUI replyToDestination;

	public JMSSenderDestinationDialog() {
		super();
	}

	@Override
	protected String getMessage() {
		return "Enter destination details to create a JMS Sender";
	}

	@Override
	protected String getTitle() {
		return "Add JMS Sender";
	}

	@Override
	protected String getDialogTitle() {
		return "Add JMS Sender";
	}

	@Override
	protected boolean hasAdvanceSection() {
		return true;
	}

	@Override
	protected void createAdvancedUI(Composite composite) {
		populateProperties();
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		GridData data = new GridData();

		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;

		Group messageDeliveryOption = new Group(composite, SWT.NONE);
		messageDeliveryOption.setText(getAdvTitle());
		messageDeliveryOption.setLayout(new FillLayout());
		messageDeliveryOption.setLayoutData(data);
		tableViewer = new DefaultMessageTableViewer(messageDeliveryOption, SWT.FULL_SELECTION, "");

		((DefaultMessageTableViewer) tableViewer).refreshTable(COLUMN_HEADERS_STRING);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);

		CellEditor[] editors = new CellEditor[2];
		editors[0] = new TextCellEditor(table);
		editors[1] = new TextCellEditor(table);
		tableViewer.setCellEditors(editors);
		tableViewer.setCellModifier(new DestinationCellModifier(tableViewer, senderProperties));
		tableViewer.setColumnProperties(new String[] { NAME_PROPERTY, VALUE_PROPERTY });

		tableViewer.setContentProvider(new DestinationTableContentProvider());
		tableViewer.setLabelProvider(new DestinationTableLabelProvider());
		tableViewer.setInput(senderProperties);
		tableViewer.refresh();

		getReplyToDestinationUI().createReplyToSection(composite);
	}

	protected JMSReplyToDestinationUI getReplyToDestinationUI() {
		if (replyToDestination == null)
			replyToDestination = new JMSReplyToDestinationUI(getSource(), getProvider(), senderProperties);
		return replyToDestination;
	}

	@Override
	protected void okPressed() {
		updateData();
		super.okPressed();
		createReplyToDestination();
	}
	
	protected void updateData() {
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
			senderProperties.putAll(getDefaultJMSSendingOptions());
			Map<String, Object> propsFromDest = createdDestination.getSenderProperties();
			if (propsFromDest != null && !propsFromDest.isEmpty()) {
				senderProperties.putAll(propsFromDest);
			}
		}
	}

	public Map<String, String> getDefaultJMSSendingOptions() {
		Map<String, String> sendingOptions = new HashMap<String, String>();
		sendingOptions.put(JMSConstants.DELIVERY_MODE, JMSConstants.NON_PERSISTENT);
		sendingOptions.put(JMSConstants.TIME_TO_LIVE, "");
		sendingOptions.put(JMSConstants.PRIORITY, "");
		return sendingOptions;
	}

	protected void createReplyToDestination() {
		ReplyToInfo replyToInfo = getReplyToDestinationUI().getReplyToInfo();
		if (replyToInfo != null && replyToInfo.isNewDest()) {
			IDestination createDestination = DestinationUtil.createDestination(replyToInfo.getType(), replyToInfo.getName(), getProvider());
			try {
				IListener createListener = createDestination.createListener(new HashMap<String, Object>());
				DataModelManager.getInstance().addDestination(((IFile) getSource()).getProject().getFile(IModelConstants.LISTENERS_FILE_PATH), createListener);
				createListener.start();
				System.out.println("Reply to Listener Created....");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}
