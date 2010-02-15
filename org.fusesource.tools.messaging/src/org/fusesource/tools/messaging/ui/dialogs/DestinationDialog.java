// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.ui.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.ServerUIUtil;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IDestinationType;
import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.server.MessagingServersUtil;
import org.fusesource.tools.messaging.ui.DefaultMessageTableViewer;
import org.fusesource.tools.messaging.ui.DestinationCellModifier;
import org.fusesource.tools.messaging.ui.DestinationTableContentProvider;
import org.fusesource.tools.messaging.ui.DestinationTableLabelProvider;
import org.fusesource.tools.messaging.ui.DestinationUtil;


public abstract class DestinationDialog extends TitleAreaDialog {

	private TableViewer tableViewer = null;
	private static String NAME_PROPERTY = "name";
	private static String VALUE_PROPERTY = "value";
	private static String COLUMN_HEADERS_STRING = "Name;Value";

	public static String DESTINATION_UI_EXT_PT = "org.fusesource.tools.messaging.DestinationUI";
	public static String ISENDER_ATTRIBUTE = "Sender";
	public static String ILISTENER_ATTRIBUTE = "Listener";

	protected Map<String, Object> senderProperties = new HashMap<String, Object>();
	protected Map<String, Object> listenerProperties = new HashMap<String, Object>();

	private Map<String, String> msgServersNameIdMap = Collections.emptyMap();
	private Map<String, IDestinationType> destTypeMap = new HashMap<String, IDestinationType>();
	private Object source;// TODO source is an IFile for now...

	private String destType;

	private String destName;

	protected Combo serversCombo;

	protected Combo destTypesCombo;

	private Text destNameText;

	private String selectedServerName;

	private static final String CLOSED_ADVANCED = "Advanced >>";

	private static final String OPENED_ADVANCED = "<< Advanced";

	private Button advancedButton;

	private Composite advancedComposite;

	protected Composite panel = null;

	protected Button newServerButton = null;

	private boolean deployed = false;

	protected IDestination createdDestination;

	protected IProvider provider = null;

	public DestinationDialog() {
		super(Display.getCurrent().getActiveShell());
	}

	@Override
	public void create() {
		super.create();
		Shell shell = getShell();
		shell.setText(getDialogTitle());
		setTitle(getTitle());
		setMessage(getMessage());
		validate();
	}

	protected abstract String getMessage();

	protected abstract String getTitle();

	protected abstract boolean hasAdvanceSection();

