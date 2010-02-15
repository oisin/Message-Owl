// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.fusesource.tools.core.message.Body;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.core.ui.TextViewerComponent;
import org.fusesource.tools.message.extensions.IMessageType;
import org.fusesource.tools.message.utils.MessageExtensionsMgr;
import org.fusesource.tools.messaging.MessageEvent;
import org.fusesource.tools.messaging.jms.ui.TextContentProvider;
import org.fusesource.tools.messaging.jms.ui.TextLabelProvider;
import org.fusesource.tools.messaging.ui.HeaderPropertyComposite;


public class DefaultMessageDetailPart implements IDetailsPage {

	protected static final int[] WEIGHTS = new int[] { 1, 5 };
	protected Composite displayArea;
	protected SourceViewer textViewer = null;
	protected IManagedForm managedForm;
	protected TreeViewer treeViewer;
	protected Composite parentComposite = null;
	protected Composite rightCompositeHolder = null;
	protected MessageEvent currentMessage;
	protected List<Action> actionsList = new ArrayList<Action>();

	public DefaultMessageDetailPart() {
	}

	public void initialize(IManagedForm managedForm) {
		this.managedForm = managedForm;
	}

	public void createContents(Composite parent) {
		parent.setLayout(new GridLayout());
		FormToolkit toolkit = managedForm.getToolkit();
		Section messageDetailSection = toolkit.createSection(parent, Section.TITLE_BAR | Section.EXPANDED);
		messageDetailSection.setText("Message Details");
		messageDetailSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		messageDetailSection.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		displayArea = toolkit.createComposite(messageDetailSection, SWT.NONE);
		displayArea.setLayout(new GridLayout());
		displayArea.setLayoutData(gridData);
		messageDetailSection.setClient(displayArea);
	}

	public void selectionChanged(IFormPart formPart, ISelection selection) {
		currentMessage = (MessageEvent) ((StructuredSelection) selection)
				.getFirstElement();
		if (displayArea.getChildren().length > 0) {
			(displayArea.getChildren()[0]).dispose();
		}
		if (currentMessage == null) {
			return;
		}
		createViewerUI(displayArea, currentMessage);
		displayArea.layout();
		updateActions(currentMessage);
	}

	public void createViewerUI(Composite composite, MessageEvent messageEvent) {
		parentComposite = composite;
		parentComposite.setLayout(new GridLayout());
		SashForm sashForm = new SashForm(parentComposite, SWT.HORIZONTAL);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.heightHint = 200;
		sashForm.setLayoutData(layoutData);
		createLeftComposite(sashForm);
		createRightComposite(sashForm);
		Message message = getConvertedMessage(messageEvent);
		treeViewer.setInput(message);
		setSelection(message);
		sashForm.setWeights(WEIGHTS);
	}

	/**
	 * fix for selecting the body by default else the first element in tree
	 * UI-440
	 * 
	 * @param message
	 */
	protected void setSelection(Message message) {
		Body body = message.getBody();
		if (body != null && body.getContent() != null)
			
			treeViewer.setSelection(new StructuredSelection(body.getContent()));
		else{
			TreeItem item = treeViewer.getTree().getItem(0);
			if (item != null && item.getData() != null)
				treeViewer
						.setSelection(new StructuredSelection(item.getData()));
		}
	}

	protected Message getConvertedMessage(MessageEvent messageEvent) {
		String id = messageEvent.getSource().getDestination().getConnection()
				.getProvider().getId();
		IMessageType messageType = MessageExtensionsMgr.getInstance()
				.getMessageTypeExtension(messageEvent.getMessage(), id);
		Message message = null;
		try {
			if (messageType != null)
				message = messageType.convertMessage(messageEvent.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	protected void createRightComposite(SashForm sashForm) {
		rightCompositeHolder = new Composite(sashForm, SWT.BORDER);
		rightCompositeHolder.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		rightCompositeHolder.setLayout(new FillLayout());
	}

	protected void createLeftComposite(SashForm sashForm) {
		Composite leftComposite = new Composite(sashForm, SWT.BORDER);
		leftComposite.setLayout(new FillLayout());
		createLeftTree(leftComposite);
	}

	protected void createLeftTree(Composite leftComposite) {
		treeViewer = new TreeViewer(leftComposite, SWT.NONE);
		treeViewer.addSelectionChangedListener(new MySelectionListener());
		setProviders();
	}

	@SuppressWarnings("unchecked")
	protected void getRightCompositeForHeader(Object data) {
		new HeaderPropertyComposite(rightCompositeHolder,
				((List<Property>) data));
	}

	public void getRightComposite(Object selectedNode) {
		if (selectedNode instanceof List) {
			getRightCompositeForHeader(selectedNode);
		} else if (selectedNode instanceof AnyType) {
			getRightCompositeForBody(selectedNode);
		}
	}

	protected void getRightCompositeForBody(Object data) {
		textViewer = TextViewerComponent.createTextViewer(rightCompositeHolder,
				data, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		textViewer.setEditable(false);
	}

	public void setInput(Object model) {
		treeViewer.setInput(model);
	}

	public void commit(boolean arg0) {
	}

	public void dispose() {
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
	}

	public void setFocus() {
	}

	public boolean setFormInput(Object arg0) {
		return false;
	}

	protected void setProviders() {
		treeViewer.setLabelProvider(new TextLabelProvider());
		treeViewer.setContentProvider(new TextContentProvider());
	}

	/**
	 * returns the actions for toolbar
	 */
	public List<Action> getActionsList() {
		return actionsList;
	}

	protected class MySelectionListener implements ISelectionChangedListener {

		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (selection.isEmpty())
				return;
			Object selectedNode = ((IStructuredSelection) selection)
					.getFirstElement();
			if (rightCompositeHolder.getChildren().length > 0) {
				(rightCompositeHolder.getChildren()[0]).dispose();
			}
			getRightComposite(selectedNode);
			rightCompositeHolder.layout();
		}
	}

	protected void updateActions(MessageEvent currentMsg) {

	}

}