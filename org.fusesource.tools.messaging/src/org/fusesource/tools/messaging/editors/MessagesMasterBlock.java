/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.editors;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.message.editors.MessageEditorConstants;
import org.fusesource.tools.message.extensions.IMessageType;
import org.fusesource.tools.message.extensions.IMessageTypeUI;
import org.fusesource.tools.message.extensions.IMessageViewerExtension;
import org.fusesource.tools.message.utils.MessageExtensionsMgr;
import org.fusesource.tools.message.utils.MessageManager;
import org.fusesource.tools.messaging.MessageEvent;
import org.fusesource.tools.messaging.cnf.actions.MessagesHistoryAction;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.IMessageChangeListener;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.ui.ImageConstants;
import org.fusesource.tools.messaging.utils.ImagesUtil;

public class MessagesMasterBlock extends MasterDetailsBlock implements IDetailsPageProvider {
    private final FormPage page;

    private MessageReceiverTable receiverTable;

    private Action saveAsAction = null;

    private Action deleteAllAction = null;

    private Action deleteAction = null;

    private TableViewer tableViewer = null;

    private IListener listener = null;

    private MessageEvent currentMessage;

    private IManagedForm managedForm;

    private SectionPart tableSection;

    private IToolBarManager toolBarManager;

    private IMessageViewerExtension currentViewerExt = null;

    public MessagesMasterBlock(FormPage page) {
        this.page = page;
    }

