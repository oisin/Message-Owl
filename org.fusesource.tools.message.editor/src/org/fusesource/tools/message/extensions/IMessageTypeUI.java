/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.message.extensions;

public interface IMessageTypeUI {

	public static final String MESSAGE_TYPE_UI_EXT_PT = "org.fusesource.tools.message.editor.MessageTypeUI";

	public IMessageEditorExtension getEditorExtension();

	public IMessageViewerExtension getViewerExtension();

	public boolean canHandle(Object msg);

	public String getProviderId();

	public String getType();

	public void setProviderId(String id);

	public void setType(String type);

}
