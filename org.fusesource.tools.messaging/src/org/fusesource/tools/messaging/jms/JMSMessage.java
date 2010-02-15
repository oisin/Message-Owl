package org.fusesource.tools.messaging.jms;

import javax.jms.Message;
import javax.jms.Session;

import org.fusesource.tools.messaging.MessagingException;


public interface JMSMessage {

	public abstract Message getJMSMessage(JMSSender sender) throws MessagingException;

	public abstract Message getJMSMessage(Session session) throws MessagingException;
}
