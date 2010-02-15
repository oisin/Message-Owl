/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.message.utils;

public class MessageMetaInfo {

	private String type = null;

	private String providerId = null;

	public MessageMetaInfo() {
	}

	public MessageMetaInfo(String type, String providerId) {
		this.type = type;
		this.providerId = providerId;
	}

	/**
	 * @param providerId
	 *            the providerId to set
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the providerId
	 */
	public String getProviderId() {
		return providerId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
}
