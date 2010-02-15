package org.fusesource.tools.messaging.core;

import java.util.List;

import org.fusesource.tools.messaging.DefaultMessagesManager;
import org.fusesource.tools.messaging.MessageEvent;


/**
 * A Manager interface to handle the messages of a listener. A default
 * implementation has been provided by the framework which will suffice for any
 * listener, Clients also has choice of implementing customized MessagesManager
 * 
 * @see DefaultMessagesManager
 * @author kiranb
 * 
 */
public interface IMessagesManager {
	public List<MessageEvent> getMessages();

	public void addNewMessage(MessageEvent me);

	public void removeMessage(MessageEvent me);

	public void clearAllMessages();

	public int getTotalMessagesCount();

	public int getMessagesCount(int flag);

	public void addMessageChangeListener(
			IMessageChangeListener msgChangeListener);

	public void removeMessageChangeListener(
			IMessageChangeListener msgChangeListener);

	public boolean hasFlag(MessageEvent message, int messageUnread);

	public void resetFlag(MessageEvent currentMessage, int messageRead);
}
