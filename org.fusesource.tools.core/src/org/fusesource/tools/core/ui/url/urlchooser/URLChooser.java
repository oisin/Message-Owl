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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionValidator;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser.FileSystemProvider;
import org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser.LocalDirectorySystemProvider;
import org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser.LocalFileSystemProvider;
import org.fusesource.tools.core.ui.url.urlchooser.workspacechooser.ResourceChooserProvider;
import org.fusesource.tools.core.ui.url.urlchooser.workspacechooser.WorkspaceChooserProvider;
import org.fusesource.tools.core.util.ResourceUtil;

/**
 */
public class URLChooser extends AbstractChooser {

    private static List newWizardCategories = new ArrayList();
    private final Map fileSystemProviders = Collections.synchronizedMap(new HashMap());
    /**
     * Style Constant to show History
     */
    public static final int HIDE_HISTORY = 16;
    private URLChooserFilter filter;
    private final List listeners = Collections.synchronizedList(new ArrayList(3));
    private MenuItem recent;
    private Menu recentMenu;
    private final List recentFiles = new LinkedList();
    private static final int RECENT_FILES_COUNT = 10;
    protected static final String DEFAULT_HISTORY_CONTEXT = "default.context";
    protected String historyContext = null;
    private final DefaultURLChooserPrefHandler urlChooserPrefHandler = DefaultURLChooserPrefHandler.getInstance();

    static {
        newWizardCategories = new ArrayList();
        newWizardCategories.add("org.fusesource.tools.newWizard");
    }

    protected URLChooser() {
    }

    public URLChooser(Composite parent) {
        this(parent, Collections.EMPTY_LIST, Collections.EMPTY_LIST, STYLE_NONE);
    }

    public URLChooser(Composite parent, int style) {
        this(parent, style, DEFAULT_HISTORY_CONTEXT);
    }

    public URLChooser(Composite parent, int style, String context) {
        this(parent, Collections.EMPTY_LIST, Collections.EMPTY_LIST, style, context);
    }

    public URLChooser(Composite parent, List supportedProviderIds, int style) {
        this(parent, supportedProviderIds, Collections.EMPTY_LIST, style);
    }

    public URLChooser(Composite parent, List supportedProviderIds, int style, String context) {
        this(parent, supportedProviderIds, Collections.EMPTY_LIST, style, context);
    }

    public URLChooser(Composite parent, List supportedProviderIds, String context) {
        this(parent, supportedProviderIds, Collections.EMPTY_LIST, STYLE_NONE, context);
    }

    /**
     * Provide a list of provider ids that is supported by this instance of the FileChooser. This
     * method may not be called again.
     * 
     * @param supportedProviderIds
     *            MAY BE NULL
     */
    public URLChooser(Composite parent, List supportedProviderIds, List tempFSProviders, int style) {
        this(parent, supportedProviderIds, tempFSProviders, null, style, DEFAULT_HISTORY_CONTEXT);
    }

