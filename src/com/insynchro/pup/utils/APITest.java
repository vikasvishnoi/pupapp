package com.insynchro.pup.utils;


import com.primavera.bo.schema.client.Activity;
import com.primavera.common.value.Unit;
import com.primavera.common.value.UnitsPerTime;
import com.primavera.integration.client.EnterpriseLoadManager;
import com.primavera.integration.client.GlobalObjectManager;
import com.primavera.integration.client.RMIURL;
import com.primavera.integration.client.Session;
import com.primavera.integration.client.bo.BOIterator;
import com.primavera.integration.client.bo.object.Project;
import com.primavera.integration.client.bo.object.Resource;
import com.primavera.integration.client.bo.object.ResourceAssignment;
import com.primavera.integration.common.DatabaseInstance;

public class APITest {
	
	 public static void main( String[] args )
	    {
	        Session session = null;
	        try
	        {
	        	System.setProperty("primavera.bootstrap.home", "C:\\Oracle\\EPPM\\p6");
	        	System.out.println("RMIURL.LOCAL_SERVICE  = "+RMIURL.LOCAL_SERVICE );
	            DatabaseInstance[] dbInstances = Session.getDatabaseInstances(
	                RMIURL.getRmiUrl( RMIURL.LOCAL_SERVICE ) );

	            // Assume only one database instance for now, and hardcode the username and
	            // password for this sample code
	            session = Session.login( RMIURL.getRmiUrl( RMIURL.LOCAL_SERVICE ),
	                dbInstances[0].getDatabaseId(), "admin", "admin" );

	            EnterpriseLoadManager elm = session.getEnterpriseLoadManager();
	            //List of all projects 
	           // BOIterator<Project> boi = elm.loadProjects( new String[]{ "Name" }, null, "Name asc" );
	          //  BOIterator<Project> boi = elm.loadProjects( new String[]{ "Name" }, null, "Name asc" );
	            BOIterator<Project> boi= elm.loadProjects(new String[]{ "Name" }, CommonUtils.parseSelection("Id", "NEWPROJ"), "Id");
	            Project proj =null;
	            
	            
	            while ( boi.hasNext() )
	            {
	                proj = boi.next();
	                System.out.println("Project Name : "+ proj.getName() );
	            } 
	            
	            
	            //************************************************Loading Activities
	            Activity activity=null;
	           // 
	            //BOIterator<com.primavera.integration.client.bo.object.Activity> boIterator =elm.loadActivities(new String[]{ "Name" }, CommonUtils.parseSelection("ProjectId", "NEWPROJ"), "ProjectId");
	            BOIterator<com.primavera.integration.client.bo.object.Activity> boIterator =elm.loadActivities(new String[]{ "Name","Id" }, CommonUtils.parseSelection("ProjectId", "NEWPROJ"), "ProjectId");
	            System.out.println("boIterator "+boIterator);
	            String actObjId=null;
	            while ( boIterator.hasNext() )
	            {
	            	com.primavera.integration.client.bo.object.Activity act = boIterator.next();
	                // Add code here to process each Activity...
	            	System.out.println("Activity : "+act.getName());
	            	System.out.println("Activity object id ***** : "+act.getObjectId().toString());
	            	if(act.getId().equalsIgnoreCase("A1010")) {
	            		actObjId=act.getObjectId().toString();
	            		System.out.println("Match--------");
	            	}
	            }
	            
	            
	            //**************************************Loading Resources assignments 
	            
	            
	            GlobalObjectManager gom = session.getGlobalObjectManager();

	            // Get order by clause for resources
	            StringBuilder sbOrderResourcesBy = new StringBuilder();
	           

	            boolean bLightRow = true;
	            int iNumResources = 0;
	            BOIterator<Resource> boIterator2 = gom.loadResources(new String[] {"Id"}, null, sbOrderResourcesBy.toString());

	            // Get order by clause for resource assignments
	            StringBuilder sbOrderAssignmentsBy = new StringBuilder();
	           // sbOrderAssignmentsBy.append(demoInfo.sOrderAssignmentsBy);
	           // sbOrderAssignmentsBy.append((demoInfo.bAscending) ? " asc" : " desc");

	            // Load the first X number of resources, where X is specified by
	            // the user
	            while (boIterator2.hasNext())
	            {
	                Resource resource = boIterator2.next();
	                String sResourceId = resource.getId();
	                System.out.println("Loading assignments for resource " + resource.getId());

	                BOIterator<ResourceAssignment> boiAsgn = resource.loadResourceAssignments(new String[]
	                    {
	                        "ObjectId", "ProjectId", "RoleId", "ActivityName", "CostAccountName"
	                    }, "upper(ProjectId)='NEWPROJ' and upper(ActivityId)='A100'", sbOrderAssignmentsBy.toString());

	                while (boiAsgn.hasNext())
	                {
	                    ResourceAssignment asgn = boiAsgn.next();
	                    System.out.println("sResourceId = "+sResourceId);
	                    System.out.println("(asgn.getProjectId() = "+asgn.getProjectId());
	                    System.out.println("asgn.getRoleId() = "+asgn.getRoleId());
	                }
	          
	          
	        }
	            
	            
	            //*******************************LOading ResourcesAssignments using elm 
	            System.out.println("*******************************LOading ResourcesAssignments using elm");
	            String strProjObjId=proj.getObjectId().toString();
	            System.out.println("strProjObjId   = "+strProjObjId);
	            System.out.println("actObjId____________________   = "+actObjId);
	            BOIterator<ResourceAssignment> boiAsgn =elm.loadResourceAssignments(new String[]{"ObjectId","ActivityObjectId",
	            		"ActivityId","ResourceObjectId","ResourceId","PlannedCost","PlannedUnits","ProjectObjectId"},"ProjectObjectId='"+strProjObjId+"' and ActivityObjectId='"+actObjId+"'", null); 
	            
	            //BOIterator<ResourceAssignment> boiP6RsrcAss = this.elm.loadResourceAssignments(new String[]{"ObjectId","ActivityObjectId",
	           // "ActivityId","ResourceObjectId","ResourceId","PlannedCost","PlannedUnits"},"ProjectObjectId='"strProjObjId"'", null); 
	            
	            
	           // BOIterator<ResourceAssignment> boiAsgn = elm.loadResourceAssignments(fields, sWhereClause, sOrderBy)
	                while (boiAsgn.hasNext())
	                {
	                    ResourceAssignment asgn = boiAsgn.next();
	                    System.out.println("sResourceId = "+asgn.getResourceId());
	                    double u=34.34;
	                    Unit actUnit=new Unit(u);
	                    		
	                    asgn.setActualUnits(actUnit);
	                    Unit remUnit=new Unit(13.78);
	                    asgn.setRemainingUnits(remUnit);
	                    UnitsPerTime unitsPerTime=new UnitsPerTime(67.01);
	                    asgn.setRemainingUnitsPerTime(unitsPerTime);
	                    asgn.update();
	                }
	                
	                
	        }
	        catch ( Exception e )
	        {
	            // Best practices would involve catching specific exceptions.  To keep this
	            // sample code short, we catch Exception
	            e.printStackTrace();
	        }
	        finally
	        {
	            if ( session != null )
	                session.logout();
	        }
	    }

}
