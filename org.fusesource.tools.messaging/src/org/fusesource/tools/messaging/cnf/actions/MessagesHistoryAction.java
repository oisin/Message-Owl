package org.fusesource.tools.messaging.cnf.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.preference.BasePreferenceHandler;
import org.fusesource.tools.messaging.ui.ImageConstants;
import org.fusesource.tools.messaging.ui.MessagesHistoryDialog;
import org.fusesource.tools.messaging.utils.ImagesUtil;


/**
 * Handles MessageHistory count changes
 * 
 * @author kiranb
 * 
 */
public class MessagesHistoryAction extends Action {

	public MessagesHistoryAction() {
		setText("Message history count");
		setId(IConstants.MESSAGES_HISTORY_COUNT);
		setToolTipText("Message history count");
		ImageDescriptor image = ImagesUtil.getInstance().getImageDescriptor(
				ImageConstants.HISTORY_IMAGE);
		setImageDescriptor(image);
	}

	public void run() {

		int value = new Integer(BasePreferenceHandler.getInstance()
				.getPreferenceValue(getMessageHistoryCountKey())).intValue();
		MessagesHistoryDialog dialog = new MessagesHistoryDialog(Display
				.getDefault().getActiveShell(), value);
		dialog.open();
		if (dialog.getReturnCode() == Window.OK) {
			int count = dialog.getHistoryCount();
			if (count != -1)
				BasePreferenceHandler.getInstance().setPreferenceValue(
						getMessageHistoryCountKey(), count + "");
		}
	}

	private String getMessageHistoryCountKey() {
		return IConstants.MESSAGES_HISTORY_COUNT;
	}
}
