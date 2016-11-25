package ru.simplgroupp.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.ejb.ApplicationAction;
import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.interfaces.BusinessObjectRef;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.rules.BooleanRuleResult;
import ru.simplgroupp.rules.NameValueRuleResult;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Roles;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.StateRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;
import ru.simplgroupp.workflow.WorkflowObjectState;
import ru.simplgroupp.workflow.WorkflowObjectStateDef;

public class WorkflowUtil {
	
	public static SimpleDateFormat dfISO8601Date = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dfISO8601DateTime = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
	
	public static String calcISOdate(Date source) {
		
		return dfISO8601Date.format(source);
	}
	
	public static String calcISOdateTime(Date source) {
		
		return dfISO8601DateTime.format(source);
	}	
	
	public static String calcISOduration(long ms) {
		long ss = (ms/1000);
		return "PT" + String.valueOf(ss) + "S";		
	}
	
	public static String calcISOcycle(long ms) {
		long ss = (ms/1000);
		return "R/PT" + String.valueOf(ss) + "S";		
	}	
	
	public static Set extractSetColumn(Collection<Object[]> source, int idxColumn) {
		Set lstRes = new HashSet(source.size());
		for (Object[] row: source) {
			lstRes.add(row[idxColumn]);
		}
		return lstRes;
	}	
	
	public static List extractListColumn(Collection<Object[]> source, int idxColumn) {
		List lstRes = new ArrayList(source.size());
		for (Object[] row: source) {
			lstRes.add(row[idxColumn]);
		}
		return lstRes;
	}
	
	public static List<String> extractExecutionIds(List<Execution> source) {
		List<String> lstRes = new ArrayList<String>(source.size());
		for (Execution ex: source) {
			lstRes.add(ex.getId());
		}
		return lstRes;
	}
	
	public static List<String> extractProcIds(List<ProcessInstance> source) {
		List<String> lstRes = new ArrayList<String>(source.size());
		for (ProcessInstance ex: source) {
			lstRes.add(ex.getId());
		}
		return lstRes;
	}
	
	/**
	 * возвращает название процесса
	 * 
	 * @param processDefId - строка, определяющая процесс
	 * @return
	 */
	public static String extractProcessDefKey(String processDefId) {
		return processDefId.substring(0, processDefId.indexOf(":"));
	}
	
	/**
	 * возвращает имя плагина
	 * 
	 * @param varsLocal - список переменных
	 * @return
	 */
	public static String getPluginName(Map<String, Object> varsLocal) {
		PluginConfig plc = (PluginConfig) varsLocal.get(ProcessKeys.VAR_PLUGIN);
		if (plc == null) {
			return null;
		} else {
			return plc.getPluginName();
		}
	}	
	
	public static boolean isPluginProcess(Map<String, Object> varsLocal, String pluginName) {
		PluginConfig plc = (PluginConfig) varsLocal.get(ProcessKeys.VAR_PLUGIN);
		if (plc == null) {
			return false;
		} else {
			return plc.getPluginName().equals(pluginName);
		}
	}
	
	public static WorkflowObjectStateDef findWFState(List<WorkflowObjectStateDef> source, StateRef stateTpl) {
		for (WorkflowObjectStateDef stateDef: source) {
			if (! "*".equals(stateTpl.getProcessDefKey()) ) {
				if (! stateTpl.getProcessDefKey().equals(stateDef.getProcessDefinitionKey())) {
					continue;
				}
			}

			if (! "*".equals(stateTpl.getPluginName()) ) {
				if (! stateTpl.getPluginName().equals(stateDef.getPluginName())) {
					continue;
				}
			}

			if (! "*".equals(stateTpl.getName()) ) {
				if (! stateTpl.getName().equals(stateDef.getStateName())) {
					continue;
				}
			}
			
			return stateDef;
		}
		return null;
	}
	
