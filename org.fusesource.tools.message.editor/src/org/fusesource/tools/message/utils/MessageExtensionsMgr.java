/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.message.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.fusesource.tools.message.MessageConstants;
import org.fusesource.tools.message.extensions.IMessageType;
import org.fusesource.tools.message.extensions.IMessageTypeUI;

public class MessageExtensionsMgr {

    // IMessageTypeUI Extension Point Constants
    public static final String EXTENSION_CLASS = "class";
    public static final String EXTENSION_ID = "id";
    public static final String EXTENSION_PROVIDERID = "providerid";
    public static final String MESSAGE_TYPE = "type";
    public static final String MESSAGE_SUPPORTED_FILE_EXTENSION = "supportedFileExtension";

    private static MessageExtensionsMgr instance;
    private static List<IMessageTypeUI> messageTypeUIExtns = Collections.emptyList();
    private static List<IMessageType> messageTypeExtns = Collections.emptyList();

    public static MessageExtensionsMgr getInstance() {
        if (instance == null) {
            instance = new MessageExtensionsMgr();
        }
        return instance;
    }

    private MessageExtensionsMgr() {
        loadMessageTypeExtensions();
        loadMessageTypeUIExtensions();
    }

    /**
     * Returns IMessageType contribution for a message type and provider
     * 
     * @param type
     * @return
     */
    public IMessageType getMessageTypeExtension(String type, String providerId) {
        IMessageType msgTypeContributor = findMessageTypeContributor(type, providerId);
        // Check if there are any default contributions for the message type.
        if (msgTypeContributor == null) {
            msgTypeContributor = findMessageTypeContributor(type, MessageConstants.DEFAULT_PROVIDER);
        }
        return msgTypeContributor;
    }

    /**
     * Returns IMessageTypeUI contribution for a message type and provider
     * 
     * @param type
     * @return
     */
    public IMessageTypeUI getMessageTypeUIExtension(String type, String providerID) {
        IMessageTypeUI msgTypeUIContributor = findMessageTypeUIContributor(type, providerID);
        // Check if there are any default contributions for the message type.
        if (msgTypeUIContributor == null) {
            msgTypeUIContributor = findMessageTypeUIContributor(type, MessageConstants.DEFAULT_PROVIDER);
        }
        return msgTypeUIContributor;
    }

    /**
     * Returns IMessageType contribution based on a received message and provider
     * 
     * @param message
     * @param providerId
     * @return
     */
    public IMessageType getMessageTypeExtension(Object message, String providerId) {
        if (message == null) {
            return null;
        }
        IMessageType messageTypeCon = findMessageTypeContributor(message, providerId);

        if (messageTypeCon == null) {
            messageTypeCon = findMessageTypeContributor(message, MessageConstants.DEFAULT_PROVIDER);
        }
        ;
        return messageTypeCon;
    }

    /**
     * Returns IMessageTypeUI contribution based on a received message and provider
     * 
     * @param message
     * @param providerId
     * @return
     */
    public IMessageTypeUI getMessageTypeUIExtension(Object message, String providerId) {
        if (message == null) {
            return null;
        }
        IMessageTypeUI messageTypeCon = findMessageTypeUIContributor(message, providerId);

        if (messageTypeCon == null) {
            messageTypeCon = findMessageTypeUIContributor(message, MessageConstants.DEFAULT_PROVIDER);
        }
        return messageTypeCon;
    }

    /**
     * gives you the list of IMessageTypeUI plug-in extensions
     * 
     * @return List<IMessageTypeUI>
     */
    public List<IMessageTypeUI> getMessageTypeUIExtensions() {
        return messageTypeUIExtns;
    }

    /**
     * gives you the list of IMessageType plug-in extensions
     * 
     * @return List<IMessageType>
     */
    public List<IMessageType> getMessageTypeExtensions() {
        return messageTypeExtns;
    }

    private void loadMessageTypeUIExtensions() {
        messageTypeUIExtns = new ArrayList<IMessageTypeUI>();
        IConfigurationElement[] extensions = getConfigElements(IMessageTypeUI.MESSAGE_TYPE_UI_EXT_PT);
        for (IConfigurationElement extnData : extensions) {
            try {
                IMessageTypeUI participant = (IMessageTypeUI) extnData.createExecutableExtension(EXTENSION_CLASS);
                participant.setProviderId(extnData.getAttribute(EXTENSION_PROVIDERID));
                participant.setType(extnData.getAttribute(MESSAGE_TYPE));
                messageTypeUIExtns.add(participant);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadMessageTypeExtensions() {
        messageTypeExtns = new ArrayList<IMessageType>();
        IConfigurationElement[] extensions = getConfigElements(IMessageType.MESSAGE_TYPE_EXT_PT);
        for (IConfigurationElement extnData : extensions) {
            try {
                IMessageType participant = (IMessageType) extnData.createExecutableExtension(EXTENSION_CLASS);
                participant.setProviderId(extnData.getAttribute(EXTENSION_PROVIDERID));
                participant.setType(extnData.getAttribute(MESSAGE_TYPE));
                participant.setSupportedFileExtension(extnData.getAttribute(MESSAGE_SUPPORTED_FILE_EXTENSION));
                messageTypeExtns.add(participant);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }

    private IConfigurationElement[] getConfigElements(String id) {
        IExtensionPoint xp = Platform.getExtensionRegistry().getExtensionPoint(id);
        if (xp == null) {
            return new IConfigurationElement[0];
        }

        return xp.getConfigurationElements();
    }

    private IMessageType findMessageTypeContributor(String type, String providerId) {
        for (IMessageType messageType : getMessageTypeExtensions()) {
            if (messageType.getType().equals(type) && messageType.getProviderId().equals(providerId)) {
                return messageType;
            }
        }
        return null;
    }

    private IMessageTypeUI findMessageTypeUIContributor(String type, String providerID) {
        for (IMessageTypeUI messageTypeUI : getMessageTypeUIExtensions()) {
            if (messageTypeUI.getType().equals(type) && messageTypeUI.getProviderId().equalsIgnoreCase(providerID)) {
                return messageTypeUI;
            }
        }
        return null;
    }

    private IMessageType findMessageTypeContributor(Object message, String providerId) {
        List<IMessageType> messageTypeExtensions = getMessageTypeExtensions();
        for (IMessageType messageType : messageTypeExtensions) {
            String id = messageType.getProviderId().trim();
            if (messageType.canHandle(message) && id.equals(providerId)) {
                return messageType;
            }
        }
        return null;
    }

    private IMessageTypeUI findMessageTypeUIContributor(Object message, String providerId) {
        List<IMessageTypeUI> messageTypeExtensions = getMessageTypeUIExtensions();
        for (IMessageTypeUI messageTypeUI : messageTypeExtensions) {
            if (messageTypeUI.canHandle(message) && messageTypeUI.getProviderId().equals(providerId)) {
                return messageTypeUI;
            }
        }
        return null;
    }
}
