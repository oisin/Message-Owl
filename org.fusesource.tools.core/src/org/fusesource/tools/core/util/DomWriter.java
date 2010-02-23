/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class DomWriter {

    public static void writeToFile(Document doc, OutputStream os) {
        TransformerFactory transfac = TransformerFactory.newInstance();
        try {
            Transformer trans = transfac.newTransformer();
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            byte buf[] = xmlString.getBytes();
            for (byte element : buf) {
                os.write(element);
            }
            buf = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(Document doc, String beanFile) {
        writeToFile(doc, new File(beanFile));
    }

    public static void writeToFile(Document doc, File beanFile) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(beanFile);
            writeToFile(doc, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
