package com.insynchro.pup.utils;


import java.text.SimpleDateFormat;

import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.GlobalObjectManager;
import com.primavera.integration.client.Session;
import com.primavera.integration.client.bo.BOIterator;
import com.primavera.integration.client.bo.object.EPS;
import com.primavera.integration.client.bo.object.OBS;
import com.primavera.integration.client.bo.object.Project;
import com.primavera.integration.common.CopyActivityOptions;
import com.primavera.integration.common.CopyProjectOptions;
import com.primavera.integration.common.CopyWBSOptions;

public class P6Utils {

	  public static synchronized Project createBackupProject(Project cwp, Session p3e,String epsId,GlobalObjectManager gom) throws Exception {
		  
		    
	        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmm");
	        System.out.println("epsId 1== "+epsId);
	        EPS backupEPS = getById(p3e, epsId,gom);
	        System.out.println("backupEPS 2== "+backupEPS.getId());
	        OBS obs = getByName(p3e, "Enterprise",gom);
	        System.out.println("obs 3== "+obs.getName()+" ,  obs.getObjectId() = "+obs.getObjectId());
	        Project backup = copy(backupEPS.getObjectId().toString(), true, p3e,cwp);
//	        backup.setUDFText(OPProject.SKALA_ORG_PROJECT_ID, cwp.getId());
//	        backup.setUDFStartDate(OPProject.SKALA_BACKUP_DATE, new Date());
//	        backup.setStatus(ProjectStatus.INACTIVE);
	        if (obs != null){
	        	System.out.println("inside if (obs != null) before");
	        	 backup.setOBSObjectId(obs.getObjectId());
	        	 System.out.println("inside if (obs != null) after");
	        }
	           
	        
	        backup.update();

	        return backup;
	    }
	  
	   public static EPS getById( Session p3e, String id , GlobalObjectManager gom ) throws Exception {

	        EPS returnValue = null;
	        BOIterator<EPS> boi = gom.loadEPS( new String[]{ "ObjectId", "Id", "Name" }, CommonUtils.parseSelection( "Id", id ), "Id" );
	        System.out.println("inside getById boi = "+boi.getAll().length);

	        if ( boi.hasNext() )
	            returnValue =  boi.next();
	        
	        
	        System.out.println(" returnValue= "+returnValue.getId());
	        return returnValue;
	    }
	   
	   
	   public static Project copy(String EPSId, boolean includeDetails, Session p3e,Project p3eProject ) throws Exception {

		   System.out.println("inside copy EPSId = "+EPSId);
	        CopyProjectOptions projOpts = new CopyProjectOptions();
	        CopyWBSOptions wbsOpts = new CopyWBSOptions();
	        CopyActivityOptions actOpts = new CopyActivityOptions();

	        projOpts.setCopyDocuments(includeDetails);
	        projOpts.setCopyFundingSources(includeDetails);
	        projOpts.setCopyIssuesThresholds(includeDetails);
	        projOpts.setCopyReports(includeDetails);
	        projOpts.setCopyRisks(includeDetails);

	        wbsOpts.setCopyActivities(includeDetails);
	        wbsOpts.setCopyHighLevelResourcePlanningAssignments(includeDetails);
	        wbsOpts.setCopyNotebook(includeDetails);
	        wbsOpts.setCopyWBSMilestones(includeDetails);
	        wbsOpts.setCopyWPsAndDocs(includeDetails);

	        actOpts.setCopyActivityCodes(includeDetails);
	        actOpts.setCopyExpenses(includeDetails);
	        actOpts.setCopyNotebook(includeDetails);
	        actOpts.setCopyRelationships(includeDetails);
	        actOpts.setCopyResourceAndRoleAssignments(includeDetails);
	        actOpts.setCopySteps(includeDetails);
	        actOpts.setCopyWPsAndDocs(includeDetails);
	        
	        System.out.println("inside copy EPSId = "+EPSId+projOpts.toString()+" "+actOpts.toString()+" "+actOpts.toString());
	        
	        ObjectId copyProjectObjectId = p3eProject.createCopy(CommonUtils.parseObjectId(EPSId), projOpts, wbsOpts, actOpts);
	        System.out.println("copyProjectObjectId =  "+copyProjectObjectId.toString());

	        Project copiedProject = null;

	        if (copyProjectObjectId != null) {
	        	
	            copiedProject = new Project(p3e);
	            copiedProject.setObjectId(copyProjectObjectId);
	            copiedProject.setName(p3eProject.getName());
	            
	        }

	        return copiedProject;
	    }
	   
	   
	   public static OBS getByName( Session p3e, String name,GlobalObjectManager gom ) throws Exception {
	        OBS returnValue = null;
	        BOIterator<OBS> boi = gom.loadOBS( new String[]{"ObjectId", "ParentObjectId", "Name"}, CommonUtils.parseSelection( "Name", name ), "Name" );
	        System.out.println("inside getbyname boi= "+boi.getAll().length);

	        if ( boi.hasNext() )
	            returnValue = boi.next();

	        return returnValue;
	    }
	   
	  
}
