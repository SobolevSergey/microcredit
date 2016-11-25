package ru.simplgroupp.workflow;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;

public class InitDecisionListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8490162882409277452L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		ActionProcessorBeanLocal actProc = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		ActionContext actionContext = (ActionContext)  varsLocal.get(ProcessKeys.VAR_ACTION_CONTEXT);
		DecisionState ds = (DecisionState) varsLocal.get(ProcessKeys.VAR_DECISION_STATE);
		
		if (ds == null) {
			ds = new DecisionState();
			for (PluginConfig conf: actionContext.getPlugins().getPluginConfigs() ) {
				ds.setExternalSystem(ProcessKeys.PREFIX_PLUGIN + conf.getPluginName(), false, 0);
			}	
		} else {
			ds.reset();
		}
		execution.setVariableLocal(ProcessKeys.VAR_DECISION_STATE, ds);
	}


}
