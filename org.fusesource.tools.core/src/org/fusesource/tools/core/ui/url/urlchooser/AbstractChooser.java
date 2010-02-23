/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.ui.url.urlchooser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser.LocalFileSystemProvider;
import org.fusesource.tools.core.ui.url.util.ImageConstants;
import org.fusesource.tools.core.ui.url.util.ImagesUtil;

/**
 */

public abstract class AbstractChooser {
    /*
     * The null type (value is 0).
     */
    public static final int STYLE_NONE = 0;

    /**
     * Style constant to activate multi file selection.
     */
    public static final int STYLE_MULTI_SELECTION = 1;

    /**
     * Style constant to enable F3 file open - selected file is opened in a new editor.
     */
    public static final int STYLE_SUPPORT_OPEN = 2;

    /**
     * Style constant to hide the text field, i.e. only show the button bar.
     */
    public static final int STYLE_HIDE_TEXT_FIELD = 4;

    /**
     * Style constant to hide the New menu (default is visible).
     */
    public static final int STYLE_HIDE_NEW_MENU = 8;

    public static final int STYLE_TEXT_READONLY = 0x100;

    public static final String CP_SUPPRESS_EDITOR = "suppressEditor";
    public static final String CP_PARENT_FOLDER = "parentFolder";
    public static final String CP_FILE_NAME = "fileName";

    /**
     * The separating character used to separate multiple url strings (when STYLE_MULTI_SELECTION is
     * on).
     */
    private static final String MULTI_SEPARATOR = ",";

    private ActionProvider lastExeced = null;
    private Composite composite;
    protected Composite parent;
    private List userActionProviders;
    private Text textField;
    protected Menu menu;
    private final List listeners = Collections.synchronizedList(new ArrayList(3));

    protected Object[] selectedValues;
    private boolean isSingleSelection = true;
    private boolean isTextFieldShown = true;
    protected String browseBtnText = "...";
    private List defaultActionProviders;
    protected List actionProviders;
    protected boolean isTextModified = false;
    private boolean supportOpen = false;
    protected int style = STYLE_NONE;
    protected boolean showRelativeFilePath = false;

    public static final Object ACTION_SEPERATOR = new Object();

    protected Button mainButton;

    protected Button openButton;

    private boolean textReadonly;

    protected AbstractChooser() {
    }

    public AbstractChooser(Composite parent) {
        this(parent, Collections.EMPTY_LIST, Collections.EMPTY_LIST, STYLE_NONE);
    }

    public AbstractChooser(Composite parent, int style) {
        this(parent, Collections.EMPTY_LIST, Collections.EMPTY_LIST, style);
    }

    public AbstractChooser(Composite parent, List supportedProviderIds, int style) {
        this(parent, supportedProviderIds, Collections.EMPTY_LIST, style);
    }

    /**
     * Provide a list of provider ids that is supported by this instance of the FileChooser. This
     * method may not be called again.
     * 
     * @param supportedProviderIds
     *            MAY BE NULL
     */
    public AbstractChooser(Composite parent, List supportedProviderIds, List tempFSProviders, int style) {
        this(parent, supportedProviderIds, tempFSProviders, null, style);
    }

    /**
     * Provide a list of provider ids that is supported by this instance of the FileChooser. This
     * method may not be called again.
     * 
     * @param supportedProviderIds
     *            MAY BE NULL
     * @param tempFSProviders
     *            a list or temporary (just for this instance) FS Providers
     * @param listener
     *            a quick way to specify a listener
     */
    public AbstractChooser(Composite parent, List supportedProviderIds, List tempFSProviders,
            SelectionChangeListener listener, int style) {
        init(parent, tempFSProviders, supportedProviderIds, listener, style);
    }

    protected void init(Composite parent, List tempFSProviders, List supportedProviderIds,
            SelectionChangeListener listener, int style) {
        this.parent = parent;
        this.userActionProviders = tempFSProviders == null ? Collections.EMPTY_LIST : tempFSProviders;
        this.defaultActionProviders = supportedProviderIds == null ? Collections.EMPTY_LIST : supportedProviderIds;
        this.style = style;
        if ((style & STYLE_MULTI_SELECTION) > 0) {
            setSingleSelection(false);
        }
        setSupportsOpen((style & STYLE_SUPPORT_OPEN) > 0);
        setTextFieldShown(!((style & STYLE_HIDE_TEXT_FIELD) > 0));
        setTextFieldReadonly(((style & STYLE_TEXT_READONLY) > 0));
        createUI(this.parent, this.defaultActionProviders, this.userActionProviders);
        if (listener != null) {
            addSelectionChangeListener(listener);
        }
    }

