package com.ibm.gswt.campus.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class ApplicationProperties {
	
	private static Logger logger = Logger.getLogger(ApplicationProperties.class);
	
	private static final String LOCAL_PROP_FILE_NAME = "app.properties";
	private static final String PROP_FILE_RESOURCE_ID = "url/appProperties";
	private static Properties properties = null;
	
	private static void load() throws IOException {
		InputStream inputStream  = getPropFileStream();
		properties = new Properties();
		properties.load(inputStream);
	}
	
	private static InputStream getPropFileStream() {
		
		try {
			Context ctx  = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:comp/env");
			URL propertiesFileLocation = (URL) envCtx.lookup(PROP_FILE_RESOURCE_ID);
			logger.info("Got property file location from JNDI: " + propertiesFileLocation);
			logger.info("Connecting to application properties file...");
			URLConnection conn = propertiesFileLocation.openConnection();
			logger.info("Reading application properties file as a stream...");
			
			return conn.getInputStream();
		} catch (NamingException e) {
			logger.error("Failed to get JNDI context. Falling back to local properties file.");
			logger.error(e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("Failed to load application properties from path returned by JNDI. Cannot continue.");
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error("Failed to read the InputStream from the application properties file. Will fall back to the local properties file.");
			logger.error(e.getMessage());
		}
		
		return ApplicationProperties.class.getClassLoader().getResourceAsStream(LOCAL_PROP_FILE_NAME);
	}
	
	public static Properties getProps() {
		if (properties == null) {
			try {
				load();
			} catch (IOException e) {
				return null;
			}
		}
		
		return properties;
	}
}
