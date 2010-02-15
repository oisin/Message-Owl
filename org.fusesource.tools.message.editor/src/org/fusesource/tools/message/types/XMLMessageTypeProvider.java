package org.fusesource.tools.message.types;

public class XMLMessageTypeProvider extends TextMessageTypeProvider {

	@Override
	protected AbstractMessageLoader getMessageLoader() {
		return new XMLMessageLoader();
	}
}
