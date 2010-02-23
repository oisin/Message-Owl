/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
// Copyright (c) 2009 Progress Software Corporation.  
package org.fusesource.tools.core.message.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ReaderUtils {

    public static Reader getReader(File file) throws IOException {
        return getReader(file.toURL());
    }

    public static String getContentAsString(URL url) throws IOException {
        Reader reader = getReader(url);
        int i = 0;
        char[] chars = new char[8192]; // BufferedReader.defaultCharBufferSize
        StringBuffer buff = new StringBuffer();
        try {
            while ((i = reader.read(chars, 0, chars.length)) != -1) {
                buff.append(chars, 0, i);
            }
            return buff.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static String getContentAsString(File file) throws IOException {
        return getContentAsString(file.toURL());
    }

    public static String getCharset(URL url) throws UnsupportedEncodingException {
        String contentEncoding;
        String charset;
        InputStream stream = null;
        try {
            URLConnection con = url.openConnection();
            contentEncoding = con.getContentEncoding();
            stream = url.openStream();
            EncodingReader.EncodingInfo encodingInfo = EncodingReader.getInstance().readCharset(stream,
                    contentEncoding == null);
            charset = encodingInfo == null ? null : encodingInfo.name();
            if (charset == null) {
                charset = contentEncoding;
            }
        } catch (IOException e) {
            throw new UnsupportedEncodingException(e.getMessage());
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return charset;
    }

    private static Reader getReader(URL url) throws IOException {
        Reader reader;
        InputStream is = null;
        try {
            URLConnection conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                // copied from Mozilla browser
                conn.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,"
                        + "text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            }
            is = conn.getInputStream();
            MyByteArrayOutputStream baos = new MyByteArrayOutputStream(16 * 1024);
            byte[] b = new byte[4 * 1024];
            while (true) {
                int read = is.read(b);
                if (read == -1) {
                    break;
                }
                baos.write(b, 0, read);
            }
            String contentEncoding = conn.getContentEncoding();
            EncodingReader.EncodingInfo encodingInfo = EncodingReader.getInstance().readCharset(baos.getBuffer(),
                    contentEncoding == null);
            String charset = encodingInfo == null ? null : encodingInfo.name();
            if (charset == null) {
                charset = contentEncoding;
            }
            if (encodingInfo != null && EncodingReader.UTF_8.equals(encodingInfo.name()) && encodingInfo.isFromBOM()) {
                int len = baos.getCount() - EncodingReader.UTF_8_BOM_LENGTH;
                is = new ByteArrayInputStream(baos.getBuffer(), EncodingReader.UTF_8_BOM_LENGTH, len);
            }
            is = new ByteArrayInputStream(baos.getBuffer(), 0, baos.getCount());

            if (charset != null) {
                reader = new InputStreamReader(is, charset);
            } else {
                reader = new InputStreamReader(is);
            }
            return reader;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static class MyByteArrayOutputStream extends ByteArrayOutputStream {
        public MyByteArrayOutputStream(int i) {
            super(i);
        }

        byte[] getBuffer() {
            return buf;
        }

        int getCount() {
            return count;
        }
    }
}
