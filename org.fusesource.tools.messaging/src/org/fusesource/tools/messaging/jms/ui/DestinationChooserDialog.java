// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.fusesource.tools.messaging.cnf.model.Listeners;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IDestinationType;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.jms.ReplyToInfo;


/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
public class DestinationChooserDialog extends TitleAreaDialog {

	private Combo destChooserCombo = null;

	private Combo destTypesCombo = null;

	protected Composite panel = null;

	private List<IListener> listenerList = null;

	private HashMap<String, HashMap<String, IListener>> typeDestinationsMap = new HashMap<String, HashMap<String, IListener>>();

	private List<IDestinationType> destinationTypeList;

	private JMSReplyToDestinationUI replyToDest;

	public DestinationChooserDialog(JMSReplyToDestinationUI senderDestinationDialog,
			List<IDestinationType> destinationTypeList, Listeners listeners) {
		super(Display.getCurrent().getActiveShell());
		this.replyToDest = senderDestinationDialog;
		this.destinationTypeList = destinationTypeList;
		buildTypeDestinationsMap(listeners);
	}

	private void buildTypeDestinationsMap(Listeners listeners) {
		if (listeners != null && listeners.getListeners() != null) {
			listenerList = listeners.getListeners();
			if (destinationTypeList != null) {
				for (IDestinationType destinationType : destinationTypeList) {
					for (IListener listener : listenerList) {
						IDestination destination = listener.getDestination();
						if (destination != null
								&& destinationType.getType().equals(destination.getDestinationType().getType())) {
							HashMap<String, IListener> map = typeDestinationsMap.get(destinationType.getType());
							if (map == null) {
								map = new HashMap<String, IListener>();
							}
							map.put(destination.getDestinationName(), listener);
							typeDestinationsMap.put(destinationType.getType(), map);
						}
					}
				}
			}
		}
	}

	@Override
	public void create() {
		super.create();
		Shell shell = getShell();
		shell.setText(getDialogTitle());
		setTitle(getTitle());
		setMessage(getMessage());
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);
		panel = new Composite((Composite) control, SWT.NONE);

		GridData data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		panel.setLayout(new GridLayout());
		panel.setLayoutData(data);
		createUI();
		return panel;
	}

	protected void createUI() {
		Group destGroup = new Group(panel, SWT.NONE);
		destGroup.setText("Destination Details");
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		destGroup.setLayout(layout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		destGroup.setLayoutData(gridData);

		gridData = new GridData();
		Label destTypeLabel = new Label(destGroup, SWT.NONE);
		destTypeLabel.setText("Destination Type:");
		destTypeLabel.setLayoutData(gridData);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		destTypesCombo = new Combo(destGroup, SWT.READ_ONLY);
		destTypesCombo.setLayoutData(gridData);
		if (destinationTypeList != null) {
			ArrayList<String> arrayList = new ArrayList<String>();
			for (IDestinationType destinationType : destinationTypeList) {
				arrayList.add(destinationType.getType());
			}
			destTypesCombo.setItems(arrayList.toArray(new String[arrayList.size()]));
		}

		gridData = new GridData();
		Label destNameLabel = new Label(destGroup, SWT.NONE);
		destNameLabel.setText("Destination Name:");
		destNameLabel.setLayoutData(gridData);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		destChooserCombo = new Combo(destGroup, SWT.NULL);
		destChooserCombo.setLayoutData(gridData);
		destChooserCombo.setFocus();

		destTypesCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				Combo combo = (Combo) arg0.getSource();
				populateDestinationNames(combo.getItem(combo.getSelectionIndex()));
			}
		});
		populateDefaults();
	}

	private void populateDefaults() {
		ReplyToInfo replyToInfo = replyToDest.getReplyToInfo();
		if(replyToInfo!=null){
			destTypesCombo.setText(replyToInfo.getType().getType());
			destChooserCombo.setText(replyToInfo.getName());
			return;
		}
		
		// Set Default for the dialog...
		if (destTypesCombo.getItemCount() > 0) {
			destTypesCombo.select(0);
			populateDestinationNames(destTypesCombo.getItem(destTypesCombo.getSelectionIndex()));
		}
	}
	
	private void populateDestinationNames(String destType) {
		HashMap<String, IListener> keys = typeDestinationsMap.get(destType);
		if (keys != null) {
			Set<String> keySet = keys.keySet();
			if (keySet != null && keySet.size() > 0)
				destChooserCombo.setItems(keySet.toArray(new String[keySet.size()]));
		} else {
			destChooserCombo.removeAll();
		}
	}
	
	protected String getMessage() {
		return "Choose an existing destination or enter a new destination name";
	}

	protected String getTitle() {
		return "Choose Destination";
	}

	protected String getDialogTitle() {
		return "Choose Destination";
	}

	@Override
	protected void okPressed() {
		String replyType = destTypesCombo.getItem(destTypesCombo.getSelectionIndex());
		int destinationIndex = destChooserCombo.getSelectionIndex(); 
		String replyName = destinationIndex >= 0 ? destChooserCombo.getItem(destinationIndex) : destChooserCombo
				.getText();
		super.okPressed();
		// check for invalid entries...
		if (replyType == null || replyType.trim().length() == 0 || replyName == null || replyName.trim().length() == 0)
			return;
		
		ReplyToInfo replyToInfo = new ReplyToInfo();
		replyToInfo.setName(replyName);
		for (IDestinationType destinationType : destinationTypeList) {
			if (destinationType.getType().equals(replyType)) {
				replyToInfo.setType(destinationType);
				break;
			}
		}
		
		// Figure out whether it is a new replyTo destination
		HashMap<String, IListener> map = typeDestinationsMap.get(replyType);
		if (map == null) {
			replyToInfo.setNewDest(true);
		} else if (map.get(replyName) == null) {
			replyToInfo.setNewDest(true);
		} else {
			replyToInfo.setNewDest(false);
		}
		replyToDest.setReplyToInfo(replyToInfo);
	}

}