	public static List<WorkflowObjectState> findWFStatesByVars(List<WorkflowObjectState> source, Map<String, Object> vars, boolean bAll) {
		List<WorkflowObjectState> lstRes = new ArrayList<WorkflowObjectState>(source.size());
		for (WorkflowObjectState state: source) {
			boolean bOk = false;
			for (Map.Entry<String,Object> entry: vars.entrySet()) {
				Object avalue = state.getDefinition().getVariablesLocal().get(entry.getKey());
				if (Utils.equalsNull(avalue, entry.getValue())) {
					if (! bAll) {
						bOk = true;
						break;
					}
				} else {
					if (bAll) {
						bOk = false;
						break;
					}
				}
			}
			if (bOk) {
				lstRes.add(state);
			}
		}
		return lstRes;
	}
	
	public static List<WorkflowObjectActionDef> findWFActionsByVars(List<WorkflowObjectActionDef> source, Map<String, Object> vars, boolean bAll) {
		List<WorkflowObjectActionDef> lstRes = new ArrayList<WorkflowObjectActionDef>(source.size());
		for (WorkflowObjectActionDef actDef: source) {
			if (actDef.getVariables() == null) {
				continue;
			}
			boolean bOk = false;
			for (Map.Entry<String,Object> entry: vars.entrySet()) {
				Object avalue = actDef.getVariables().get(entry.getKey());
				if (Utils.equalsNull(avalue, entry.getValue())) {
					if (! bAll) {
						bOk = true;
						break;
					}
				} else {
					if (bAll) {
						bOk = false;
						break;
					}
				}
			}
			if (bOk) {
				lstRes.add(actDef);
			}
		}
		return lstRes;
	}	
	
	public static WorkflowObjectState findWFState1(List<WorkflowObjectState> source, StateRef stateTpl) {
		for (WorkflowObjectState state: source) {
			if (! "*".equals(stateTpl.getProcessDefKey()) ) {
				if (! stateTpl.getProcessDefKey().equals(state.getDefinition().getProcessDefinitionKey())) {
					continue;
				}
			}

			if (! "*".equals(stateTpl.getPluginName()) ) {
				if (! stateTpl.getPluginName().equals(state.getDefinition().getPluginName())) {
					continue;
				}
			}

			if (! "*".equals(stateTpl.getName()) ) {
				if (! stateTpl.getName().equals(state.getDefinition().getStateName())) {
					continue;
				}
			}
			
			return state;
		}
		return null;
	}	
	
	public static WorkflowObjectActionDef findWFAction1(List<WorkflowObjectState> source, String signalRef) {
		for (WorkflowObjectState state: source) {
			WorkflowObjectActionDef actDef = findWfActionA(state.getDefinition().getActions(), signalRef);
			if (actDef != null) {
				return actDef;
			}
		}
		return null;
	}	
	
	public static void checkWFActionsEnabled1(List<WorkflowObjectState> source, RuntimeServices services, AbstractContext context) {
		for (WorkflowObjectState state: source) {
			checkWFActionsEnabled(state.getDefinition().getActions(), services, context);			
		}
	}
	
	public static void checkWFActionsEnabled( List<WorkflowObjectActionDef> source, RuntimeServices services,
			AbstractContext context) {
		for (WorkflowObjectActionDef def: source) {
			if (! def.isEnabled()) {
				continue;
			}
			if (def.isForProcess()) {
				BooleanRuleResult bres = services.getRulesBean().getProcessActionEnabled(SignalRef.valueOf(def.getSignalRef()), context); 
				def.setEnabled( bres.getResultValue() == null || bres.getResultValue() );
				def.setComment(RuleUtil.messagesToString(bres.getInfoMessages()));
			} else {
				BooleanRuleResult bres = services.getRulesBean().getObjectActionEnabled(def, context);
				def.setEnabled( bres.getResultValue() == null || bres.getResultValue() );
				def.setComment(RuleUtil.messagesToString(bres.getInfoMessages()));
				if (bres.getResultValue() == null || bres.getResultValue()) {
					try {
						NameValueRuleResult eres = services.getRulesBean().executeObjectActionStart(def, services.getActionProcessor(), context);
						def.putVariables(eres.getVariables());
					} catch (ActionException e) {
						bres.setResultValue(false);
					}
					
				}
			}
		}
	}
	
