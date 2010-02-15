package org.fusesource.tools.message.types;


/**
 * 
 * @author sgupta
 * 
 */
public class TextMessageTypeProvider extends SimpleMessageTypeProvider {

	@Override
	protected AbstractMessageLoader getMessageLoader() {
		return new TextMessageLoader();
	}
}
