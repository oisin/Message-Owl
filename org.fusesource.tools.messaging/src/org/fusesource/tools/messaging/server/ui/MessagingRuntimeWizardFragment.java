// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.server.ui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.eclipse.wst.server.ui.wizard.WizardFragment;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.server.MessagingServerConfiguration;
import org.fusesource.tools.messaging.server.MessagingServerConfigurationFactory;
import org.fusesource.tools.messaging.server.MessagingServersUtil;
import org.fusesource.tools.messaging.utils.ImagesUtil;


public class MessagingRuntimeWizardFragment extends WizardFragment {
	private static final String ERROR_DESCRIPTION = "Please enter all the connection details.";
	private static final String MESSAGE_DESCRIPTION = "Enter connection details. Ensure that you start FUSE Messaging Broker manually before trying to connect to it.";
	protected Map<String, String> connectionParams = Collections.emptyMap();
	private Composite dynamicComp;
	protected Text textComponent;
	private IWizardHandle wizardHandle = null;
	private Button autoConnect = null;

	public MessagingRuntimeWizardFragment() {
		setComplete(false);
	}

	private void initConnectParams() {
		try {
			connectionParams = getServerConfiguration().getProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Composite createComposite(Composite parent, IWizardHandle wizard) {
		try {
			initConnectParams();
			wizardHandle = wizard;
			//TODO: Extend this wizard from fusejms plugin and set FUSE specific title/desc
			wizard.setTitle("FUSE Messaging Broker");
			wizard.setDescription(MESSAGE_DESCRIPTION);
			wizard.setImageDescriptor(ImagesUtil.getInstance().getImageDescriptor("icons/fuse_broker_big.gif"));
			createControl(parent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dynamicComp;
	}

	protected void createControl(Composite parent) {

		if (connectionParams.isEmpty())
			return;

		dynamicComp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		dynamicComp.setLayout(layout);
		GridData data = null;

		Set<String> keys = connectionParams.keySet();
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			String key = (String) i.next();
			String value = (String) connectionParams.get(key);
			Label label = new Label(dynamicComp, SWT.NONE);
			if (key != null && key.trim().length() > 0)
				label.setText(key + ":");
			data = new GridData();
			data.horizontalSpan = 1;
			label.setLayoutData(data);
			if (key != null) {
				if ("password".equalsIgnoreCase(key) || key.contains("Password") || key.contains("password"))
					textComponent = new Text(dynamicComp, SWT.BORDER | SWT.PASSWORD);
			 else 
				textComponent = new Text(dynamicComp, SWT.BORDER);
			}

			textComponent.setData(key);
			textComponent.setText(value);

			data = new GridData();
			data.horizontalSpan = 2;
			data.grabExcessHorizontalSpace = true;
			data.horizontalAlignment = SWT.FILL;
			textComponent.setLayoutData(data);
			textComponent.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					validatePage();
				}
			});
		}
		
		autoConnect = new Button(dynamicComp, SWT.CHECK);
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		layoutData.horizontalSpan = 3;
		autoConnect.setLayoutData(layoutData);
		autoConnect.setText("Auto connect if Server is running");
		autoConnect.setSelection(true);
	}

	@Override
	public boolean hasComposite() {
		return true;
	}

	@Override
	public void enter() {
		validatePage();
	}

	private boolean validatePage() {
		boolean isValid = true;
		try {
			if (dynamicComp != null && !dynamicComp.isDisposed()) {
				Control[] controls = dynamicComp.getChildren();
				for (int i = 0; i < controls.length; i++) {
					if (controls[i] instanceof Text) {
						if (((Text) controls[i]).getText() != null && ((Text) controls[i]).getText().length() <= 0) {
							isValid = false;
						}
					}
				}
			} else {
				isValid = false;
			}
		} catch (Exception e1) {
			isValid = false;
		}
		setComplete(isValid);
		if (isValid) {
			wizardHandle.setMessage(MESSAGE_DESCRIPTION, IStatus.OK);
		} else {
			wizardHandle.setMessage(ERROR_DESCRIPTION, IStatus.ERROR);
		}
		return isValid;
	}

	private MessagingServerConfiguration getServerConfiguration() throws Exception {
		return MessagingServerConfigurationFactory.getInstance().getConfiguration(getServer());
	}

	private IServer getServer() {
		TaskModel taskModel = getTaskModel();
		IServer server = (IServer) taskModel.getObject(TaskModel.TASK_SERVER);
		return server;
	}

	@Override
	public void performFinish(final IProgressMonitor monitor) throws CoreException {
		super.performFinish(monitor);

		if (connectionParams.isEmpty())
			return;
		Display.getDefault().syncExec(new Thread() {
			public void run() {
				try {
					Control[] controls = dynamicComp.getChildren();
					Set<String> keys = connectionParams.keySet();
					String key = IConstants.EMPTY_STRING;
					HashMap<String, String> connectionProps = new HashMap<String, String>();
					for (int i = 0; i < controls.length; i++) {
						if (controls[i].getData() != null) {
							for (Iterator<String> keysIter = keys.iterator(); keysIter.hasNext();) {
								key = (String) keysIter.next();
								if (controls[i].getData().toString().equals(key)) {
									connectionProps.put(key, ((Text) controls[i]).getText());
								}
							}
						}
					}
					MessagingServerConfiguration serverConfiguration = getServerConfiguration();
					serverConfiguration.setProperties(connectionProps);
					serverConfiguration.setAutoConnect(autoConnect.getSelection());
					
					if(autoConnect.getSelection())
						MessagingServersUtil.connectToServer(getServer()); 
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