	public static void addDetailWFActions1(List<WorkflowObjectState> source, BizActionDAO bizDAO) {
		List<BizActionEntity> lstAct = bizDAO.listBPActions();
		for (WorkflowObjectState state: source) {
			addDetailWFActions(state.getDefinition().getActions(), lstAct);
		}
	}
	
	public static void addDetailWFActions(List<WorkflowObjectActionDef> source, List<BizActionEntity> lstAct) {
		for (WorkflowObjectActionDef def: source) {
			if (def.isForProcess()) {
				SignalRef ref = SignalRef.valueOf(def.getSignalRef());
				BizActionEntity ent = findBizActP(lstAct, ref.getProcessDefKey(), ref.getPluginName(), ref.getName());
				if (ent != null) {
					def.setBusinessObjectClass(ent.getBusinessObjectClass());
					def.setCandidateGroups(ent.getCandidateGroups());
					def.setCandidateUsers(ent.getCandidateUsers());
				}
			}
		}
	}
	
	private static BizActionEntity findBizActP(List<BizActionEntity> lstAct, String processDefKey, String pluginName, String signalRef) {
		for (BizActionEntity ent: lstAct) {
			if (processDefKey.equals(ent.getProcessDefKey()) && signalRef.equalsIgnoreCase(ent.getSignalRef())) {
				return ent;
			}
		}
		return null;
	}
	
	public static void checkWFActionsByRefs1( List<WorkflowObjectState> source, BusinessObjectRef[] bizRefs) {
		for (WorkflowObjectState state: source) {
			checkWFActionsByRefs(state.getDefinition().getActions(), bizRefs);
		}
	}
	
	public static void checkWFActionsByRefs( List<WorkflowObjectActionDef> source, BusinessObjectRef[] bizRefs) {
		for (WorkflowObjectActionDef def: source) {
			if (! def.isEnabled()) {
				continue;
			}
			checkWFActionByRefs(def, bizRefs, false);
		}
	}
	
	public static void checkWFActionByRefs( WorkflowObjectActionDef def, BusinessObjectRef[] bizRefs, boolean bAll) {
		int n = 0;
		for (BusinessObjectRef ref: bizRefs) {
			if (ref.getBusinessObjectClass().equals(def.getBusinessObjectClass())) {
				n++;
			}
		}
		if (bAll) {
			if (n != bizRefs.length) {
				def.setEnabled(false);
			}
		} else {
			if (n == 0) {
				def.setEnabled(false);
			}
		}
	}	
	
	public static void checkWFActionsByRoles1( List<WorkflowObjectState> source, List<Roles> roles) {
		for (WorkflowObjectState state: source) {
			checkWFActionsByRoles(state.getDefinition().getActions(), roles);
		}
	}
	
	public static void checkWFActionsByRoles( List<WorkflowObjectActionDef> source, List<Roles> roles) {
		for (WorkflowObjectActionDef def: source) {
			if (! def.isEnabled()) {
				continue;
			}
			checkWFActionByRoles(def, roles);
		}
	}
	
	public static void checkWFActionsByRunning1( List<WorkflowObjectState> source, List<ApplicationAction> runningActions) {
		for (WorkflowObjectState state: source) {
			checkWFActionsByRunning(state.getDefinition().getActions(), runningActions);
		}
	}	
	
	public static void checkWFActionsByRunning( List<WorkflowObjectActionDef> source, List<ApplicationAction> runningActions) {
		for (WorkflowObjectActionDef def: source) {
			if (! def.isEnabled()) {
				continue;
			}
			checkWFActionsByRunning(def, runningActions);
		}
	}	
	
