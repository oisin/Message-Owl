/**
 * 
 * @since 
 * @author sgupta
 * @version 
 */
package org.fusesource.tools.messaging.ui;

import java.util.Map;

import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.fusesource.tools.messaging.core.IConnection;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IDestinationType;
import org.fusesource.tools.messaging.core.IProvider;
import org.fusesource.tools.messaging.server.MessagingServersUtil;


public class DestinationUtil {

	public static IProvider getProvider(String selectedServerName) throws Exception {
		Map<String, String> msgServersNameIdMap = MessagingServersUtil.getMsgServersNameIdMap();
		IServer foundServer = ServerCore.findServer(msgServersNameIdMap.get(selectedServerName));
		return MessagingServersUtil.getProvider(foundServer);
	}

	public static IConnection getConnection(IProvider provider) {
		IConnection connection = null;
		try {
			connection = provider.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static IDestination createDestination(IDestinationType type, String name, IProvider provider) {
		try {
			IConnection connection = getConnection(provider);
			if (connection != null) {
				System.out.println("creating destination...");
				return connection.createDestination(type, name);
			} else {
				System.out.println("[&&&&&&&&&&&] Connection is NULL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
