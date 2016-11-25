package ru.simplgroupp.workflow;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

/**
 * Сохраняет переменные из decisionState в таблицу misc
 * @author irina
 * @deprecated
 */
public class SaveStateVarsListener extends AbstractExecutionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2716691922205595086L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		checkActionProcessor();
		
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		
		DecisionState ds = (DecisionState) varsLocal.get(ProcessKeys.VAR_DECISION_STATE);
		Number businessObjectId = (Number) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID);
		
		actionProcessor.saveCRMiscVariables(businessObjectId.intValue(), ds.getVars());
	}
}
