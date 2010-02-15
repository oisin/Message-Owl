package org.fusesource.tools.messaging;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.IMessageChangeListener;
import org.fusesource.tools.messaging.core.IMessagesManager;
import org.fusesource.tools.messaging.preference.BasePreferenceHandler;
import org.fusesource.tools.messaging.preference.PreferenceChangedListener;
import org.fusesource.tools.messaging.preference.PreferenceEvent;


/**
 * Default Implementation of IMessagesManager
 * 
 * @author kiranb
 * 
 */
public class DefaultMessagesManager implements IMessagesManager, PreferenceChangedListener {
	private List<MessageEvent> receivedMsgs;
	private List<IMessageChangeListener> subscribers = new ArrayList<IMessageChangeListener>();
	private int historyCount = IConstants.DEFAULT_MESSAGES_HISTORY_COUNT;

	public DefaultMessagesManager(IListener listener) {
		receivedMsgs = new ArrayList<MessageEvent>();
		initHistoryCount();
		addPreferenceChangeListener();
	}

	private void addPreferenceChangeListener() {
		BasePreferenceHandler.getInstance().addPreferenceChangedListener(this);
	}

	private void initHistoryCount() {
		try {
			historyCount = new Integer(BasePreferenceHandler.getInstance().getPreferenceValue(
					IConstants.MESSAGES_HISTORY_COUNT)).intValue();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public void messageEvent(MessageEvent me) {
		addNewMessage(me);
	}

	public synchronized void addNewMessage(MessageEvent me) {
		if (receivedMsgs.size() == historyCount)
			removeMessage(receivedMsgs.get(0));
		receivedMsgs.add(me);
		fireEvents(me, IMessageChangeListener.MESSAGE_ADDED);
	}

	public synchronized void clearAllMessages() {
		List<MessageEvent> msgList = new ArrayList<MessageEvent>(receivedMsgs);
		receivedMsgs.clear();
		fireClearAllEvent(msgList);
	}

	public synchronized void removeMessage(MessageEvent me) {
		receivedMsgs.remove(me);
		fireEvents(me, IMessageChangeListener.MESSAGE_REMOVED);
	}

	public synchronized int getMessagesCount(int flag) {
		int count = 0;
		for (MessageEvent message : receivedMsgs) {
			if (hasFlag(message, flag))
				count++;
		}
		return count;
	}

	public int getTotalMessagesCount() {
		return receivedMsgs.size();
	}

	public List<MessageEvent> getMessages() {
		return receivedMsgs;
	}

	public void fireEvents(MessageEvent me, int eventKind) {
		List<IMessageChangeListener> changeListeners = getListeners();
		for (IMessageChangeListener subscriber : changeListeners) {
			subscriber.messageChangeEvent(me, eventKind);
		}
	}

	private void fireClearAllEvent(List<MessageEvent> msgList) {
		List<IMessageChangeListener> changeListeners = getListeners();
		for (IMessageChangeListener messageChangeListener : changeListeners) {
			messageChangeListener.messagesClearedEvent(msgList);
		}
	}

	private List<IMessageChangeListener> getListeners() {
		List<IMessageChangeListener> changeListeners;
		synchronized (subscribers) {
			changeListeners = new ArrayList<IMessageChangeListener>(subscribers);
		}
		return changeListeners;
	}

	public void addMessageChangeListener(IMessageChangeListener newMsgListener) {
		synchronized (subscribers) {
			subscribers.add(newMsgListener);
		}
	}

	public void removeMessageChangeListener(IMessageChangeListener newMsgListener) {
		synchronized (subscribers) {
			subscribers.remove(newMsgListener);
		}
	}

	public boolean hasFlag(MessageEvent me, int checkFlag) {
		return (me.getFlag() == checkFlag) ? true : false;
	}

	public synchronized void resetFlag(MessageEvent me, int newFlag) {
		me.setFlag(newFlag);
		fireEvents(me, newFlag);
	}

	public void valueChanged(PreferenceEvent event) {
		if (IConstants.MESSAGES_HISTORY_COUNT.equals(event.getKey())) {
			initHistoryCount();
			updateMessagesList();
		}
	}

	private synchronized void updateMessagesList() {
		if (receivedMsgs.size() > historyCount) {
			int toRemove = receivedMsgs.size() - historyCount;
			List<MessageEvent> tobeRemovedMsgs = new ArrayList<MessageEvent>(receivedMsgs.subList(0, toRemove));
			for (MessageEvent messageEvent : tobeRemovedMsgs) {
				removeMessage(messageEvent);
			}
		}
	}
}