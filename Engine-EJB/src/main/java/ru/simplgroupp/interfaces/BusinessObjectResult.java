package ru.simplgroupp.interfaces;

import java.io.Serializable;

import ru.simplgroupp.exception.ExceptionInfo;

public class BusinessObjectResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6655243913010542108L;
	private String businessClassName;
	private Object businessObjectId;
	private Boolean answered;
	private ExceptionInfo error;
	
	public BusinessObjectResult() {
		super();
	}

	public BusinessObjectResult(String clsName, Object aid, Boolean answ, ExceptionInfo aerror) {
		super();
		businessClassName = clsName;
		businessObjectId = aid;
		error = aerror;
		if (error == null) {
			answered = answ;
		} else {
			answered = null;
		}
	}

	public String getBusinessClassName() {
		return businessClassName;
	}

	public Object getBusinessObjectId() {
		return businessObjectId;
	}

	public Boolean getAnswered() {
		return answered;
	}

	public ExceptionInfo getError() {
		return error;
	}

}
