package com.insynchro.pup.domain;

import java.io.Serializable;
import java.util.List;

public class ImportResultBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String erroMsg;
	private List<XlsDataBean> xlsDataBeans;
	
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErroMsg() {
		return erroMsg;
	}
	public void setErroMsg(String erroMsg) {
		this.erroMsg = erroMsg;
	}
	public List<XlsDataBean> getXlsDataBeans() {
		return xlsDataBeans;
	}
	public void setXlsDataBeans(List<XlsDataBean> xlsDataBeans) {
		this.xlsDataBeans = xlsDataBeans;
	}
	
	

}
