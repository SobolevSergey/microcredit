package ru.simplgroupp.ejb;

import java.io.Serializable;

import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.interfaces.ActionProcessorBeanMini;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.workflow.ProcessKeysMini;

public class ActionContextProxy implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String customizationKey;
	public boolean persistent;
	
	public ActionContextProxy() {
		
	}
	
	public ActionContextProxy(String customKey, boolean bPersistent) {
		customizationKey = customKey;
		persistent = bPersistent;
	}	
	
	private Object readResolve() {
		ActionProcessorBeanMini actProc = (ActionProcessorBeanMini) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeysMini.VAR_ACTION_PROCESSOR);
//		ActionProcessorBeanMini actProc = (ActionProcessorBeanMini) EJBUtil.findEJB("java:module/ActionProcessorBean");
		return actProc.createActionContextObject(customizationKey, persistent);
	}	
	
}