    public URLChooser(Composite parent, List supportedProviderIds, List tempFSProviders, int style, String context) {
        this(parent, supportedProviderIds, tempFSProviders, null, style, context);
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
    public URLChooser(Composite parent, List supportedProviderIds, List tempFSProviders,
            URLChooserChangeListener listener, int style) {
        this(parent, tempFSProviders, supportedProviderIds, listener, style, DEFAULT_HISTORY_CONTEXT);
    }

    public URLChooser(Composite parent, List supportedProviderIds, List tempFSProviders,
            URLChooserChangeListener listener, int style, String context) {
        if (context == null) {
            historyContext = DEFAULT_HISTORY_CONTEXT;
        } else {
            historyContext = context;
        }
        loadFileSystemProviders();
        init(parent, tempFSProviders, supportedProviderIds, listener, style);
    }

    protected void init(Composite parent, List tempFSProviders, List supportedProviderIds,
            URLChooserChangeListener listener, int style) {
        super.init(parent, tempFSProviders, supportedProviderIds, null, style);
        if (listener != null) {
            addURLChangeListener(listener);
        }
    }

    @Override
    protected void openSelectedValue() {
        openInEditor();
    }

    protected void openInEditor() {
        URL url = getSelectedValueAsURL();

        if (url != null) {
            for (Iterator iterator = actionProviders.iterator(); iterator.hasNext();) {
                FileSystemProvider provider = (FileSystemProvider) iterator.next();
                if (provider.validate(url)) {
                    provider.open(url);
                }
            }
        }
    }

    public URL[] acceptDrop(DropTargetEvent data) {
        Object[] objects = acceptObjectDrop(data);
        return toUrlArray(objects);
    }

    private URL[] toUrlArray(Object[] objects) {
        if (objects == null) {
            return null;
        }
        List retVal = new ArrayList();
        for (Object object : objects) {
            if (object instanceof URL) {
                retVal.add(object);
            }
        }
        return (URL[]) retVal.toArray(new URL[retVal.size()]);
    }

    @Override
    protected Collection getProvidersList(List providersList) {
        if (providersList.size() == 0) {
            return fileSystemProviders.values();
        }
        List retList = new ArrayList(providersList.size());
        for (Iterator iterator = providersList.iterator(); iterator.hasNext();) {
            String id = iterator.next().toString();
            Object o = fileSystemProviders.get(id);
            if (o != null) {
                retList.add(o);
            }
        }
        return retList;
    }

    public void setPathAsRelative() {
        showRelativeFilePath = true;
    }

    public URL[] getSelectedValuesAsURL() {
        if (selectedValues == null || selectedValues.length == 0) {
            return null;
        }
        return stringArrayToURLArray(selectedValues);
    }

    public String getSelectedValue() {
        if (selectedValues == null || selectedValues.length == 0) {
            return null;
        }

        return selectedValues[0].toString();
    }

    public String[] getSelectedValues() {
        return toStringArray(selectedValues);
    }

    public URL getSelectedValueAsURL() {
        String value = getSelectedValue();

        if (value == null) {
            return null;
        }

        try {
            return new URL(value);
        } catch (Exception e) {
        }
        return null;
    }

    public void setSelectedValue(URL value) {
        setSelectedValue(value == null ? (String) null : value.toString());
    }

    public void setSelectedValues(String[] values) {
        // Only update if there's a change
        if (Arrays.equals(selectedValues, values)) {
            return;
        }

        selectedValues = new String[(values != null) ? values.length : 0];

        if (selectedValues.length > 0) {
            // Check because arraycopy will throw a NPE if 'values' is null even
            // if the copy length is ZERO :(
            System.arraycopy(values, 0, selectedValues, 0, selectedValues.length);
        }
        updateSelectedValues(selectedValues);
    }

    public void setSelectedValues(URL[] values) {
        setSelectedValues(urlArrayToStringArray(values));
    }

    public void addURLChangeListener(URLChooserChangeListener l) {
        if (l != null && !listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void removeURLChangeListener(URLChooserChangeListener l) {
        listeners.remove(l);
    }

    @Override
    protected boolean isSelectedValueValid() {
        return (getSelectedValueAsURL() != null);
    }

    public static void addNewWizardCategory(String category) {
        newWizardCategories.add(category);
    }

    public static void removeNewWizardCategory(String category) {
        newWizardCategories.remove(category);
    }

    @Override
    protected void addNewMenuItems(Menu newMenu) {
        for (Iterator iter = newWizardCategories.iterator(); iter.hasNext();) {
            String categoryName = (String) iter.next();

            IWizardCategory category = PlatformUI.getWorkbench().getNewWizardRegistry().findCategory(categoryName);
            if (category == null) {
                continue;
            }

            IWizardDescriptor[] W = category.getWizards();
            for (IWizardDescriptor element : W) {
                IWizardDescriptor wd = element;

                if (isProjectWizard(wd)) {
                    continue;
                }

                if (!isNewWizardProvider(wd)) {
                    continue;
                }

                MenuItem miNewItem = new MenuItem(newMenu, SWT.NONE);
                miNewItem.setText(wd.getLabel());
                miNewItem.setData(wd.getId());
                if (wd.getImageDescriptor() != null) {
                    miNewItem.setImage(wd.getImageDescriptor().createImage());
                }
                miNewItem.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent event) {
                        launchWizard((String) ((MenuItem) event.getSource()).getData());
                    }
                });
            }
        }
    }

