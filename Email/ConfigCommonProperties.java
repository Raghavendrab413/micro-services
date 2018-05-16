package com.allconnect.filemerge.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.allconnect.property.management.cache.PropertyManager;

public class ConfigCommonProperties {
	private static final Logger logger = Logger.getLogger(ConfigCommonProperties.class);
	
	public static String EMAIL_ENABLED = null;
	public static String EMAIL_DFLT_FROM_EMAIL = null;
	public static String EMAIL_ERROR_EMAIL = null;
	public static String EMAIL_HOST = null;
	public static String EMAIL_PORT = null;
	public static String EMAIL_DFLT_MIME_TYPE = null;
	public static String EMAIL_PROTOCOL = null;
	public static String EMAIL_SECURE_CONNECTION_TYPE = null;
	public static String EMAIL_USERNAME = null;
	public static String EMAIL_PASSWORD = null;
	
	public static String HOST_NAME = null;
	public static Properties props = null;
	
	static {
		String COMMON_NAME = System.getProperty("PROCESS_NAME");
		try {
			props = PropertyManager.getPropertiesFile("ep_rope/" + COMMON_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*props = new Properties();     
	      try {
	       props.load(new FileInputStream("E:\\Allconnect\\EP\\FileMergeProcess.properties"));
	      } catch (IOException e) {
	       e.printStackTrace();
	      }*/

		if(!props.isEmpty()) {
			logger.info("Inside setting FileMergeProcess properties");
		
		EMAIL_ENABLED = props.getProperty("EMAIL_ENABLED");
		EMAIL_DFLT_FROM_EMAIL = props.getProperty("EMAIL_DFLT_FROM_EMAIL");
		EMAIL_ERROR_EMAIL = props.getProperty("EMAIL_ERROR_EMAIL");
		EMAIL_HOST = props.getProperty("EMAIL_HOST");
		EMAIL_PORT = props.getProperty("EMAIL_PORT");
		EMAIL_DFLT_MIME_TYPE = props.getProperty("EMAIL_DFLT_MIME_TYPE");
		EMAIL_PROTOCOL = props.getProperty("EMAIL_PROTOCOL");
		EMAIL_SECURE_CONNECTION_TYPE = props.getProperty("EMAIL_SECURE_CONNECTION_TYPE");
		EMAIL_USERNAME = props.getProperty("EMAIL_USERNAME");
		EMAIL_PASSWORD = props.getProperty("EMAIL_PASSWORD");
		HOST_NAME = props.getProperty("HOST_NAME");
		}
	}
}
