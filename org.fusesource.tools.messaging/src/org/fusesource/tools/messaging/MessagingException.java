package org.fusesource.tools.messaging;

/**
 * Common Messaging exception
 * 
 * @author kiranb
 * 
 */
public class MessagingException extends Exception {
	private static final long serialVersionUID = 6200932667257058364L;

	public MessagingException() {
	}

	public MessagingException(String msg) {
		super(msg);
	}

	public MessagingException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
