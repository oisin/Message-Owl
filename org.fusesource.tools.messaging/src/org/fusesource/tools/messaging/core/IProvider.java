package org.fusesource.tools.messaging.core;

import java.util.List;
import java.util.Map;

import org.fusesource.tools.messaging.MessagingException;
import org.fusesource.tools.messaging.server.MessagingServerDelegate;
import org.fusesource.tools.messaging.server.ui.MessagingRuntimeWizardFragment;


/**
 * This interface represents a provider instance. An entry point to plug-in a provider is
 * through WTP Server contribution Also see {@link MessagingServerDelegate} An
 * entry point to get a provider by the framework is through WTP Server
 * contribution.
 */
public interface IProvider {
	/**
	 * @return Returns the id of the provider
	 */
	public String getId();

	/**
	 * Returns the name of the provider
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Return a map of connection parameters that is required to successfully
	 * connect to a server Each key in the map will be populated in the
	 * messaging server runtime UI to take the user inputs see
	 * {@link MessagingRuntimeWizardFragment}
	 * 
	 * @return
	 */
	public Map<String, String> getConnectionParams();

	/**
	 * Entry point to create a connection to a server
	 * 
	 * @param properties
	 *            Input properties required to create a connection
	 * @return IConnection - Represents an abstract connection
	 * @throws MessagingException
	 */
	public IConnection createConnection(Map<String, String> properties)
			throws MessagingException;

	/**
	 * Return a connection object created by the provider
	 * 
	 * @return
	 */
	public IConnection getConnection();

	/**
	 * Return the supported destination types by the provider
	 * 
	 * @return
	 */
	public List<IDestinationType> getDestinationTypes();

}