// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.core.message.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class EncodingReader {

    public static final String UTF_8 = "UTF-8";
    public static final String UTF_16 = "UTF-16";
    public static final String UTF_16BE = "UTF-16BE";
    public static final String UTF_16LE = "UTF-16LE";
    public static final String UCS_4 = "UCS-4";
    public static final String UCS_4BE = "UCS-4BE";
    public static final String UCS_4LE = "UCS-4LE";
    public static final String UCS_4_3412 = "UCS-4-3412";
    public static final String UCS_4_2143 = "UCS-4-2143";

    public static final int UTF_8_BOM_LENGTH = 3;

    private static final String LATIN_1 = "8859_1";

    private static int FROM_BOM = 1;
    private static int FROM_DECL = 2;
    private static int FROM_SYS = 3;


    private static EncodingReader instance = new EncodingReader();


    public static EncodingReader getInstance() {
        return instance;
    }

    public EncodingInfo readCharset(byte[] bytes) {
        return readCharset(bytes,true);
    }

    /**
     *
     * @param bytes
     * @param doDefault if true, returns the default charset of the platform if
     * the charset could not be determined from the content
     * @return
     */
    public EncodingInfo readCharset(byte[] bytes,boolean doDefault) {
        int len = Math.min(bytes.length, 4);
        int first4[] = new int[4];
        for (int i = 0; i < len; i++) {
            first4[i] = bytes[i] & 0xFF;
        }
        EncodingInfo charset = checkfirst4bytes(first4, len);
        if (charset != null)
            return charset;

        if (startsWithDecalaration(first4)) {
            try {
                charset = findEncodingDeclaration(new String(bytes, 0, Math.min(bytes.length, 1024), LATIN_1));
                if (charset != null)
                    return charset;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return doDefault?defaultEncoding():null;
    }

    public EncodingInfo readCharset(InputStream stream) throws IOException {
        return readCharset(stream,true);
    }


    /**
     * If the stream supports <b>mark/reset</b>, the stream is reset to where it was
     * before the method invocation.
     * @param stream  stream is not closed by the EncodingReader
     * @param doDefault if true, returns the default charset of the platform if
     * the charset could not be determined from the content
     * @throws IOException
     */
    public EncodingInfo readCharset(InputStream stream, boolean doDefault) throws IOException {
        boolean mark = stream.markSupported();
        if (mark)
            stream.mark(1024);
        try {
            int first4[] = new int[4];
            int len = 0;
            for (int i = 0; i < first4.length; i++) {
                int r = stream.read();
                if (r == -1)
                    break;
                len++;
                first4[i] = r;
            }
            EncodingInfo charset = checkfirst4bytes(first4, len);
            if (charset != null) {
                return charset;
            }
            if (startsWithDecalaration(first4)) {
                try {
                    byte[] bytes = new byte[1024];
                    for (int i = 0; i < len; i++) {
                        bytes[i] = ((byte) first4[i]);
                    }
                    int i = stream.read(bytes, len, bytes.length - len);
                    if (i == -1)
                        return defaultEncoding();
                    charset = findEncodingDeclaration(new String(bytes, 0, i, LATIN_1));
                    if (charset != null)
                        return charset;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return doDefault?defaultEncoding():null;
        } finally {
            try {
                if (mark)
                    stream.reset();
            } catch (IOException e) {
            }
        }
    }


    private EncodingInfo checkfirst4bytes(int[] bytes, int length) {
        if (length < 3)
            return null;
        int byte1 = bytes[0];
        int byte2 = bytes[1];
        int byte3 = bytes[2];
        if (byte1 == 0xEF && byte2 == 0xBB && byte3 == 0xBF) {
            return bomEncoding(UTF_8);
        }
        if (length < 4)
            return null;
        int byte4 = bytes[3];
        if (byte1 == 0xFE && byte2 == 0xFF) {
            if (byte3 == 0x00 && byte4 == 0x00) {
                return bomEncoding(UCS_4_3412);
            } else if (byte3 != 0x00 || byte4 != 0x00) {
                return bomEncoding(UTF_16);
            }
        } else if (byte1 == 0xFF && byte2 == 0xFE) {
            if (byte3 == 0x00 && byte4 == 0x00) {
                return bomEncoding(UCS_4);
            } else if (byte3 != 0x00 || byte4 != 0x00) {
                return bomEncoding(UTF_16);
            }
        } else if (byte1 == 0x00 && byte2 == 0x00) {
            if (byte3 == 0xFE && byte4 == 0xFF) {
                return bomEncoding(UCS_4);
            } else if (byte3 == 0xFF && byte4 != 0xFE) {
                return bomEncoding(UCS_4_2143);
            }
        }
        // no BOM present; try to guess from the way version declaration is encoded
        // this does not comply fully with the suggestions in xml spec (which are non-normative btw.)
        if (byte1 == 0x00 && byte2 == 0x00
                && byte3 == 0x00 && byte4 == '<') {
            return declEncoding(UCS_4BE);
        }
        if (byte1 == '<' && byte2 == 0x00
                && byte3 == 0x00 && byte4 == 0x00) {
            return declEncoding(UCS_4LE);
        }
        if (byte1 == 0x00 && byte2 == 0x00
                && byte3 == '<' && byte4 == 0x00) {
            return declEncoding(UCS_4_2143);
        }
        if (byte1 == 0x00 && byte2 == '<'
                && byte3 == 0x00 && byte4 == 0x00) {
            return declEncoding(UCS_4_3412);
        }
        if (byte1 == 0x00 && byte2 == '<'
                && byte3 == 0x00 && byte4 == '?') {
            return declEncoding(UTF_16BE);
        } else if (byte1 == '<' && byte2 == 0x00
                && byte3 == '?' && byte4 == 0x00) {
            return declEncoding(UTF_16LE);
        }
        return null;
    }

    private boolean startsWithDecalaration(int[] first4) {
        return first4[0] == '<' && first4[1] == '?'
                && first4[2] == 'x' && first4[3] == 'm';
    }


    private EncodingInfo findEncodingDeclaration(String declaration) {
        String encoding = "encoding";
        int position = declaration.indexOf(encoding) + encoding.length();
        if (position == -1)
            return null;
        char c = 0;
        // get rid of white space before equals sign
        while (position < declaration.length()) {
            c = declaration.charAt(position++);
            if (!Character.isSpace(c)) {
                break;
            }
        }
        if (c != '=') { // malformed
            return null;
        }
        // get rid of white space after equals sign
        while (position < declaration.length()) {
            c = declaration.charAt(position++);
            if (!Character.isSpace(c)) {
                break;
            }
        }
        char delimiter = c;
        if (delimiter != '\'' && delimiter != '"') { // malformed
            return null;
        }
        // now positioned to read encoding name
        StringBuffer encodingName = new StringBuffer();
        while (position < declaration.length()) {
            c = declaration.charAt(position++);
            if (c == delimiter) break;
            encodingName.append(c);
        }
        if (c != delimiter)
            return null;
        return declEncoding(encodingName.toString());
    }

    public static void main(String[] args) throws IOException {
        EncodingInfo s = getInstance().readCharset(new byte[0]);
        System.out.println("s = " + s);

    }

    private static EncodingInfo bomEncoding(String name){
        return new EncodingInfo(name,FROM_BOM);

    }

    private static EncodingInfo declEncoding(String name){
        return new EncodingInfo(name,FROM_DECL);
    }
    private static EncodingInfo defaultEncoding() {
        return new EncodingInfo(System.getProperty("file.encoding"),FROM_SYS);
    }


    public static class EncodingInfo {
        private String encoding;
        private int type;

        private EncodingInfo(String encoding,int type){
            this.encoding = encoding;
            this.type = type;
        }

        public String name() {
            return encoding;
        }

        public boolean isFromBOM(){
            return type == FROM_BOM;
        }

        public boolean isFromDeclaration(){
            return type == FROM_DECL;
        }

        public boolean isSystemDefault(){
            return type == FROM_SYS;
        }

        public String toString() {
            return encoding;
        }
    }

}
