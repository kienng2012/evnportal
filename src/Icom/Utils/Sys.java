// Copyright (c) 2000 Just Objects B.V. <just@justobjects.nl>
// Distributable under LGPL license. See terms of license at gnu.org.

package Icom.Utils;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Timer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.StringCharacterIterator;
import java.text.CharacterIterator;

public class Sys {

	private Sys(){
		
	}
	
	private static Sys mInstance;
	private static Object mLock = new Object();
	
	public static Sys getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new Sys();
			}
			return mInstance;
		}
	}
	
	/** Load properties file from classpath. */
	public Properties loadPropertiesResource(String aResourcePath) throws IOException {
		try {			
			ClassLoader classLoader = Sys.class.getClassLoader();
			
			Properties properties = new Properties();
			
			// Try loading it.
			properties.load(classLoader.getResourceAsStream(aResourcePath));			
			return properties;
		} catch (Throwable t) {
			throw new IOException("Loi: " + aResourcePath);
		}
	}

	public boolean savePropertiesResource(Properties properties, String aResourcePath) throws IOException {
		boolean result=false;
		try {												
			
			// Try loading it.						 		
			properties.store(new FileOutputStream(aResourcePath), null);
						
			result=true;
		} catch (IOException e) {
			System.out.print("Not Save Informaton into Properties!");
			throw e;
		}
		
		return result;
	}
	
	

}
