package ru.simplgroupp.ejb;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.exception.WorkflowRuntimeException;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal.ExecutionMode;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal.SyncMode;
import ru.simplgroupp.rules.CreditRequestContext;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.Prolong;
import ru.simplgroupp.transfer.Refinance;
import ru.simplgroupp.transfer.Users;
import ru.simplgroupp.util.AppUtil;
import ru.simplgroupp.util.EventKeys;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.WorkflowUtil;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;
import ru.simplgroupp.workflow.WorkflowObjectState;
import ru.simplgroupp.workflow.WorkflowObjectStateDef;

import javax.ejb.*;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
@Local(WorkflowBeanLocal.class)
//@AccessTimeout(15000)
public class WorkflowBean implements WorkflowBeanLocal {

	@Inject Logger logger;

	@EJB
	private ServiceBeanLocal servBean;

	@EJB
	private WorkflowEngineBeanLocal wfEngine;
	
	@EJB
	RulesBeanLocal rulesBean;
	
	@EJB 
	ActionProcessorBeanLocal actProc;
	
	@EJB
	BizActionBeanLocal bizBean;
	
	@EJB
	BizActionDAO bizDAO;
	
	@EJB
	BizActionProcessorBeanLocal bizProc;
	
	@EJB
	PaymentDAO payDAO;
	
	@EJB
	ProductBeanLocal productBean;

	@Override
	public List<WorkflowObjectStateDef> getCreditProcessStatesForRequest() {		
		
		return wfEngine.getCreditProcessStatesForRequest();
	}
	
	@Override
	public ProcessInstance startOrFindProcPayment(int paymentId, int accountTypeCode, int creditId ) throws WorkflowException {
		ActionContext ctx = actProc.createActionContext(null, true);
		String businessKey = BusinessObjectRef.toString(Payment.class.getName(), new Integer(paymentId)); 
		ProcessInstance procInst = wfEngine.findProcessByBusinessKey(ProcessKeys.DEF_PAYMENT, businessKey);
		if (procInst == null) {
			procInst = wfEngine.startProcess(ProcessKeys.DEF_PAYMENT, businessKey , 
				Utils.<String, Object>mapOf(
						ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, Payment.class.getName(), 
						ProcessKeys.VAR_BUSINESS_OBJECT_ID, paymentId, 
						"accountType", accountTypeCode,
						"creditId", new Integer(creditId),
						ProcessKeys.VAR_ACTION_CONTEXT, ctx
				));
		}
			return procInst;		
	}
	
	@Override
	public ProcessInstance startOrFindProcCR(int creditRequestId, Integer clientUserId) throws WorkflowRuntimeException, WorkflowException {
		ActionContext ctx = actProc.createActionContext(null, true);
		ProcessInstance procInst = wfEngine.startOrFindProcess(ProcessKeys.DEF_CREDIT_REQUEST, ProcessKeys.VAR_CREDIT_REQUEST_ID, new Integer(creditRequestId), CreditRequest.class.getName(), 
			Utils.<String, Object>mapOf(ProcessKeys.VAR_CLIENT_USER_ID, clientUserId, ProcessKeys.VAR_ACTION_CONTEXT, ctx, "startPoint", null));
		return procInst;
	}
	
	@Override
	public ProcessInstance startOrFindProcCROffline(int creditRequestId, Integer clientUserId) throws WorkflowRuntimeException, WorkflowException {
		ActionContext ctx = actProc.createActionContext(null, true);
		ProcessInstance procInst = wfEngine.startOrFindProcess(ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE, ProcessKeys.VAR_CREDIT_REQUEST_ID, new Integer(creditRequestId), CreditRequest.class.getName(), 
			Utils.<String, Object>mapOf(ProcessKeys.VAR_CLIENT_USER_ID, clientUserId, ProcessKeys.VAR_ACTION_CONTEXT, ctx, "startPoint", null));
		return procInst;
	}	
	
