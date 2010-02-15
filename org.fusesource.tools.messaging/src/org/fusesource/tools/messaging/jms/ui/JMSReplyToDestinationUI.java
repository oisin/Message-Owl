package org.fusesource.tools.messaging.jms.ui;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.fusesource.tools.core.util.ResourceUtil;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.cnf.model.DataModelManager;
import org.fusesource.tools.messaging.cnf.model.IModelConstants;
import org.fusesource.tools.messaging.cnf.model.Listeners;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IDestinationType;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.jms.ReplyToInfo;
import org.fusesource.tools.messaging.server.MessagingServersUtil;
import org.fusesource.tools.messaging.ui.DestinationUtil;


public class JMSReplyToDestinationUI {

	protected Text replyToTxt;

	protected ReplyToInfo replyToInfo = null;

	protected Object source;

	protected IProvider provider;

	protected Map<String, Object> senderProperties;

	protected JMSSimpleMessageEditorExtension editorExtension;

	public JMSReplyToDestinationUI(JMSSimpleMessageEditorExtension editorExtension) {
		this.editorExtension = editorExtension;

	}

	public JMSReplyToDestinationUI(Object source, IProvider provider, Map<String, Object> senderProperties) {
		this.source = source;
		this.provider = provider;
		this.senderProperties = senderProperties;
	}

	protected void createReplyToDestination() {
		if (replyToInfo != null && replyToInfo.isNewDest()) {
			IDestination createDestination = DestinationUtil.createDestination(replyToInfo.getType(), replyToInfo.getName(), provider);
			try {
				IListener createListener = createDestination.createListener(new HashMap<String, Object>());
				DataModelManager.getInstance().addDestination(((IFile) source).getProject().getFile(IModelConstants.LISTENERS_FILE_PATH),
						createListener);
				createListener.start();
				System.out.println("Reply to Listener Created....");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void createReplyToSection(Composite composite) {
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		Group replyToGrp = new Group(composite, SWT.NONE);
		replyToGrp.setText("ReplyTo Destination");
		replyToGrp.setLayout(new FillLayout());
		replyToGrp.setLayoutData(data);

		GridLayout layout = new GridLayout();
		replyToGrp.setLayout(layout);
		layout.numColumns = 4;

		Label label = new Label(replyToGrp, SWT.NONE);
		label.setText("ReplyTo Destination:");
		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;

		replyToTxt = new Text(replyToGrp, SWT.BORDER);
		replyToTxt.setLayoutData(data);
		if (replyToInfo != null)
			replyToTxt.setText(replyToInfo.getName());
		replyToTxt.setEnabled(false);

		final Button destinationChooser = new Button(replyToGrp, SWT.PUSH);
		destinationChooser.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		destinationChooser.setText("...");

		Button resetReplyTo = new Button(replyToGrp, SWT.PUSH);
		resetReplyTo.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		resetReplyTo.setText("Reset");
		
		resetReplyTo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				performResetReplyTo();
			}


			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});		
		
		destinationChooser.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				launchDestinationChooser();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});

		replyToTxt.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				senderProperties.put(JMSConstants.JMSREPLY_TO, replyToTxt.getText().trim());
			}
		});
	}

	protected void launchDestinationChooser() {
		try {
			Listeners listeners = null;
			List<IDestinationType> destinationTypes = null;
			IProject project = getProject();
			if (project != null)
				listeners = DataModelManager.getInstance().loadListeners(project.getFile(File.separator + IModelConstants.LISTENERS_FILE_PATH));
			if (getProvider() != null)
				destinationTypes = getProvider().getDestinationTypes();
			DestinationChooserDialog chooserDestinationDialog = new DestinationChooserDialog(this, destinationTypes, listeners);
			int input = chooserDestinationDialog.open();
			if (input == TitleAreaDialog.OK && editorExtension != null) {
				editorExtension.setReplyToDest(replyToInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected IProject getProject() {
		IFile file = (IFile) getSource();
		if (file == null)
			return null;
		return file.getProject();
	}

	public Object getSource() {
		if (source == null) {
			source = ResourceUtil.getCurrentIFile();
		}
		return source;
	}

	public IProvider getProvider() {
		if (provider == null) {
			provider = MessagingServersUtil.getProvider((MessagingServersUtil.getDeployedServer(getProject())));
		}
		return provider;
	}

	public void setReplyToInfo(ReplyToInfo replyToInfo) {
		if (replyToInfo == null)
			return;
		this.replyToInfo = replyToInfo;
		replyToTxt.setText(this.replyToInfo.toString());
		if (senderProperties != null)
			senderProperties.put(JMSConstants.JMSREPLY_TO, replyToInfo);
	}

	public ReplyToInfo getReplyToInfo() {
		return replyToInfo;
	}

	public void setReplyToTxt(String replyToText) {
		replyToTxt.setText(replyToText);
	}

	protected void performResetReplyTo() {
		if (senderProperties != null)
			senderProperties.put(JMSConstants.JMSREPLY_TO, null);
		replyToInfo = null;
		replyToTxt.setText("");
		if (editorExtension != null)
			editorExtension.setReplyToDest(replyToInfo);
	}
}
