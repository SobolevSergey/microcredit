package ru.simplgroupp.workflow;

import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class DecActiveCalls implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		
		List<String> lst = (List<String>) varsLocal.get(ProcessKeys.VAR_ACTIVE_PLUGIN_CALLS);
		String pluginName = (String) varsLocal.get("plugin_Name");
		lst.remove(pluginName);
		execution.setVariableLocal(ProcessKeys.VAR_ACTIVE_PLUGIN_CALLS, lst);
	
	}

}
