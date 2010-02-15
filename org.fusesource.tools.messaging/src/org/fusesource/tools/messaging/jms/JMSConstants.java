// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.jms;

/**
 * Useful constans for JMS
 * @author kiranb
 * 
 */
public interface JMSConstants {
	// JMS Destination Constants
	public static final String QUEUE_TYPE = "Queue";
	public static final String TOPIC_TYPE = "Topic";

	// JMS Connection Parameters
	public static final String CONNECTION_ID = "Connection Id";
	public static final String CONNECTION_FACTORY_CLASS = "Connection Factory";
	public static final String URL = "Url";
	public static final String USER_NAME = "User Name";
	public static final String PASSWORD = "Password";

	// JMS Header Constants
	public static final String JMSPRIORITY = "JMSPriority";

	public static final String JMSEXPIRATION = "JMSExpiration";

	public static final String JMSTYPE = "JMSType";

	public static final String JMSREDELIVERED = "JMSRedelivered";

	public static final String JMSREPLY_TO = "JMSReplyTo";

	public static final String JMSCORRELATION_ID = "JMSCorrelationID";

	public static final String JMSTIMESTAMP = "JMSTimestamp";

	public static final String JMSMESSAGE_ID = "JMSMessageID";

	public static final String JMSDESTINATION = "JMSDestination";

	public static final String JMSDESTINATIONTYPE = "JMSDestinationType";

	public static final String JMSDELIVERYMODE = "JMSDeliveryMode";

	public static final String JMSMESSAGEID = "JMSMessageID";

	// Other JMS Constants
	public static final String DELIVERY_MODE = "Delivery Mode";

	public static final String PERSISTENT = "PERSISTENT";

	public static final String NON_PERSISTENT = "NON_PERSISTENT";

	public static final String PRIORITY = "Priority";

	public static final String TIME_TO_LIVE = "Time To Live";

	public static final String DURABLE_SUBSCRIPTION_NAME = "DURABLE SUBSCRIPTION NAME";

	public static String JMS_MESSAGE_SELECTOR = "MessageSelector";
	
	public static String DEFAULT_JMS_PROVIDER = "JMS";
}
