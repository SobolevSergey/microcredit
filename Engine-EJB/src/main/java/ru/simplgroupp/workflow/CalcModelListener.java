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
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;

public class CalcModelListener implements JavaDelegate {

	private static final Logger logger = Logger.getLogger(CalcModelListener.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		ActionProcessorBeanLocal actProc = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		
		ActionContext actionContext = (ActionContext)  varsLocal.get(ProcessKeys.VAR_ACTION_CONTEXT);
		String modelKey = (String) varsLocal.get(ProcessKeys.VAR_MODEL_KEY);
		Integer modelId = Convertor.toInteger(varsLocal.get(ProcessKeys.VAR_MODEL_ID));
		Map<String, Object> mpVars = (Map<String, Object>) varsLocal.get(ProcessKeys.VAR_MODEL_PARAMS);
		if (mpVars == null) {
			mpVars = new HashMap<String, Object>();
		}
		
		DecisionState ds = (DecisionState) varsLocal.get(ProcessKeys.VAR_DECISION_STATE);
		String businessObjectClass = (String) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
		Object businessObjectId = varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID);
		
		try {
			Integer productId=null, wayId = null;
			if (CreditRequest.class.getName().equals(businessObjectClass)) {
				CreditRequest cRequest = actProc.getCreditRequest(Convertor.toInteger(businessObjectId), Utils.setOf());
				if (cRequest != null) {
					if (cRequest.getProduct()!=null){
					    productId=cRequest.getProduct().getId();
					}
					if (cRequest.getWay()!=null){
					    wayId=cRequest.getWay().getId();
					}					
				}
			}
			
			Map<String, Object> mapRes = actProc.calcModel(modelKey, ds, businessObjectClass, businessObjectId, actionContext, modelId,productId, wayId, mpVars);

			if (mapRes.size() > 0) {
				Map<String, Object> varData = (Map<String, Object> ) varsLocal.get(ProcessKeys.VAR_DATA);
				if (varData == null) {
					varData = new HashMap<>(mapRes.size());
				}
				varData.putAll(mapRes);
				execution.setVariableLocal(ProcessKeys.VAR_DATA, varData);
			}			
		} catch (Throwable e) {
			logger.severe("Произошла ошибка при расчете модели "+e.getMessage());
			ds.addError("1", e.getMessage());
			ds.finish(false, 1);
		}
		
		execution.setVariableLocal(ProcessKeys.VAR_MODEL_PARAMS, mpVars);
		execution.setVariableLocal(ProcessKeys.VAR_DECISION_STATE, ds);
		if (ds.isInProcess()) {
			execution.setVariableLocal(ProcessKeys.VAR_BUSINESS_ERRORS, null);
		} else if (ds.isCompletedOK()) {
			execution.setVariableLocal(ProcessKeys.VAR_BUSINESS_ERRORS, null);
		} else if (ds.isCompletedErrors()) {
			execution.setVariableLocal(ProcessKeys.VAR_BUSINESS_ERRORS, ds.getErrors());
		}
		
		Number bSave = (Number) mpVars.get(ProcessKeys.VAR_SAVE_VARIABLES);
		if (bSave == null) {
			bSave = 0;
		}
		actProc.setAIModelParamValues(modelId, actionContext, mpVars, (bSave.intValue() == 1));
	}

}
