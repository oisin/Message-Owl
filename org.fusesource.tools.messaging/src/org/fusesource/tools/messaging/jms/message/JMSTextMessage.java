package org.fusesource.tools.messaging.jms.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.fusesource.tools.message.types.TextMessage;
import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.jms.JMSMessage;
import org.fusesource.tools.messaging.jms.JMSSender;


/**
 * 
 * @author sgupta
 * 
 */
public class JMSTextMessage extends TextMessage implements JMSMessage {

	public JMSTextMessage() {
	}

	public Message getJMSMessage(JMSSender sender) throws MessagingException {
		Message message = getMsgDelegate().getJMSMessage(this, sender);
		setContent(message);
		return message;
	}

	public Message getJMSMessage(Session session) throws MessagingException {
		Message message = getMsgDelegate().getJMSMessage(this, session);
		setContent(message);
		return message;
	}

	protected JMSSimpleMessageDelegate getMsgDelegate() {
		return new JMSSimpleMessageDelegate() {
			@Override
			protected Message createJMSMessage(Session session) throws JMSException {
				return session.createTextMessage();
			}
		};
	}
	
	private void setContent(Message message) throws MessagingException {
		try {
			((javax.jms.TextMessage)message).setText(getMessageBody());
		} catch (JMSException e) {
			e.printStackTrace();
			throw new MessagingException("Failed to compose JMS Message");
		}
	}
}