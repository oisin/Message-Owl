package org.fusesource.tools.messaging.preference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.fusesource.tools.messaging.IConstants;
import org.fusesource.tools.messaging.plugin.FuseMessagingPlugin;


public class BasePreferenceHandler implements PreferenceHandler, IPropertyChangeListener {

	private IPreferenceStore store = null;

	private List<PreferenceChangedListener> preferenceChangedListenerList = new ArrayList<PreferenceChangedListener>();

	private static BasePreferenceHandler basePreferenceHandler;

	private BasePreferenceHandler() {
		setPreferenceStore();
		initializeDefaultPreferences();
		store.addPropertyChangeListener(this);
	}

	private void initializeDefaultPreferences() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		preferenceStore.setDefault(IConstants.MESSAGES_HISTORY_COUNT, IConstants.DEFAULT_MESSAGES_HISTORY_COUNT);
	}

	private void setPreferenceStore() {
		store = FuseMessagingPlugin.getDefault().getPreferenceStore();
	}

	public static BasePreferenceHandler getInstance() {
		if (basePreferenceHandler == null)
			basePreferenceHandler = new BasePreferenceHandler();
		return basePreferenceHandler;
	}

	public void propertyChange(PropertyChangeEvent event) {
		String prefKey = event.getProperty();
		String prefValue = store.getString(prefKey);
		firePreferenceChangedEvent(prefKey, prefValue);
	}

	/**
	 * @param event
	 */
	public void firePreferenceChangedEvent(String prefKey, Object prefValue) {
		PreferenceEvent event = new PreferenceEvent(prefKey, prefValue);
		for (int i = 0; i < preferenceChangedListenerList.size(); i++) {
			PreferenceChangedListener listener = preferenceChangedListenerList.get(i);
			listener.valueChanged(event);
		}
	}

	public IPreferenceStore getPreferenceStore() {
		return store;
	}

	public void addPreferenceChangedListener(PreferenceChangedListener listener) {
		preferenceChangedListenerList.add(listener);
	}

	public void removePreferenceChangedListener(PreferenceChangedListener listener) {
		preferenceChangedListenerList.remove(listener);
	}

	public String getPreferenceValue(String key) {
		return store.getString(key);
	}

	public void setPreferenceValue(String key, String value) {
		store.setValue(key, value);
	}

}