	protected String getDialogTitle() {
		return "Add Destination";
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
		Label name = new Label(destGroup, SWT.NONE);
		name.setText("Server:");
		name.setLayoutData(gridData);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		serversCombo = new Combo(destGroup, SWT.READ_ONLY);
		serversCombo.setLayoutData(gridData);

		gridData = new GridData();
		gridData.horizontalSpan = 1;
		newServerButton = new Button(destGroup, SWT.PUSH);
		newServerButton.setText("New Server...");
		newServerButton.setLayoutData(gridData);
		newServerButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				boolean isServerCreated = ServerUIUtil.showNewServerWizard(Display.getDefault().getActiveShell(),
						IConstants.FUSE_PRJ_MODULE_ID, null, null);
				if (isServerCreated) {
					populateServerNames(true, false);
					// TODO optimize here, to get the newly added server only...
					if(serversCombo.getItemCount()>0)
						serversCombo.select(0);//Default selection
				}
			}
		});

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

		gridData = new GridData();
		Label destNameLabel = new Label(destGroup, SWT.NONE);
		destNameLabel.setText("Destination Name:");
		destNameLabel.setLayoutData(gridData);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		destNameText = new Text(destGroup, SWT.BORDER);
		destNameText.setLayoutData(gridData);

		if (hasAdvanceSection())
			showAdvanceSection(destGroup);
		addListeners();
		populateServerNames(false, false);
		populateDestinationTypes();
		destNameText.setFocus();
		validate();
	}

	private void addListeners() {
		serversCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				initServersComboData();
			}
		});

		serversCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				initServersComboData();
			}
		});
		destNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		destTypesCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				validate();
			}

		});
	}

	protected void createAdvancedUI(Composite composite) {
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
		tableViewer.setCellModifier(new DestinationCellModifier(tableViewer, senderProperties));// sender
																								// props??
		tableViewer.setColumnProperties(new String[] { NAME_PROPERTY, VALUE_PROPERTY });

		tableViewer.setContentProvider(new DestinationTableContentProvider());
		tableViewer.setLabelProvider(new DestinationTableLabelProvider());
		tableViewer.setInput(senderProperties);// sender props??
		tableViewer.refresh();

	}

	protected String getAdvTitle() {
		return "Destination Properties";
	}

	private void initServersComboData() {
		int selectionIndex = serversCombo.getSelectionIndex();
		if (selectionIndex != -1) {
			setSelectedServerName(serversCombo.getItem(selectionIndex));
			populateDestinationTypes();
			validate();
		}
	}

	private void showAdvanceSection(Composite destGroup) {
		if (hasAdvanceSection()) {
			GridData data = new GridData();
			data.horizontalSpan = 3;
			data.horizontalAlignment = SWT.END;
			data.grabExcessHorizontalSpace = true;
			advancedButton = new Button(destGroup, SWT.PUSH);
			advancedButton.setText(CLOSED_ADVANCED);
			advancedButton.setLayoutData(data);
			advancedButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					handleAdvancedButtonSelect();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					handleAdvancedButtonSelect();
				}
			});

		}
	}

	protected void handleAdvancedButtonSelect() {
		Shell shell = getShell();
		Point shellSize = shell.getSize();
		Composite composite = (Composite) getContents();

		if (advancedButton.getText().equalsIgnoreCase(CLOSED_ADVANCED)) {
			advancedComposite = new Composite(panel, SWT.NONE);
			GridData data = new GridData();
			data.horizontalAlignment = SWT.FILL;
			data.grabExcessHorizontalSpace = true;
			advancedComposite.setLayoutData(data);
			createAdvancedUI(advancedComposite);
			panel.layout();
			composite.layout();
			Point advComSize = advancedComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			shell.setSize(shellSize.x, shellSize.y += advComSize.y);
			advancedButton.setText(OPENED_ADVANCED);
		} else if (advancedButton.getText().equalsIgnoreCase(OPENED_ADVANCED)) {
			Point advComSize = advancedComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			shell.setSize(shellSize.x, shellSize.y -= advComSize.y);
			composite.layout();
			advancedButton.setText(CLOSED_ADVANCED);
		}
	}

	protected void populateServerNames(boolean updateMap, boolean deployed) {
		if (msgServersNameIdMap.isEmpty() || updateMap) {
			msgServersNameIdMap = getServersMap();
		}
		// populateDestinationTypes();
		serversCombo.setItems(msgServersNameIdMap.keySet().toArray(new String[] {}));
		if (deployed) {
			newServerButton.setEnabled(false);
			serversCombo.select(0);
			serversCombo.setEnabled(false);
			populateDestinationTypes();
			setSelectedServerName(serversCombo.getItem(serversCombo.getSelectionIndex()));
		}
		// Expecting only one or none.
		IServer deployedServer = MessagingServersUtil.getDeployedServer(((IFile) getSource()).getProject());
		if (deployedServer == null) {
			this.deployed = false;
			return;
		}
		String serverToSelect = deployedServer.getName();
		int indexOf = serversCombo.indexOf(serverToSelect);
		serversCombo.select(indexOf);
		this.deployed = true;
	}

	private Map<String, String> getServersMap() {
		return MessagingServersUtil.getMsgServersNameIdMap();
	}

	protected void populateDestinationTypes() {
		if (getSelectedServerName() == null)
			return;
		try {
			if (getProvider() == null || getProvider().getDestinationTypes() == null) {
				destTypeMap.clear();
				return;
			}
		} catch (Exception e) {
			// ignore...
			destTypeMap.clear();
			return;
		}
		List<IDestinationType> destinationTypes = getProvider().getDestinationTypes();
		List<String> typesList = new ArrayList<String>();
		destTypeMap.clear();
		for (IDestinationType destinationType : destinationTypes) {
			destTypeMap.put(destinationType.getType(), destinationType);
			typesList.add(destinationType.getType());
		}
		destTypesCombo.setItems(typesList.toArray(new String[] {}));
		if (destTypesCombo.getItemCount() > 0) {
			destTypesCombo.select(0);
		}
	}

	protected void updateData() {
		setDestType(destTypesCombo.getItem(destTypesCombo.getSelectionIndex()));
		setDestName(destNameText.getText());
		setSelectedServerName(serversCombo.getItem(serversCombo.getSelectionIndex()));
	}

	@Override
	protected void okPressed() {
		updateData();
		super.okPressed();
		try {
			if (!deployed)
				MessagingServersUtil.deployModule(((IFile) getSource()).getProject(), selectedServerName);
			createdDestination = DestinationUtil.createDestination(getDestinationType(), getDestName(), getProvider());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateServerControls() {
		serversCombo.setEnabled(!deployed);
		newServerButton.setEnabled(!deployed);
	}

	protected void validate() {
		String errorMessage = null;
		if (serversCombo.getSelectionIndex() == -1) {
			errorMessage = "Please choose a server";
		} else if (destTypesCombo.getSelectionIndex() == -1) {
			errorMessage = "Please choose a destination type";
		} else if (destNameText.getText().trim().length() == 0) {
			errorMessage = "Please enter a destination name";
		}

		if (errorMessage != null) {
			setErrorMessage(errorMessage);
		} else {
			setErrorMessage(null);
			setMessage(getMessage());
		}
		
		updateServerControls();
		updateUIControls(errorMessage != null ? false : true);
	}

	protected void updateUIControls(boolean canEnable) {
		if (hasAdvanceSection()) {
			advancedButton.setEnabled(canEnable);
		}
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
		Button button = getButton(IDialogConstants.OK_ID);
		if (button != null) {
			if (newErrorMessage != null)
				button.setEnabled(false);
			else
				button.setEnabled(true);
		}
	}

	public String getSelectedServerName() {
		return selectedServerName;
	}

	public void setSelectedServerName(String serverName) {
		this.selectedServerName = serverName;
	}

	public String getDestName() {
		return destName;
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}

	public IDestinationType getDestinationType() {
		return destTypeMap.get(getDestType());
	}

	public IDestination getCreatedDestination() {
		return createdDestination;
	}

	public String getDestType() {
		return destType;
	}

	public void setDestType(String destType) {
		this.destType = destType;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public IProvider getProvider() {
		if (provider == null) {
			try {
				provider = DestinationUtil.getProvider(getSelectedServerName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return provider;
	}
}
