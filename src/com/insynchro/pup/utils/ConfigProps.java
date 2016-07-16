package com.insynchro.pup.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

/**
 * Singleton class to manage the config parameters 
 */

public class ConfigProps {

	/** store the last modify state of the configuration file*/
	static Date  lastModifyDateTime = new Date();	
	/**
	 */
	public static String contextPath="";
	
	/** Configuration properties.*/
	private static Properties properties = new Properties();

	/**
	 */
	//private static Logger logger = Logger.getLogger(ConfigProps.class);
	
	/**
	 * private constructors to load properties.   
	 */
	private ConfigProps() {
		try {
			loadProps();
		} catch (Exception e) {			
			System.out.println("ConfigProps,property file operation ," +
								 "Error in loading configuration file in ConfigProps constructore,"+"\n"+e.getMessage());	
			
			
		}
	}

	/** Create the only instance, save it to a private static variable */
	private static ConfigProps instance = new ConfigProps();

	
	/**
	 * @return Instance of ConfigProps
	 */
	public static ConfigProps getInstance() {
		
		if(null==ConfigProps.instance){
			System.out.println("getting instance 1");
			ConfigProps.instance = new ConfigProps();
		}		
		return ConfigProps.instance;
	}
	
	/** object of property listener continuously monitor the property file and dynamically load the props files*/
    public static PropertyListner propsListner = new PropertyListner();
    
	/**
	 * To set the context path.
	 * @param conPath set the context path.
	 */
	public void setContextPath(String conPath){
		contextPath=conPath;
	}
	
	/** Initialize the configuration properties.
	 * @throws Exception
	 */
	public void configInit() throws Exception {		
			loadProps();			
			this.propsListner.startPropsMonitoring(propsListner);
	}
	
	
	/**
	 * It loads the config properties for PCS.
	 * @throws Exception
	 */
	public synchronized void loadProps()throws Exception {
		//String propertyFile ="/Users/vikas/Desktop/Insynchro/ixd/palmsws.properties";// System.getProperty("palmsws.config");//"c:/ixd/palmsws.properties";
		String propertyFile ="C:\\pup-conf\\pup.properties";
		
		if ((new File(propertyFile).exists())) {
		System.out.println("Config.Properties file location = " + propertyFile);
		}
		
		if (!(new File(propertyFile).exists())) {
		    URL url = this.getClass().getClassLoader().getResource("pup.properties");
		    if (url != null) {
		        propertyFile = url.getFile();
		    }
		}	
		
		try {
			FileInputStream fis = new FileInputStream(propertyFile);
			synchronized (properties) {
				properties.clear();
				properties.load(fis);
				
			}	
			System.out.println("Successfully loaded the property file.");
			System.out.println("Newly Loaded property file: "+properties.toString());
			fis.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("ConfigProps,property file operation ," +
					 			 "Configuration file not found,"+
					 			 "\n"+e.getMessage());
			throw new Exception("Configuration file not found.");
			
		} catch (IOException e) {
			System.out.println("ConfigProps,property file operation ," +
								 "Error in loading configuration file,"+
								 "\n"+e.getMessage());
			throw new Exception("Error in loading configuration file ");
		}
	}

	/**
	 * Returns the value of a property.
	 * @param key - property name
	 * @return value of the property
	 */
	public synchronized String getProperty(String key) {
		String val = this.properties.getProperty(key);	
		if (val == null)
			return null;
		else {
			String trimmed = val.trim();
			if (!trimmed.equals(val))
				System.out.println("value for key " + key
						+ " has leading or trailing spaces. "
						+ "Trimming from \"" + val + "\" to \"" + trimmed
						+ "\"");
			return trimmed;
		}
	}

}
