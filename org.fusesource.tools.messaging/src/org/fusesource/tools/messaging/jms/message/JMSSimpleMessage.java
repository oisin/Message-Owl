/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.jms.message;

import javax.jms.Message;
import javax.jms.Session;

import org.fusesource.tools.message.types.SimpleMessage;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.jms.JMSMessage;
import org.fusesource.tools.messaging.jms.JMSSender;


public class JMSSimpleMessage extends SimpleMessage implements JMSMessage {

	public JMSSimpleMessage() {
	}

	public Message getJMSMessage(JMSSender sender) throws MessagingException {
		return getMsgDelegate().getJMSMessage(this, sender);
	}

	public Message getJMSMessage(Session session) throws MessagingException {
		return getMsgDelegate().getJMSMessage(this, session);
	}

	protected JMSSimpleMessageDelegate getMsgDelegate() {
		return new JMSSimpleMessageDelegate();
	}
}
