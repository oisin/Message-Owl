/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.navigator.IDescriptionProvider;
import org.eclipse.wst.server.core.IServer;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.cnf.model.BaseGroupComponent;
import org.fusesource.tools.messaging.cnf.model.ListenerComponent;
import org.fusesource.tools.messaging.cnf.model.ListenersRootComponent;
import org.fusesource.tools.messaging.cnf.model.SenderComponent;
import org.fusesource.tools.messaging.cnf.model.SendersRootComponent;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IDestinationType;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.IMessageChangeListener;
import org.fusesource.tools.messaging.core.IMessagesManager;
import org.fusesource.tools.messaging.core.ISender;
import org.fusesource.tools.messaging.server.MessagingServersUtil;
import org.fusesource.tools.messaging.ui.FontBasedLabelProvider;
import org.fusesource.tools.messaging.ui.ImageConstants;
import org.fusesource.tools.messaging.utils.ImagesUtil;

/**
 * Label provider implementation for Messaging Project
 */

public class MsgProjectLabelProvider extends FontBasedLabelProvider implements IDescriptionProvider {
    private static final String TYPE_INFO_UNAVAILABLE = "TYPE INFO UNAVAILABLE";
    private static final String STATUS_OFFLINE_SUFFIX = " is Offline";
    private static final String STATUS_ONLINE_SUFFIX = " is Online";

    @Override
    public String getText(Object element) {
        if (element instanceof SendersRootComponent) {
            return ((SendersRootComponent) element).getName();
        } else if (element instanceof ListenersRootComponent) {
            return ((ListenersRootComponent) element).getName();
        } else if (element instanceof SenderComponent) {
            ISender sender = ((SenderComponent) element).getSender();
            if (hasLabelProvider(sender)) {
                return ((LabelProvider) sender).getText(sender);
            }
            return getDefaultText(sender.getDestination());
        } else if (element instanceof ListenerComponent) {
            IListener listener = ((ListenerComponent) element).getListener();
            return getListenerDisplayText(listener);
        }
        return null;
    }

    private boolean hasLabelProvider(Object element) {
        if (element instanceof LabelProvider) {
            return true;
        }
        return false;
    }

    @Override
    public Image getImage(Object element) {
        ImagesUtil instance = ImagesUtil.getInstance();
        if (element instanceof SendersRootComponent) {
            return instance.getImage(ImageConstants.SENDER_COMPONENT_IMAGE);
        } else if (element instanceof ListenersRootComponent) {
            return instance.getImage(ImageConstants.LISTENER_COMPONENT_IMAGE);
        } else if (element instanceof SenderComponent) {
            ISender sender = ((SenderComponent) element).getSender();
            if (hasLabelProvider(sender)) {
                return ((LabelProvider) sender).getImage(sender);
            }
            return instance.getImage(ImageConstants.SENDER);
        } else if (element instanceof ListenerComponent) {
            IListener listener = ((ListenerComponent) element).getListener();
            if (hasLabelProvider(listener)) {
                return ((LabelProvider) listener).getImage(listener);
            }
            return instance.getImage(ImageConstants.LISTENER);
        }
        return null;
    }

    @Override
    public Font getFont(Object element) {
        if (element instanceof ListenerComponent) {
            IListener listener = ((ListenerComponent) element).getListener();
            IMessagesManager messagesManager = listener.getMessagesManager();
            if (messagesManager == null) {
                return MsgProjectChangeHandler.DEFAULT_FONT;
            }
            int unreadCount = messagesManager.getMessagesCount(IMessageChangeListener.MESSAGE_UNREAD);
            if (unreadCount > 0) {
                return MsgProjectChangeHandler.BOLD_FONT;// TODO not
                // good...temp code
            }
        }
        return MsgProjectChangeHandler.DEFAULT_FONT;// TODO not good...temp
        // code
    }

    private String getListenerDisplayText(IListener listener) {
        StringBuffer displayText = new StringBuffer();
        if (hasLabelProvider(listener)) {
            displayText.append(((LabelProvider) listener).getText(listener));
        } else {
            displayText.append(getDefaultText(listener.getDestination()));
        }
        if (listener.getMessagesManager() == null) {
            return displayText.toString();
        }
        // Append the message received count
        int unreadCount = listener.getMessagesManager().getMessagesCount(IMessageChangeListener.MESSAGE_UNREAD);
        if (unreadCount <= 0) {
            return displayText.toString();
        }
        displayText.append(" (" + unreadCount + ")");
        return displayText.toString();
    }

    public String getDefaultText(IDestination destination) {
        return destination != null ? destination.getDestinationName() : IConstants.EMPTY_STRING;
    }

    /**
     * shows the type & name of the selected project viewer element in the status bar
     */
    public String getDescription(Object anElement) {
        if (anElement instanceof IProject) {
            IProject project = (IProject) anElement;
            IServer deployedServer = MessagingServersUtil.getDeployedServer(project);
            if (deployedServer != null) {
                return project.getName() + " is deployed in " + deployedServer.getName();
            }
        } else if (anElement instanceof SenderComponent) {
            ISender sender = ((SenderComponent) anElement).getSender();
            return getType(sender.getDestination()) + ": " + getDefaultText(sender.getDestination());
        } else if (anElement instanceof ListenerComponent) {
            IListener listener = ((ListenerComponent) anElement).getListener();
            return getType(listener.getDestination()) + ": " + getDefaultText(listener.getDestination());
        } else if (anElement instanceof BaseGroupComponent) {
            return getStatusMessage((BaseGroupComponent) anElement);
        }
        return null;
    }

    public String getType(IDestination destination) {
        if (destination == null) {
            return TYPE_INFO_UNAVAILABLE;
        }
        IDestinationType type = destination.getDestinationType();
        if (type == null) {
            return TYPE_INFO_UNAVAILABLE;
        }
        return type.getType();
    }

    private String getStatusMessage(BaseGroupComponent group) {
        IProject project = group.getFile().getProject();
        String status = project.getName();
        boolean isOnline = MessagingServersUtil.isMsgProjectOnline(project);
        if (isOnline) {
            return status + STATUS_ONLINE_SUFFIX;
        }
        return status + STATUS_OFFLINE_SUFFIX;
    }
}
