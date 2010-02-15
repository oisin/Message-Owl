package org.fusesource.tools.messaging.ui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.fusesource.tools.messaging.IConstants;



public class DefaultMessageTableViewer extends TableViewer implements TableModel {

	private String prefKey;

	protected String columnString = IConstants.EMPTY_STRING;

	public DefaultMessageTableViewer(Composite parent, String prefKey) {
		this(parent, SWT.FULL_SELECTION | SWT.MULTI, prefKey);
	}

	public DefaultMessageTableViewer(Composite parent, int style, String prefKey) {
		super(parent, style);
		addListeners();
		this.prefKey = prefKey;
	}
	
	public DefaultMessageTableViewer(Table table){
		super(table);
	}

	private void addListeners() {
		getTable().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				onDispose();
			}
		});
	}

	protected int getDefaultWidth() {
		return 1000;
	}

	public String getDefaultPreferenceValue() {
		return IConstants.EMPTY_STRING;
	}

	public String getPreferenceKey() {
		return prefKey;
	}

	public void setPreferenceKey(String prefKey) {
		this.prefKey = prefKey;
	}

	public void removeAll() {
		TableColumn[] tableColumns = this.getTable().getColumns();
		for (TableColumn tableColumn : tableColumns) {
			tableColumn.dispose();
		}
	}

	public void addColumns() {
		String[] columns = columnString.split(IConstants.COLUMN_DELIM);
		for (int i = 0; i < columns.length; i++) {
			Column column = new Column(columns[i], 0.11f);
			TableColumn c = new TableColumn(this.getTable(), SWT.FULL_SELECTION);
			c.setText(column.getColumnName());
			c.setWidth((int) ((float) getDefaultWidth() * column.getWidthValue()));
			c.setResizable(true);
			c.setMoveable(true);
		}
	}

	public String getColumnString() {
		return columnString;
	}

	public void refreshTable(String columnString) {
		getTable().setHeaderVisible(true);
		if (columnString == null || columnString.equals(IConstants.EMPTY_STRING))
			columnString = getDefaultPreferenceValue();
		this.columnString = columnString;
		removeAll();
		addColumns();
		saveModel();
		refresh();
	}

	protected void onDispose() {
		saveModel();
	}

	public void saveModel() {

	}
}
