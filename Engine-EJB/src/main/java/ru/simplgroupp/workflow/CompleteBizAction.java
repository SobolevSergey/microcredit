package ru.simplgroupp.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.util.EventKeys;

public class CompleteBizAction implements JavaDelegate {
	
	@EJB
	protected ServiceBeanLocal servBean;
	
	protected void checkActionProcessor() {
		if (servBean == null) {
			servBean = (ServiceBeanLocal) Context.getProcessEngineConfiguration().getBeans().get("commonService");
		}		
	}	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Map<String, Object> varsLocal = execution.getVariables();		
		checkActionProcessor();		
		
		HashMap<String, Object> mp = new HashMap<String, Object>(2);
		mp.put(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS));
		mp.put(ProcessKeys.VAR_BUSINESS_OBJECT_ID, varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID));
		servBean.firedEvent(EventKeys.EVENT_BIZACT_COMPLETED, mp);		
	}

}
