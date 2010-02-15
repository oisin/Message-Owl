// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.message.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.fusesource.tools.core.message.Body;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.MessagePackage;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.message.editors.MessageEditorConstants;
import org.fusesource.tools.message.editors.MessageEditorPageBean;
import org.fusesource.tools.message.extensions.IMessageEditorExtension;
import org.fusesource.tools.message.extensions.IMessageTypeUI;
import org.fusesource.tools.message.presentation.MessageEditor;
import org.fusesource.tools.message.utils.EMFUtil;
import org.fusesource.tools.message.utils.MessageExtensionsMgr;


/**
 * 
 * @author sgupta
 * 
 */
public class MessageEditorHelper {

	private Composite bodyComposite = null;

	private Message messageModel = null;

	private Composite bodyContentComposite = null;

	private EditingDomain editingDomain;

	private Composite container;

	private Collection<MessageEditorPageBean> editorPages;

	private MessageEditor editor;

	public Collection<MessageEditorPageBean> getPages(Composite container, EditingDomain editingDomain, Message message, MessageEditor editor) {
		this.editingDomain = editingDomain;
		this.container = container;
		this.messageModel = message;
		this.editor = editor;
		editorPages = new ArrayList<MessageEditorPageBean>();
		Composite bodyUI = createBodyUI();
		// Create a body page
		editorPages.add(new MessageEditorPageBean(MessageEditorConstants.MESSAGE_BODY_PAGE_NAME, bodyUI));
		addRequiredPages();
		return editorPages;
	}

	public Composite createBodyUI() {
		bodyComposite = new Composite(container, SWT.NONE);
		bodyComposite.setLayout(new GridLayout(5, false));

		GridData data = new GridData();
		Label messageType = new Label(bodyComposite, SWT.NONE);
		messageType.setText("Message Type:");
		data.horizontalSpan = 2;
		messageType.setLayoutData(data);

		data = new GridData();
		data.horizontalSpan = 3;
		final Combo messageTypeCombo = new Combo(bodyComposite, SWT.READ_ONLY);
		List<String> messageTypes = EMFUtil.getDisplayMessageTypes();
		messageTypeCombo.setItems(messageTypes.toArray(new String[messageTypes.size()]));
		messageTypeCombo.setLayoutData(data);

		disposeAndCreateBodyContentComposite();
		String type = messageModel.getType();

		// convert the type in the format of ${TYPE(ProviderName)
		String displayType = EMFUtil.formatDisplayType(type, messageModel.getProviderId());
		messageTypeCombo.select(messageTypes.indexOf(displayType));

		messageTypeCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				editorPages.clear();
				refreshAll(messageTypeCombo);
				editor.addNewPagesToEditor(editorPages);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		return bodyComposite;
	}

	private void refreshAll(final Combo messageTypeCombo) {
		reset();
		disposeAndCreateBodyContentComposite();
		String type = messageTypeCombo.getItem(messageTypeCombo.getSelectionIndex());
		EMFUtil.loadHeadersAndMessgeAttributes(type, messageModel, editingDomain);
		addRequiredPages();
		bodyComposite.layout();
		refreshBodyUI();
	}

	private void addRequiredPages() {
		Collection<MessageEditorPageBean> contentComposite = createBodyContentComposite();
		if (contentComposite != null)
			editorPages.addAll(contentComposite);
	}

	private void reset() {
		ArrayList<Command> arrayList = new ArrayList<Command>();
		Properties properties = messageModel.getProperties();
		Body body = messageModel.getBody();
		MessagePackage einstance = MessagePackage.eINSTANCE;
		if (properties != null) {
			EList<Property> property = properties.getProperty();
			if (property != null && !property.isEmpty())
				arrayList.add(RemoveCommand.create(editingDomain, properties, einstance.getProperties(),
						new StructuredSelection(property).toList()));
		}
		if (body != null){
			arrayList.add(RemoveCommand.create(editingDomain, messageModel, einstance.getBody(),
					new StructuredSelection(body).toList()));
		}
		if (arrayList.size() > 0) {
			CompoundCommand deleteHeaders = new CompoundCommand(arrayList);
			editingDomain.getCommandStack().execute(deleteHeaders);
		}
	}

	private void disposeAndCreateBodyContentComposite() {
		if (bodyContentComposite != null) {
			bodyContentComposite.dispose();
		}
		bodyContentComposite = new Composite(bodyComposite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 5;
		bodyContentComposite.setLayoutData(data);
	}

	private Collection<MessageEditorPageBean> createBodyContentComposite() {
		String type = messageModel.getType();
		String providerId = messageModel.getProviderId();
		IMessageTypeUI messageTypeUI = MessageExtensionsMgr.getInstance().getMessageTypeUIExtension(type, providerId);
		if (messageTypeUI != null) {
			IMessageEditorExtension editorExtension = messageTypeUI.getEditorExtension();
			editorExtension.createBody(bodyContentComposite, editingDomain, messageModel);
			return editorExtension.getEditorPages(container, editingDomain, messageModel);
		}
		return null;
	}

	public void refreshBodyUI() {
		bodyContentComposite.layout();
	}

}
