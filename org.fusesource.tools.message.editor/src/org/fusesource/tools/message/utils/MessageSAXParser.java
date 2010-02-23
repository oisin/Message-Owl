/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.fusesource.tools.message.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.fusesource.tools.message.MessageConstants;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MessageSAXParser extends DefaultHandler {

    private final MessageMetaInfo fileInfoObject = new MessageMetaInfo();

    public MessageMetaInfo parse(String uri) {
        return run(uri);
    }

    public MessageMetaInfo parse(File file) {
        return run(file.getAbsolutePath());
    }

    public MessageMetaInfo parse(IFile ifile) {
        return run(ifile.getLocationURI().toString());
    }

    private MessageMetaInfo run(String uri) {
        parseDocument(uri);
        return fileInfoObject;
    }

    private void parseDocument(String uri) {

        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse(uri, this);
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    // Event Handlers
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.endsWith(MessageConstants.MESSAGE_START_TAG)) {
            fileInfoObject.setType(attributes.getValue(MessageConstants.MESSAGE_TYPE_ATTRIBUTE));
            fileInfoObject.setProviderId(attributes.getValue(MessageConstants.MESSAGE_PROVIDER_ID_ATTRIBUTE));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

}
