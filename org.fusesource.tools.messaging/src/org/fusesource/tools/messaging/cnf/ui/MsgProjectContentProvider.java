/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.MessageEvent;
import org.fusesource.tools.messaging.cnf.model.BaseComponent;
import org.fusesource.tools.messaging.cnf.model.BaseGroupComponent;
import org.fusesource.tools.messaging.cnf.model.DataModelManager;
import org.fusesource.tools.messaging.cnf.model.IModelConstants;
import org.fusesource.tools.messaging.cnf.model.ListenerComponent;
import org.fusesource.tools.messaging.cnf.model.Listeners;
import org.fusesource.tools.messaging.cnf.model.ListenersRootComponent;
import org.fusesource.tools.messaging.cnf.model.SenderComponent;
import org.fusesource.tools.messaging.cnf.model.Senders;
import org.fusesource.tools.messaging.cnf.model.SendersRootComponent;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.IMessageChangeListener;
import org.fusesource.tools.messaging.core.ISender;

/**
 * Content provider implementation for Messaging Project
 */
public class MsgProjectContentProvider implements ITreeContentProvider {
    public static String SENDERS_TEXT = "Producers";
    public static String LISTENERS_TEXT = "Consumers";
    private StructuredViewer viewer;
    private final MsgProjectChangeHandler changeHandler;
    private final MessageChangeListener msgChangeListener;

    public MsgProjectContentProvider() {
        changeHandler = new MsgProjectChangeHandler();
        msgChangeListener = new MessageChangeListener();
    }

    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof IProject) {
            return getRootNodes((IProject) parentElement);
        } else if (parentElement instanceof SendersRootComponent) {
            return getSenderNodes((SendersRootComponent) parentElement);
        } else if (parentElement instanceof ListenersRootComponent) {
            return getListenerNodes((ListenersRootComponent) parentElement);
        }
        return IConstants.NO_CHILDREN;
    }

    private Object[] getRootNodes(IProject parentElement) {
        IFile sendersFile = (parentElement).getFile(IModelConstants.SENDERS_FILE_PATH);
        IFile listenersFile = (parentElement).getFile(IModelConstants.LISTENERS_FILE_PATH);

        return new Object[] { new SendersRootComponent(SENDERS_TEXT, sendersFile),
                new ListenersRootComponent(LISTENERS_TEXT, listenersFile) };
    }

    private Object[] getSenderNodes(SendersRootComponent root) {
        Senders senders = (Senders) DataModelManager.getInstance().getModel(root.getFile());
        if (senders == null) {
            return IConstants.NO_CHILDREN;
        }
        List<SenderComponent> senderNodes = new ArrayList<SenderComponent>();
        for (Object object : senders.getChildren()) {
            senderNodes.add(new SenderComponent(root, (ISender) object));
        }
        return senderNodes.toArray();
    }

    private Object[] getListenerNodes(ListenersRootComponent root) {
        Listeners listeners = (Listeners) DataModelManager.getInstance().getModel(root.getFile());
        if (listeners == null) {
            return IConstants.NO_CHILDREN;
        }
        List<ListenerComponent> listenerNodes = new ArrayList<ListenerComponent>();
        for (Object object : listeners.getChildren()) {
            IListener listener = (IListener) object;
            listenerNodes.add(new ListenerComponent(root, listener));
            // TODO remove the old one??
            listener.getMessagesManager().removeMessageChangeListener(msgChangeListener);
            listener.getMessagesManager().addMessageChangeListener(msgChangeListener);
        }
        root.setChildren(listenerNodes.toArray());
        return listenerNodes.toArray();
    }

    public Object getParent(Object element) {
        if (element instanceof BaseComponent) {
            return ((BaseComponent) element).getParent();
        } else if (element instanceof BaseGroupComponent) {
            return ((BaseGroupComponent) element).getFile().getProject();
        }
        return null;
    }

    public boolean hasChildren(Object element) {
        if (element instanceof SendersRootComponent || element instanceof ListenersRootComponent) {
            return true;
        } else if (element instanceof SenderComponent || element instanceof ListenerComponent) {
            return false;
        }
        return false;
    }

    public Object[] getElements(Object inputElement) {
        return IConstants.NO_CHILDREN;
    }

    public void dispose() {
        changeHandler.cleanUp();
    }

    public void inputChanged(Viewer aViewer, Object oldInput, Object newInput) {
        viewer = (TreeViewer) aViewer;
        changeHandler.initViewer(viewer);
    }

    public Viewer getViewer() {
        return viewer;
    }

    class MessageChangeListener implements IMessageChangeListener {
        public void messageChangeEvent(MessageEvent me, int kind) {
            if (me != null) {
                changeHandler.updateUI(me.getSource());
            }
        }

        public void messagesClearedEvent(List<MessageEvent> clearedMsgs) {
            if (clearedMsgs == null || clearedMsgs.size() == 0) {
                return;
            }
            MessageEvent messageEvent = clearedMsgs.get(0);
            changeHandler.updateUI(messageEvent.getSource());
        }
    }
}
