package ru.simplgroupp.interfaces;

import java.util.Map;


public interface ApplicationEventListener {
	
	public void firedEvent(String eventName, Map<String, Object> params, RuntimeServices runtimeServices);
}