    @Override
    protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
        this.managedForm = managedForm;
        buildTableSection(managedForm, parent);
        MessageEditorInput input = (MessageEditorInput) page.getEditor().getEditorInput();
        listener = input.getListener();
        if (listener != null) {
            showMessages(listener);
        }
        addTableListener();
        addDelKeyListener();
    }

    private void addTableListener() {
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                markAsRead(event);
                updateActions();
                managedForm.fireSelectionChanged(tableSection, event.getSelection());
            }

        });
    }

    private void markAsRead(SelectionChangedEvent event) {
        currentMessage = (MessageEvent) ((StructuredSelection) event.getSelection()).getFirstElement();
        if (currentMessage != null) {
            listener.getMessagesManager().resetFlag(currentMessage, IMessageChangeListener.MESSAGE_READ);
        }
        receiverTable.updateUI();
    }

    protected void updateActions() {
        boolean b = !(tableViewer.getSelection().isEmpty());
        saveAsAction.setEnabled(b);
        deleteAction.setEnabled(b);
        deleteAllAction.setEnabled(b);
    }

    /**
     * creates the message detail section
     * 
     * @param managedForm
     * @param parent
     */
    protected void buildTableSection(final IManagedForm managedForm, Composite parent) {
        FormToolkit toolkit = managedForm.getToolkit();
        Section messageTableSection = toolkit
                .createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
        messageTableSection.setText("Received Messages");
        messageTableSection.setDescription("");
        messageTableSection.marginWidth = 10;
        messageTableSection.marginHeight = 5;

        Composite tableComposite = toolkit.createComposite(messageTableSection, SWT.NONE);
        GridLayout tableSectionLayout = new GridLayout();
        tableSectionLayout.numColumns = 1;
        messageTableSection.setLayout(tableSectionLayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        tableComposite.setLayoutData(gridData);

        tableComposite.setLayout(new FillLayout());
        receiverTable = new MessageReceiverTable();
        receiverTable.createControl(tableComposite, 200);
        tableViewer = receiverTable.getViewer();

        messageTableSection.setClient(tableComposite);
        tableSection = new SectionPart(messageTableSection);
        managedForm.addPart(tableSection);
    }

    /**
     * creates the too bar
     * 
     * @param managedForm
     */
    @Override
    protected void createToolBarActions(IManagedForm managedForm) {

        final ScrolledForm form = managedForm.getForm();
        sashForm.setOrientation(SWT.VERTICAL);
        form.reflow(true);

        addSaveAsAction(getToolBarManager());
        addDeleteAction(getToolBarManager());
        addDeleteAllAction(getToolBarManager());
        addMessagesHistoryAction(getToolBarManager());
    }

    private IToolBarManager getToolBarManager() {
        if (toolBarManager != null) {
            return toolBarManager;
        }
        final ScrolledForm form = managedForm.getForm();
        toolBarManager = form.getToolBarManager();
        return toolBarManager;
    }

    private void addMessagesHistoryAction(IToolBarManager toolBarManager) {
        toolBarManager.add(new MessagesHistoryAction());
    }

    private void addDeleteAllAction(IToolBarManager toolBarManager) {
        deleteAllAction = new Action("Delete All", IAction.AS_PUSH_BUTTON) {
            @Override
            public void run() {
                clearMessages();
                updateActions();
            }
        };
        deleteAllAction.setToolTipText("Delete All");
        deleteAllAction.setImageDescriptor(ImagesUtil.getInstance().getImageDescriptor(
                ImageConstants.MESSAGE_DELETE_ALL));
        deleteAllAction.setEnabled(false);
        toolBarManager.add(deleteAllAction);
    }

    private void addDeleteAction(IToolBarManager toolBarManager) {
        deleteAction = new Action("Delete", IAction.AS_PUSH_BUTTON) {
            @Override
            public void run() {
                deleteMessage();
                updateActions();
            }
        };
        deleteAction.setToolTipText("Delete");
        deleteAction.setImageDescriptor(ImagesUtil.getInstance().getImageDescriptor(ImageConstants.MESSAGE_DELETE));
        deleteAction.setEnabled(false);

        toolBarManager.add(deleteAction);
    }

    private void addSaveAsAction(IToolBarManager toolBarManager) {
        saveAsAction = new Action("Save As", IAction.AS_PUSH_BUTTON) {
            @Override
            public void run() {
                saveMessage();
                updateActions();
            }
        };
        saveAsAction.setToolTipText("Save As");
        saveAsAction.setImageDescriptor(ImagesUtil.getInstance().getImageDescriptor(ImageConstants.MESSAGE_SAVE));
        saveAsAction.setEnabled(false);
        toolBarManager.add(saveAsAction);
    }

    @Override
    protected void registerPages(DetailsPart detailsPart) {
        detailsPart.setPageProvider(this);
    }

    public void showMessages(IListener listener) {
        receiverTable.setInput(listener);
    }

    protected void clearMessages() {
        boolean answer = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Delete All",
                "Are you sure you want to delete all messages from the listener?");
        if (!answer) {
            return;
        }

        listener.getMessagesManager().clearAllMessages();
        receiverTable.reloadTableData();
    }

    protected void deleteMessage() {
        ISelection selection = tableViewer.getSelection();
        MessageEvent messageEvent = (MessageEvent) ((StructuredSelection) selection).getFirstElement();
        if (messageEvent == null) {
            return;
        }
        boolean answer = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Delete",
                "Are you sure you want to delete the message from the listener?");
        if (!answer) {
            return;
        }

        listener.getMessagesManager().removeMessage(messageEvent);
        tableViewer.setInput(listener.getMessagesManager().getMessages());
        receiverTable.updateUI();
    }

    protected void saveMessage() {
        Object obj = getSelectedItem();
        if (obj != null) {
            try {
                SaveAsDialog dlg = new SaveAsDialog(Display.getCurrent().getActiveShell());
                if (dlg.open() == Window.OK) {
                    IPath path = dlg.getResult();
                    if (path.getFileExtension() == null) {
                        path = path.addFileExtension(MessageEditorConstants.MESSAGE_FILE_EXTENSION);
                    }
                    if (path != null) {
                        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
                        MessageEvent messageEvent = (MessageEvent) obj;
                        String id = messageEvent.getSource().getDestination().getConnection().getProvider().getId();
                        IMessageType messageType = MessageExtensionsMgr.getInstance().getMessageTypeExtension(
                                messageEvent.getMessage(), id);
                        if (messageType == null) {
                            // FIXME should fix this later, passing JMS id for now
                            messageType = MessageExtensionsMgr.getInstance().getMessageTypeExtension(
                                    messageEvent.getMessage(), JMSConstants.DEFAULT_JMS_PROVIDER);
                        }
                        Message messageToSave = messageType.convertMessage(messageEvent.getMessage());
                        MessageManager.save(file, messageToSave, getProgressMonitor());
                    }
                }
                dlg.close();
            } catch (Exception ex) {
                ex.printStackTrace(); // TODO : Fix this - show error dialog
            }
        }
    }

    protected Object getSelectedItem() {
        Object obj = ((StructuredSelection) getViewer().getSelection()).getFirstElement();
        return obj;
    }

    public static IProgressMonitor getProgressMonitor() {
        return new NullProgressMonitor();
    }

    public TableViewer getViewer() {
        return tableViewer;
    }

    public MessageEvent getCurrentMessage() {
        return currentMessage;
    }

    public IDetailsPage getPage(Object arg0) {
        if (arg0 instanceof IMessageTypeUI) {
            IMessageTypeUI messageTypeUI = (IMessageTypeUI) arg0;
            IMessageViewerExtension createViewerUI = messageTypeUI.getViewerExtension();
            updateBlockOnSelectionChange(createViewerUI);
            return createViewerUI.getDetailsPage();
        }
        return null;
    }

    public Object getPageKey(Object arg0) {

        Object keyObject = null;
        MessageEvent messageEvent = (MessageEvent) arg0;
        String id = messageEvent.getSource().getDestination().getConnection().getProvider().getId();
        keyObject = MessageExtensionsMgr.getInstance().getMessageTypeUIExtension(messageEvent.getMessage(), id);
        if (keyObject == null) {
            keyObject = MessageExtensionsMgr.getInstance().getMessageTypeUIExtension(messageEvent.getMessage(),
                    JMSConstants.DEFAULT_JMS_PROVIDER);
        }
        return keyObject;
    }

    public void updateBlockOnSelectionChange(IMessageViewerExtension newViewerExt) {
        IToolBarManager toolBar = getToolBarManager();
        Collection<Action> actions = null;
        if (currentViewerExt != null) {
            actions = currentViewerExt.getActions();
            for (Action action : actions) {
                toolBar.remove(new ActionContributionItem(action));
            }
        }
        currentViewerExt = newViewerExt;
        actions = currentViewerExt.getActions();
        for (Action action : actions) {
            toolBar.add(new ActionContributionItem(action));
        }
        toolBar.update(true);
    }

    public void clearBlock() {
        if (receiverTable != null) {
            receiverTable.removeNotificationsListener();
        }
    }

    private void addDelKeyListener() {
        tableViewer.getTable().addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (SWT.DEL == e.keyCode) {
                    deleteMessage();
                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });
    }
}
