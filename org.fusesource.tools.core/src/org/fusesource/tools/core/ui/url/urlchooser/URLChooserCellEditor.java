package org.fusesource.tools.core.ui.url.urlchooser;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser.LocalDirectorySystemProvider;
import org.fusesource.tools.core.ui.url.urlchooser.workspacechooser.ResourceChooserProvider;
import org.fusesource.tools.core.ui.url.urlchooser.workspacechooser.WorkspaceChooserProvider;


import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class URLChooserCellEditor extends CellEditor {

    public static final String EDITOR_ID = URLChooserCellEditor.class.getName();

    public static final ILabelProvider LABEL_PROVIDER = new LabelProvider();

    protected URLChooser m_chooser;

    /**
     * The value of this cell editor; initially <code>null</code>.
     */
    protected String[] strValue = null;

    public URLChooserCellEditor(Composite composite) {
        this(composite, null);
    }

    public URLChooserCellEditor(Composite composite, String args) {
        super(composite);
        setArgs(args);
    }

    // ------------------------------------------------------------------------
    //
    // CellEditor implementation
    //
    // ------------------------------------------------------------------------

    protected Control createControl(Composite parent) {
    	

        m_chooser = new MyURLChooser(parent, null, null, null, getURLChooserStyle());

        m_chooser.addURLChangeListener(new URLChooserChangeListener() {
            public void urlChanged(String[] urls) {
                markDirty();
                doSetValue(urls);
                URLChooserCellEditor.this.focusLost();
            }
        });

        return m_chooser.getUI();
    }

    /**
     * Subclasses can override the method to return specific URLCHooser style.
     */
    protected int getURLChooserStyle() {
        return URLChooser.STYLE_SUPPORT_OPEN;
    }

    private class MyURLChooser extends URLChooser {
        private boolean hasFocus;
        private Listener listener, filter;
        private Button bSelect;
        private Button bBrowse;

        public MyURLChooser(Composite parent) {
            super(parent);
        }

        public MyURLChooser(Composite parent,
                            List supportedProviderIds,
                            List tempFSProviders,
                            URLChooserChangeListener listener,
                            int style) {
            super(parent, supportedProviderIds, tempFSProviders, listener, style);

            // Sonic00029856 - All this listener and filter weirdness is here to handle
            // the loss of focus from the 'URLChooser' custom widget. In reality,
            // URLChooser is not a custom widget...so this code pretends and issues
            // FocusIn and FocusOut change events from the main composite URLChooser::getUI().
            // Code was lifted from the CCombo implementation.
            this.listener = new Listener() {
                public void handleEvent(Event event) {
                    if (MyURLChooser.this.getUI() == event.widget) {
                        chooserEvent(event);
                        return;
                    }

                    switch (event.type) {
                        case SWT.FocusIn: {
                            handleFocus(SWT.FocusIn);
                            m_chooser.populateRecentMenu();
                            break;
                        }
                    }
                }
            };
            getTextControl().addListener(SWT.FocusIn, this.listener);
//            getTextControl().addFocusListener(new FocusAdapter() {
//                public void focusLost(FocusEvent e) {
//                    URLChooserCellEditor.this.focusLost();
//                }
//            });
            bBrowse.addListener(SWT.FocusIn, this.listener);
            bSelect.addListener(SWT.FocusIn, this.listener);

            filter = new Listener() {
                public void handleEvent(Event event) {
                    Control eControl = (Control) event.widget;
                    Control mControl = MyURLChooser.this.getUI();

                    if (eControl.isDisposed() || mControl.isDisposed())
                        return;

                    if (eControl.getShell() == mControl.getShell()) {
                        handleFocus(SWT.FocusOut);
                    }
                }
            };
            parent.addDisposeListener(new DisposeListener(){

				public void widgetDisposed(DisposeEvent e) {
					Display display = getUI().getDisplay();	
					display.removeFilter(SWT.FocusIn, filter);
				}
            	
            });
        }

        void chooserEvent(Event event) {
            switch (event.type) {
                case SWT.Dispose:
                    Display display = getUI().getDisplay();
                    display.removeFilter(SWT.FocusIn, filter);
                    break;
            }
        }

        void handleFocus(int type) {
            if (getUI().isDisposed())
                return;
            switch (type) {
                case SWT.FocusIn: {
                    if (hasFocus)
                        return;
                    hasFocus = true;
                    Display display = getUI().getDisplay();
                    display.removeFilter(SWT.FocusIn, filter);
                    display.addFilter(SWT.FocusIn, filter);
                    Event e = new Event();
                    getUI().notifyListeners(SWT.FocusIn, e);
                    break;
                }
                case SWT.FocusOut: {
                    hasFocus = false;
                    Control focusControl = getUI().getDisplay().getFocusControl();
                    if ((focusControl == getTextControl()) || (focusControl == bBrowse) || (focusControl == bSelect))
                        return;
                    Display display = getUI().getDisplay();
                    display.removeFilter(SWT.FocusIn, filter);
                    Event e = new Event();
                    getUI().notifyListeners(SWT.FocusOut, e);
                    break;
                }
            }
        }

        protected Text createText(Composite parent) {
            Text text = new Text(parent, SWT.SINGLE);

            // Handles the ENTER key being pressed
            text.addSelectionListener(new SelectionAdapter() {
                public void widgetDefaultSelected(SelectionEvent e) {
                    handleDefaultSelection(e);
                }
            });
            text.addKeyListener(new KeyAdapter() {
                // hook key pressed - see PR 14201
                public void keyPressed(KeyEvent e) {
                    keyReleaseOccured(e);
                }
            });
            text.addTraverseListener(new TraverseListener() {
                public void keyTraversed(TraverseEvent e) {
                    if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
                        e.doit = false;
                    }
                }
            });

            text.addMouseListener(new MouseAdapter() {
                public void mouseUp(MouseEvent e) {
                    if ((e.stateMask & SWT.CTRL) > 0)
                        openInEditor();
                }

                public void mouseDoubleClick(MouseEvent e) {
                    if ((e.stateMask & SWT.CTRL) > 0)
                        openInEditor();
                }
            });

            return text;
        }

        protected Composite createUIImpl(final Composite parent, List providersList, List tempFSProviders) {
            setMenu(new Menu(parent.getShell()));
            Composite composite = createParent(parent);
            composite.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    getMenu().dispose();
                }
            });

            composite.setLayout(new MyLayout());

            prepareTextField(composite);
            buildBrowseButtons(composite);

            addProviders(providersList, tempFSProviders);
            addDropTarget();

            return composite;
        }

        protected void buildBrowseButtons(Composite composite) {
            Button btnPush = new Button(composite, SWT.PUSH);
            btnPush.setText("...");
            btnPush.setData("browseName", "PUSH");
            btnPush.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    execFileSystemProvider();
                }
            });
            bBrowse = btnPush;

            Button btnSelect = new Button(composite, SWT.ARROW | SWT.DOWN);
            btnSelect.setData("browseName", "SELECT");
            btnSelect.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    Button item = (Button) e.widget;
                    Rectangle rect = item.getBounds();
                    Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
                    pt.y += rect.height;
                    showMenu(pt, true);
                }
            });
            bSelect = btnSelect;
        }

        /**
         * Internal class for laying out the dialog.
         */
        private class MyLayout extends Layout {
            public void layout(Composite editor, boolean force) {
                Rectangle bounds = editor.getClientArea();

                Control cTxt = getTextControl();
                Control cBtnP = getBrowseBarControl(editor, "browseName", "PUSH");
                Control cBtnS = getBrowseBarControl(editor, "browseName", "SELECT");

                Point btnPSize = cBtnP.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
                Point btnSSize = cBtnS.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);

                cTxt.setBounds(0, 0, bounds.width - 2 - btnPSize.x - btnSSize.x, bounds.height);
                cBtnP.setBounds(bounds.width - btnPSize.x - btnSSize.x, 0, btnPSize.x, bounds.height);
                cBtnS.setBounds(bounds.width - btnSSize.x, 0, btnSSize.x, bounds.height);
            }

            public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
                if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT)
                    return new Point(wHint, hHint);

                Control cTxt = getTextControl();
                Control cBtnP = getBrowseBarControl(editor, "browseName", "PUSH");
                Control cBtnS = getBrowseBarControl(editor, "browseName", "SELECT");

                Point contentsSize = cTxt.computeSize(wHint, hHint, force);
                Point btnPSize = cBtnP.computeSize(wHint, hHint, force);
                Point btnSSize = cBtnS.computeSize(wHint, hHint, force);

                // Just return the button width to ensure the button is not clipped
                // if the label is long.
                // The label will just use whatever extra width there is
                return new Point(btnPSize.x + btnSSize.x, Math.max(contentsSize.y, btnPSize.y + btnSSize.y));
            }

            private Control getBrowseBarControl(Composite editor, String key, String value) {
                Control[] child = editor.getChildren();
                for (int i = 0; i < child.length; i++)
                    if ((child[i].getData(key) != null) && child[i].getData(key).equals(value))
                        return child[i];
                return null;
            }
        }
    }

    /*
     * (non-Javadoc) Method declared on CellEditor. The focus is set to the cell editor's button.
     */
    protected void doSetFocus() {
        m_chooser.getTextControl().setFocus();
        m_chooser.getTextControl().selectAll();
    }

    protected Object doGetValue() {
        // Sonic00031383
        // Special case for when the user types something into the text field part
        // of the URLChooser. In this case there's no auto-mechanism for getting
        // the value into the strValue of the URLChooserCellEditor so we do it
        // manually here...
        Object[] strValue = URLChooserCellEditor.this.strValue;
        if (m_chooser != null) {
            if (m_chooser.isTextModified())
                strValue = m_chooser.parseText();
        }


        if (strValue == null)
            return "";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strValue.length; i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(strValue[i]);
        }
        return sb.toString();
    }

    /*
     * (non-Javadoc) Method declared on CellEditor.
     */
    protected void doSetValue(Object newValue) {
        String[] oldValue = strValue;

        if (newValue instanceof String[]) {
            strValue = (String[]) newValue;
        } else if (newValue instanceof String) {
            strValue = splitStringIntoArray((String) newValue);
        } else if (newValue instanceof URL) {
            strValue = new String[]{newValue.toString()};
        } else if (newValue instanceof URL[]) {
            strValue = URLChooser.urlArrayToStringArray((URL[]) newValue);
        }

        if (!Arrays.equals(oldValue, strValue)) {
            if (m_chooser != null)
                m_chooser.setSelectedValues(strValue);
        }
    }

    /**
     * Default implementation is to tokenize the string into potentially multiple url strings separated by the ','
     * character. May be subclassed to provide alternate tokenization.
     *
     * @param s The full url string that may be split.
     * @return An array of url strings. If the incoming string represents a single url string then the array will
     *         contain a single item.
     */
    protected String[] splitStringIntoArray(String s) {
        StringTokenizer st = new StringTokenizer(s, ",");
        List list = new ArrayList();
        while (st.hasMoreTokens())
            list.add(st.nextToken().trim());

        return (String[]) list.toArray(new String[0]);
    }

    // *.wsdl=WSDL Files, *.xml=XML Files
    public void setArgs(String args) {
        URLChooserFilter filters = new URLChooserFilter();

        StringTokenizer st = new StringTokenizer((args != null) ? args : "", ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();

            int index = token.indexOf('=');
            String key, value;

            if (index == -1) {
                key = token;
                value = token;
            } else {
                key = token.substring(0, index).trim();
                value = token.substring(index + 1).trim();
            }

            if (!key.equals("*.*"))
                filters.put(key, value);
        }

        if (m_chooser != null) {
            if (!filters.isEmpty()) {
                m_chooser.setFilters(filters);
            }
        }
    }

    /**
     * Handles a default selection event from the text control by applying the editor value and deactivating this cell
     * editor.
     *
     * @param event the selection event
     * @since 3.0
     */
    protected void handleDefaultSelection(SelectionEvent event) {
        strValue = m_chooser.getSelectedValues();

        // same with enter-key handling code in keyReleaseOccured(e);
        fireApplyEditorValue();
        deactivate();
    }

    /**
     * Processes a key release event that occurred in this cell editor.
     * <p/>
     * The <code>TextCellEditor</code> implementation of this framework method ignores when the RETURN key is pressed
     * since this is handled in <code>handleDefaultSelection</code>. An exception is made for Ctrl+Enter for
     * multi-line texts, since a default selection event is not sent in this case.
     * </p>
     *
     * @param keyEvent the key event
     */
    protected void keyReleaseOccured(KeyEvent keyEvent) {
        Text text = m_chooser.getTextControl();

        if (keyEvent.character == '\r') { // Return key
            // Enter is handled in handleDefaultSelection.
            // Do not apply the editor value in response to an Enter key event
            // since this can be received from the IME when the intent is -not-
            // to apply the value.
            // See bug 39074 [CellEditors] [DBCS] canna input mode fires bogus event from Text Control
            //
            // An exception is made for Ctrl+Enter for multi-line texts, since
            // a default selection event is not sent in this case.
            if (text != null && !text.isDisposed() && (text.getStyle() & SWT.MULTI) != 0) {
                if ((keyEvent.stateMask & SWT.CTRL) != 0) {
                    super.keyReleaseOccured(keyEvent);
                }
            }

            return;
        }

        if (text != null && !text.isDisposed() && (text.getStyle() & SWT.SINGLE) != 0) {
            if (keyEvent.keyCode == SWT.F3) {
                m_chooser.openInEditor();
            }
        }

        super.keyReleaseOccured(keyEvent);
    }

    public URL[] acceptDrop(DropTargetEvent data) {
        return m_chooser.acceptDrop(data);
    }

    public URLChooser getURLChooser() {
        return m_chooser;
    }

}
