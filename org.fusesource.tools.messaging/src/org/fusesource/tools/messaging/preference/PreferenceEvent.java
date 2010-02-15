package org.fusesource.tools.messaging.preference;

public class PreferenceEvent {

	private String key;
	private Object value;

	public PreferenceEvent(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}
}
