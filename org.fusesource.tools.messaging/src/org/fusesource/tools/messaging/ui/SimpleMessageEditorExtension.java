// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.MessagePackage;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.message.editors.MessageEditorConstants;
import org.fusesource.tools.message.editors.MessageEditorPageBean;
import org.fusesource.tools.message.extensions.IMessageEditorExtension;
import org.fusesource.tools.messaging.IConstants;


/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
public class SimpleMessageEditorExtension implements IMessageEditorExtension {

	protected int count = -1;

	protected TableViewer propertiesTableViewer;

	protected Composite propertiesComposite = null;

	protected EditingDomain editingDomain;

	protected Message messageModel;

	public void createBody(Composite parent, EditingDomain editingDomain,
			Message messageModel) {
		parent.setLayout(new GridLayout(1, false));
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		label.setText("This message type does not support body.  Please configure headers in the Headers tab.");
	}

	public Collection<MessageEditorPageBean> getEditorPages(Composite container, EditingDomain editingDomain,
			Message messageModel) {
		this.editingDomain = editingDomain;
		this.messageModel = messageModel;
		List<MessageEditorPageBean> list = new ArrayList<MessageEditorPageBean>();
		list.add(new MessageEditorPageBean(MessageEditorConstants.HEADER,
				createPropertiesTab(container, editingDomain)));
		return list;
	}

	protected Composite createPropertiesTab(Composite parent,
			final EditingDomain editingDomain) {
		propertiesComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		propertiesComposite.setLayout(layout);
		layout.numColumns = 1;

		Group propertiesGroup = new Group(propertiesComposite, SWT.NONE);
		propertiesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		propertiesGroup.setLayout(new GridLayout(3, false));
		propertiesGroup.setText("Headers");

		propertiesTableViewer = new DefaultMessageTableViewer(propertiesGroup, SWT.FULL_SELECTION,
				IConstants.EMPTY_STRING);
		propertiesTableViewer.addFilter(new PropertyFilter());
		buildTable(propertiesTableViewer, propertiesGroup, editingDomain, false);

		if (messageModel != null && messageModel.getProperties() != null) {
			propertiesTableViewer.setInput(messageModel.getProperties());
		}
		return propertiesComposite;
	}

	protected void buildTable(final TableViewer tableViewer, Group group, final EditingDomain editingDomain,
			boolean isHeader) {
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.verticalAlignment = SWT.FILL;
		((DefaultMessageTableViewer) tableViewer).refreshTable("Name;Value");
		Table table = tableViewer.getTable();
		table.setLayoutData(data);
		table.setLinesVisible(true);

		final CellEditor[] editors = new CellEditor[2];
		editors[0] = new TextCellEditor(table);
		editors[1] = new TextCellEditor(table);
		editors[1].addListener(new ICellEditorListener() {
			public void applyEditorValue() {
			}

			public void cancelEditor() {
			}

			public void editorValueChanged(boolean oldValidState, boolean newValidState) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Property property = (Property) selection.getFirstElement();
				modifyProperty((String) editors[1].getValue(), property,
						MessagePackage.eINSTANCE.getProperty_Value());
			}
		});

		editors[0].addListener(new ICellEditorListener() {
			public void applyEditorValue() {
			}

			public void cancelEditor() {
			}

			public void editorValueChanged(boolean oldValidState, boolean newValidState) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Property property = (Property) selection.getFirstElement();
				modifyProperty((String) editors[0].getValue(), property,
						MessagePackage.eINSTANCE.getProperty_Name());
			}
		});

		tableViewer.setColumnProperties(new String[] { "Name", "Value" });
		tableViewer.setCellModifier(new HeadersModifierSupport(tableViewer));
		tableViewer.setCellEditors(editors);

		Composite buttonComposite = new Composite(group, SWT.NONE);
		data = new GridData();
		data.horizontalSpan = 1;
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = SWT.FILL;
		buttonComposite.setLayoutData(data);

		GridLayout gridLayout = new GridLayout();
		buttonComposite.setLayout(gridLayout);
		gridLayout.numColumns = 1;
		propertyOperations(tableViewer, buttonComposite);
		tableViewer.setContentProvider(new HeadersContentProvider());
		tableViewer.setLabelProvider(new HeadersLabelProvider());
	}

	/**
	 * 
	 * @param tableViewer
	 * @param buttonComposite
	 */
	protected void propertyOperations(final TableViewer tableViewer, Composite buttonComposite) {
		GridData data;
		final Button addProperty = new Button(buttonComposite, SWT.PUSH);
		addProperty.setText("  Add  ");
		data = new GridData();
		data.horizontalSpan = 1;
		data.verticalAlignment = SWT.TOP;
		addProperty.setLayoutData(data);

		final Button deleteProperty = new Button(buttonComposite, SWT.PUSH);
		deleteProperty.setText("Delete");
		data = new GridData();
		data.verticalAlignment = SWT.TOP;
		data.horizontalSpan = 1;
		deleteProperty.setLayoutData(data);

		addProperty.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.widget == addProperty) {
					createNewDefaultHeader(getMessageModel().getProperties());
					tableViewer.refresh();
					propertiesTableViewer.refresh();
				}
			}
		});

		deleteProperty.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.widget == deleteProperty) {
					int selectionIndex = tableViewer.getTable().getSelectionIndex();

					Object elementAt = tableViewer.getElementAt(selectionIndex);
					if (elementAt instanceof Property) {
						Property property = (Property) elementAt;
						deleteProperty(property);
						tableViewer.refresh();
					}
				}
			}
		});
	}

	/**
	 * 
	 * @param properties
	 */
	protected void createNewDefaultHeader(Properties properties) {
		Collection<?> newChildDescriptors = getEditingDomain().getNewChildDescriptors(properties, null);
		CommandParameter commandParameter = null;
		for (Object object : newChildDescriptors) {
			Object value = ((CommandParameter) object).getValue();
			if (value instanceof Property) {
				commandParameter = (CommandParameter) object;
				Property property = (Property) value;
				property.setName("new_name_" + ++count);
				property.setValue("new_value_" + count);
				property.setIsheader(false);
				break;
			}
		}
		Command create = CreateChildCommand.create(getEditingDomain(), properties, commandParameter,
				new StructuredSelection(properties).toList());
		getEditingDomain().getCommandStack().execute(create);
		propertiesTableViewer.refresh();
	}

	/**
	 * modify the property name in the model
	 * 
	 * @param name
	 * @param property
	 * @param attribute
	 */
	protected void modifyProperty(String value, Property property, EAttribute attribute) {
		Command command = SetCommand.create(getEditingDomain(), property, attribute, value);
		getEditingDomain().getCommandStack().execute(command);
	}

	/**
	 * @param property
	 */
	protected void deleteProperty(Property property) {
		Command create = DeleteCommand.create(getEditingDomain(), property);
		editingDomain.getCommandStack().execute(create);
	}

	/**
	 * @return the editingDomain
	 */
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * @return the messageModel
	 */
	public Message getMessageModel() {
		return messageModel;
	}
	
	protected class PropertyFilter extends ViewerFilter {
		
		public PropertyFilter(){}
		
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof Property) {
				Property property = (Property) element;
				if (!property.isIsheader())
					return true;
			}
			return false;
		}
	}
}
