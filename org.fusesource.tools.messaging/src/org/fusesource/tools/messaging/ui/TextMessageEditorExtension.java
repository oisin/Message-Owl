/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.messaging.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.fusesource.tools.core.message.Body;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.MessagePackage;
import org.fusesource.tools.core.ui.TextViewerComponent;
import org.fusesource.tools.core.ui.url.urlchooser.AbstractChooser;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooser;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserChangeListener;
import org.fusesource.tools.core.ui.url.urlchooser.URLChooserFilter;
import org.fusesource.tools.core.ui.url.urlchooser.filesystemchooser.LocalFileSystemProvider;
import org.fusesource.tools.core.ui.url.urlchooser.workspacechooser.WorkspaceChooserProvider;
import org.fusesource.tools.message.utils.EMFUtil;

public class TextMessageEditorExtension extends SimpleMessageEditorExtension {

    private static String[] extensions = new String[] { "*.txt" };

    protected SourceViewer textEditor = null;

    @Override
    public void createBody(Composite parent, EditingDomain editingDomain, Message messageModel) {
        this.messageModel = messageModel;
        this.editingDomain = editingDomain;
        Body bodyType = messageModel.getBody();

        GridLayout gridLayout = new GridLayout();
        parent.setLayout(gridLayout);
        gridLayout.numColumns = 5;

        GridData data = new GridData();
        final Button fromFileButton = new Button(parent, SWT.RADIO);
        fromFileButton.setText("From File:");
        data.horizontalSpan = 2;
        fromFileButton.setLayoutData(data);
        fromFileButton.setSelection(false);

        data = new GridData();

        ArrayList lst = new ArrayList();
        lst.add(WorkspaceChooserProvider.ID);
        lst.add(LocalFileSystemProvider.ID);
        final URLChooser chooser = new URLChooser(parent, lst, AbstractChooser.STYLE_NONE);
        final Text fileUrl = chooser.getTextControl();
        chooser.setEnabled(false);
        chooser.setFilters(new URLChooserFilter(getExtensions()));
        data.horizontalSpan = 2;
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        Composite ui = chooser.getUI();
        ui.setLayoutData(data);
        chooser.addURLChangeListener(new URLChooserChangeListener() {
            public void urlChanged(String[] urls) {
                if (fileUrl != null) {
                    URL selectedURL = chooser.getSelectedValueAsURL();
                    if (selectedURL != null) {
                        String text = chooser.getInputFile(selectedURL);
                        modifyBodySource(text, true, true);
                    }
                }
            }
        });

        data = new GridData();
        final Button edit = new Button(parent, SWT.RADIO);
        edit.setText("Edit:");
        data.horizontalSpan = 5;
        edit.setLayoutData(data);
        edit.setSelection(true);

        createViewer(parent);
        data = new GridData();
        data.horizontalSpan = 5;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.FILL;
        textEditor.getControl().setLayoutData(data);

        if (bodyType != null) {
            EObject content = bodyType.getContent();
            if (content instanceof AnyTypeImpl) {
                AnyTypeImpl anyTypeImpl = (AnyTypeImpl) content;
                FeatureMap mixed = anyTypeImpl.getMixed();
                for (Entry entry : mixed) {
                    if (entry.getValue() instanceof String) {
                        textEditor.getDocument().set((String) entry.getValue());
                    }
                }
            }
            String string = bodyType.getFileRef();
            if (string != null) {
                fileUrl.setText(string);
            }
            if (bodyType.isUseFileRef()) {
                fromFileButton.setSelection(true);
                fileUrl.setEnabled(true);
                chooser.setEnabled(true);
                textEditor.setEditable(false);
                edit.setSelection(false);
            }
        } else {
            setContentToModel("");
        }

        fromFileButton.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if (event.widget == fromFileButton) {
                    fileUrl.setEnabled(true);
                    chooser.setEnabled(true);
                    textEditor.setEditable(false);
                    modifyBodySource(null, false, true);
                }
            }
        });

        edit.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if (event.widget == edit) {
                    fileUrl.setEnabled(false);
                    chooser.setEnabled(false);
                    textEditor.setEditable(true);
                    modifyBodySource(null, false, false);
                }
            }
        });

        textEditor.getDocument().addDocumentListener(new IDocumentListener() {
            public void documentAboutToBeChanged(DocumentEvent arg0) {
            }

            public void documentChanged(DocumentEvent arg0) {
                setContentToModel(textEditor.getDocument().get());
            }
        });

        fileUrl.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                modifyBodySource(fileUrl.getText(), true, true);
            }
        });
    }

    protected void createViewer(Composite parent) {
        textEditor = TextViewerComponent.createTextViewer(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    }

    protected String[] getExtensions() {
        return extensions;
    }

    /**
	 * 
	 */
    protected void setContentToModel(String anyType) {

        Body body = messageModel.getBody();
        if (body == null) {
            Collection<?> newChildDescriptors = editingDomain.getNewChildDescriptors(messageModel, null);
            CommandParameter commandParameter = null;
            for (Object object : newChildDescriptors) {
                if (((CommandParameter) object).getValue() instanceof Body) {
                    commandParameter = (CommandParameter) object;
                    break;
                }
            }
            Command create = CreateChildCommand.create(editingDomain, messageModel, commandParameter,
                    new StructuredSelection(messageModel).toList());
            editingDomain.getCommandStack().execute(create);
            body = messageModel.getBody();
        }
        setContentToModel(body, anyType);
    }

    protected void setContentToModel(Body body, String contentStr) {
        try {
            EObject content = body.getContent();
            EObject newContent = EMFUtil.strToAnyType(contentStr);
            Command create = null;
            if (content instanceof AnyType) {
                AnyType contentData = (AnyType) newContent;
                create = SetCommand.create(editingDomain, body, MessagePackage.eINSTANCE.getBody_Content(),
                        contentData, 0);
            } else if (content == null) {
                Collection<?> newChildDescriptors = editingDomain.getNewChildDescriptors(body, null);
                CommandParameter commandParameter = null;
                for (Object object : newChildDescriptors) {
                    if (((CommandParameter) object).getValue() instanceof AnyType) {
                        commandParameter = (CommandParameter) object;
                        break;
                    }
                }
                FeatureMap mixed = ((AnyType) commandParameter.getValue()).getMixed();
                mixed.add(XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_Text(), "");
                FeatureMapUtil.addText(mixed, contentStr);
                create = CreateChildCommand.create(editingDomain, body, commandParameter, new StructuredSelection(body)
                        .toList());
            }
            Command createCmd = SetCommand.create(editingDomain, body, MessagePackage.eINSTANCE.getBody_UseFileRef(),
                    false, 0);
            List<Command> commandList = new ArrayList<Command>();
            commandList.add(create);
            commandList.add(createCmd);
            CompoundCommand compoundCommand = new CompoundCommand(commandList);
            editingDomain.getCommandStack().execute(compoundCommand);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 
     * @param text
     *            this is the file url to be used
     * @param useText
     *            true if text is not null | not to be used
     * @param useFileRef
     *            whether to use the file ref as body or not
     */
    protected void modifyBodySource(String text, boolean useText, boolean useFileRef) {
        Body body = null;
        try {
            body = messageModel.getBody();
            List<Command> commandList = new ArrayList<Command>();
            if (useText) {
                Command create = SetCommand.create(editingDomain, body, MessagePackage.eINSTANCE.getBody_FileRef(),
                        text, 0);
                commandList.add(create);
            }
            Command createCmd = SetCommand.create(editingDomain, body, MessagePackage.eINSTANCE.getBody_UseFileRef(),
                    useFileRef, 0);
            commandList.add(createCmd);
            CompoundCommand compoundCommand = new CompoundCommand(commandList);
            editingDomain.getCommandStack().execute(compoundCommand);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
