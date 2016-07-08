// Copyright (c) 2000 Just Objects B.V. <just@justobjects.nl>
// Distributable under LGPL license. See terms of license at gnu.org.

package Icom.Utils;

import java.util.Properties;

/**
 * Loads and maintains overall configuration.
 * 
 * @version $Id: Config.java,v 1.2 2006/05/06 00:10:11 justb Exp $
 * @author Just van den Broecke - Just Objects &copy;
 **/
public class Config {
	
	private Config(){
		
	}
	
	private static Config mInstance;
	private static Object mLock = new Object();
	
	public static Config getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new Config();
			}
			return mInstance;
		}
	}
	

	private static final String PROPERTIES_RESOURCE = "ApplicationResources.properties";
	
	private static Properties properties = null;

	public void loadGlobalConfig() {
		Utils.logger.info("MESSAGE: Config is loading");
		try {
			properties = Sys.getInstance().loadPropertiesResource(PROPERTIES_RESOURCE);
		} catch (Throwable t) {
			Utils.logger
					.error("MESSAGE: Config is cannot find properties file: "
							+ PROPERTIES_RESOURCE + t.getMessage());
		}

		Utils.logger.info("MESSAGE: Config is loaded values=" + properties);
	}

	/**
	 * Initialize event sources from properties file.
	 */
	public void load() {

		Utils.logger.info("MESSAGE: Config is loading");
		try {
			properties = Sys.getInstance().loadPropertiesResource(PROPERTIES_RESOURCE);
		} catch (Throwable t) {
			Utils.logger
					.error("MESSAGE: Config is cannot find properties file: "
							+ PROPERTIES_RESOURCE + " "
							+ t.getLocalizedMessage());
			return;
		}
		Utils.logger.info("MESSAGE: Config is loaded values=" + properties);
	}

	public String getProperty(String aName) {

		String value = properties.getProperty(aName);
		if (value == null) {
			throw new IllegalArgumentException("Unknown property: " + aName);
		}
		return value.trim();
	}

	public String getProperty(String aName, String sDefault) {

		String value = properties.getProperty(aName);
		if (value == null) {
			if(sDefault!= null)
				return sDefault;
			else
				throw new IllegalArgumentException("Unknown property: " + aName);
		}
		return value.trim();
	}

	public boolean getBoolProperty(String aName) {
		String value = getProperty(aName);
		try {
			return value.equals("true");
		} catch (Throwable t) {
			throw new IllegalArgumentException("Illegal property value: "
					+ aName + " val=" + value);
		}
	}

	public int getIntProperty(String aName) {
		String value = getProperty(aName);
		try {
			return Integer.parseInt(value);
		} catch (Throwable t) {
			throw new IllegalArgumentException("Illegal property value: "+ aName + " val=" + value);
		}
	}

	public long getLongProperty(String aName) {
		String value = getProperty(aName);
		try {
			return Long.parseLong(value);
		} catch (Throwable t) {
			throw new IllegalArgumentException("Illegal property value: "
					+ aName + " val=" + value);
		}
	}

	public boolean setStringProperty(String key, String value) {
		boolean result = false;
		try {
			properties.setProperty(key, value);

			result = true;
		} catch (Exception e) {
			System.err.println("SAVE CONFIG INFOR ERROR: " + e.getMessage());
		}
		return result;
	}

	public boolean storeProperties() {
		boolean result = false;
		try {
			Sys.getInstance().savePropertiesResource(properties, PROPERTIES_RESOURCE);
			result = true;
		} catch (Exception e) {
			System.err.println("SAVE CONFIG INFOR ERROR: " + e.getMessage());
		}
		return result;
	}

	public boolean hasProperty(String aName) {
		return properties.containsKey(aName);
	}
}
