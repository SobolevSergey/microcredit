package ru.simplgroupp.workflow;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;

public class PreparePluginCalls implements JavaDelegate {
	
	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
	}		

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		checkActionProcessor();
		
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		
		ActionContext actionContext = (ActionContext)  varsLocal.get(ProcessKeys.VAR_ACTION_CONTEXT);
		DecisionState ds = (DecisionState) varsLocal.get(ProcessKeys.VAR_DECISION_STATE);
		String modelKey = (String) varsLocal.get(ProcessKeys.VAR_MODEL_KEY);
		
		List<String> lst = actionProcessor.getActivePluginCalls(ds, modelKey, actionContext);
		execution.setVariableLocal(ProcessKeys.VAR_ACTIVE_PLUGIN_CALLS, lst);
	}

}
