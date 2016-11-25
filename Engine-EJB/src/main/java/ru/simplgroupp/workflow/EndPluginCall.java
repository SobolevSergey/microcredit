package ru.simplgroupp.workflow;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.exception.ExceptionInfo;

public class EndPluginCall implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5585256953795090281L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Map<String,Object> varsLocal = execution.getVariables();
		String pluginName = (String) varsLocal.get("plugin_Name");
		if (StringUtils.isBlank(pluginName)) {
			return;
		}
		ExceptionInfo exp = (ExceptionInfo) varsLocal.get(ProcessKeys.VAR_LAST_ERROR);
		
		DecisionState ds = (DecisionState) varsLocal.get(ProcessKeys.VAR_DECISION_STATE);
		
		ds.getExternalSystems().get(ProcessKeys.PREFIX_PLUGIN + pluginName).setNeedCall(false);
		if (exp != null) {
			ds.addError(String.valueOf(exp.getCode()), exp.getMessage());
		}
		execution.setVariable(ProcessKeys.VAR_DECISION_STATE, ds);
	}

}
