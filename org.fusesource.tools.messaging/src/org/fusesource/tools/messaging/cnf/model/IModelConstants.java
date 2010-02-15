package org.fusesource.tools.messaging.cnf.model;

import java.io.File;

public interface IModelConstants {
	final String SENDERS_EXT = "senders";
	final String META_FOLDER = ".settings";
	final String SENDERS_FILE_NAME = "Senders" + "." + SENDERS_EXT;
	final String LISTENERS_EXT = "listeners";
	final String LISTENERS_FILE_NAME = "Listeners" + "." + LISTENERS_EXT;
	final String SENDERS_FILE_PATH = META_FOLDER + File.separator + SENDERS_FILE_NAME;
	final String LISTENERS_FILE_PATH = META_FOLDER + File.separator + LISTENERS_FILE_NAME;
}