/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.ui;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.fusesource.tools.core.message.MessagePackage;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.jms.ReplyToInfo;
import org.fusesource.tools.messaging.ui.DefaultMessageTableViewer;
import org.fusesource.tools.messaging.ui.HeadersContentProvider;
import org.fusesource.tools.messaging.ui.HeadersLabelProvider;
import org.fusesource.tools.messaging.ui.HeadersModifierSupport;
import org.fusesource.tools.messaging.ui.SimpleMessageEditorExtension;


public class JMSSimpleMessageEditorExtension extends SimpleMessageEditorExtension {

	protected TableViewer headerTableViewer;
	
	protected Property replyToProperty = null;
	
	protected JMSReplyToDestinationUI replyToDestinationUI = null;

	protected Composite createPropertiesTab(Composite parent,
			final EditingDomain editingDomain) {
		propertiesComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		propertiesComposite.setLayout(layout);
		layout.numColumns = 1;

		Group headerGroup = new Group(propertiesComposite, SWT.NONE);
		headerGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		headerGroup.setLayout(new GridLayout());
		headerGroup.setText("JMS Headers");
		headerTableViewer = new DefaultMessageTableViewer(headerGroup, SWT.FULL_SELECTION, IConstants.EMPTY_STRING);
		headerTableViewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof Property) {
					Property property = (Property) element;
					if (property.isIsheader() && !JMSConstants.JMSREPLY_TO.equals(property.getName()))
						return true;
				}
				return false;
			}
		});
		buildTable(headerTableViewer, headerGroup, editingDomain, true);
		createReplyToComposite(headerGroup);

		Group propertiesGroup = new Group(propertiesComposite, SWT.NONE);
		propertiesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		propertiesGroup.setLayout(new GridLayout(3, false));
		propertiesGroup.setText("JMS Properties");

		propertiesTableViewer = new DefaultMessageTableViewer(propertiesGroup, SWT.FULL_SELECTION,
				IConstants.EMPTY_STRING);
		propertiesTableViewer.addFilter(new PropertyFilter());
		buildTable(propertiesTableViewer, propertiesGroup, editingDomain, false);

		if (messageModel != null && messageModel.getProperties() !=null ) {
			headerTableViewer.setInput(messageModel.getProperties());
			propertiesTableViewer.setInput(messageModel.getProperties());
			headerTableViewer.refresh();
			propertiesTableViewer.refresh();
			setReplyToProperty();
		}
		return propertiesComposite;
	}

	
	protected void setReplyToDest(ReplyToInfo replyToInfo){
		modifyProperty(replyToInfo == null ? "" : replyToInfo.toString(), replyToProperty, MessagePackage.eINSTANCE.getProperty_Value());
	}


	protected void setReplyToProperty() {
		Properties properties = messageModel.getProperties();
		EList<Property> propertyList = properties.getProperty();
		for (Property prop : propertyList) {
			if(prop.isIsheader() && JMSConstants.JMSREPLY_TO.equals(prop.getName())){
				replyToProperty = prop;
				if (replyToDestinationUI != null) {
					replyToDestinationUI.setReplyToTxt(replyToProperty.getValue());
				}
				break;
			}
		}
	}
	
	protected void createReplyToComposite(Composite parent) {
		Composite replyToComposite = new Composite(parent, SWT.NONE);
		replyToComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		replyToComposite.setLayout(new GridLayout());
		replyToDestinationUI = new JMSReplyToDestinationUI(this);
		replyToDestinationUI.createReplyToSection(replyToComposite);
	}

	protected void buildTable(final TableViewer tableViewer, Group group, final EditingDomain editingDomain,
			boolean isHeader) {
		((DefaultMessageTableViewer) tableViewer).refreshTable("Name;Value");
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		table.setLinesVisible(true);

		final CellEditor[] editors = new CellEditor[2];
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


		tableViewer.setColumnProperties(new String[] { "Name", "Value" });
		tableViewer.setCellModifier(new JMSHeadersModifierSupport(tableViewer));
		tableViewer.setCellEditors(editors);

		if (!isHeader) {
			buildPropertiesSection(tableViewer, group, table, editors);
		}
		tableViewer.setContentProvider(new HeadersContentProvider());
		tableViewer.setLabelProvider(new HeadersLabelProvider());
	}


	protected void buildPropertiesSection(final TableViewer tableViewer, Group group, Table table, final CellEditor[] editors) {
		Composite buttonComposite = new Composite(group, SWT.NONE);
		GridData data = new GridData();
		data.horizontalSpan = 1;
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = SWT.FILL;
		buttonComposite.setLayoutData(data);

		GridLayout gridLayout = new GridLayout();
		buttonComposite.setLayout(gridLayout);
		gridLayout.numColumns = 1;

		propertyOperations(tableViewer, buttonComposite);
		editors[0] = new TextCellEditor(table);
		editors[0].addListener(new ICellEditorListener() {
			public void applyEditorValue() {
			}

			public void cancelEditor() {
			}

			public void editorValueChanged(boolean oldValidState, boolean newValidState) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Property property = (Property) selection.getFirstElement();
				modifyProperty((String) editors[0].getValue(), property, MessagePackage.eINSTANCE.getProperty_Name());
			}
		});
	}

	/**
	 * 
	 * @param properties
	 * @param isHeader
	 */
	protected void createNewDefaultHeader(Properties properties, boolean isHeader) {
		Collection<?> newChildDescriptors = getEditingDomain().getNewChildDescriptors(properties, null);
		CommandParameter commandParameter = null;
		for (Object object : newChildDescriptors) {
			Object value = ((CommandParameter) object).getValue();
			if (value instanceof Property) {
				commandParameter = (CommandParameter) object;
				Property property = (Property) value;
				property.setName("new_name_" + ++count);
				property.setValue("new_value_" + count);
				property.setIsheader(isHeader);
				break;
			}

		}
		Command create = CreateChildCommand.create(getEditingDomain(), properties, commandParameter,
				new StructuredSelection(properties).toList());
		getEditingDomain().getCommandStack().execute(create);
		headerTableViewer.refresh();
		propertiesTableViewer.refresh();
	}

	class HeadersModifyListener extends EContentAdapter {
		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			Object notifier = notification.getNotifier();
			if (notifier instanceof Properties) {
				headerTableViewer.refresh();
			}
		}
	}

	/**
	 * @param property
	 */
	protected void deleteProperty(Property property) {
		Command create = DeleteCommand.create(getEditingDomain(), property);
		editingDomain.getCommandStack().execute(create);
		if (property.isIsheader())
			headerTableViewer.refresh();
		else
			propertiesTableViewer.refresh();
	}
	
	class JMSHeadersModifierSupport extends HeadersModifierSupport {

		public JMSHeadersModifierSupport(Viewer viewer) {
			super(viewer);
		}

		/**
		 * Returns whether the property can be modified
		 * 
		 * @param element
		 *            the element
		 * @param property
		 *            the property
		 * @return boolean
		 */
		public boolean canModify(Object element, String property) {
			if (element instanceof Property) {
				Property prop = (Property) element;
				if (prop.isIsheader()) {
					String name = prop.getName();
					if (name == null)
						return true;
					if (!(JMSConstants.JMSCORRELATION_ID.equals(name) || JMSConstants.JMSREPLY_TO.equals(name) || JMSConstants.JMSTYPE.equals(name)))
						return false;
					else
						return true;
				}
			}
			return true;
		}
	}
	
	public Property getReplyToProperty() {
		return replyToProperty;
	}
}
