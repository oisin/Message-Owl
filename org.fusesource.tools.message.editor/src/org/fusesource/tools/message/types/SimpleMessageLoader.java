/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.message.types;

import org.fusesource.tools.core.message.Message;

public class SimpleMessageLoader extends AbstractMessageLoader {
	@Override
	protected Message getNewMessage() {
		return new SimpleMessage();
	}
}
