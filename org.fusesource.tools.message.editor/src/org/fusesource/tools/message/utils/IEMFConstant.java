package org.fusesource.tools.message.utils;

import org.fusesource.tools.message.MessageConstants;
import org.fusesource.tools.message.editors.MessageEditorConstants;

public interface IEMFConstant {

	final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<message:message xmlns:message=\"http://fuse.com/tools/message\" \nproviderName=\""
			+ MessageConstants.DEFAULT_PROVIDER + "\" providerId=\"" + MessageConstants.DEFAULT_PROVIDER
			+ "\" type=\"" + MessageEditorConstants.DEFAULT_TYPE + "\">" + "<message:properties/><message:body><message:content>";

	final String XML_FOOTER = "</message:content></message:body> </message:message>";
}
