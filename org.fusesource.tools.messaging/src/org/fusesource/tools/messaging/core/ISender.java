// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.core;

import java.io.Serializable;
import java.util.Map;

import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.messaging.MessagingException;


/**
 * This interface represents a Sender instance. An entry point to create a
 * sender is from a Destination @see {@link IDestination#createListener(Map)}
 * Optionally, clients can also implement ILabelProvider to customize text/image
 * displayed for each sender in the Project Explorer
 */
public interface ISender extends Serializable {
	/**
	 * Messaging framework calls this method when a new Sender is created or
	 * when a Connection is re-activated/updated for this sender. Clients can do
	 * any required initialization here to setup a sender
	 * 
	 * @throws MessagingException
	 */
	public void start() throws MessagingException;

	/**
	 * Messaging framework calls this method when a connection is disconnected,
	 * when a sender is deleted. Clients can release any resource held by this
	 * sender here.
	 * 
	 * @throws MessagingException
	 */
	public void stop() throws MessagingException;

	/**
	 *Return true if the sender is started/ready
	 * 
	 * @return
	 */
	public boolean isStarted();
	
	/**
	 * Clients can implement their send logic in this method
	 * 
	 * @param msgToSend
	 * @throws MessagingException
	 */
	public void send(Message msgToSend) throws MessagingException;

	/**
	 * Return the destination object for which this sender has been created
	 * 
	 * @return
	 */
	public IDestination getDestination();

	/**
	 * Return the properties associated by the Sender
	 * 
	 * @return
	 */
	public Map<String, Object> getProperties();
}
