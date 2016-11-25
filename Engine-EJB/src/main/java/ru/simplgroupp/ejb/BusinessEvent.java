package ru.simplgroupp.ejb;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.toolkit.common.Identifiable;

public class BusinessEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8649069069816668059L;
	private String businessObjectClass;
	private Object businessObjectId;
	private Object businessObject;
	private int eventCode;
	private ExceptionInfo lastError;
	private Date eventTime;
	private Map<String, Object> params = new HashMap<String,Object>(0);
	
	public <T extends Identifiable> BusinessEvent(T businessObject, int eventCode, ExceptionInfo lastError ) {
		this.businessObjectClass = businessObject.getClass().getName();
		this.businessObjectId = businessObject.getId();
		this.businessObject = businessObject;
		this.eventCode = eventCode;
		this.lastError = lastError;
		eventTime = new Date();		
	}
	
	public BusinessEvent(String businessObjectClass,  Object businessObjectId, int eventCode, ExceptionInfo lastError ) {
		this.businessObjectClass = businessObjectClass;
		this.businessObjectId = businessObjectId;
		this.businessObject = null;
		this.eventCode = eventCode;
		this.lastError = lastError;
		eventTime = new Date();
	}

	public String getBusinessObjectClass() {
		return businessObjectClass;
	}

	public Object getBusinessObjectId() {
		return businessObjectId;
	}

	public ExceptionInfo getLastError() {
		return lastError;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public int getEventCode() {
		return eventCode;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public Object getBusinessObject() {
		return businessObject;
	}

}