    protected void launchWizard(String id) {
        IWizardDescriptor wd = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(id);

        if (wd == null) {
            return;
        }

        try {
            INewWizard wizard = (INewWizard) wd.createWizard();

            wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
            if (m_customizationProvider != null) {
                ((NewWizardProvider) wizard).setNewParameterMap(m_customizationProvider.getCustomizationMap());
            }
            ((NewWizardProvider) wizard).setUrlChooser(this);

            WizardDialog dialog = new WizardDialog(parent.getShell(), wizard);
            if (dialog.open() == Window.OK) {
                // acceptUrl(((NewWizardProvider) wizard).getPrimaryPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptUrl(String path) {
        URL url = null;

        try {
            for (Iterator iterator = actionProviders.iterator(); iterator.hasNext();) {
                FileSystemProvider provider = (FileSystemProvider) iterator.next();
                url = provider.convertToURL(path);
                if (url != null) {
                    updateSelectedValues(new URL[] { url });
                    break;
                }
            }
            if (url == null) {
                url = new URL(path);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isProjectWizard(IWizardDescriptor descriptor) {
        String[] tags = descriptor.getTags();

        for (String tag : tags) {
            if (tag.equals("project")) {
                return true;
            }
        }

        return false;
    }

    private boolean isNewWizardProvider(IWizardDescriptor descriptor) {
        // Don't like this but I can't see any other way of determining if the
        // wizard
        // supports the necessary interface in order for us to extract the new
        // resource
        // URL later on.
        IWorkbenchWizard wizard = null;
        try {
            wizard = descriptor.createWizard();
            if ((wizard == null) || !(wizard instanceof NewWizardProvider)) {
                return false;
            }

            String[] ext = ((NewWizardProvider) wizard).getSupportedExtensions();
            if (!extensionsSupportSubset(ext)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (wizard != null) {
                wizard.dispose();
            }
        }
        return true;
    }

    /**
     * Compares the list of extensions in the parameter with the extension filters set on the
     * URLChooser. The ext parameter does NOT expect a "*." prefix...just the extension.
     * 
     * @param ext
     * @return
     */
    private boolean extensionsSupportSubset(String[] ext) {
        // Missing or filter with just 1 entry means *.*
        if ((filter == null) || (filter.size() == 1)) {
            return true;
        }

        for (Iterator iter = filter.keySet().iterator(); iter.hasNext();) {
            String check = (String) iter.next();

            for (String element : ext) {
                if (check.toLowerCase().endsWith("." + element.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private NewCustomizationProvider m_customizationProvider = null;

    public void setNewCustomizationProvider(NewCustomizationProvider provider) {
        m_customizationProvider = provider;
    }

    @Override
    protected void execActionProvider(final ActionProvider provider) {
        ((FileSystemProvider) provider).setFilters(filter);
        super.execActionProvider(provider);
    }

    // ------------------------------------------------------------------------

    /**
     * Method executes the default (1st) or last executed File System Provider's chooser.
     */
    protected void execFileSystemProvider() {
        execActionProvider();
    }

    @Override
    protected void fireSelectionChanged() {
        fireURLChanged();
    }

    protected void fireURLChanged() {
        isTextModified = false; // this is critical or the text field fires this
        // method again
        List cloned = new ArrayList(listeners);
        updateRecentOnURLChange();
        for (Iterator iterator = cloned.iterator(); iterator.hasNext();) {
            Object listener = iterator.next();
            if (listener instanceof URLChooserChangeListener) {
                try {
                    ((URLChooserChangeListener) listener).urlChanged(toStringArray(selectedValues));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    private void updateRecentOnURLChange() {
        if ((style & HIDE_HISTORY) == 0) {
            for (Object selectedValue : selectedValues) {
                try {
                    URL url = null;
                    if (selectedValue instanceof URL) {
                        url = (URL) selectedValue;
                    } else {
                        url = new URL((String) selectedValue);
                    }
                    updateHistory(url);
                    urlChooserPrefHandler.addURL(historyContext, url);
                } catch (MalformedURLException e) {
                }
            }
        }
    }

    private String[] toStringArray(Object[] selectedValues) {
        if (selectedValues == null) {
            return null;
        }
        String[] strArray = new String[selectedValues.length];
        for (int i = 0; i < strArray.length; i++) {
            strArray[i] = selectedValues[i] != null ? selectedValues[i].toString() : null;
        }
        return strArray;
    }

    /**
     * Takes a Map of extension/name pairs (extension = key, name = value) and sets them up as the
     * filters.
     * 
     * @param filterMap
     */
    public void setFilters(URLChooserFilter filter) {
        // if the filter contains *.* then first remove it and then add it back
        // in at the end
        this.filter = filter;
        if (filter.containsKey("*.*")) {
            filter.remove("*.*");
            filter.put("*.*", "All Files");
        }
    }

    public void addFileSystemProvider(FileSystemProvider provider) {
        fileSystemProviders.put(provider.getID(), provider);
    }

    public void removeFileSystemProvider(FileSystemProvider provider) {
        fileSystemProviders.remove(provider.getID());
    }

    public FileSystemProvider[] getFileSystemProviders() {
        List list = new ArrayList(fileSystemProviders.values());
        return (FileSystemProvider[]) list.toArray(new FileSystemProvider[0]);
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

    public static String[] urlArrayToStringArray(URL[] urls) {
        String[] values = new String[urls.length];

        for (int i = 0; i < urls.length; i++) {
            values[i] = urls[i].toString();
        }

        return values;
    }

    public static URL[] stringArrayToURLArray(Object[] selectedValues) {
        ArrayList list = new ArrayList();

        for (Object selectedValue : selectedValues) {
            try {
                if (selectedValue instanceof URL) {
                    list.add(selectedValue);
                } else {
                    list.add(new URL((String) selectedValue));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (URL[]) list.toArray(new URL[0]);
    }

    protected void populateRecentMenu() {
        recentFiles.clear();
        URL[] urls = urlChooserPrefHandler.getURLs(historyContext);
        if (urls.length == 0 && recent != null) {
            recent.setEnabled(false);
            return;
        }
        URL[] filteredUrls = filterURLS(urls);
        for (int i = 0; i < RECENT_FILES_COUNT && i < filteredUrls.length; i++) {
            recentFiles.add(filteredUrls[i].toString());
        }
        updateRecentMenu();
    }

    private URL[] filterURLS(URL[] urls) {
        if (filter == null) {
            return urls;
        }
        String[] filterExtensions = filter.getFilterExtensions();
        if (filterExtensions.length == 1 && "*.*".equals(filterExtensions[0])) {
            return urls;
        }

        List filteredList = new ArrayList();
        for (URL url2 : urls) {
            String url = url2.toString();
            if ((!url.startsWith("sonic") && !url.startsWith("file")) && !urlHasExtension(url)) {
                filteredList.add(url2);
                continue;
            }
            for (String filterExtension : filterExtensions) {
                String extn = filterExtension.substring(filterExtension.indexOf("*") + 1, filterExtension.length());
                if (url.endsWith(extn)) {
                    filteredList.add(url2);
                    break;
                }
            }
        }
        return (URL[]) filteredList.toArray(new URL[0]);
    }

    private boolean urlHasExtension(String url) {
        int qIdx = url.lastIndexOf('?');
        if (qIdx != -1) {
            return false;
        }
        try {
            URL _url = new URL(url);
            String file = _url.getFile();
            if (file != null) {
                int lastDotIndex = file.lastIndexOf(".");
                return lastDotIndex != -1;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateHistory(URL url) {
        if (url != null) {
            String urlStr = url.toString();
            recentFiles.remove(urlStr);
            recentFiles.add(0, urlStr);
            if (recentFiles.size() > RECENT_FILES_COUNT) {
                recentFiles.remove(recentFiles.get(recentFiles.size() - 1));
            }
            updateRecentMenu();
        }
    }

    private void updateRecentMenu() {
        if (recentMenu != null) {
            MenuItem[] items = recentMenu.getItems();
            for (MenuItem item : items) {
                item.dispose();
            }
            _updateRecentMenu();
        }
    }

    private void addRecentMenu() {
        recent = new MenuItem(getMenu(), SWT.CASCADE);
        recent.setText("Recent");
        recent.setData("RECENT");

        recentMenu = new Menu(getMenu());
        recent.setMenu(recentMenu);
    }

    private void _updateRecentMenu() {
        boolean showRecentFiles = false;
        if (recentFiles.size() > 0 && !showRelativeFilePath) {
            showRecentFiles = true;
        }
        recent.setEnabled(showRecentFiles);
        for (Iterator iterator = recentFiles.iterator(); iterator.hasNext();) {
            String urlStr = (String) iterator.next();
            try {
                final URL url = new URL(urlStr);
                MenuItem mi = new MenuItem(recentMenu, SWT.NONE);
                mi.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        setSelectedValue(url);
                    }
                });
                mi.setText(urlStr);
            } catch (MalformedURLException e) {
            }
        }
    }

    @Override
    protected void addCustomMenus() {
        super.addCustomMenus();
        if ((style & HIDE_HISTORY) == 0) {
            addRecentMenu();
            populateRecentMenu();
        }
    }

    public void removeURL(URL url) {
        urlChooserPrefHandler.removeURL(historyContext, url);
        recentFiles.remove(url.toString());
        updateRecentMenu();
    }

    public String getInputFile(URL selectedUrl) {
        File file = new File(selectedUrl.getFile());
        return file.getAbsolutePath();
    }

    private void loadFileSystemProviders() {
        try {
            addFileSystemProvider(new LocalFileSystemProvider());
            addFileSystemProvider(new LocalDirectorySystemProvider());
            WorkspaceChooserProvider provider = new WorkspaceChooserProvider();
            provider.setSelectionValidator(new ISelectionValidator() {
                public String isValid(Object selection) {
                    if (selection instanceof IPath) {
                        return ((IPath) selection).toFile().isFile() ? null : "Folders cannot be selected.";
                    }
                    return "Invalid selection.";
                }
            });
            addFileSystemProvider(provider);
            if (ResourceUtil.getCurrentActiveFile() != null) {
                ResourceChooserProvider resourceProvider = new ResourceChooserProvider();
                resourceProvider.setSelectionValidator(new ISelectionValidator() {
                    public String isValid(Object selection) {
                        if (selection instanceof IPath) {
                            return ((IPath) selection).toFile().isFile() ? null : "Folders cannot be selected.";
                        }
                        return "Invalid selection.";
                    }
                });
                addFileSystemProvider(resourceProvider);
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
