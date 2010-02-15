package org.fusesource.tools.core.preferences;
//Copyright © 2009 Progress Software Corporation. All Rights Reserved.
/**
 * @author skoppell
 */
import java.util.Map;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IPreferenceFilter;

public class Filter implements IPreferenceFilter{

	public Filter() {

	}
	
	public Map getMapping(String arg0) {
		return null;
	}

	public String[] getScopes() {
		
		return new String[]{ConfigurationScope.SCOPE};
	}

}
