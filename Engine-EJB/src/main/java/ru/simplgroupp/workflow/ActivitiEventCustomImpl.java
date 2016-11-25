package ru.simplgroupp.workflow;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;

public class ActivitiEventCustomImpl extends ActivitiEventImpl implements ActivitiEvent {
	
	private String customEventName;
	private Map<String, Object> params = new HashMap<String,Object>(0);

	public ActivitiEventCustomImpl(String custEventName, Map<String, Object> parms) {
		super(ActivitiEventType.CUSTOM);
		customEventName = custEventName;
		params.putAll( parms );
	}

	public String getCustomEventName() {
		return customEventName;
	}
	
	public Object getParameter(String name) {
		return params.get(name);
	}

}
