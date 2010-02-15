package org.fusesource.tools.core.ui;
//Copyright © 2009 Progress Software Corporation. All Rights Reserved.
/**
 * @author skoppell
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IPreferenceFilter;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.swt.widgets.Composite;
import org.fusesource.tools.core.Activator;
import org.fusesource.tools.core.preferences.FUSEComposite;
import org.fusesource.tools.core.preferences.Filter;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.Preferences;


public class StringFieldEditor extends
		org.eclipse.jface.preference.StringFieldEditor {

	public StringFieldEditor(String serialNumberKey, String string,
			Composite comp) {
		super(serialNumberKey, string, comp);
	}

	@Override
	public void load() {
		Preferences licenseNode = getPreferenceNode();
		if(getTextControl() != null){
			String value = licenseNode.get(FUSEComposite.SERIAL_NUMBER_KEY, null);
			if(value != null)
				getTextControl().setText(value);
		}
		valueChanged();
	}
	
	@Override
	public void store() {
		Preferences licenseNode = getPreferenceNode();
		licenseNode.put(FUSEComposite.SERIAL_NUMBER_KEY, getTextControl().getText());
		try {
			getPreferenceService().applyPreferences(getPreferenceService().getRootNode(),
					new IPreferenceFilter[] {new Filter()});
		} catch (CoreException e) {
			e.printStackTrace();
		}
		updateAboutMappings();
	}

	private Preferences getPreferenceNode() {
		IPreferencesService service = getPreferenceService();
		Preferences licenseNode = service.getRootNode().node(
				ConfigurationScope.SCOPE).node(Activator.PLUGIN_ID);
		return licenseNode;
	}

	private IPreferencesService getPreferenceService() {
		IPreferencesService service = Platform.getPreferencesService();
		return service;
	}
	
	private String getBundleFileLocation(String pluginID, String file)
			throws IOException {
		Bundle bundle = Platform.getBundle(pluginID);
		if (bundle != null) {
			URL deployXML = FileLocator.find(bundle, new Path(file),
					Collections.emptyMap());
			if (deployXML != null) {
				URL fileURL = FileLocator.toFileURL(deployXML);
				if (fileURL != null) {
					return new Path(fileURL.getFile()).toOSString();
				}
			}

		}
		return "";
	}
	
	private void updateAboutMappings() {
		String fileName = "about.mappings";
		String pluginID = "org.fusesource.tools.branding";
		String filePath = null;
		try {
			filePath  = getBundleFileLocation(pluginID, fileName);
			File file = new File(filePath);

			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			properties.elements();
			properties.put("1", getTextControl().getText());
			properties.store(new FileOutputStream(file), null);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
