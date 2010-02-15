package org.fusesource.tools.messaging.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.MessageEvent;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.IMessageChangeListener;
import org.fusesource.tools.messaging.core.IMessagesManager;
import org.fusesource.tools.messaging.ui.Column;
import org.fusesource.tools.messaging.ui.DefaultMessageTableViewer;
import org.fusesource.tools.messaging.ui.TableContentProvider;
import org.fusesource.tools.messaging.ui.TableLabelProvider;


public class MessageReceiverTable {

	private static final String RECEIVED_MESSAGES = "Received Messages";

	protected static final Column[] COLUMNS = { new Column(RECEIVED_MESSAGES, 1.0f) };

	protected TableViewer tableViewer;

	protected static Font DEFAULT_FONT, BOLD_FONT;

	protected List<MessageEvent> allMessagesList;

	protected IListener listener;

	private IMessageChangeListener newMsgNotifications;

	private boolean isColumnsVisible = false;

	private boolean isBlockCleared = false;

	public MessageReceiverTable() {
		allMessagesList = new ArrayList<MessageEvent>();
	}

	/**
	 * 
	 * @param parent
	 * @param hintHeight should pass actual value or -1
	 */
	protected void createControl(Composite parent, int hintHeight) {
		Composite composite = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		setupViewer(composite);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		if (hintHeight != -1)
			gridData.heightHint = hintHeight;
		tableViewer.getTable().setLayoutData(gridData);
		initFonts();
	}

	protected void initFonts() {
		DEFAULT_FONT = tableViewer.getTable().getFont();
		if (BOLD_FONT == null) {
			FontData[] fontData = DEFAULT_FONT.getFontData();
			for (FontData fdata : fontData) {
				fdata.setStyle(fdata.getStyle() | SWT.BOLD);
			}
			BOLD_FONT = new Font(tableViewer.getTable().getDisplay(), fontData);
		}
	}

	protected void setupViewer(Composite parent) {
		tableViewer = new DefaultMessageTableViewer(parent, SWT.NONE | SWT.FULL_SELECTION, IConstants.EMPTY_STRING);

		Table table = tableViewer.getTable();
		setTableProperties(table);
		addColumns(parent.getBounds());
		tableViewer.setColumnProperties(getColumnProperties());

		tableViewer.setContentProvider(getContentProvider());
		tableViewer.setLabelProvider(getLabelProvider());
		tableViewer.setInput(getInput());
	}

	protected String[] getColumnProperties() {
		return new String[] { RECEIVED_MESSAGES };
	}

	protected void addColumns(Rectangle bounds) {
		int width = bounds.width;

		if (width < 100) {
			width = getDefaultWidth();
		}

		Column[] columns = getColumns();
		for (int i = 0; i < columns.length; i++) {
			TableColumn c = new TableColumn(tableViewer.getTable(), SWT.FULL_SELECTION);
			c.setText(columns[i].getColumnName());
			c.setWidth((int) ((float) width * columns[i].getWidthValue()));
			c.setResizable(true);
		}
	}

	protected int getDefaultWidth() {
		return 500;
	}

	protected Object getInput() {
		return allMessagesList;
	}

	protected IBaseLabelProvider getLabelProvider() {
		return new MRTLabelProvider();
	}

	protected IContentProvider getContentProvider() {
		return new TableContentProvider();
	}

	protected void setTableProperties(Table table) {
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	protected Column[] getColumns() {
		return COLUMNS;
	}

	public TableViewer getViewer() {
		return tableViewer;
	}

	public IListener getListener() {
		return listener;
	}

	// Called when selection changes...
	public void setInput(IListener listener) {
		this.listener = listener;
		addNotificationListener();
		reloadTableData();
	}

	protected void reloadTableData() {
		allMessagesList.clear();
		IMessagesManager messagesManager = listener.getMessagesManager();
		if (messagesManager == null)
			return;
		List<MessageEvent> messages = new ArrayList<MessageEvent>(messagesManager.getMessages());
		if (messages.isEmpty()) {
			updateUI();
			return;
		}
		// Mark all as read...
		for (MessageEvent messageEvent : messages) {
			messagesManager.resetFlag(messageEvent, IMessageChangeListener.MESSAGE_READ);
		}

		allMessagesList.addAll(messages);
		updateColumns();
		updateUI();
	}

	protected void updateColumns() {
		if(isColumnsVisible)
			return;
		if (allMessagesList != null && allMessagesList.size() > 0) {
			MessageEvent messageTypeWrapper = allMessagesList.get(0);
			Map<String, String> metadata = messageTypeWrapper.getMetadata();
			if (metadata != null) {
				StringBuffer columnString = new StringBuffer();
				Set<String> keySet = metadata.keySet();
				for (String string : keySet) {
					columnString.append(string);
					columnString.append(";");
				}
				((DefaultMessageTableViewer) tableViewer).refreshTable(columnString.toString());
				isColumnsVisible = true;
			}
		}
	}

	public void updateUI() {
		Runnable object = new Runnable() {
			public void run() {
				/*
				 * UI-822 - When message in-flow is too fast and the editor gets
				 * disposed, already scheduled threads try to refresh the
				 * viewer. We get widget disposed exception that case. Fix is to
				 * skip the refresh call when the editor is actually disposed
				 */
				if (!isBlockCleared) {
					getViewer().refresh();
				}
			}
		};
		Display.getDefault().asyncExec(object);
	}

	private void addNotificationListener() {
		if (newMsgNotifications == null) {
			newMsgNotifications = new IMessageChangeListener() {

				public void messageChangeEvent(final MessageEvent me, int kind) {
					if (IMessageChangeListener.MESSAGE_ADDED == kind) {
						allMessagesList.add(me);
						Runnable object = new Runnable() {
							public void run() {
								updateColumns();
								updateUI();
							}
						};
						Display.getDefault().asyncExec(object);
					} else if (IMessageChangeListener.MESSAGE_REMOVED == kind) {
						allMessagesList.remove(me);
						updateUI();
					}
				}

				public void messagesClearedEvent(List<MessageEvent> clearedMsgs) {
					allMessagesList.clear();
					updateUI();
				}
			};
			IMessagesManager messagesManager = listener.getMessagesManager();
			if (messagesManager != null)
				messagesManager.addMessageChangeListener(newMsgNotifications);
		}
	}

	public void removeNotificationsListener() {
		if (listener == null || newMsgNotifications == null)
			return;
		listener.getMessagesManager().removeMessageChangeListener(newMsgNotifications);
		isBlockCleared = true;
	}

	protected class MRTLabelProvider extends TableLabelProvider {
		public Font getFont(Object element) {
			if (element instanceof MessageEvent) {
				MessageEvent message = (MessageEvent) element;
				if (listener.getMessagesManager().hasFlag(message, IMessageChangeListener.MESSAGE_UNREAD))
					return BOLD_FONT;
			}
			return DEFAULT_FONT;
		}
	}
}
