package com.insynchro.pup.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
public class PropertyListner implements Runnable {
	
	static ConfigProps property=ConfigProps.getInstance();
	
	/** Flag for config property file  */
	boolean propsChanged = false;	
	
	/**Thread which continuously monitoring the property files*/
	public Thread monitor=null;;
	
	/**Initialize the logger*/
	//private static Logger logger =Logger.getLogger(PropertyListner.class);
		
	
	/**
	 * This method continuously monitor the property files if any changes will done thread reload the property file
	 * */
	public void run() 
	{	
		try{
			while(true)
			{			
				propsChanged = isPropertyFileModified(ConfigProps.lastModifyDateTime);				
				if(propsChanged){
					property.loadProps();
					propsChanged=false;	
					
				}else{
					Thread.sleep(1000);
				}		
			
			}
		}catch (Exception e) {
			
			System.out.println("ConfigProps,property file operation ," + "Configuration property file monitoring has problem,"+
					 			 "\n"+e.getMessage());
		}		
	}
	
	/**
	 * Thread continuously monitor the property files
	 * 
	 * @param lastModifyDateTime
	 * @return true if file is modified else return false.
	 * @throws IOException
	 */
	private boolean isPropertyFileModified(Date lastModifyDateTime) throws IOException 
	{

		
		//String propertyFile ="/Users/vikas/Desktop/Insynchro/ixd/palmsws.config";//System.getProperty("palmsws.config");"c:/ixd/palmsws.properties"
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
								
		int diff;
		try
		{
			
			File file = new File(propertyFile);
			
	        // Get the last modification information.
	        Long lastModified = file.lastModified();
	        //System.out.printlnLog("in PropertyListner class and  isPropertyFileModified method :: lastModified:"+lastModified);	       
	        
	        // Create a new date object and pass last modified information
	        // to the date object.
	        Date date = new Date(lastModified);   
	        
	        diff= date.compareTo(lastModifyDateTime);	       
	        //System.out.printlnLog("in PropertyListner class and  isPropertyFileModified method :: Diffreence:"+diff);
	        
	       
	        if(diff!=0)
	        {
	        	propsChanged = true;	
	        	ConfigProps.lastModifyDateTime = date;
	        }
		   
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("ConfigProps,property file operation ," + "Error while checking the state of the property file,"+ "\n");		
		}
		
		
		return propsChanged;
	}
	
	/**
	 * Start the monitoring of the property file
	 * */
	public void startPropsMonitoring(PropertyListner propsListner)
	{		
		monitor = new Thread(propsListner);
		System.out.println("Monitor of the property is strated");
		monitor.start();		
	}
	
}
