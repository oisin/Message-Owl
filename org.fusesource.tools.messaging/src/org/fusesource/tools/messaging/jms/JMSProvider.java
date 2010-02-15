package org.fusesource.tools.messaging.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.fusesource.tools.messaging.DestinationType;
import org.fusesource.tools.messaging.core.IDestinationType;
import org.fusesource.tools.messaging.core.IProvider;


/**
 * Abstract implementation of IProvider for JMS. All JMS Providers can extend
 * this class to add provider specific functionality
 * 
 * @author kiranb
 * 
 */
public abstract class JMSProvider implements IProvider, JMSConstants {
	private String providerId;

	private String providerName;

	protected List<IDestinationType> supportedTypes = Collections.emptyList();

	protected Map<String, String> connectParams = Collections.emptyMap();

	protected JMSConnection connection;

	public JMSProvider(String providerId, String providerName) {
		this.providerId = providerId;
		this.providerName = providerName;
		init();
	}

	/**
	 * Initialize connection parameters and destination types
	 */
	protected void init() {
		initConnectParams();
		initTypes();
	}

	/**
	 * Clients should override this method to populate the map with provider
	 * specific parameters like Connection Factory Class etc.,
	 */
	protected abstract void initConnectParams();

	/**
	 * Clients can override this method to add any additional destination types.
	 * Currently supporting Queue and Topics
	 */
	protected void initTypes() {
		String destinationTypes[] = { QUEUE_TYPE, TOPIC_TYPE };
		supportedTypes = new ArrayList<IDestinationType>();
		for (int i = 0; i < destinationTypes.length; i++) {
			supportedTypes.add(new DestinationType(destinationTypes[i]));
		}
	}

	public JMSConnection getConnection() {
		return connection;
	}

	public Map<String, String> getConnectionParams() {
		return connectParams;
	}

	public List<IDestinationType> getDestinationTypes() {
		return supportedTypes;
	}

	public String getId() {
		return providerId;
	}

	public String getName() {
		return providerName;
	}
}