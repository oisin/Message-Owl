// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging;

import java.util.Collections;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.wst.server.ui.ServerUIUtil;
import org.fusesource.tools.messaging.server.MessagingServersUtil;


/**
 * First page while creating a new Messaging Project
 * 
 * @author kiranb
 * 
 */
public class MsgProjectCreationPage extends WizardNewProjectCreationPage {
	private String selectedServerId;
	private Map<String, String> msgServersIdNameMap = Collections.emptyMap();

	public MsgProjectCreationPage(String pageName) {
		super(pageName);
		setTitle("Create a Messaging Project");
		setDescription("Create a new Messaging Project");
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		createMessagingServersGroup((Composite) getControl());
		Dialog.applyDialogFont(getControl());
		Composite composite = (Composite) getControl();
		composite.layout();
	}

	public void createMessagingServersGroup(Composite composite) {
		Group serversGroup = new Group(composite, SWT.NONE);
		serversGroup.setFont(composite.getFont());
		serversGroup.setText("Messaging Servers");
		serversGroup
				.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		serversGroup.setLayout(new GridLayout(3, false));
		Label servers = new Label(serversGroup, SWT.NONE);
		servers.setText("Choose Server:");

		final Combo serversCombo = new Combo(serversGroup, SWT.READ_ONLY);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		serversCombo.setLayoutData(data);
		populateServerNames(serversCombo, false);
		serversCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				setSelectedServerId(serversCombo.getItem(serversCombo
						.getSelectionIndex()));
				setPageComplete(validate() && isPageComplete());
			}
		});

		Button newServer = new Button(serversGroup, SWT.NONE);
		newServer.setData(new GridData(SWT.RIGHT));
		newServer.setText("New Server...");
		newServer.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				boolean isServerCreated = ServerUIUtil.showNewServerWizard(
						Display.getDefault().getActiveShell(),
						IConstants.FUSE_PRJ_MODULE_ID, null, null);
				if (isServerCreated) {
					populateServerNames(serversCombo, true);
					// TODO optimize here, to get the newly added server only...
					if (serversCombo.getItemCount() > 0) {
						serversCombo.select(0);
						setSelectedServerId(serversCombo.getItem(serversCombo.getSelectionIndex()));
					}
				}
			}

		});
	}

	private void populateServerNames(Combo serversCombo, boolean getUpdatedList) {
		if (msgServersIdNameMap.isEmpty() || getUpdatedList)
			msgServersIdNameMap = MessagingServersUtil.getMsgServersNameIdMap();
		serversCombo.setItems(msgServersIdNameMap.values().toArray(
				new String[] {}));
	}

	// TODO Add required validation here
	public boolean validate() {
		// if (selectedServerId == null) {
		// if (!(getProjectName() == null || getProjectName().trim().length() ==
		// 0)) {
		// if (hasMsgServers)
		// setErrorMessage("Please choose a Messaging server");
		// else
		// setErrorMessage("Please create and choose a Messaging server");
		// }
		// return true;
		// }
		setErrorMessage(null);
		return true;
	}

	public String getSelectedServerId() {
		return selectedServerId;
	}

	public void setSelectedServerId(String selectedServerId) {
		this.selectedServerId = selectedServerId;
	}
}
