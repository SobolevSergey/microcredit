package ru.simplgroupp.workflow;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;

public class SaveModelParams implements JavaDelegate {
	
	private static final Logger logger = Logger.getLogger(SaveModelParams.class.getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		ActionProcessorBeanLocal actProc = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		ActionContext actionContext = (ActionContext)  varsLocal.get(ProcessKeys.VAR_ACTION_CONTEXT);
		Integer modelId = Convertor.toInteger(varsLocal.get(ProcessKeys.VAR_MODEL_ID));
        
		Map<String, Object> mpVars = (Map<String, Object>) execution.getVariableLocal(ProcessKeys.VAR_MODEL_PARAMS);
		Number bSave = (Number) mpVars.get(ProcessKeys.VAR_SAVE_VARIABLES);
		if (bSave == null) {
			bSave = 0;
		}
		
		// сохраняем переменные
		mpVars.put(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS));
		mpVars.put(ProcessKeys.VAR_BUSINESS_OBJECT_ID, varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID));
		mpVars.put(ProcessKeys.VAR_SAVE_VARIABLES, bSave);
		actProc.setAIModelParamValues(modelId, actionContext, mpVars, (bSave.intValue() == 1));
	}

}
