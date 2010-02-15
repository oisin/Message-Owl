package org.fusesource.tools.core.preferences;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.fusesource.tools.core.ui.StringFieldEditor;


public class FUSEComposite extends Composite {

	private static final String FUSE_TOOLS_WIKI = "FUSE Tools wiki: ";

	private static final String FUSE_TOOLS_ISSUE_TRACKER = "FUSE Tools Issue Tracker: ";

	private static final String FUSE_TOOLS_FORUM = "FUSE Tools Forum: ";

	private static final String FUSE_HOME_PAGE = "FUSE Home Page: ";

	private static final String FID_WIKI = "FID_WIKI";

	private static final String FID_ISSUE_TRACKER = "FID_ISSUE_TRACKER";

	private static final String TOOLS_FORUM_URL = "TOOLS_FORUM_URL";

	private static final String FUSE_HOME_PAGE_URL = "FUSE_HOME_PAGE_URL";

	private static final String BUNDLE_NAME = "org.fusesource.tools.core.preferences.fuselinks";
	
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String SERIAL_NUMBER_KEY = "org.fusesource.tools.license.serial";
	
	private StringFieldEditor serialNumberEditor;
	
	public FUSEComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		addFuseLinks();
		addSerialNumer();
		this.setLayout(gridLayout);
	}

	public StringFieldEditor getSerialNumberEditor(){
		return serialNumberEditor;
	}
	private void addSerialNumer(){
		
		Composite comp = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		data.widthHint = 400;
		comp.setLayout(layout);
		comp.setLayoutData(data);
		serialNumberEditor = new StringFieldEditor(SERIAL_NUMBER_KEY,"Serial Number :",comp);
	}
	/**
	 * This method initializes FUSE
	 * 
	 */
	private void addFuseLinks() {
		Group fuse = new Group(this, SWT.NONE);
		fuse.setText("FUSE Links");
		
		fuse.setLayout(getGridLayout());
		fuse.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER,
				true, false));
		
		addLink(fuse, FUSE_HOME_PAGE, getLink(FUSE_HOME_PAGE_URL));
		addLink(fuse, FUSE_TOOLS_FORUM, getLink(TOOLS_FORUM_URL));
		addLink(fuse, FUSE_TOOLS_ISSUE_TRACKER, getLink(FID_ISSUE_TRACKER));
		addLink(fuse, FUSE_TOOLS_WIKI, getLink(FID_WIKI));
	}

	private GridLayout getGridLayout() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginRight = 20;
		gridLayout.marginLeft = 5;
		return gridLayout;
	}
	
	private void addLink(Group group, String string, String url) {
		Label label = new Label(group, SWT.RIGHT);
		label.setText(string);
		label.setLayoutData(new GridData(GridData.BEGINNING,
				GridData.CENTER, true, false));

		Link link = new Link(group, SWT.NONE);
		link.setText(url);
		link.addSelectionListener(new HyperlinkListener());
	}

	private String getLink(String key) {
		try {
			String value = RESOURCE_BUNDLE.getString(key);
			return "<a href=\"" + value + "\"" + ">" + value + "</a>";
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}		
	}
	
	private static class HyperlinkListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent selectionevent) {
		}

		public void widgetSelected(SelectionEvent selectionevent) {
			try {
				String url = selectionevent.text;
				IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
				support.getExternalBrowser().openURL(new URL(url));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
