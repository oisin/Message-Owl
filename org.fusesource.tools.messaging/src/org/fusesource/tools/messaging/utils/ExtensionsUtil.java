package org.fusesource.tools.messaging.utils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.fusesource.tools.messaging.ui.dialogs.DestinationDialog;


public class ExtensionsUtil {
	
	public static final String EXTENSION_CLASS = "class";
	public static final String EXTENSION_ID = "id";
	public static final String EXTENSION_PROVIDERID = "providerid";
	public static final String MESSAGE_TYPE = "type";

	private static IConfigurationElement[] msgTypeUIConfigElements;
	public static DestinationDialog getDestinationUIExtension(String type, String providerID) {
		if (type == null || providerID == null) {
			return null;
		}
		IConfigurationElement[] extensions = getConfigElementsForMsgTypeUI(DestinationDialog.DESTINATION_UI_EXT_PT);
		String tempType = "";
		String tempProviderId = "";
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement extnData = extensions[i];
			try {
				DestinationDialog participant = (DestinationDialog) extnData.createExecutableExtension(EXTENSION_CLASS);
				tempProviderId = (String) extnData.getAttribute(EXTENSION_PROVIDERID);
				tempType = (String) extnData.getAttribute(MESSAGE_TYPE);
				if (type.equals(tempType) && providerID.equals(tempProviderId)) {
					return participant;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static IConfigurationElement[] getConfigElementsForMsgTypeUI(String id) {
		IExtensionPoint xp = Platform.getExtensionRegistry().getExtensionPoint(id);
		if (xp == null)
			return new IConfigurationElement[0];

		msgTypeUIConfigElements = xp.getConfigurationElements();
		return msgTypeUIConfigElements;
	}

}
