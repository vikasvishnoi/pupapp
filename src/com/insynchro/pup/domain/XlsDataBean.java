package com.insynchro.pup.domain;

import java.io.Serializable;
import java.util.Date;

public class XlsDataBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectId;
	private String activityId;
	private String resourceId;
	private double actualUnits;
	private double remainingUnits;
	private double remainingUnitsPerTime;
	private String remarks;
	private Date actualStart;
	private Date actualFinish;
	private boolean flag;
	
	
	
	
	public Date getActualStart() {
		return actualStart;
	}
	public void setActualStart(Date actualStart) {
		this.actualStart = actualStart;
	}
	public Date getActualFinish() {
		return actualFinish;
	}
	public void setActualFinish(Date actualFinish) {
		this.actualFinish = actualFinish;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public double getActualUnits() {
		return actualUnits;
	}
	public void setActualUnits(double actualUnits) {
		this.actualUnits = actualUnits;
	}
	public double getRemainingUnits() {
		return remainingUnits;
	}
	public void setRemainingUnits(double remainingUnits) {
		this.remainingUnits = remainingUnits;
	}
	public double getRemainingUnitsPerTime() {
		return remainingUnitsPerTime;
	}
	public void setRemainingUnitsPerTime(double remainingUnitsPerTime) {
		this.remainingUnitsPerTime = remainingUnitsPerTime;
	}
	
	

}