    private void setTextFieldReadonly(boolean b) {
        textReadonly = b;
    }

    private void createUI(Composite parent, List supportedProviderIds, List tempFSProviders) {
        composite = createUIImpl(parent, supportedProviderIds, tempFSProviders);
    }

    public Composite getUI() {
        return composite;
    }

    protected Composite createUIImpl(final Composite parent, List providersList, List tempFSProviders) {
        setMenu(new Menu(parent.getShell()));

        Composite composite = createParent(parent);
        composite.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                getMenu().dispose();
            }
        });

        GridLayout layout = new GridLayout(isTextFieldShown() ? 2 : 1, false);
        layout.marginWidth = 1; // We do this to remove the default (5 pixel) border
        layout.marginHeight = 0;
        composite.setLayout(layout);

        if (isTextFieldShown()) {
            prepareTextField(composite);
        }

        Control button = createBrowseButton(composite);
        if (isTextFieldShown()) {
            textField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        }
        button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, isTextFieldShown() ? false : true, false));

        addProviders(providersList, tempFSProviders);

        addDropTarget();

        return composite;
    }

    public void setEnabled(boolean enabled) {
        if (isTextFieldShown()) {
            textField.setEnabled(enabled);
        }
        mainButton.setEnabled(enabled);
        openButton.setEnabled(enabled);

    }

    protected Control createBrowseButton(Composite composite) {
        Composite comp = new Composite(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.horizontalSpacing = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        comp.setLayout(gridLayout);
        mainButton = new Button(comp, SWT.PUSH);
        mainButton.setText(browseBtnText);
        GridData gridData = new GridData();
        mainButton.setLayoutData(gridData);
        mainButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                execActionProvider();
            }
        });
        openButton = new Button(comp, SWT.PUSH);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
        openButton.setLayoutData(gridData);
        openButton.setImage(ImagesUtil.getInstance().getImage(ImageConstants.WIDGET_OPEN));
        openButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Button item = (Button) e.widget;
                Rectangle rect = item.getBounds();
                Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
                pt.y += rect.height;
                showMenu(pt, true);
            }
        });
        return comp;
    }

    protected Composite createParent(final Composite parent) {
        Composite c = new Composite(parent, SWT.NONE);
        c.setBackground(parent.getBackground()); // Have to do this since SWT has no concept of
        // transparent widgets
        // (see SWT.NO_BACKGROUND & SWT FAQ).
        return c;
    }

    protected Control prepareTextField(Composite parent) {
        textField = createText(parent);
        hookTextFieldListeners();
        return textField;
    }

    protected Text createText(Composite parent) {
        Text text = new Text(parent, SWT.NONE | SWT.SINGLE | SWT.BORDER | (getTextReadonly() ? SWT.READ_ONLY : 0));
        return text;
    }

    private boolean getTextReadonly() {
        return textReadonly;
    }

    private void hookTextFieldListeners() {
        textField.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!internalUpdate) {
                    selectedValues = parseText();
                    fireSelectionChanged();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        textField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (!internalUpdate) {
                    isTextModified = true;
                    selectedValues = parseText();
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (isTextModified) {
                    selectedValues = parseText();
                    fireSelectionChanged(); // isTextModified is set to false in fireURLChanged
                    isTextModified = false;
                }
            }
        });
        if (isSingleSelection() && supportOpen()) {
            // Irene's URL Open code
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.keyCode == SWT.F3) {
                        openSelectedValue();
                    }
                }
            });
            textField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseUp(MouseEvent e) {
                    if ((e.stateMask & SWT.CTRL) > 0) {
                        openSelectedValue();
                    }
                }

                @Override
                public void mouseDoubleClick(MouseEvent e) {
                    if ((e.stateMask & SWT.CTRL) > 0) {
                        openSelectedValue();
                    }
                }
            });
        }
    }

    protected abstract void openSelectedValue();

    public boolean supportOpen() {
        return supportOpen;
    }

    protected void setSupportsOpen(boolean open) {
        supportOpen = open;
    }

    protected void addProviders(List defaultActions, List userActions) {
        Collection list = getProvidersList(defaultActions);
        list.remove(ACTION_SEPERATOR);
        this.actionProviders = new ArrayList(list);
        this.actionProviders.addAll(userActions);
    }

    protected void addDropTarget() {
        if (!isTextFieldShown) {
            return;
        }

        int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK | DND.DROP_DEFAULT;

        DropTarget target = new DropTarget(textField, operations);
        target.setTransfer(getTransferTypes());

        target.addDropListener(new DropTargetAdapter() {
            @Override
            public void dragEnter(DropTargetEvent event) {
                if (event.detail == DND.DROP_DEFAULT) {
                    event.detail = (event.operations & DND.DROP_COPY) != 0 ? DND.DROP_COPY : DND.DROP_NONE;
                }
            }

            @Override
            public void drop(DropTargetEvent event) {
                // A drop has occurred, copy over the data
                if (event.data == null) { // no data to copy, indicate failure in event.detail
                    event.detail = DND.DROP_NONE;
                    return;
                }
                Object[] selection = acceptObjectDrop(event);
                if (selection != null) {
                    setSelectedValues(selection);
                }
            }
        });
    }

    private Transfer[] getTransferTypes() {
        Set transfers = new HashSet();
        for (Iterator iterator = actionProviders.iterator(); iterator.hasNext();) {
            ActionProvider provider = (ActionProvider) iterator.next();
            if (provider.supportsDnd()) {
                Transfer[] types = provider.getTransferTypes();
                transfers.addAll(Arrays.asList(types));
            }
        }
        return (Transfer[]) transfers.toArray(new Transfer[transfers.size()]);
    }

    public Object[] acceptObjectDrop(DropTargetEvent data) {
        for (Iterator iterator = actionProviders.iterator(); iterator.hasNext();) {
            ActionProvider provider = (ActionProvider) iterator.next();
            Object[] urls = provider.acceptDrop(data);
            if (urls != null) {
                return urls;
            }
        }
        return null;
    }

    public Object[] parseText() {
        if (textField.isDisposed()) {
            return new Object[0];
        }

        String text = textField.getText();

        StringTokenizer st = new StringTokenizer(text, MULTI_SEPARATOR);
        List res = new ArrayList(st.countTokens());
        while (st.hasMoreTokens()) {
            res.add(st.nextToken().trim());
        }

        return res.toArray(new Object[0]);
    }

    protected abstract Collection getProvidersList(List providersList);

    public void setBrowseButtonText(String text) {
        this.browseBtnText = text;
        mainButton.setText(browseBtnText);
    }

    private void setTextFieldShown(boolean show) {
        this.isTextFieldShown = show;
    }

    public boolean isTextFieldShown() {
        return isTextFieldShown;
    }

    /**
     * Returns objects currently selected. The type of the objects will depend on the ActionProvider
     * which add selections.
     * 
     * @return array of selected objects
     */
    public Object[] getSelectedObjects() {
        return selectedValues;
    }

    public Object getSelectedObject() {
        if (selectedValues == null || selectedValues.length == 0) {
            return null;
        }

        return selectedValues[0];
    }

    public void setSelectedValue(Object value) {
        setSelectedValue(value, false);
    }

    public void setSelectedValue(Object value, boolean suppressEvents) {
        setSelectedValues((value != null) ? new Object[] { value } : null, suppressEvents);
    }

    public void setSelectedValues(Object[] values, boolean suppressEvents) {
        // Only update if there's a change
        if (Arrays.equals(selectedValues, values)) {
            return;
        }

        selectedValues = new Object[(values != null) ? values.length : 0];

        if (selectedValues.length > 0) {
            // Check because arraycopy will throw a NPE if 'values' is null even if the copy length
            // is ZERO :(
            System.arraycopy(values, 0, selectedValues, 0, selectedValues.length);
        }
        updateSelectedValues(selectedValues, suppressEvents);
    }

    public void setSelectedValues(Object[] values) {
        setSelectedValues(values, false);
    }

    protected void addSelectionChangeListener(SelectionChangeListener l) {
        if (l != null && !listeners.contains(l)) {
            listeners.add(l);
        }
    }

    protected void removeSelectionChangeListener(SelectionChangeListener l) {
        listeners.remove(l);
    }

    public Text getTextControl() {
        return textField;
    }

    protected void showMenu(Point pt, boolean visible) {
        populateMenu();
        updateMenuEnablement(getMenu());
        getMenu().setLocation(pt.x, pt.y);
        getMenu().setVisible(visible);
    }

    private boolean m_menuInit = false;

    private boolean internalUpdate;

    private void populateMenu() {
        if (!m_menuInit) {
            addProviderItems(getProvidersList(this.defaultActionProviders), this.userActionProviders);
            m_menuInit = true;
        }
    }

    protected Menu getMenu() {
        return menu;
    }

    protected void setMenu(Menu menu) {
        this.menu = menu;
    }

    protected void addProviderItems(Collection list, List tempFSProviders) {
        addOtherMenus();
        addActionProviderMenuItems(list, tempFSProviders);
    }

    protected void addActionProviderMenuItems(Collection list, List tempFSProviders) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            Object action = iterator.next();
            if (action.equals(ACTION_SEPERATOR)) {
                createSeperator();
            } else {
                createProviderItem((ActionProvider) action);
            }
        }

        for (Iterator iterator = tempFSProviders.iterator(); iterator.hasNext();) {
            createProviderItem((ActionProvider) iterator.next());
        }
    }

    protected void addOtherMenus() {
        boolean bShowMenu = !((style & STYLE_HIDE_NEW_MENU) > 0);
        boolean bShowOpen = ((style & STYLE_SUPPORT_OPEN) > 0);

        if (bShowMenu) {
            _addNewMenuSupport();
        }

        if (bShowOpen) {
            _addOpenMenuSupport();
        }

        addCustomMenus();

        boolean showSep = bShowMenu || bShowOpen;
        addSeparator(showSep);
    }

    /** override to always show separator */
    protected void addSeparator(boolean showSep) {
        if (showSep) {
            new MenuItem(menu, SWT.SEPARATOR);
        }
    }

    protected void addCustomMenus() {
    }

    protected void createSeperator() {
        new MenuItem(menu, SWT.SEPARATOR);
    }

    protected void createProviderItem(final ActionProvider provider) {
        MenuItem menuItem = new MenuItem(menu, SWT.NONE);
        menuItem.setText(provider.getDisplayName());
        menuItem.setData(provider);
        menuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                execActionProvider(provider);
            }
        });
    }

    // ------------------------------------------------------------------------

    protected void updateMenuEnablement(Menu menu) {
        boolean bOpen = false;
        MenuItem miOpen = findMenuItemWithData(menu, "OPEN");
        if (miOpen != null) {
            // if (getSelectedValueAsURL() != null)
            if (isSelectedValueValid()) {
                // TODO - need to find out a way of checking to see if URL is valid
                bOpen = true;
            }
            miOpen.setEnabled(bOpen);
        }

        boolean bNew = false;
        MenuItem miNew = findMenuItemWithData(menu, "NEW");
        if ((miNew != null) && (miNew.getMenu() != null)) {
            bNew = (miNew.getMenu().getItemCount() > 0);
        }
        if (miNew != null) {
            miNew.setEnabled(bNew);
        }
    }

    protected abstract boolean isSelectedValueValid();

    private void _addOpenMenuSupport() {
        MenuItem miOpen = new MenuItem(menu, SWT.NONE);
        miOpen.setText("Open");
        miOpen.setData("OPEN");
        miOpen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                openSelectedValue();
            }
        });
    }

    private void _addNewMenuSupport() {
        MenuItem miNew = new MenuItem(menu, SWT.CASCADE);
        miNew.setText("New");
        miNew.setData("NEW");

        Menu mNew = new Menu(menu);
        miNew.setMenu(mNew);

        addNewMenuItems(mNew);

    }

    protected abstract void addNewMenuItems(Menu newMenu);

    // ------------------------------------------------------------------------

    /**
     * Method executes the specific (supplied) File System Provider.
     */
    protected void execActionProvider(final ActionProvider provider) {
        lastExeced = provider.isSelectionSupported() ? provider : lastExeced;
        String initialPath = null;
        if (isTextFieldShown) {
            initialPath = textField.getText();
        }
        provider.setSelectionType(isSingleSelection);
        provider.run(selectedValues);
        Object[] selection = null;
        if (provider.isSelectionSupported()) {
            selection = provider.getSelection();
        }
        if (selection != null) {
            updateSelectedValues(selection);
        }
    }

    /**
     * Method executes the default (1st) or last executed File System Provider's chooser.
     */
    protected void execActionProvider() {
        if (lastExeced != null) {
            execActionProvider(lastExeced);
        } else {
            if ((actionProviders != null) && !actionProviders.isEmpty()) {
                for (Object provider : actionProviders) {
                    ActionProvider p = (ActionProvider) provider;
                    if (p != null && "Resource".equals(p.getID())) {
                        execActionProvider(p);
                    }
                }
            }
        }
    }

    private MenuItem findMenuItemWithData(Menu menu, Object data) {
        MenuItem[] items = menu.getItems();
        for (MenuItem item : items) {
            if ((item.getData() != null) && (item.getData().equals(data))) {
                return item;
            }
        }
        return null;
    }

    public boolean isTextModified() {
        return isTextModified;
    }

    protected void updateSelectedValues(Object[] values, boolean suppressEvents) {
        if (isSingleSelection() && values.length > 1) {
            selectedValues = new Object[1];
            selectedValues[0] = values[0];
        } else {
            selectedValues = values;
        }

        if (isTextFieldShown) {
            internalUpdate = true;
            if (showRelativeFilePath && !LocalFileSystemProvider.isFileChooserDialogOpen()) {
                try {
                    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                    String path = root.getLocation().toString();
                    File workspace = new File(path);
                    URL workspacleUrl = workspace.toURL();
                    String workspacePath = workspacleUrl.toString();
                    int workSpacePathLength = workspacePath.length();

                    String text = getText(selectedValues);
                    int index = text.lastIndexOf(workspacePath);
                    String subtext = "/" + text.substring(index + workSpacePathLength);
                    textField.setText(subtext);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            } else {
                textField.setText(getText(selectedValues));
            }
            LocalFileSystemProvider.setFileChooserDialogOpen(false);
            internalUpdate = false;
        }

        if (!suppressEvents) {
            fireSelectionChanged();
        } else {
            isTextModified = false;
        }
    }

    protected void updateSelectedValues(Object[] values) {
        updateSelectedValues(values, false);
    }

    protected void fireSelectionChanged() {
        isTextModified = false; // this is critical or the text field fires this method again
        List cloned = new ArrayList(listeners);
        for (Iterator iterator = cloned.iterator(); iterator.hasNext();) {
            Object listener = iterator.next();
            if (listener instanceof SelectionChangeListener) {
                try {
                    ((SelectionChangeListener) listener).selectionChanged(selectedValues);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    public boolean isSingleSelection() {
        return isSingleSelection;
    }

    public void setSingleSelection(boolean isSingleOrMulti) {
        isSingleSelection = isSingleOrMulti;
    }

    public interface NewCustomizationProvider {
        Map getCustomizationMap();
    }

    public static Map getDefaultCustomizationMap(IFile refEditorFile) {
        Map map = new HashMap();
        map.put(CP_FILE_NAME, refEditorFile.getName());
        map.put(CP_PARENT_FOLDER, refEditorFile.getParent().getFullPath());
        return map;
    }

    private String getText(Object[] selectedValues) {
        StringBuffer buf = new StringBuffer();

        for (Object selectedValue : selectedValues) {
            // Don't output empty (null) urls to the string
            if (selectedValue == null) {
                continue;
            }

            // If the buffer is not empty then we are adding an additional URL
            // so we put the MULTI character in to separate the URLs.
            if (buf.length() > 0) {
                buf.append(MULTI_SEPARATOR).append(" ");
            }

            // Add the url to the buffer
            buf.append(getDisplayValue(selectedValue));
        }
        return buf.toString();
    }

    protected String getDisplayValue(Object object) {
        return object.toString();
    }

}
