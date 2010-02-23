/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.message.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.fusesource.tools.core.message.Body;
import org.fusesource.tools.core.message.DocumentRoot;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.MessageFactory;
import org.fusesource.tools.core.message.MessagePackage;
import org.fusesource.tools.core.message.Properties;
import org.fusesource.tools.core.message.Property;
import org.fusesource.tools.core.message.util.MessageResourceFactoryImpl;
import org.fusesource.tools.message.MessageConstants;
import org.fusesource.tools.message.editors.MessageEditorConstants;
import org.fusesource.tools.message.extensions.IMessageType;
import org.fusesource.tools.message.extensions.IMessageTypeUI;

public class EMFUtil {

    public static void createHeaderInModel(Map<String, String> defaultHeaders, Message messageModel,
            EditingDomain editingDomain) {
        Properties properties = null;
        try {
            properties = messageModel.getProperties();
            EMFUtil.createNewProviderHeaders(properties, defaultHeaders, editingDomain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createNewProviderHeaders(Properties headers, Map<String, String> defaultMap,
            EditingDomain editingDomain) {

        ArrayList<Command> arrayList = new ArrayList<Command>();
        Set<String> keySet = defaultMap.keySet();

        for (String key : keySet) {
            Collection<?> newChildDescriptors = editingDomain.getNewChildDescriptors(headers, null);
            for (Object object : newChildDescriptors) {
                Object value = ((CommandParameter) object).getValue();
                if (value instanceof Property) {
                    Property property = ((Property) value);
                    property.setName(key);
                    String headerValue = defaultMap.get(key);
                    property.setValue(headerValue);
                    property.setIsheader(true);
                    arrayList.add(CreateChildCommand.create(editingDomain, headers, (object), new StructuredSelection(
                            headers).toList()));
                }
            }
        }
        CompoundCommand compoundCommand = new CompoundCommand(arrayList);
        editingDomain.getCommandStack().execute(compoundCommand);
    }

    /**
     * Get the type from the display type
     * 
     * @param displayType
     * @return
     */
    public static String getMessageType(String displayType) {
        int indexOf = displayType.indexOf(MessageEditorConstants.DISPLAY_TYPE_PREFIX);
        if (indexOf > -1) {
            displayType = displayType.substring(0, indexOf).trim();
        }
        return displayType;
    }

    /**
     * Get the type from the display type
     * 
     * @param displayType
     * @return
     */
    public static String getMessageProvider(String displayType) {
        int start = displayType.lastIndexOf(MessageEditorConstants.DISPLAY_TYPE_PREFIX)
                + MessageEditorConstants.DISPLAY_TYPE_PREFIX.length();
        int end = displayType.lastIndexOf(MessageEditorConstants.DISPLAY_TYPE_SUFFIX);
        return displayType.substring(start, end);
    }

    public static List<String> getDisplayMessageTypes() {
        List<String> displayTypes = new ArrayList<String>();
        final List<IMessageTypeUI> messageTypeExtensions = MessageExtensionsMgr.getInstance()
                .getMessageTypeUIExtensions();

        // The displayable message type format is ${TYPE(ProviderId)
        for (IMessageTypeUI messageTypeExts : messageTypeExtensions) {
            String displayType = formatDisplayType(messageTypeExts.getType(), messageTypeExts.getProviderId());
            displayTypes.add(displayType);
        }
        return displayTypes;
    }

    public static String formatDisplayType(String type, String providerId) {
        StringBuffer buffer = new StringBuffer(type);
        buffer.append(MessageEditorConstants.DISPLAY_TYPE_PREFIX);
        buffer.append(providerId);
        buffer.append(MessageEditorConstants.DISPLAY_TYPE_SUFFIX);
        return buffer.toString();
    }

    public static EObject strToAnyType(String string) throws Exception {
        AnyType anyType = XMLTypeFactory.eINSTANCE.createAnyType();
        FeatureMap featureMap = anyType.getMixed();
        featureMap.add(XMLTypePackage.eINSTANCE.getAnyType_Any(), FeatureMapUtil.createTextEntry(string));
        return anyType;
    }

    public static Message strToMessage(String string) throws Exception {
        MessageFactory factory = MessageFactory.eINSTANCE;
        Message message = factory.createMessage();
        message.setProperties(factory.createProperties());
        Body body = factory.createBody();
        message.setBody(body);
        body.setContent(strToAnyType(string));
        return message;
    }

    public static Resource createResource() {
        final URI epackageURI = URI.createURI(MessagePackage.eINSTANCE.getNsURI());
        final ResourceSet rs = new ResourceSetImpl();
        rs.getPackageRegistry().put(MessagePackage.eNS_URI, MessagePackage.eINSTANCE);
        rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new MessageResourceFactoryImpl());
        final Resource resource = rs.createResource(epackageURI);
        return resource;
    }

    /**
     * @param fileUrl
     * @param fromMessage
     * @throws IOException
     */
    public static void loadContent(Message toMessage, Message fromMessage) throws IOException {
        toMessage.setProperties(fromMessage.getProperties());
        toMessage.setBody(fromMessage.getBody());
        toMessage.setType(fromMessage.getType());
        toMessage.setProviderId(fromMessage.getProviderId());
        toMessage.setProviderName(fromMessage.getProviderName());
    }

    public static void loadHeadersAndMessgeAttributes(String type, Message message, EditingDomain editingDomain) {
        String providerId = EMFUtil.getMessageProvider(type);
        String messageType = EMFUtil.getMessageType(type);
        IMessageTypeUI messageTypeUI = MessageExtensionsMgr.getInstance().getMessageTypeUIExtension(messageType,
                providerId);
        try {
            IMessageType messageTypeExt = MessageExtensionsMgr.getInstance().getMessageTypeExtension(messageType,
                    providerId);
            // IProvider provider =
            // MessagingServersUtil.getProvider(messageTypeUI.getProviderId());
            if (messageTypeExt != null) {
                Map<String, String> defaultHeaders = messageTypeExt.getHeaders();
                EMFUtil.createHeaderInModel(defaultHeaders, message, editingDomain);
            }
        } catch (Exception exception) {
            // TODO: Remove stack trace
            exception.printStackTrace();
        }
        modifyMessageType(messageTypeUI, message, editingDomain);
    }

    /**
     * @param editingDomain
     * @param message
     * @param type
     */
    public static void modifyMessageType(IMessageTypeUI messageTypeUI, Message messageModel, EditingDomain editingDomain) {
        MessagePackage einstance = MessagePackage.eINSTANCE;
        Command typeCommand = SetCommand.create(editingDomain, messageModel, einstance.getMessage_Type(), messageTypeUI
                .getType(), 0);
        Command providerIdCommand = SetCommand.create(editingDomain, messageModel, einstance.getMessage_ProviderId(),
                messageTypeUI.getProviderId(), 0);
        Command providerNameCommand = SetCommand.create(editingDomain, messageModel, einstance
                .getMessage_ProviderName(), messageTypeUI.getProviderId(), 0);
        List<Command> list = new ArrayList<Command>();
        list.add(typeCommand);
        list.add(providerIdCommand);
        list.add(providerNameCommand);
        CompoundCommand create = new CompoundCommand(list);
        editingDomain.getCommandStack().execute(create);
    }

    public static EObject createInitialModel() {
        EClass eClass = ExtendedMetaData.INSTANCE.getDocumentRoot(MessagePackage.eINSTANCE);
        EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature("message");
        EObject rootObject = MessageFactory.eINSTANCE.create(eClass);
        rootObject.eSet(eStructuralFeature, EcoreUtil.create((EClass) eStructuralFeature.getEType()));
        return rootObject;
    }

    public static void saveMessageToDisk(final IFile modelFile, Message messageType) throws IOException {
        MessagePackage messagePackage = MessagePackage.eINSTANCE;
        MessageFactory messageFactory = MessageFactory.eINSTANCE;
        ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
                ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
        BasicCommandStack commandStack = new BasicCommandStack();
        AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack,
                new HashMap<Resource, Boolean>());

        // Create a resource set
        ResourceSet resourceSet = new ResourceSetImpl();

        // Get the URI of the model file.
        URI fileURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);

        // Create a resource for this file.
        Resource resource = resourceSet.createResource(fileURI);

        // Add the initial model object to the contents.
        EObject rootObject = EMFUtil.createInitialModel();
        if (rootObject != null) {
            resource.getContents().add(rootObject);
        }
        EList<EObject> contents = resource.getContents();
        if (contents != null && contents.size() > 0) {
            DocumentRoot object = (DocumentRoot) contents.get(0);
            Message messageModel = object.getMessage();

            CommandParameter commandParameter = new CommandParameter(messageModel, messagePackage
                    .getMessage_Properties(), messageFactory.createProperties(), new StructuredSelection(messageModel)
                    .toList());
            Command command = CreateChildCommand.create(editingDomain, messageModel, commandParameter,
                    new StructuredSelection(messageModel).toList());
            editingDomain.getCommandStack().execute(command);

            if (!MessageConstants.MESSAGE_TYPE.equalsIgnoreCase(EMFUtil.getMessageType(messageType.getType()))) {
                CommandParameter cmdParameter = new CommandParameter(messageModel, messagePackage.getMessage_Body(),
                        messageFactory.createBody(), new StructuredSelection(messageModel).toList());
                Command cmd = CreateChildCommand.create(editingDomain, messageModel, cmdParameter,
                        new StructuredSelection(messageModel).toList());
                editingDomain.getCommandStack().execute(cmd);
            }
            loadContent(messageModel, messageType);
        }
        // Save the contents of the resource to the file system.
        //
        Map<Object, Object> saveOptions = new HashMap<Object, Object>();
        saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
        saveOptions.put(XMLResource.OPTION_ENCODING, MessageConstants.DEFAULT_ENCODING);
        resource.save(saveOptions);
    }

    public static String getMessageBody(AnyType anyType) {
        String value = MessageEditorConstants.EMPTY_STRING;
        FeatureMap mixed = anyType.getMixed();
        for (Entry entry : mixed) {
            if (entry.getValue() instanceof String) {
                value = (String) entry.getValue();
            }
        }
        return value;
    }

    public static EList<EObject> getContents(String fileURL) {
        File file = new File(fileURL);
        URI uri = URI.createFileURI(file.getAbsolutePath());
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource resource = resourceSet.getResource(uri, true);
        EList<EObject> contents = resource.getContents();
        return contents;
    }

    public static Message getMessageModel(String fileURL) {
        EList<EObject> contents = EMFUtil.getContents(fileURL);
        if (contents != null && contents.size() > 0) {
            DocumentRoot object = (DocumentRoot) contents.get(0);
            return object.getMessage();
        }
        return null;
    }

    public static Map<String, Object> getHeaders(Message message) {
        Map<String, Object> headers = new HashMap<String, Object>();
        Properties properties = message.getProperties();
        EList<Property> property = properties.getProperty();
        for (Property pty : property) {
            headers.put(pty.getName(), pty.getValue());
        }
        return headers;
    }

}
