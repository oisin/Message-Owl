package org.fusesource.tools.core.ui.url.editor.resource;

/*
 
 */
public class URLSpaceConverter {

	/**
	 * Converts spaces to platform specific character. For now it works only in
	 * windows platform
	 * 
	 * @param url
	 * @return
	 */
	public static String encode(String url) {
		return url.replaceAll(" ", "%20");
	}

	/**
	 * Converts encode string back space saperated url.
	 * 
	 * @param url
	 * @return
	 */
	public static String decode(String url) {
		return url.replaceAll("%20", " ");
	}
}
