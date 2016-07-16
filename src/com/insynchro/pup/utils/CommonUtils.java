package com.insynchro.pup.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.insynchro.pup.domain.ImportResultBean;
import com.insynchro.pup.domain.XlsDataBean;
import com.primavera.common.value.ObjectId;

public class CommonUtils {
	private static final long LIMIT = 10000000000L;
	private static long last = 0;
	
	public static String getCurrentDate() {
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
		return modifiedDate;
	}
	
	
	

	public static long getID() {
	  // 10 digits.
	  long id = System.currentTimeMillis() % LIMIT;
	  if ( id <= last ) {
	    id = (last + 1) % LIMIT;
	  }
	  return last = id;
	}
	
	public static void main(String[] a){
		getNewFileName("aeeee.ert",1);
		
	}
	
	public static String getNewFileName(String fileName,int count) {
		String newFileName="";
		System.out.println(fileName);
		if(count==0) {
			newFileName=fileName;
		} else if(count>0) {
			String extension = "";

			int i = fileName.lastIndexOf('.');
			int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

			if (i > p) {
			    extension = fileName.substring(i+1);
			    System.out.println(extension);
			}
			
			String filePart1=fileName.substring(0,i);
			newFileName=filePart1+"("+count+")."+extension;
			System.out.println(newFileName);
			
		}
		
		return newFileName;
	}
	
	 
	 public static String parseSelection( String field, Object value ) {

	        if ( value == null ) value = "";
	        return "upper(" + field + ")='" + StringEscapeUtils.escapeSql(value.toString()).toUpperCase() + "'";

	    }
	 
	 
	 
	 /**
	     * Converts a String to an Object Id
	     *
	     * @param stringIds The string list to convert
	     * @return The Object Id array
	     */
	    public static ObjectId[] parseObjectIds( String[] stringIds ) {

	        ObjectId[] objectIds = null;

	         if( stringIds != null && stringIds.length > 0 ) {

	             objectIds = new ObjectId[ stringIds.length ];

	             for( int i = 0, len = stringIds.length ; i < len ; i ++ )
	                objectIds[ i ] = parseObjectId( stringIds[ i ] );
	         }

	        return objectIds;
	    }


	    /**
	     * Converts a String to an Object Id
	     *
	     * @param string The string to convert
	     * @return The string as an Object Id
	     */
	    public static ObjectId parseObjectId( String string ) {

	        return new ObjectId( Integer.parseInt( string ) );
	    }
	    
	    
	   public static Date convertStrToDate(String strDate,String format) {
		   SimpleDateFormat formatter = new SimpleDateFormat(format);
		   String dateInString = strDate;
		   Date date =null;
			try {

			    if(strDate!=null) {
			    	date = formatter.parse(dateInString);
					System.out.println(date);
					System.out.println(formatter.format(date));
			    }

			} catch (Exception e) {
				e.printStackTrace();
			}
			return date;
	   }
	    
	   
	
	   
	    public static String writeToCSV(ImportResultBean resultBeans,String errorLogFileDir)
	    {
	    	final String CSV_SEPARATOR = ",";
	    	String logFileLocation=null;
	        try
	        {
	        	System.out.println("inside writeToCSV.... ");
	            logFileLocation=errorLogFileDir+ File.separator+"error-logs.csv";
	            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errorLogFileDir+ File.separator+"error-logs.csv"), "UTF-8"));
	            for (XlsDataBean xlsDataBean : resultBeans.getXlsDataBeans())
	            {
	                StringBuffer oneLine = new StringBuffer();
	                
	                oneLine.append(xlsDataBean.getProjectId());
	                oneLine.append(CSV_SEPARATOR);
	                
	                oneLine.append(xlsDataBean.getActivityId());
	                oneLine.append(CSV_SEPARATOR);
	                
	                oneLine.append(xlsDataBean.getResourceId());
	                oneLine.append(CSV_SEPARATOR);
	                
	                oneLine.append(xlsDataBean.getActualUnits());
	                oneLine.append(CSV_SEPARATOR);
	                
	                oneLine.append(xlsDataBean.getRemainingUnits());
	                oneLine.append(CSV_SEPARATOR);
	                
	                oneLine.append(xlsDataBean.getRemainingUnitsPerTime());
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(xlsDataBean.getRemarks());
	                
	                
	                
	                bw.write(oneLine.toString());
	                bw.newLine();
	            }
	            bw.flush();
	            bw.close();
	        }
	        catch (UnsupportedEncodingException e) {
	        	System.out.println("UnsupportedEncodingException in writeToCSV : "+e.getMessage());
	        }
	        catch (FileNotFoundException e){
	        	System.out.println("FileNotFoundException in writeToCSV : "+e.getMessage());
	        }
	        catch (IOException e){
	        	System.out.println("IOException in writeToCSV : "+e.getMessage());
	        }
	        
	        return logFileLocation;
	    }

	

}
