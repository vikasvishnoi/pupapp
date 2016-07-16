package com.insynchro.pup.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.insynchro.pup.domain.ImportResultBean;
import com.insynchro.pup.domain.XlsDataBean;
import com.insynchro.pup.excel.importer.XlsImporter;
import com.insynchro.pup.utils.CommonUtils;
import com.insynchro.pup.utils.ConfigProps;

/**
 * Servlet implementation class ImportXlsToP6
 */
public class ImportXlsToP6Controller extends HttpServlet {
	Logger logger = Logger.getLogger(ImportXlsToP6Controller.class);
	private static final long serialVersionUID = 1L;
	private static ConfigProps confProperty = ConfigProps.getInstance();
	// upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImportXlsToP6Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession(false);
		logger.info("inside ImportXlsToP6Controller session = "+session);
		if(session!=null) {
			
			logger.info("Inside ImportXlsToP6 controller");
			response.setContentType("application/json; charset=UTF-8");
			ImportResultBean importResultBean=null;
			List<XlsDataBean> errorLogs=null;
			List<XlsDataBean> dataLogs=null;
			
			String uploadPath=confProperty.getProperty("xlsFileDirPath");
			String xlsFileName="import"+"_"+CommonUtils.getCurrentDate()+"_"+new Date().getTime()+".xls";
			String primaveraHome=confProperty.getProperty("primavera.bootstrap.home");
			String p6User=session.getAttribute("p6UserName").toString();//"admin";
			String p6Pass=session.getAttribute("p6Password").toString();//"admin";
			String dataDate=null;
			PrintWriter out =null;
			boolean xlsFileValidation=false;
			logger.info("uploadPath : "+uploadPath +" , xlsFileName = "+xlsFileName+" , primaveraHome = "+primaveraHome+" , p6User= "+p6User+" , p6Pass = "+p6Pass);
			// configures upload settings
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        // sets memory threshold - beyond which files are stored in disk
	        factory.setSizeThreshold(MEMORY_THRESHOLD);
	        // sets temporary location to store files
	        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
	 
	        ServletFileUpload upload = new ServletFileUpload(factory);
	         
	        // sets maximum size of upload file
	        upload.setFileSizeMax(MAX_FILE_SIZE);
	         
	        // sets maximum size of request (include file + form data)
	        upload.setSizeMax(MAX_REQUEST_SIZE);
	 
	         
	        // creates the directory if it does not exist
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdir();
	        }
		
			try {
				importResultBean=new ImportResultBean();
				
				// parses the request's content to extract file data
	            @SuppressWarnings("unchecked")
	            List<FileItem> formItems = upload.parseRequest(request);
	            logger.info("formItems.size()********************* "+formItems.size());
	            if (formItems != null && formItems.size() > 0) {
	                // iterates over form's fields
	                for (FileItem item : formItems) {
	                	
	                    // processes only fields that are not form fields
	                    if (!item.isFormField()) {
	                        String fileName = new File(item.getName()).getName();
	                        logger.info("fileName+++++++++++++++++++++++++++++ "+fileName);
	                        String filePath = uploadPath + File.separator + xlsFileName;
	                        logger.info("filePath =================== "+filePath);
	                        File storeFile = new File(filePath);
	                        logger.info("storeFile =================== "+storeFile);
	                        
	                        if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
	                        	 // saves the file on disk
	                            item.write(storeFile);
	                            xlsFileValidation=true;
	                        } 
	                       
	                     
	                    } else {
	                    	
	                    	if(item.getFieldName().equalsIgnoreCase("dataDate")) {
	                    		dataDate= item.getString();
	                    	}
	                    
	                    	logger.info("dataDate =========== "+dataDate);
	                    }
	                }
	            } //File upload done 
	            
	            if(xlsFileValidation) {
	            	 dataLogs=XlsImporter.loadXlsDataToCollection(uploadPath+ File.separator+xlsFileName);
	                 if(dataLogs!=null && dataLogs.size()>0) {
	                	 logger.info("dataLogs size = "+dataLogs.size());
	                 	  /* for(XlsDataBean bean:dataLogs){
	                 		  logger.info("Proj id :"+bean.getProjectId()+" Act : "+bean.getActivityId()+" Res: "+bean.getResourceId()+" ActualUnits "+bean.getActualUnits()+" "+bean.getRemainingUnits()+" "+bean.getRemainingUnitsPerTime());
	     	                }*/
	                 	   errorLogs=XlsImporter.loadXlsDataToP6(dataLogs, primaveraHome, p6User, p6Pass,dataDate);
	                 	   
	                 	  
	     		            
	     	                if(errorLogs!=null && errorLogs.size()>0) {
	     	                	//Data imported with errors
	     	                	logger.info("errorLogs == "+errorLogs.size());
	     	                	logger.info("Response ----------------- 107");
	     	                	 importResultBean.setErrorCode("107");
	     							importResultBean.setErroMsg("Data imported with errors.");
	     							importResultBean.setXlsDataBeans(errorLogs);
	     	                } else {
	     	                	//Data imported successfully without errors
	     	                	logger.info("Response ----------------- 100");
	     	                	 importResultBean.setErrorCode("100");
	     							importResultBean.setErroMsg("Data imported successfully");
	     							importResultBean.setXlsDataBeans(errorLogs);
	     	                }
	     	                
	                 } else { //Validation if no records in file
	                	 logger.info("Response ----------------- 103");
	                 	importResultBean.setErrorCode("103");
	     				importResultBean.setErroMsg("Data file is empty..");
	                 	
	                 }
	            	
	            } else {
	            	logger.info("Inside xls validation else ");
	            	logger.info("Response ----------------- 101");
					importResultBean.setErrorCode("101");
					importResultBean.setErroMsg("Please upload  .xls or xlsx file.");
				}
	           
	            //Writing error logs to CSV files 
	            if(importResultBean.getXlsDataBeans().size()>0){
	            	String errorLogFile=CommonUtils.writeToCSV(importResultBean, uploadPath);
	            	logger.info("errorLogFile +++++++ "+errorLogFile);
	            	session.setAttribute("errorLogFile", errorLogFile);
	            
	            	}
	            
	            //Send response 
	            JSONObject json=new JSONObject(importResultBean);
	            logger.info("json ------------------ "+json.toString());
	            out = response.getWriter();
	            out.print(json);
	            out.flush();
	            
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		} else {
			//session expired
			response.sendRedirect("login.jsp");
		}
		
	}

}
