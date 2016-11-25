package ru.simplgroupp.workflow;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;

public class PreparePluginVars implements JavaDelegate {
	
	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
	}		

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		
		List<String> lst = (List<String>) varsLocal.get(ProcessKeys.VAR_ACTIVE_PLUGIN_CALLS);
		
		String pluginName = lst.get(0);
		varsLocal.put("plugin_Name", pluginName);
		ActionContext actionContext = (ActionContext)  varsLocal.get(ProcessKeys.VAR_ACTION_CONTEXT);
		if (actionContext == null) {
			actionContext = (ActionContext)  execution.getVariable(ProcessKeys.VAR_ACTION_CONTEXT);
		}
		
		checkActionProcessor();
		
		PluginConfig plc = actionContext.getPlugins().getPluginConfig(pluginName); 
		varsLocal.put(ProcessKeys.VAR_PLUGIN, plc);
//		String process_Name = actionProcessor.getPlugins().getPluginConfig(pluginName).getProcessName();
		execution.setVariablesLocal(varsLocal);	
	}

}