	public static void checkWFActionsByRunning(WorkflowObjectActionDef def, List<ApplicationAction> runningActions) {
		for (ApplicationAction act: runningActions) {
			if (act.isExclusive()) {
				if (! act.getId().equals(def.getSignalRef())) {
					def.setEnabled(false);
					return;
				}
			}
		}
	}
	
	public static void checkWFActionByRoles(WorkflowObjectActionDef def, List<Roles> roles) {
		if (! StringUtils.isBlank(def.getCandidateGroups()) && (! UserUtil.hasRoles(roles, def.getCandidateGroups().split(","), false) )) {
			def.setEnabled(false);
		}
	}

	public static WorkflowObjectActionDef findWFAction(List<WorkflowObjectStateDef> source, String signalRef) {
		for (WorkflowObjectStateDef stateDef: source) {
			WorkflowObjectActionDef actDef = findWfActionA(stateDef.getActions(), signalRef);
			if (actDef != null) {
				return actDef;
			}
		}
		return null;
	}
	
	public static WorkflowObjectActionDef findWfActionA(List<WorkflowObjectActionDef> source, String signalRef) {
		for (WorkflowObjectActionDef def: source) {
			if (signalRef.equals(def.getSignalRef())) {
				return def;
			}
		}
		return null;
	}
	
	public static void fillWFO(WorkflowObjectState wfo, Set options,CreditRequestDAO crDAO) throws KassaException {
		if (CreditRequest.class.getName().equals(wfo.getBusinessObjectClass())) {
			wfo.setBusinessObject(crDAO.getCreditRequest(((Number) wfo.getBusinessObjectId()).intValue(), options));
		}		
	}
	
	public static void fillWFO(List<WorkflowObjectState> source, Set options, CreditRequestDAO crDAO) throws KassaException {
		for (WorkflowObjectState wfo: source) {
			fillWFO(wfo, options, crDAO);
		}
	}

	/**
	 * Является ли строка корректным выражением ISO 8601
	 * @param schedule - проверяемая строка
	 * @throws WorkflowException - если строка некорректна
	 */
	public static void isISOSchedule(String schedule) throws WorkflowException {
		// TODO Auto-generated method stub
		
	}
	
	public static int findBORef(BusinessObjectRef[] source, String className) {
		for (int i = 0; i < source.length; i++) {
			if (source[i].getBusinessObjectClass().equals(className)) {
				return i;
			}
		}
		return -1;
	}
	
	public static BusinessObjectRef[] extractBizRefs(AbstractContext ctx) {
		BusinessObjectRef[] res = new BusinessObjectRef[10];
		int n = 0;
		if (ctx.getClient() != null) {
			res[n] = new BusinessObjectRef( ctx.getClient().getClass().getName(), ctx.getClient().getId());
			n++;
		}
		if (ctx.getCreditRequest() != null) {
			res[n] = new BusinessObjectRef( ctx.getCreditRequest().getClass().getName(), ctx.getCreditRequest().getId());
			n++;
		}
		if (ctx.getCredit() != null) {
			res[n] = new BusinessObjectRef( ctx.getCredit().getClass().getName(), ctx.getCredit().getId());
			n++;
		}
		if (ctx.getProlong() != null) {
			res[n] = new BusinessObjectRef( ctx.getProlong().getClass().getName(), ctx.getProlong().getId());
			n++;
		}	
		if (ctx.getPayment() != null) {
			res[n] = new BusinessObjectRef( ctx.getPayment().getClass().getName(), ctx.getPayment().getId());
			n++;
		}
		if (ctx.getRefinance() != null) {
			res[n] = new BusinessObjectRef( ctx.getRefinance().getClass().getName(), ctx.getRefinance().getId());
			n++;
		}			
		return Arrays.copyOf(res, n);
	}
	
}
