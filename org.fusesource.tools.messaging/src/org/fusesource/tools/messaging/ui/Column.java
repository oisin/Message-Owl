package org.fusesource.tools.messaging.ui;

import org.fusesource.tools.messaging.IConstants;

public class Column {
	private String columnName = IConstants.EMPTY_STRING;
	private float widthValue;

	public Column(String columnName) {
		this(columnName, 0.1f);
		this.columnName = columnName;
	}

	public Column(String columnName, float widthValue) {
		this.columnName = columnName;
		this.widthValue = widthValue;
	}

	public String getColumnName() {
		return columnName;
	}

	public float getWidthValue() {
		return widthValue;
	}
}