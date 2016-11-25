package ru.simplgroupp.workflow;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.Expression;
import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.ejb.BusinessEvent;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.transfer.EventCode;

public class BusinessEventTask implements JavaDelegate {
	
	private static final Logger logger = Logger.getLogger(BusinessEventTask.class.getName());	
	
	Expression businessObjectClass;
	Expression businessObjectId;
	Expression eventCode;
	Expression lastError;
	Expression eventType;
	
	@EJB
	protected ActionProcessorBeanLocal actionProcessor;
	
	protected void checkActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
		}
	}	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		if (businessObjectClass == null ||businessObjectId == null || eventCode == null ) {
			logger.log(Level.SEVERE, "Необходимо задать businessObjectClass, businessObjectId, eventCode");
			return;
		}
		checkActionProcessor();
		
		String sBusinessObjectClass = (String) businessObjectClass.getValue(execution);
		Object oBusinessObjectId = businessObjectId.getValue(execution);
		Number sEventCode = null;
		String sEventType = null;
		if (eventType != null) {
			sEventType = (String) eventType.getValue(execution);	
		}		
		if ( StringUtils.isNotBlank(sEventType) ) {
			if (sEventType.contains("success")) {
				sEventCode = EventCode.PAYMENT_COMPLETED_SUCCESS;
			} else {
				sEventCode = EventCode.PAYMENT_COMPLETED_FAILURE;
			}
		} else {
			sEventCode = (Number) eventCode.getValue(execution);
			if (sEventCode == null) {
				logger.log(Level.SEVERE, "Необходимо задать eventCode");
				return;			
			}
			
		}
		ExceptionInfo eLastError = null;
		if (lastError != null) {
			eLastError = (ExceptionInfo) lastError.getValue(execution);
		}
		
		BusinessEvent bevt = new BusinessEvent(sBusinessObjectClass, oBusinessObjectId, sEventCode.intValue(), eLastError);
		actionProcessor.fireBusinessEvent(bevt);
		
	}

}
