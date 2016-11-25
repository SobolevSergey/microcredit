package ru.simplgroupp.interfaces.service;

import java.util.Map;

import javax.ejb.Local;

import ru.simplgroupp.ejb.BusinessEvent;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.toolkit.common.Identifiable;

@Local
public interface BusinessEventSender {

	public void fireEvent(BusinessEvent event);

	void fireEvent(String businessObjectClass, Object businessObjectId,
			int eventCode, ExceptionInfo lastError);

	void fireEvent(int eventCode, ExceptionInfo lastError);

	void fireEvent(String businessObjectClass, Object businessObjectId,
			int eventCode, ExceptionInfo lastError, Map<String, Object> params);

	void fireEvent(int eventCode);

	void fireEvent(String businessObjectClass, Object businessObjectId,
			int eventCode, Map<String, Object> params);

	void fireEvent(String businessObjectClass, Object businessObjectId,
			int eventCode);

	public <T extends Identifiable> void fireEvent(T businessObject, int eventCode, ExceptionInfo lastError, Map<String,Object> params);

	public <T extends Identifiable> void fireEvent(T businessObject, int eventCode, ExceptionInfo lastError);
	
	public <T extends Identifiable> void fireEvent(T businessObject, int eventCode);

	public <T extends Identifiable> void fireEvent(T businessObject, int eventCode, Map<String,Object> params);
}
