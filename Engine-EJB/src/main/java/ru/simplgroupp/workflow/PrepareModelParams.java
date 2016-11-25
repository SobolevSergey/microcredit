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

public class PrepareModelParams implements JavaDelegate {
	
	private static final Logger logger = Logger.getLogger(PrepareModelParams.class.getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		ActionProcessorBeanLocal actProc = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		ActionContext actionContext = (ActionContext)  varsLocal.get(ProcessKeys.VAR_ACTION_CONTEXT);
		Integer modelId = null;
		if (varsLocal.get(ProcessKeys.VAR_MODEL_ID) == null) {
			String modelKey = (String) varsLocal.get(ProcessKeys.VAR_MODEL_KEY);
			String businessObjectClass = (String) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
			Object businessObjectId = varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID);
			
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
			modelId = actProc.findActiveModelId(modelKey, productId, wayId);
			varsLocal.put((String) ProcessKeys.VAR_MODEL_ID, modelId);
			execution.setVariableLocal((String) ProcessKeys.VAR_MODEL_ID, modelId);
		} else {
			modelId = Convertor.toInteger(varsLocal.get(ProcessKeys.VAR_MODEL_ID));
		}
		actProc.checkDefaultModelParams(modelId, actionContext);
        
		Map<String, Object> mpVars = actProc.getAIModelParams(modelId, actionContext); 	
		Number bSave = (Number) mpVars.get(ProcessKeys.VAR_SAVE_VARIABLES);
		if (bSave == null) {
			bSave = 0;
		}
		mpVars.put(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS));
		mpVars.put(ProcessKeys.VAR_BUSINESS_OBJECT_ID, varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID));
		mpVars.put(ProcessKeys.VAR_SAVE_VARIABLES, bSave);
		execution.setVariableLocal(ProcessKeys.VAR_MODEL_PARAMS, mpVars);
		
		actProc.deleteResults((String) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS), (Integer) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID));
	}

}
