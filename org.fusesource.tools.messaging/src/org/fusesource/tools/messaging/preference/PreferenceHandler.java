package org.fusesource.tools.messaging.preference;

public interface PreferenceHandler {
	public void addPreferenceChangedListener(PreferenceChangedListener listener);

	public void setPreferenceValue(String key, String value);

	public String getPreferenceValue(String key);

	public void removePreferenceChangedListener(
			PreferenceChangedListener listener);

}
