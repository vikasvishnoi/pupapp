package com.insynchro.pup.excel.importer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.insynchro.pup.domain.XlsDataBean;
import com.insynchro.pup.utils.CommonUtils;
import com.insynchro.pup.utils.P6Utils;
import com.primavera.common.value.BeginDate;
import com.primavera.common.value.EndDate;
import com.primavera.common.value.ObjectId;
import com.primavera.common.value.Unit;
import com.primavera.common.value.UnitsPerTime;
import com.primavera.integration.client.EnterpriseLoadManager;
import com.primavera.integration.client.GlobalObjectManager;
import com.primavera.integration.client.RMIURL;
import com.primavera.integration.client.Session;
import com.primavera.integration.client.bo.BOIterator;
import com.primavera.integration.client.bo.enm.ActivityStatus;
import com.primavera.integration.client.bo.enm.DurationType;
import com.primavera.integration.client.bo.object.Activity;
import com.primavera.integration.client.bo.object.Project;
import com.primavera.integration.client.bo.object.Resource;
import com.primavera.integration.client.bo.object.ResourceAssignment;
import com.primavera.integration.client.bo.object.WBS;
import com.primavera.integration.common.DatabaseInstance;
import com.primavera.integration.common.JobId;

public class XlsImporter {
	static Logger logger = Logger.getLogger(XlsImporter.class);
	public static List<XlsDataBean> loadXlsDataToP6(List<XlsDataBean> list, String primaveraHome, String dbuser,
			String dbPass, String dataDate) {

		List<XlsDataBean> errorLogs = null;
		Session session = null;
		String errorMsg;
		try {
			System.out.println("inside loadXlsDataToCache method");
			System.out.println("inside loadXlsDataToCache method");
			// XlsDataBean xlsDataBean=null;

			// **********************************Create p6 API
			// session****************************************************************************

			System.setProperty("primavera.bootstrap.home", primaveraHome);
			System.out.println("RMIURL.LOCAL_SERVICE  = " + RMIURL.LOCAL_SERVICE);
			DatabaseInstance[] dbInstances = Session.getDatabaseInstances(RMIURL.getRmiUrl(RMIURL.LOCAL_SERVICE));
			// Assume only one database instance for now, and hardcode the
			// username and
			// password for this sample code
			session = Session.login(RMIURL.getRmiUrl(RMIURL.LOCAL_SERVICE), dbInstances[0].getDatabaseId(), dbuser,
					dbPass);

			System.out.println("Session = " + session);
			EnterpriseLoadManager elm = session.getEnterpriseLoadManager();
			GlobalObjectManager gom = session.getGlobalObjectManager();
			// ******************************************End
			// P6***********************************************************************************
			BOIterator<Project> projectBoi = null;
			Project proj = null;
			com.primavera.integration.client.bo.object.Activity activity = null;
			String actObjId = null;
			String projectObjId = null;

			errorLogs = new ArrayList<XlsDataBean>();

			Set<String> existingProj = new LinkedHashSet<String>();

			for (XlsDataBean xlsDataBean : list) {
				errorMsg="";
				// P6 Actions
				// EPS Archive Available then copy project into EPS ARchive
				System.out.println("Reource ID --------------------------- " + xlsDataBean.getResourceId());
				projectBoi = elm.loadProjects(new String[] { "Name", "Status", "ObjectId" },
						CommonUtils.parseSelection("Id", xlsDataBean.getProjectId()), "Id");

				int projectCount = projectBoi.getAll().length;
				System.out.println(
						"project id **** " + xlsDataBean.getProjectId() + " *** " + xlsDataBean.getActivityId());
				System.out.println("boi  size ----- = " + projectBoi.getAll().length);
				BOIterator<WBS> wbsBoi = null;
				WBS wbs = null;
				ObjectId wbsObjId = null;
				ObjectId actObId = null;
				boolean actvityStatusFlag = false;
				if (projectCount == 1) { // If project id is exist
					String projectStatus = "NA";
					while (projectBoi.hasNext()) {
						proj = projectBoi.next();
						// Verfiy WBS Gen
						// WBS q=null;
						// q.get

						try {
							wbsBoi = elm.loadWBS(new String[] { "Code" },
									"Code='GEN' and ProjectObjectId='" + proj.getObjectId() + "'", "Code");
						} catch (NoSuchElementException e) {
							System.out.println("Caught exception while loading wbs");
						}

						System.out.println("wbsBoi*******" + wbsBoi.getAll().length);
						if (wbsBoi.getAll().length == 0) {
							// Create WBS GEN
							System.out.println("wbs not found***********");
							wbs = new WBS(session);
							wbs.setProjectObjectId(proj.getObjectId());
							wbs.setCode("GEN");
							wbs.setName("General");
							wbsObjId = wbs.create();
							System.out.println("wbsObjId= " + wbsObjId);
							wbsBoi = elm.loadWBS(new String[] { "Code" },
									"Code='GEN' and ProjectObjectId='" + proj.getObjectId() + "'", "Code");

							// wbs.update();
							System.out.println("Created WBS GEN");
						}
						wbs = wbsBoi.next();
						wbsObjId = wbs.getObjectId();
						// System.out.println("wbs --------------------------=
						// "+wbs);
						// Todo if WGS GEN not found log error and continue loop
						// for next record
						System.out.println("Project Name : " + proj.getName());
						projectStatus = proj.getStatus().toString();
						projectObjId = proj.getObjectId().toString();
						System.out.println("Project Status :: -- " + proj.getStatus().toString());

					}
					/*****************
					 * Active Project
					 *********************************************************************/
					if (projectStatus.equalsIgnoreCase("Active")) {

						// Bacup project into Archive EPS
						Project projectTemp = null;// P6Utils.createBackupProject(proj,
													// session, "Archive", gom);
						// System.out.println("projectTemp after backup = " +
						// projectTemp.getName());

					/*	if (!existingProj.contains(proj.getName())) {
							projectTemp = P6Utils.createBackupProject(proj, session, "Archive", gom);
							System.out.println("projectTemp after backup = " + projectTemp.getName());
						}*/
						if (!existingProj.contains(proj.getName())) {
							try {
								projectTemp = P6Utils.createBackupProject(proj, session, "Archive", gom);
								System.out.println("projectTemp after backup = " + projectTemp.getName());
							} catch(Exception e1) {
								
								errorMsg=errorMsg+"<br/>"+"Error: Unable to create backup project. Please verify that the responsible manager has access to the [Enterprise] EPS node";
								
							}
							
						}

						existingProj.add(proj.getName());

						// ******************Activity
						// Validation****************************************************************

						//
						BeginDate beginDate=null;
						EndDate endDate=null;
						BOIterator<com.primavera.integration.client.bo.object.Activity> activityIterator = elm
								.loadActivities(new String[] { "Name", "Id", "ObjectId", "Status","ActualStartDate","ActualFinishDate" },
										"upper(ProjectId)='" + xlsDataBean.getProjectId().toUpperCase()
												+ "' and upper(Id)='" + xlsDataBean.getActivityId().toUpperCase() + "'",
										"ProjectId");
						System.out.println("activityIterator " + activityIterator);

						int activitySize = activityIterator.getAll().length;
						if (activitySize == 0) {
							System.out.println("inside if (activitySize == 0) ");
							// TODO : Create new activity in WBS[GEN]
							// and reload the activity to refill
							// activityIterator
							activity = new Activity(session);
							activity.setWBSObjectId(wbsObjId);
							activity.setProjectObjectId(proj.getObjectId());
							activity.setId(xlsDataBean.getActivityId());
							activity.setName(xlsDataBean.getActivityId() + "-PUP");
							activity.setDurationType(DurationType.FIXED_UNITS);
							//TODO for Actual Start / Actual Finish 
							/* If Activity in XL has actual start AND actual finish, then update actual start and finish in p6 
							 * If Activity in XL has actual start, then update actual start in p6
                               If Activity in XL has Actual Finish but no Actual Start Log Error*/
								
							actObId = activity.create();
							actObjId = actObId.toString();
							System.out.println("actObjId 	if (activitySize == 0) === " + actObjId);

							// reloading activity
							activityIterator = elm.loadActivities(new String[] { "Name", "Id", "ObjectId", "Status","ActualStartDate","ActualFinishDate" },
									"upper(ProjectId)='" + xlsDataBean.getProjectId().toUpperCase()
											+ "' and upper(Id)='" + xlsDataBean.getActivityId().toUpperCase() + "'",
									"ProjectId");
							System.out.println("activityIterator size====== " + activityIterator.getAll().length);

						}
						while (activityIterator.hasNext()) {
							activity = activityIterator.next();
							if (activity.getStatus().getValue().equals(ActivityStatus.COMPLETED.getValue()))
								actvityStatusFlag = true;
							
							//Actuals date check 
							

							System.out.println("actvityStatusFlag ========================== " + actvityStatusFlag);
							// Add code here to process each Activity...
							System.out.println("Activity : " + activity.getName());
							System.out.println("Activity object id ***** : " + activity.getObjectId().toString());
							if (activity.getId().equalsIgnoreCase(xlsDataBean.getActivityId())) {
								actObId = activity.getObjectId();
								actObjId = activity.getObjectId().toString();
								System.out.println("Match--------");
							}
							
							//Actual start and actual finish checks 
							/*
							 * 
							 * */
							if(activity.getActualStartDate()==null && activity.getActualFinishDate()==null) {
								if(xlsDataBean.getActualStart()!=null && xlsDataBean.getActualFinish()!=null ) {
									System.out.println("Check 1 : act start ! null && act finish !null");
									
									if(xlsDataBean.getActualStart().getTime() > xlsDataBean.getActualFinish().getTime()) {
										//errorMsg=(xlsDataBean.getRemarks()!=null) ? xlsDataBean.getRemarks()+"<br/>" : "";
										errorMsg=errorMsg+"<br/>"+"Error: Actual Start can't be greater then Actual Finish";
										//xlsDataBean.setRemarks(errorMsg+"Error: Actual Start can't be greater then Actual Finish");
										System.out.println("errorMsg : "+errorMsg);
									} else {
										System.out.println("else setting act start and finish to p6");
									beginDate=new BeginDate(xlsDataBean.getActualStart());
									activity.setActualStartDate(beginDate);
									
									endDate=new EndDate(xlsDataBean.getActualFinish());
									activity.setActualFinishDate(endDate); 
									}
									
								} else if (xlsDataBean.getActualStart()!=null && xlsDataBean.getActualFinish()==null) {
									System.out.println("Check 2 : if act start ! null && act finish =null");
									beginDate=new BeginDate(xlsDataBean.getActualStart());
									activity.setActualStartDate(beginDate);
								} else if (xlsDataBean.getActualStart()==null && xlsDataBean.getActualFinish()==null) {
									System.out.println("Check 3 : if act start = null && act finish !=null");
									//errorMsg=(xlsDataBean.getRemarks()!=null) ? xlsDataBean.getRemarks()+"<br/>" : "";
									//xlsDataBean.setRemarks(errorMsg+"Error: Actual Start and Actual Finish is null");
									errorMsg=errorMsg+"<br/>"+"Warning!: Actual Start  and Actual Finish does't exist in excel.";
									System.out.println("Check 3 :errorMsg : "+errorMsg);
								}
							} else if(activity.getActualStartDate()!=null && activity.getActualFinishDate()==null) {
								if(xlsDataBean.getActualStart()!=null && xlsDataBean.getActualFinish()!=null ) {
									endDate=new EndDate(xlsDataBean.getActualFinish());
									activity.setActualFinishDate(endDate);
								} 
								
							} /*else if(activity.getActualStartDate()==null && activity.getActualFinishDate()==null) {
								if(xlsDataBean.getActualStart()!=null && xlsDataBean.getActualFinish()!=null ) {
									beginDate=new BeginDate(xlsDataBean.getActualStart());
									activity.setActualStartDate(beginDate);
									
									endDate=new EndDate(xlsDataBean.getActualFinish());
									activity.setActualFinishDate(endDate);
								} 
								
							}*/
							try {
								System.out.println("***Updating Activity : "+activity.getId()+" activdate = ity act st date = "+activity.getActualStartDate()+" , activity act end = "+activity.getActualFinishDate());
							activity.update();
							} catch(Exception e2) {
								errorMsg=errorMsg+"<br/>"+"Error!:Activity Id : ["+activity.getId()+"] "+e2.getMessage();
								System.out.println("Error : in activity id ="+activity.getId()+" update "+errorMsg);
							}
							
						}

						// ******************Activity Validation
						// End****************************************************************

						// ******************Loading
						// Resources**********************************************************************

						System.out.println("*** projectObjId = " + projectObjId + " actObjId = " + actObjId
								+ " xlsDataBean.getResourceId() = " + xlsDataBean.getResourceId().toUpperCase()
								+ " ***");
						BOIterator<ResourceAssignment> resAsgn = elm.loadResourceAssignments(
								new String[] { "ObjectId", "ActivityObjectId", "ActivityId", "ResourceObjectId",
										"ResourceId", "PlannedCost", "PlannedUnits", "ProjectObjectId" },
								"ProjectObjectId='" + projectObjId + "' and ActivityObjectId='" + actObjId
										+ "' and upper(ResourceId)='" + xlsDataBean.getResourceId().toUpperCase() + "'",
								null);
						int resAsgnSize = resAsgn.getAll().length;
						System.out.println("resAsgn = " + resAsgn);
						System.out.println("resAsgnSize = " + resAsgnSize);
						ResourceAssignment asgn = null;

						// Resource resource=null;

						if (resAsgnSize == 0) {
							// TODO: Validate if resource available
							// if not
							// available then throw a fatal error [Log and skip
							// remaining process for this record
							// ]
							// otherwise assign resource to this activity

							// Load resource
							BOIterator<Resource> resBoi = elm.loadResources(new String[] { "Id", "ObjectId", },
									"Id='" + xlsDataBean.getResourceId() + "'", "Id");
							if (resBoi.getAll().length == 0) {
								//errorMsg=(xlsDataBean.getRemarks()!=null) ? xlsDataBean.getRemarks()+"<br/>" : "";
								//xlsDataBean.setRemarks(errorMsg+"Resource is not available");
								errorMsg=errorMsg+"<br/>"+"Resource is not available";
								//errorLogs.add(xlsDataBean);

							} else {
								// Assign Resource
								Resource resource = resBoi.next();
								ResourceAssignment resourceAssignment = new ResourceAssignment(session);
								resourceAssignment.setActivityObjectId(actObId);
								resourceAssignment.setResourceObjectId(resource.getObjectId());
								ObjectId resAsgnmntObjId = resourceAssignment.create();

								System.out.println("resourceAssignment =*********************** " + resourceAssignment);

								resAsgn = elm.loadResourceAssignments(
										new String[] { "ObjectId", "ActivityObjectId", "ActivityId", "ResourceObjectId",
												"ResourceId", "PlannedCost", "PlannedUnits", "ProjectObjectId" },
										"ProjectObjectId='" + projectObjId + "' and ActivityObjectId='" + actObjId
												+ "' and upper(ResourceId)='"
												+ xlsDataBean.getResourceId().toUpperCase() + "'",
										null);
								// System.out.println("sResourceId = " +
								// resourceAssignment.getResourceId());

								resourceAssignment.setObjectId(resAsgnmntObjId);
								double actualUnits = xlsDataBean.getActualUnits();
								Unit actUnit = new Unit(actualUnits);

								resourceAssignment.setActualUnits(actUnit);
								resourceAssignment.update();

								Unit remUnit = new Unit(xlsDataBean.getRemainingUnits());
								resourceAssignment.setRemainingUnits(remUnit);
								resourceAssignment.update();

								UnitsPerTime unitsPerTime = new UnitsPerTime(xlsDataBean.getRemainingUnitsPerTime());
								resourceAssignment.setRemainingUnitsPerTime(unitsPerTime);
								resourceAssignment.update();
								System.out.println("Assign Resource resAsgnmntObjId = " + resAsgnmntObjId);

							}

						} else {
							// *******************Resources
							// end***********************************************************************

							// *******************Ready to update Resource
							// Assignments
							// actuals**************************************
							asgn = resAsgn.next();

							System.out.println("asgn =*********************** " + asgn);

							System.out.println("sResourceId = " + asgn.getResourceId());

							double actualUnits = xlsDataBean.getActualUnits();
							Unit remUnit = new Unit(xlsDataBean.getRemainingUnits());
							Unit actUnit = new Unit(actualUnits);
							UnitsPerTime unitsPerTime = new UnitsPerTime(xlsDataBean.getRemainingUnitsPerTime());

							if (actvityStatusFlag) {
								asgn.setActualUnits(actUnit);
								asgn.update();
							} else {
								asgn.setActualUnits(actUnit);
								asgn.update();

								asgn.setRemainingUnits(remUnit);
								asgn.update();

								asgn.setRemainingUnitsPerTime(unitsPerTime);
								asgn.update();
							}

							System.out.println("updated Resource Assignments actuals successfully");

						}

						// session.getJobManager().schedule(project,
						// newDataDate);
						// session.getJobManager().summarize(project);
						// *******************Ready to update Resource
						// Assignments actuals
						// end**************************************

					} else {
						// Log Error and terminate process
						// System.exit(0);
						//errorMsg=(xlsDataBean.getRemarks()!=null) ? xlsDataBean.getRemarks()+"<br/>" : "";
						//xlsDataBean.setRemarks(errorMsg+"Project not active");
						errorMsg=errorMsg+"<br/>"+"Project not active";
						//errorLogs.add(xlsDataBean);
					}
				} else {
					// Else if project id does't exist in P6 then log error and
					// terminate process..
					// System.exit(0);
					//errorMsg=(xlsDataBean.getRemarks()!=null) ? xlsDataBean.getRemarks()+"<br/>" : "";
					//xlsDataBean.setRemarks(errorMsg+"Project does't exist");
					errorMsg=errorMsg+"<br/>"+"Project does't exist";
					//errorLogs.add(xlsDataBean);

				}
				
				if(!errorMsg.isEmpty()) {
				xlsDataBean.setRemarks(errorMsg);
				errorLogs.add(xlsDataBean);
				}
			}

			// Project Scheduling
			if (proj != null) {
				System.out.println("Inside project scheduling");
				JobId schedulingJobId = session.getJobManager().schedule(proj,
						CommonUtils.convertStrToDate(dataDate, "yyyy-MM-dd"));
				JobId summJobId = session.getJobManager().summarize(proj);

				System.out.println("schedulingJobId.getPrimaryKeyObject(); = " + schedulingJobId.getPrimaryKeyObject());
				System.out.println("summJobId.getPrimaryKeyObject(); = " + summJobId.getPrimaryKeyObject());

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (session != null) {
					session.logout();
					session = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in finally of loadXlsDataToP6 method");
			}
		}
		return errorLogs;
	}

	public static List<XlsDataBean> loadXlsDataToCollection(String filePath) {

		List<XlsDataBean> data = null;

		FileInputStream fis = null;
		try {
			System.out.println("inside loadXlsDataToCollection method");
			fis = new FileInputStream(filePath);
			System.out.println("inside loadXlsDataToCollection method fis = " + fis);

			XlsDataBean xlsDataBean = null;
			// Using XSSF for xlsx format, for xls use HSSF
			Workbook workbook = new XSSFWorkbook(fis);

			// for(int i=0;i<numberOfSheets;i++) {
			Sheet sheet = workbook.getSheetAt(0);// First sheet
			Iterator<Row> rowIterator = sheet.iterator();

			// iterating over each row
			data = new ArrayList<XlsDataBean>();
			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				// Iterating over each cell (column wise) in a particular row.
				xlsDataBean = new XlsDataBean();
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					// Column Item at index 0

					// ProjectID Cell
					if (cell.getColumnIndex() == 0 && Cell.CELL_TYPE_STRING == cell.getCellType()
							&& !cell.getStringCellValue().equalsIgnoreCase("Project ID")) {
						System.out.println("Found Project type cell..1....." + cell.getStringCellValue());
						if (cell.getStringCellValue() != null)
							xlsDataBean.setProjectId(cell.getStringCellValue());
						// Column description
					}
					// ActivityID Cell
					else if (cell.getColumnIndex() == 1 && Cell.CELL_TYPE_STRING == cell.getCellType()
							&& !cell.getStringCellValue().equalsIgnoreCase("Activity ID")) {
						System.out.println("Found Activity ID cell....2..." + cell.getStringCellValue());
						if (cell.getStringCellValue() != null)
							xlsDataBean.setActivityId(cell.getStringCellValue());
					}
					// ResourceId cell
					else if (cell.getColumnIndex() == 2 && Cell.CELL_TYPE_STRING == cell.getCellType()
							&& !cell.getStringCellValue().equalsIgnoreCase("Resource ID")) {
						System.out.println("Found Resource ID cell...3...." + cell.getStringCellValue());
						if (cell.getStringCellValue() != null)
							xlsDataBean.setResourceId(cell.getStringCellValue());

					}
					// actualUnits cell
					else if (cell.getColumnIndex() == 3 && Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						System.out.println("Found getNumericCellValue1....4..." + cell.getNumericCellValue());
						if (cell.getNumericCellValue() != 0.0)
							xlsDataBean.setActualUnits(cell.getNumericCellValue());
					}
					// remainingUnits cell
					else if (cell.getColumnIndex() == 4 && Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						System.out.println("Found getNumericCellValue2...5...." + cell.getNumericCellValue());
						if (cell.getNumericCellValue() != 0.0)
							xlsDataBean.setRemainingUnits(cell.getNumericCellValue());
					}
					// remainingUnitsPerTime
					else if (cell.getColumnIndex() == 5 && Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						System.out.println("Found getNumericCellValue3....5..." + cell.getNumericCellValue());
						if (cell.getNumericCellValue() != 0.0)
							xlsDataBean.setRemainingUnitsPerTime(cell.getNumericCellValue());
					}
					// Actual start
					/*else if (cell.getColumnIndex() == 6 && !cell.getStringCellValue().equalsIgnoreCase("Actual Start")) {
						
						System.out.println("Found Actual Start...col.7..." + cell.getStringCellValue());
						
						if (cell.getStringCellValue() != null)	
						xlsDataBean.setActualStart(
									CommonUtils.convertStrToDate(cell.getStringCellValue(), "dd.MM.yyyy"));
					}
					// Actual Finish
					else if (cell.getColumnIndex() == 7 && !cell.getStringCellValue().equalsIgnoreCase("Actual Finish")) {
						System.out.println("Found Actual Finish...col.8..." + cell.getStringCellValue());
						
						if (cell.getStringCellValue() != null)
							xlsDataBean.setActualFinish(
									CommonUtils.convertStrToDate(cell.getStringCellValue(), "dd.MM.yyyy"));
					}*/
					//Actual start
					else if(cell.getColumnIndex() == 6 && cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){ 
						System.out.println("Found Actual start getDateCellValue Start...col.6..." + cell.getDateCellValue());
						/*xlsDataBean.setActualStart(
								CommonUtils.convertStrToDate(cell.getDateCellValue(), "dd.MM.yyyy"));*/
						xlsDataBean.setActualStart(
								cell.getDateCellValue());
						}
					// Actual Finish
					else if(cell.getColumnIndex() == 7 && cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){ 
						System.out.println("Found Actual finish getDateCellValue Start...col.7..." + cell.getDateCellValue());
						/*xlsDataBean.setActualFinish(
								CommonUtils.convertStrToDate(cell.getDateCellValue(), "dd.MM.yyyy"));*/
						xlsDataBean.setActualFinish(cell.getDateCellValue());
						}


				}

				/*
				 * if (xlsDataBean.getProjectId() != null &&
				 * xlsDataBean.getActivityId() != null &&
				 * xlsDataBean.getResourceId() != null &&
				 * xlsDataBean.getRemainingUnits() != 0.0 &&
				 * xlsDataBean.getRemainingUnitsPerTime() != 0.0 &&
				 * xlsDataBean.getActualUnits() != 0.0)
				 */
				if (xlsDataBean.getProjectId() != null)
					data.add(xlsDataBean);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in finally of loadXlsDataToCollection method");
			}

		}
		return data;
	}

}
