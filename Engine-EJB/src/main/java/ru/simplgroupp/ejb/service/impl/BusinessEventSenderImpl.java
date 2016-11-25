package ru.simplgroupp.ejb.service.impl;

import java.util.Map;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import ru.simplgroupp.ejb.BusinessEvent;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.interfaces.service.BusinessEventSender;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.transfer.annotation.ABusinessEventQualifier;

@Stateless
public class BusinessEventSenderImpl implements BusinessEventSender  {

    @Inject
    Event<BusinessEvent> fireBusinessEvent;
    
    @Asynchronous
    @Override
	public void fireEvent(BusinessEvent event) {
    	fireBusinessEvent
    		.select(new ABusinessEventQualifier(event.getBusinessObjectClass(), event.getEventCode()) )
    		.fire(event);
	} 
    
    @Asynchronous
    @Override    
    public <T extends Identifiable> void fireEvent(T businessObject, int eventCode, ExceptionInfo lastError, Map<String,Object> params) {
    	BusinessEvent event = new BusinessEvent(businessObject, eventCode, lastError);
    	if (params != null) {
    		event.getParams().putAll(params);
    	}
    	fireEvent(event);
    }  
    
    @Asynchronous
    @Override    
    public <T extends Identifiable> void fireEvent(T businessObject, int eventCode, Map<String,Object> params) {
    	BusinessEvent event = new BusinessEvent(businessObject, eventCode, null);
    	if (params != null) {
    		event.getParams().putAll(params);
    	}
    	fireEvent(event);
    }     
    
    @Asynchronous
    @Override    
    public <T extends Identifiable> void fireEvent(T businessObject, int eventCode, ExceptionInfo lastError) {
    	BusinessEvent event = new BusinessEvent(businessObject, eventCode, lastError);
    	fireEvent(event);
    } 
    
    @Asynchronous
    @Override    
    public <T extends Identifiable> void fireEvent(T businessObject, int eventCode) {
    	BusinessEvent event = new BusinessEvent(businessObject, eventCode, null);
    	fireEvent(event);
    }    
    
    @Asynchronous
    @Override    
    public void fireEvent(String businessObjectClass,  Object businessObjectId, int eventCode, ExceptionInfo lastError, Map<String,Object> params) {
    	BusinessEvent event = new BusinessEvent(businessObjectClass, businessObjectId, eventCode, lastError);
    	if (params != null) {
    		event.getParams().putAll(params);
    	}
    	fireEvent(event);
    }
    
    @Asynchronous
    @Override    
    public void fireEvent(String businessObjectClass,  Object businessObjectId, int eventCode, Map<String,Object> params) {
    	BusinessEvent event = new BusinessEvent(businessObjectClass, businessObjectId, eventCode, null);
    	if (params != null) {
    		event.getParams().putAll(params);
    	}
    	fireEvent(event);
    } 
    
    @Asynchronous
    @Override    
    public void fireEvent(String businessObjectClass,  Object businessObjectId, int eventCode) {
    	BusinessEvent event = new BusinessEvent(businessObjectClass, businessObjectId, eventCode, null);
    	fireEvent(event);
    }    
    
    @Asynchronous
    @Override     
    public void fireEvent(int eventCode, ExceptionInfo lastError) {
    	BusinessEvent event = new BusinessEvent("ru.simplgroupp.transfer.System", null, eventCode, lastError);
    	fireEvent(event);    	
    }
    
    @Asynchronous
    @Override     
    public void fireEvent(int eventCode) {
    	BusinessEvent event = new BusinessEvent("ru.simplgroupp.transfer.System", null, eventCode, null);
    	fireEvent(event);    	
    }    

	@Override
	@Asynchronous
	public void fireEvent(String businessObjectClass, Object businessObjectId, int eventCode, ExceptionInfo lastError) {
		fireEvent(businessObjectClass, businessObjectId, eventCode, lastError, null);
		
	}
}