	@Override
	public ProcessInstance startOrFindProcCR(int creditRequestId, Integer clientUserId, String startPoint) throws WorkflowRuntimeException, WorkflowException {
		ActionContext ctx = actProc.createActionContext(null, true);
		ProcessInstance procInst = wfEngine.startOrFindProcess(ProcessKeys.DEF_CREDIT_REQUEST, ProcessKeys.VAR_CREDIT_REQUEST_ID, new Integer(creditRequestId), CreditRequest.class.getName(), 
			Utils.<String, Object>mapOf(ProcessKeys.VAR_CLIENT_USER_ID, clientUserId, ProcessKeys.VAR_ACTION_CONTEXT, ctx, "startPoint", startPoint));
		return procInst;
	}
	

	@Override
	public List<String> getTopLevelActivityIds(int creditRequestId) {
		return wfEngine.getTopLevelActivityIds(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId));
	}

	@Override
	public void removeProcCR(int creditRequestId)
			throws WorkflowRuntimeException, WorkflowException {
		wfEngine.removeProcessByBusinessKey(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId));
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void signalProcCR(int creditRequestId) throws WorkflowRuntimeException, WorkflowException {
		wfEngine.signalProcessByBusinessKey(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId));
	}	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void signalProcCR(int creditRequestId, String signalRef, Map<String, Object> params) throws WorkflowRuntimeException, WorkflowException {
		wfEngine.signalProcessByBusinessKey(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId), signalRef, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void messageProcCR(int creditRequestId, String signalRef, Map<String, Object> params) throws WorkflowRuntimeException, WorkflowException {
		wfEngine.messageProcessByBusinessKey(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId), signalRef, params);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void goProcCR(int creditRequestId, String signalRef, Map<String, Object> params) throws WorkflowRuntimeException, WorkflowException {
		SignalRef ref = SignalRef.valueOf(signalRef);
		if (ref.isSignal()) {
			wfEngine.signalProcessByBusinessKey(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId), ref.toString(), params);	
		} else if (ref.isMessage()) {
			wfEngine.messageProcessByBusinessKey(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId), ref.toString(), params);	
		}
		
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void goProcCROffline(int creditRequestId, String signalRef, Map<String, Object> params) throws WorkflowRuntimeException, WorkflowException {
		SignalRef ref = SignalRef.valueOf(signalRef);
		if (ref.isSignal()) {
			wfEngine.signalProcessByBusinessKey(ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId), ref.toString(), params);	
		} else if (ref.isMessage()) {
			wfEngine.messageProcessByBusinessKey(ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId), ref.toString(), params);	
		}
		
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void goProcAll(String businessObjectClass, Object businessObjectId, String signalRef, Map<String, Object> params, boolean bTop) throws WorkflowRuntimeException, WorkflowException {
		SignalRef ref = SignalRef.valueOf(signalRef);
		if (ref.isSignal()) {
			wfEngine.signalProcessByBusinessKey(null, businessObjectClass + ":" + businessObjectId.toString(), ref.toString(), params);	
		} else if (ref.isMessage()) {
			wfEngine.messageProcessByBusinessKey(null, businessObjectClass + ":" + businessObjectId.toString(), ref.toString(), params);	
		}
		
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void goProcClientReject(int creditRequestId, Integer clientUserId) throws WorkflowRuntimeException, WorkflowException {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("clientUserId", clientUserId);
		
		WorkflowObjectActionDef actDef = new WorkflowObjectActionDef(false);
		actDef.setBusinessObjectClass(CreditRequest.class.getName());
		actDef.setSignalRef(ProcessKeys.MSG_CLIENT_REJECT);
		goProc(CreditRequest.class.getName(), new Integer(creditRequestId), actDef, SignalRef.toString("*", null, "msgNone"), params);
	}
	
	@Override
	public boolean isProcessRunning(String processDefKey, String businessObjectClass, Object businessObjectId ) {
		String businessKey = businessObjectClass + ":" + ((businessObjectId == null)?"":businessObjectId.toString());
		ProcessInstance pinst = wfEngine.findProcessByBusinessKey(processDefKey, businessKey);
		return (pinst != null);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void goProc(String businessObjectClass, Object businessObjectId, WorkflowObjectActionDef actionDef, String signalRef, Map<String, Object> params) throws WorkflowRuntimeException, WorkflowException {
		SignalRef ref = null;
		if (! StringUtils.isBlank(signalRef)) {
			ref = SignalRef.valueOf(signalRef);
		}
		String businessKey = null;
		
		if (actionDef.isForProcess()) {
			businessKey = businessObjectClass + ":" + ((businessObjectId == null)?"":businessObjectId.toString());
			if (ref.isSignal()) {
				wfEngine.signalProcessByBusinessKey(null, businessKey, ref.toString(), params);	
			} else if (ref.isMessage()) {
				wfEngine.messageProcessByBusinessKey(null, businessKey, ref.toString(), params);	
			}
			return;
		} else {
			businessKey = actionDef.getBusinessObjectClass() + ":" + ((businessObjectId == null)?"":businessObjectId.toString());			
		}
		
		int nProc = 0;
		if (ref == null) {
			ProcessInstance pinst = wfEngine.findProcess(actionDef.getProcessDefinitionKey(), params);
			if (pinst != null) {
				nProc = 1;
			}
		} else if (ref.isSignal()) {
			nProc = wfEngine.signalProcessByBusinessKey(actionDef.getProcessDefinitionKey(), businessKey, ref.toString(), params);	
		} else if (ref.isMessage()) {
			nProc = wfEngine.messageProcessByBusinessKey(actionDef.getProcessDefinitionKey(), businessKey, ref.toString(), params);	
		}
		if (nProc > 0) {
			return;			
		}
		
		// если такого процесса ещё нет, запускаем его
		ActionContext ctx = actProc.createActionContext(null, true);
		Map<String,Object> mpParams = new HashMap<String,Object>(5); 		
		if (params != null) {
			mpParams.putAll(params);
		}
		mpParams.put(ProcessKeys.VAR_ACTION_CONTEXT, ctx);
		mpParams.put(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, actionDef.getBusinessObjectClass());
		mpParams.put(ProcessKeys.VAR_BUSINESS_OBJECT_ID, businessObjectId);
		mpParams.put(ProcessKeys.VAR_DATA, params);
		ProcessInstance procInst = wfEngine.startProcess(actionDef.getProcessDefinitionKey(), businessKey, mpParams);
	}	
	
	@Override
	public List<WorkflowObjectStateDef> getProcCRWfActions(int creditRequestId, Set<String> variableNames, final boolean bIncludeWithActions) {
		return wfEngine.getProcessWfActions(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId), variableNames, bIncludeWithActions);
	}
	
	@Override
	public List<WorkflowObjectStateDef> getProcCROfflineWfActions(int creditRequestId, Set<String> variableNames, final boolean bIncludeWithActions) {
		return wfEngine.getProcessWfActions(ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId), variableNames, bIncludeWithActions);
	}
	
	@Override
	public List<WorkflowObjectState> getProcCRWfActions1(int creditRequestId, Set<String> variableNames, final boolean bIncludeWithActions) {
		return wfEngine.getProcessWfActions1(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId), variableNames, bIncludeWithActions);
	}	
	
	@Override
	public List<WorkflowObjectState> getProcWfActions(String businessObjectClass, Object businessObjectId, Set<String> variableNames, final boolean bIncludeWithActions) {
		String businessKey = businessObjectClass + ":" + ((businessObjectId == null)?"":businessObjectId.toString());
		return wfEngine.getProcessWfActions1(null, businessKey, variableNames, bIncludeWithActions);
	}	

	
	@Override
	public void optionsChanged(Integer productId,String bpName) {
		
		Map<String, Object> opts =productBean.getProductConfigForBP(productId, bpName);
		wfEngine.updateProcessVariables(bpName, ProcessKeys.VAR_OPTIONS, opts);
		servBean.firedEvent(EventKeys.EVENT_BPROC_SETTINGS_CHANGED, Utils.<String, Object>mapOf("businessProcessDefName", bpName));
	}
	
	@Override
	public ProcessInstance startPluginProcess(PluginConfig plc, String businessObjectClass, Object businessObjectId, Map<String, Object> params, ActionContext context) throws WorkflowException {
//		ActionContextProxy proxy = new ActionContextProxy(context.getCustomizationKey(), context.isPersistent());
		return wfEngine.startProcess(ProcessKeys.DEF_SUB_STANDART, businessObjectClass + ":" + ((businessObjectId == null)?"":businessObjectId.toString()), Utils.<String, Object>mapOf(
				ProcessKeys.VAR_PLUGIN, plc, 
				ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, businessObjectClass, 
				ProcessKeys.VAR_BUSINESS_OBJECT_ID, businessObjectId, 
				ProcessKeys.VAR_LAST_ERROR, null,
				ProcessKeys.VAR_ACTION_CONTEXT, context,
				ProcessKeys.VAR_DATA, params));
	}
	
	@Override
	public ProcessInstance findPluginProcess(String pluginName, String businessObjectClass, Object businessObjectId) {
		return wfEngine.findProcess(ProcessKeys.DEF_SUB_STANDART, Utils.<String, Object>mapOf(ProcessKeys.VAR_PLUGIN_NAME, pluginName, ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, businessObjectClass, ProcessKeys.VAR_BUSINESS_OBJECT_ID, businessObjectId));
	}
	
	@Override
	public ProcessInstance findProcess(String processDefKey, String businessObjectClass, Object businessObjectId) {
		return wfEngine.findProcess(processDefKey, Utils.<String, Object>mapOf( ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, businessObjectClass, ProcessKeys.VAR_BUSINESS_OBJECT_ID, businessObjectId));
	}	
	
	@Override
	public Map<String, Object> getProcessVariables(String processDefKey, String businessObjectClass, Object businessObjectId) {
		ProcessInstance pinst = wfEngine.findProcess(processDefKey, Utils.<String, Object>mapOf( ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, businessObjectClass, ProcessKeys.VAR_BUSINESS_OBJECT_ID, businessObjectId));
		if (pinst == null) {
			return null;
		}
		return wfEngine.getProcessVariables(pinst.getId());
	}
	
	@Override
	public Map<String, Object> findVariableInParents(String exId, final String varName) {
		final Object[] res = new Object[1];  
		try {
			wfEngine.iterateToParents(exId, new ExecutionTest() {

				@Override
				public boolean test(Execution execution, ProcessInstance process) 
						throws Throwable {
					Map<String, Object> vars = wfEngine.getExecutionVariables(execution.getId());
					if (vars.containsKey(varName)) {
						res[0] = vars;
						return true;
					} else {
						return false;
					}
				}}, false);
		} catch (Throwable e) {
			logger.log(Level.SEVERE,"Не удалось найти процесс с переменной", e);
		}
		return (Map<String, Object>) res[0];
	}
	
	@Override
	public void endPacketProcess(String procInstanceId, String pluginName) {
		ProcessInstance pinst = wfEngine.getProcessInstance(procInstanceId);
		String procDefKey = WorkflowUtil.extractProcessDefKey(pinst.getProcessDefinitionId());
		wfEngine.messageProcessByInstanceId(procInstanceId, (new SignalRef(procDefKey, pluginName, ProcessKeys.MSG_SUB_PACKET_END)).toString(), null);
	}
	
	@Override
	public ProcessInstance startOrFindPacketProcess(PluginConfig plc, ActionContext actionContext) throws WorkflowException {
		String procDefKey = plc.getProcessPacketName();
		if (ProcessKeys.PREFIX_DEF_SUB_PACKET.equals(procDefKey)) {
			if (plc.getExecutionMode().equals(ExecutionMode.AUTOMATIC)) {
				procDefKey = procDefKey + "A";
			} else if (plc.getExecutionMode().equals(ExecutionMode.MANUAL)) {
				procDefKey = procDefKey + "M";
			} else {
				return null;
			}
			if (plc.getSyncMode().equals(SyncMode.ASYNC)) {
				procDefKey = procDefKey + "A";
			} else if (plc.getSyncMode().equals(SyncMode.SYNC)) {
				procDefKey = procDefKey + "S";
			} else {
				return null;
			}
		}
		
		ProcessInstance pinst = wfEngine.findProcess(procDefKey, Utils.<String, Object>mapOf(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, plc.getPlugin().getBusinessObjectClass()));
		if (pinst == null) {
			pinst = wfEngine.startProcess(procDefKey, plc.getPlugin().getBusinessObjectClass(), Utils.<String, Object>mapOf(
					ProcessKeys.VAR_PLUGIN, plc, 
					ProcessKeys.VAR_ACTION_CONTEXT, actionContext,
					ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, plc.getPlugin().getBusinessObjectClass(), 
					ProcessKeys.VAR_LAST_ERROR, null
			));
		}
		return pinst;
	}
	
	/**
	 * Передаёт результаты пакетной обработки в основной процесс subStandart
	 * @param lstRes
	 */
	@Override
	public void handlePacketResults(List<BusinessObjectResult> lstRes, String pluginName, boolean bSync) {
		for (BusinessObjectResult bres: lstRes) {
			ProcessInstance pinst = findPluginProcess(pluginName, bres.getBusinessClassName(), bres.getBusinessObjectId());
			if (pinst == null) {
				continue;
			}
			
			if (bres.getError() != null) {
				if (bres.getError().getResultType().equals(ResultType.FATAL)) {
					wfEngine.messageProcessByInstanceId(pinst.getId(), SignalRef.toString(ProcessKeys.DEF_SUB_STANDART, pluginName, ProcessKeys.MSG_PACKET_FATAL_ERROR), Utils.<String, Object>mapOf(ProcessKeys.VAR_LAST_ERROR, bres.getError()));
				}
				// TODO
			} else {
				if (bSync) {
					wfEngine.messageProcessByInstanceId(pinst.getId(), SignalRef.toString(ProcessKeys.DEF_SUB_STANDART, pluginName, ProcessKeys.MSG_PACKET_SENDED), null);
				} else {
					if (bres.getAnswered()) {
						wfEngine.messageProcessByInstanceId(pinst.getId(), SignalRef.toString(ProcessKeys.DEF_SUB_STANDART, pluginName, ProcessKeys.MSG_ASYNC_PACKET_RECEIVED), null);	
					} else {
						// TODO
					}					
				}
			}
		}
	}
	
	/**
	 * Сообщаем в пакетный процесс, что к текущему пакету добавлен новый объект
	 */
	@Override
	public void messageAddToPacket(String businessObjectClass, Object businessObjectId, String pluginName) {
		ProcessInstance pinst = findPluginProcess(pluginName, businessObjectClass, businessObjectId);
		if (pinst == null) {
			return;
		}
		
		wfEngine.messageProcessByInstanceId(pinst.getId(), SignalRef.toString(null, pluginName, ProcessKeys.MSG_ADD_TO_PACKET), null);
	}
	
	@Override
	public WorkflowObjectState getWFOByPrimaryKey(String primaryKey) {
		String[] spk = primaryKey.split(":");
		WorkflowObjectStateDef def = new WorkflowObjectStateDef(spk[0] + ":" + spk[1] + ":" + spk[2]);
		WorkflowObjectState state = new WorkflowObjectState(def);
		state.setTaskId(spk[3]);
		state.setBusinessObjectClass(spk[4]);
		state.setBusinessObjectId(Long.parseLong(spk[5]));
		return state;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void repaymentReceivedContact(int creditRequestId, Map<String, Object> data) throws WorkflowRuntimeException, WorkflowException {
        logger.info("Repayment received Contact for "+creditRequestId);
        repaymentReceived(creditRequestId, Account.CONTACT_TYPE, data);
	
        logger.info("Repayment received Contact for "+creditRequestId + " after goProcCR");
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void repaymentReceivedArius(int creditRequestId, Map<String, Object> data) throws WorkflowRuntimeException, WorkflowException {
        logger.info("Repayment received Arius for "+creditRequestId);
        repaymentReceived(creditRequestId, Account.CARD_TYPE, data);

        logger.info("Repayment received Arius for "+creditRequestId + " after goProcCR");
    }


    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void repaymentReceived(int creditRequestId, int accountType, Map<String, Object> data) throws WorkflowRuntimeException, WorkflowException {

		Map<String, Object> mp = data;
		if (mp == null) {
			mp = new HashMap<String,Object>(2);
		}
		mp.put("accountType", accountType);
		
		//смотрим порядок различных выплат
		List<Integer> listSumIds = rulesBean.getPaymentToOrder();
		listSumIds.add(null);
		List<List> priority = new ArrayList<>(listSumIds.size());
		for (Integer sumId: listSumIds) {
			priority.add(new ArrayList(1));
		}

		int paymentId = Convertor.toInteger(mp.get("paymentId"));
		Payment payment = payDAO.getPayment(paymentId, Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST, BaseCredit.Options.INIT_PROLONGS, BaseCredit.Options.INIT_REFINANCES));
		
		List<String> lstBusinessKeys = new ArrayList<String>(5);
		lstBusinessKeys.add(CreditRequest.class.getName() + ":" + String.valueOf(creditRequestId));
		lstBusinessKeys.add(Credit.class.getName() + ":" + payment.getCredit().getId().toString());
		lstBusinessKeys.add(Payment.class.getName() + ":" + payment.getId().toString());
		Refinance refin =payment.getCredit().getLastRefinance();
		if (refin != null) {
			lstBusinessKeys.add(Refinance.class.getName() + ":" + refin.getId().toString());	
		}
		Prolong prolong =payment.getCredit().getDraftProlong();
		if (prolong != null) {
			lstBusinessKeys.add(Prolong.class.getName() + ":" + prolong.getId().toString());
		}

		List<String> lstInst = wfEngine.findExecutionIds(ProcessKeys.MSG_PAYMENT_RECEIVED, lstBusinessKeys, ProcessKeys.VAR_PAYMENT_FORM + ".type", new Long(Payment.TO_SYSTEM), ProcessKeys.PREFIX_WF_ACTION);
		Map<String,Object> vars = null;
		// выстраиваем по приоритету
		for (String executionId: lstInst) {
			vars = wfEngine.getExecutionVariables(executionId, Utils.setOf(ProcessKeys.VAR_PAYMENT_FORM + ".sumId"));
			Number nSumId = (Number) vars.get(ProcessKeys.VAR_PAYMENT_FORM + ".sumId");
			int idx = listSumIds.indexOf(new Integer(nSumId.intValue()));
			if (idx < 0) {
				idx = listSumIds.indexOf(null);
			}
			priority.get(idx).add(executionId);
		}
	
		logger.info("Repayment received for "+creditRequestId + " after processes");

		List<BizActionEntity> lstBiz = servBean.getBizActionsForEvent(EventCode.EXTERNAL_PAYMENT_RECEIVED);		
		CreditRequestContext ctx = (CreditRequestContext) AppUtil.getDefaultContext(payment, Payment.class.getName());
		ctx.getParams().putAll(mp);
		for (BizActionEntity biz: lstBiz) {
			try {
				vars = null;
				if (biz.getBusinessObjectClass().equals(CreditRequest.class.getName()) && ctx.getCreditRequest() != null) {
					vars = bizProc.executeBizAction(ctx.getCreditRequest(), biz, ctx, true);
				} else if (biz.getBusinessObjectClass().equals(Credit.class.getName()) && ctx.getCredit() != null) {
					vars = bizProc.executeBizAction(ctx.getCredit(), biz, ctx, true);
				} else if (biz.getBusinessObjectClass().equals(Payment.class.getName()) && ctx.getPayment() != null) {
					vars = bizProc.executeBizAction(ctx.getPayment(), biz, ctx, true);
				} else if (biz.getBusinessObjectClass().equals(Refinance.class.getName()) && ctx.getRefinance() != null) {
					vars = bizProc.executeBizAction(ctx.getRefinance(), biz, ctx, true);
				} else if (biz.getBusinessObjectClass().equals(Prolong.class.getName()) && ctx.getProlong() != null) {
					vars = bizProc.executeBizAction(ctx.getProlong(), biz, ctx, true);
				} else if (biz.getBusinessObjectClass().equals(PeopleMain.class.getName()) && ctx.getClient() != null) {
					vars = bizProc.executeBizAction(ctx.getClient(), biz, ctx, true);
				}
				if (vars != null) {
					Number nSumId = (Number) vars.get(ProcessKeys.VAR_PAYMENT_FORM + ".sumId");
					int idx = listSumIds.indexOf(nSumId);
					if (idx < 0) {
						idx = listSumIds.indexOf(null);
					}
					priority.get(idx).add(biz);					
				}
			} catch (ActionException e) {
				logger.log(Level.SEVERE,"Ошибка при выполнении бизнес-действия " + biz.getId(), e);
			}
		}
		
		boolean bOk = false;
		for (List alst: priority) {
			for (Object aobj: alst) {
				
				if (aobj instanceof String) {
					String executionId = (String) aobj;
					wfEngine.messageExecution(executionId,  SignalRef.toString(null, null, ProcessKeys.MSG_PAYMENT_RECEIVED), Utils.<String, Object>mapOf(ProcessKeys.VAR_DATA, mp));					
				} else if (aobj instanceof BizActionEntity) {
					BizActionEntity biz = (BizActionEntity) aobj;
					try {
						if (biz.getBusinessObjectClass().equals(CreditRequest.class.getName()) && ctx.getCreditRequest() != null) {
							 bizProc.executeBizAction(ctx.getCreditRequest(), biz, ctx, false);
						} else if (biz.getBusinessObjectClass().equals(Credit.class.getName()) && ctx.getCredit() != null) {
							vars = bizProc.executeBizAction(ctx.getCredit(), biz, ctx, false);
						} else if (biz.getBusinessObjectClass().equals(Payment.class.getName()) && ctx.getPayment() != null) {
							vars = bizProc.executeBizAction(ctx.getPayment(), biz, ctx, false);
						} else if (biz.getBusinessObjectClass().equals(Refinance.class.getName()) && ctx.getRefinance() != null) {
							vars = bizProc.executeBizAction(ctx.getRefinance(), biz, ctx, false);
						} else if (biz.getBusinessObjectClass().equals(Prolong.class.getName()) && ctx.getProlong() != null) {
							vars = bizProc.executeBizAction(ctx.getProlong(), biz, ctx, false);
						} else if (biz.getBusinessObjectClass().equals(PeopleMain.class.getName()) && ctx.getClient() != null) {
							vars = bizProc.executeBizAction(ctx.getClient(), biz, ctx, false);
						}
						
					} catch (ActionException e) {
						logger.log(Level.SEVERE,"Ошибка при выполнении бизнес-действия " + biz.getId(), e);
					}					
				}
				bOk = true;
				break;
			}
			if (bOk) {
				break;
			}
		}
		//		goProcAll(Payment.class.getName(), data.get("paymentId"), SignalRef.toString(null, null, ProcessKeys.MSG_PAYMENT_RECEIVED), Utils.<String, Object>mapOf(ProcessKeys.VAR_DATA, mp), true);		
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void repaymentReceivedYandex(int creditRequestId, Map<String, Object> data) throws WorkflowRuntimeException, WorkflowException {
		logger.info("Repayment received Yandex for "+creditRequestId);
		repaymentReceived(creditRequestId, Account.YANDEX_TYPE, data);
		logger.info("Repayment received Yandex for "+creditRequestId + " after bizactions");
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void repaymentReceivedQiwi(int creditRequestId, Map<String, Object> data) throws WorkflowRuntimeException, WorkflowException {
		logger.info("Repayment received Qiwi for "+creditRequestId);
		repaymentReceived(creditRequestId, Account.QIWI_TYPE, data);

		logger.info("Repayment received Qiwi for "+creditRequestId + " after goProcCR");
		
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void repaymentReceivedPayonline(int creditRequestId, Map<String,
            Object> data) throws WorkflowRuntimeException, WorkflowException {
    	logger.info("Repayment received Payonline for "+creditRequestId);
    	repaymentReceived(creditRequestId, Account.CARD_TYPE, data);
  
        logger.info("Repayment received Payonline for "+creditRequestId + " after goProcCR");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void repaymentReceivedQiwiGate(Integer creditRequestId, Map<String, Object> data) throws WorkflowException {
    	logger.info("Repayment received Qiwi gate for "+creditRequestId);
    	repaymentReceived(creditRequestId, Account.QIWI_TYPE, data);
     
        logger.info("Repayment received Qiwi gate for "+creditRequestId + " after goProcCR");
    }

	@Override
	public void repaymentReceivedWinpay(int creditRequestId, Map<String, Object> data) throws WorkflowException {
		logger.info("Repayment received Winpay for " + creditRequestId);
		repaymentReceived(creditRequestId, Account.CARD_TYPE, data);

		logger.info("Repayment received Winpay for " + creditRequestId + " after goProcCR");
	}

	@Override
	public void runDebugModelCR(CreditRequest ccRequest, ActionContext actionContext, Map<String, Object> varsResult, int modelId ) throws WorkflowException {
//		ActionContextProxy proxy = new ActionContextProxy(actionContext.getCustomizationKey(), actionContext.isPersistent());
		
	  	ProcessInstance pinst = wfEngine.startProcess(ProcessKeys.DEF_DEBUG_MODEL, "ru.simplgroupp.transfer.CreditRequest:" + String.valueOf(ccRequest.getId()), Utils.<String, Object>mapOf(
	  			ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, CreditRequest.class.getName(),
	  			ProcessKeys.VAR_BUSINESS_OBJECT_ID, ccRequest.getId(),
	  			ProcessKeys.VAR_MODEL_KEY, ModelKeys.TARGET_CREDIT_DECISION,
	  			ProcessKeys.VAR_MODEL_ID, modelId,
	  			ProcessKeys.VAR_ACTION_CONTEXT, actionContext ));
	  		  	
		while ( ! "taskBeforeEnd".equalsIgnoreCase(pinst.getActivityId()) ) {
			// TODO
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE,e.getMessage(), e);
			}
			pinst = wfEngine.getProcessInstance(pinst.getProcessInstanceId());
		}		
		
		Map<String, Object> varsLocal = wfEngine.getProcessVariables(pinst.getId());
		varsResult.putAll(varsLocal);
		
		wfEngine.signalProcessInstance(pinst.getId());
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void assignTaskToUser(String taskId, Users user) {
		wfEngine.assingTaskTo(taskId, user.getId().toString());
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeTaskFromUser(String taskId) {
		wfEngine.assingTaskTo(taskId,"");
	}
	
	@Override
	public void dummy() {
		
	}
	
	@Override
	public void messageProcMA(int actionId, String signalRef) throws WorkflowException {
		wfEngine.messageProcessByBusinessKey(ProcessKeys.DEF_ACT_STANDART, BizAction.class.getName() + ":" + String.valueOf(actionId), SignalRef.toString(ProcessKeys.DEF_ACT_STANDART, null, signalRef), null);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void messageProcMAtTx(int actionId, String signalRef) throws WorkflowException {
		wfEngine.messageProcessByBusinessKey(ProcessKeys.DEF_ACT_STANDART, BizAction.class.getName() + ":" + String.valueOf(actionId), SignalRef.toString(ProcessKeys.DEF_ACT_STANDART, null, signalRef), null);
	}	
	
}

