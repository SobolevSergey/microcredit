package ru.simplgroupp.workflow;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import ru.simplgroupp.ejb.PluginConfig;

public class PluginInitListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5895538715659984250L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		PluginConfig plc = (PluginConfig) varsLocal.get(ProcessKeys.VAR_PLUGIN);		
		execution.setVariableLocal(ProcessKeys.VAR_PLUGIN_NAME, plc.getPluginName());
		if (! varsLocal.containsKey(ProcessKeys.VAR_LAST_ERROR)) {
			execution.setVariableLocal(ProcessKeys.VAR_LAST_ERROR, null);
		}
	}

}
