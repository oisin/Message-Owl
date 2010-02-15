package org.fusesource.tools.messaging.core;

import java.io.Serializable;
import java.util.Map;

import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.ui.dialogs.DestinationDialog;


/**
 * This interface represents a destination instance. An entry point to create a
 * destination is from a connection {@link IConnection} Each destination has a
 * destination type and a name
 */
public interface IDestination extends Serializable {

	/**
	 * Each destination is of a particular destination type
	 * 
	 * @return
	 */
	public IDestinationType getDestinationType();

	/**
	 * Return a connection associated with the destination
	 * 
	 * @return
	 */
	public IConnection getConnection();

	/**
	 * Return the name of the destination
	 * 
	 * @return
	 */
	public String getDestinationName();

	/**
	 * An entry point to create a Sender for the destination
	 * 
	 * @param senderProps
	 *            - Properties to be used while creating a sender
	 * @return a sender instance
	 * @throws MessagingException
	 */
	public ISender createSender(Map<String, Object> senderProps)
			throws MessagingException;

	/**
	 * An entry point to create a Listener for the destination
	 * 
	 * @param listenerProps
	 *            - Properties to be used while creating a listener
	 * @return a listener instance
	 * @throws MessagingException
	 */
	public IListener createListener(Map<String, Object> listenerProps)
			throws MessagingException;

	/**
	 * Return a map of sender properties to be shown in the UI
	 * 
	 * @see DestinationDialog
	 * @return
	 */
	public Map<String, Object> getSenderProperties();

	/**
	 * Return a map of listener properties to be shown in the UI
	 * 
	 * @see DestinationDialog
	 * @return
	 */
	public Map<String, Object> getListenerProperties();

	/**
	 * Update the connection for the destination, when a connection is closed
	 * and reopened, this method will be called to update the connection for
	 * this destinaion
	 * 
	 * @param con
	 */
	public void setConnection(IConnection con);
}
