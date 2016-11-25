package ru.simplgroupp.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.dao.interfaces.RulesDAO;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.ManualPayBeanLocal;
import ru.simplgroupp.interfaces.RuleSetBeanLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.service.HolidaysService;
import ru.simplgroupp.persistence.AIConstantEntity;
import ru.simplgroupp.persistence.AIRuleEntity;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.rules.AbstractRuleResult;
import ru.simplgroupp.rules.BooleanRuleResult;
import ru.simplgroupp.rules.CreditRequestContext;
import ru.simplgroupp.rules.NameValueRuleResult;
import ru.simplgroupp.rules.watched.FieldInfo;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.NameValuePair;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.util.RulesKeys;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;

/**
 * работа с правилами
 */
@Local(RulesBeanLocal.class)
@Stateless
public class RulesBean implements RulesBeanLocal {
	
	@EJB 
	RulesDAO rulesDAO;
	
    @EJB
    RuleSetBeanLocal ruleSet;
    
    @EJB
    HolidaysService holiServ;
    
    @EJB
    BizActionBeanLocal bizBean;
    
    @EJB
    BizActionDAO bizDAO;
    
    @EJB
    ActionProcessorBeanLocal actProc;
      
    @Inject
    private Event<RuleEvent> evtAIRule;    
	
	@Override
	public List<FieldInfo> calcWatchedFields() {
		StatelessKieSession ks = ruleSet.newWatchedFieldsSession();
		List<FieldInfo> lst =  new ArrayList<FieldInfo>();
		
		ks.execute(Arrays.asList(lst));
		
		return lst;
	}
	
		
	/**
	 * возвращает константы для правила (пакета)
	 * @param ruleName - название правила (пакета)
	 * @return
	 */
	private Map<String,Object> fireConstantRules(String ruleName) {
		List<AIRuleEntity> lstRules = rulesDAO.listRules(ruleName, null, 2, null);
		if (lstRules.size() == 0) {
			return null;
		} else {
			AIRuleEntity aiRule = lstRules.get(0);
			HashMap<String, Object> res = new HashMap<String, Object>(2);
			consToMap(aiRule.getConstants(), res);
			return res;
		}
	}	
	
	/**
	 * записываем константы из коллекции в map
	 * @param source - коллекция
	 * @param dest - map, в который пишем
	 */
	private static void consToMap(List<AIConstantEntity> source, Map<String, Object> dest) {
		for (AIConstantEntity aiCon: source) {
			Object avalue = null;
			if ("C".equals(aiCon.getDataType())) {
				String svalue = aiCon.getDataValue();
				if (StringUtils.isBlank(svalue) && (! StringUtils.isBlank(aiCon.getDataValueText()))) {
					svalue = aiCon.getDataValueText();
				}
				avalue = Convertor.toValue(aiCon.getDataType(), svalue);
			} else {
				avalue = Convertor.toValue(aiCon.getDataType(), aiCon.getDataValue());	
			}
			
			dest.put(aiCon.getName(), avalue);
		}		
	}
	
	@Override
	public BooleanRuleResult canCreateRequest(CreditRequestContext context) {
		BooleanRuleResult res = new BooleanRuleResult();		
		// TODO
		res.setResultValue(true);
		return res;
	}
	
	@Override
	public BooleanRuleResult canProlongRequest(CreditRequestContext context) {
		BooleanRuleResult res = new BooleanRuleResult();		
		// TODO
		res.setResultValue(true);
		return res;
	}	
	
	@Override
	public BooleanRuleResult canSignOferta(CreditRequestContext context) {
		BooleanRuleResult res = new BooleanRuleResult();		
		// TODO
		res.setResultValue(true);
		return res;
	}	
	
	@Override
	public BooleanRuleResult canCompleteRequest(CreditRequestContext context) {
		BooleanRuleResult res = new BooleanRuleResult();
		
		StatelessKieSession ks = ruleSet.newReturnSession();
		try {
			ks.execute(Arrays.asList(res, context));
		} catch (Exception ex) {
			res.addError(0, ex.getMessage());
		}		

		return res;
	}
	
	@Override
	public NameValueRuleResult getReturnLimits(Credit credit, Map<String, Object> creditParams) {
		NameValueRuleResult res = new NameValueRuleResult();
		
		StatelessKieSession ks = ruleSet.newReturnSession();
		try {
			ks.execute(Arrays.asList(res, credit, creditParams));
		} catch (Exception ex) {
			res.addError(0, ex.getMessage());
		}			

		return res;
	}
	
	@Override
	public NameValueRuleResult calcCreditInitial(Map<String, Object> creditParams) {
		NameValueRuleResult res = new NameValueRuleResult();
		
		StatelessKieSession ks = ruleSet.newCreditCalcSession();

		ks.execute(Arrays.asList(res, creditParams, new NameValuePair("ruleKey", RulesKeys.CREDIT_CALC_INITIAL + ".1")));
		
		return res;		
	}
	
	@Override
	public NameValueRuleResult calcCreditInitial(CreditRequest ccRequest,Integer addDays) {
		NameValueRuleResult res = new NameValueRuleResult();
		
		StatelessKieSession ks = ruleSet.newCreditCalcSession();
		ks.execute(Arrays.asList(res, holiServ, ccRequest,addDays));
	
		return res;		
	}	
	
	@Override
	public NameValueRuleResult calcCreditData(Credit credit, Date dateCalc, Map<String, Object> limits) {
		NameValueRuleResult res = new NameValueRuleResult();
		
		StatelessKieSession ks = ruleSet.newCreditCalcSession();
		ks.execute(Arrays.asList(res, limits, credit, dateCalc));
		
		return res;	
	}

	@Override
	public NameValueRuleResult calcCreditToCollector(Credit credit, Map<String, Object> cparams) {
		NameValueRuleResult bResult = new NameValueRuleResult();

		StatelessKieSession ks = ruleSet.newCreditCollectorSession();
		ks.execute(Arrays.asList(credit, cparams, bResult));

		return bResult;
	}
	
	@Override
	public NameValueRuleResult calcInitialStake(double creditSum, int creditDays, Map<String, Object> limits) {
		NameValueRuleResult res = new NameValueRuleResult();
		
		StatelessKieSession ks = ruleSet.newCreditCalcSession();
		ks.execute(Arrays.asList(res, limits, new Double(creditSum), new Integer(creditDays), new NameValuePair("ruleKey", RulesKeys.CREDIT_CALC_INITIAL_STAKE)));
			
		return res;		
	}	
	
	@Override
	public List<WorkflowObjectActionDef> getObjectActions(String businessObjectClass, Boolean forOne, Boolean forMany) {
		List<BizActionEntity> lstBiz = null;
		lstBiz = bizDAO.listBOActions(businessObjectClass, forOne, forMany, null);
		List<WorkflowObjectActionDef> lstRes = new ArrayList<WorkflowObjectActionDef>(lstBiz.size());
		for (BizActionEntity ent: lstBiz) {
			WorkflowObjectActionDef def = new WorkflowObjectActionDef(false);
			
			def.setBusinessObjectClass(ent.getBusinessObjectClass());
			def.setName(ent.getDescription());
			def.setSignalRef(ent.getSignalRef());
			
			def.setAssignee(ent.getAssignee());			
			def.setCandidateGroups(ent.getCandidateGroups());
			def.setCandidateUsers(ent.getCandidateUsers());
			
			lstRes.add(def);
		}
		return lstRes;

	}
	
	@Override
	public NameValueRuleResult executeObjectAction(WorkflowObjectActionDef actionDef, ActionProcessorBeanLocal actProc, AbstractContext context) throws ActionException {
		NameValueRuleResult res = new NameValueRuleResult();
		
		StatelessKieSession ks = ruleSet.newObjectActionsSession(actionDef.getBusinessObjectClass(), actionDef.getSignalRef());
		if (ks == null) {
			return null;
		}	
		
		Map<String, Object> cparams = context.getParams();
		if (cparams == null) {
			cparams = new HashMap<String,Object>(5);
		}
		Map<String, Object> options = fireConstantRules(RulesKeys.BA_OPTIONS_PREFIX + "." + actionDef.getBusinessObjectClass() + "." + actionDef.getSignalRef());
		if (options != null) {
			for (Map.Entry<String, Object> ent: options.entrySet()) {
				cparams.put("option." + ent.getKey(), ent.getValue());
			}
		}
		ks.execute(Arrays.asList(res, actionDef, context, cparams, actProc));
		
		return res;		
	}	
	
	@Override
	public NameValueRuleResult executeObjectActionStart(WorkflowObjectActionDef actionDef, ActionProcessorBeanLocal actProc, AbstractContext context) throws ActionException {
		NameValueRuleResult res = new NameValueRuleResult();
		
		StatelessKieSession ks = ruleSet.newObjectActionsSessionStart(actionDef.getBusinessObjectClass(), actionDef.getSignalRef());
		if (ks == null) {
			return null;
		}	
		
		Map<String, Object> cparams = context.getParams();
		if (cparams == null) {
			cparams = new HashMap<String,Object>(5);
		}
		Map<String, Object> options = fireConstantRules(RulesKeys.BA_OPTIONS_PREFIX + "." + actionDef.getBusinessObjectClass() + "." + actionDef.getSignalRef());
		if (options != null) {
			for (Map.Entry<String, Object> ent: options.entrySet()) {
				cparams.put("option." + ent.getKey(), ent.getValue());
			}
		}
		ks.execute(Arrays.asList(res, actionDef, context, cparams, actProc));
		
		return res;		
	}	
	
	@Override
	public BooleanRuleResult getObjectActionEnabled(WorkflowObjectActionDef actionDef, AbstractContext context) {
		BooleanRuleResult res = new BooleanRuleResult();
				
		StatelessKieSession ks = ruleSet.newObjectActionsSession(actionDef.getBusinessObjectClass(), actionDef.getSignalRef());
		if (ks == null) {
			return null;
		}	
		
		Map<String, Object> cparams = context.getParams();
		try {
			ks.execute(Arrays.asList(res, actionDef, context, cparams, actProc));
		} catch (Exception ex) {
			res.addError(0, ex.getMessage());
		}		
		
		if (! res.hasErrors() && res.getResultValue() == null) {
			res.setResultValue(true);
		}
		
		return res;		
	}
	
	@Override
	public BooleanRuleResult getProcessActionEnabled(final SignalRef actionRef, AbstractContext context) {
		BooleanRuleResult res = new BooleanRuleResult();
		res.setResultValue(true);
		Map<String, Object> cparams = context.getParams();

		StatelessKieSession ks = ruleSet.newProcessActionsSession(actionRef.getProcessDefKey(), actionRef.getPluginName(), actionRef.getName());
		if (ks == null) {
			return null;
		}
		
		try {
			ks.execute(Arrays.asList(res, cparams, actionRef, context, actProc));
		} catch (Exception ex) {
			res.addError(0, ex.getMessage());
		}

/*		
		ks.insert(res);
		ks.insert(cparams);
		
		try {
			try {
				ks.fireAllRules(new AgendaFilter() {
	
					@Override
					public boolean accept(Match arg0) {
						String ss = arg0.getRule().getName();
						return ss.startsWith(RulesKeys.BP_ACTIONS_PREFIX + actionRef.toString() + ".");
					}});
			} finally {
				ks.dispose();
			}	
		} catch (Exception ex) {
			res.addError(0, ex.getMessage());
		}
*/		
		return res;
	}
	
	private void executeSession(KieSession ks, final String prefix, AbstractRuleResult res) {
		try {
			try {
				ks.fireAllRules(new AgendaFilter() {
	
					@Override
					public boolean accept(Match arg0) {
						String ss = arg0.getRule().getName();
						return ss.startsWith(prefix + ".");
					}});
			} finally {
				ks.dispose();
			}	
		} catch (Exception ex) {
			res.addError(0, ex.getMessage());
		}
	}
	
	@Override
	public Map<String,Object> getFormattedMessages() {
		return fireConstantRules(RulesKeys.SYSTEM_MESSAGES_PREFIX);
	}	
	
	@Override
	public String getFormattedMessage(String messageKey) {
		AIConstantEntity aiCon = rulesDAO.findConstant(RulesKeys.SYSTEM_MESSAGES_PREFIX, messageKey);
		if (aiCon == null) {
			return null;
		} else {
			return aiCon.getDataValue();
		}
		
	}
	
	@Override
	public Map<String,Object> getFormattedMessagesForBP(String bpName) {
		return fireConstantRules(RulesKeys.BP_MESSAGES_PREFIX + "." + bpName);
	}	
	
	
    @Override
	public Map<String,Object> getOptionsForBP(String bpName) {
		return fireConstantRules(RulesKeys.BP_OPTIONS_PREFIX + "." + bpName);
	}	
	
	@Override
	public Map<String,Object> getOptionsForBOP(String actionRef) {
		return fireConstantRules(RulesKeys.BP_OPTIONS_PREFIX + "." + ProcessKeys.DEF_ACT_STANDART + "." + actionRef);
	}	
	
	
	@Override
	public Map<String,Object> getPluginsOptions(String customKey) {
		String sname = RulesKeys.PLUGINS_PREFIX;
		if (! StringUtils.isBlank(customKey)) {
			sname = sname + "." + customKey;
		}
		return fireConstantRules(sname);
	}
	
	@Override
	public List<Integer> getPaymentToOrder() {
		Map<String,Object> mp = fireConstantRules(RulesKeys.PAYMENT_ORDER_TO);
		
		List<Integer> lstRes = new ArrayList<Integer>(mp.size());
		Set<String> st = new TreeSet<String>(mp.keySet());
		for (String sidx: st) {
			Object nvalue = mp.get(sidx);
			if (nvalue != null && nvalue instanceof Number) {
				lstRes.add(((Number) nvalue).intValue() );
			}
		}
		return lstRes;
	}
	
	@Override
	public Map<String,Object> getPluginsOptions(String pluginName, String customKey) {
		String sname = RulesKeys.PLUGINS_PREFIX;
		if (! StringUtils.isBlank(customKey)) {
			sname = sname + "." + customKey;
		}		
		List<AIConstantEntity> lst = rulesDAO.listConstants(sname, pluginName + "%");
		Map<String,Object> dest = new HashMap<String,Object>(lst.size());
		consToMap(lst, dest);
		return dest;
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public boolean deletePluginOptions(String customKey) {
		String sname = RulesKeys.PLUGINS_PREFIX;
		if (! StringUtils.isBlank(customKey)) {
			sname = sname + "." + customKey;
		}		
		AIRuleEntity ent = rulesDAO.findRuleByName(sname);
		if (ent == null) {
			return false;
		}
		rulesDAO.deleteRule(ent.getId());
		return true;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void setPluginsOptions(String customKey, String description, Map<String,Object> source) {
		String sname = RulesKeys.PLUGINS_PREFIX;
		if (! StringUtils.isBlank(customKey)) {
			sname = sname + "." + customKey;
		}		
		
		// добавляем правило, если его ещё нет
    	List<AIRuleEntity> lstRules = rulesDAO.listRules(sname, null, null, null);
    	if (lstRules.size() == 0) {
    		// TODO
    		AIRuleEntity ent = this.createRule(AIRuleEntity.RULE_TYPE_CONSTANT, sname, description,null,null);
    	} else {
    		AIRuleEntity ent = lstRules.get(0);
    		ent.setDescription(description);
    		saveRule(ent);
    	}
    	
		saveConstants(sname, source);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public AIRuleEntity saveRule(AIRuleEntity rule) {
		AIRuleEntity rule1 = rulesDAO.saveRule(rule);
		if (rule1.isStaticConstantRules()) {
			evtAIRule.fire(new RuleEvent(rule1.getPackageName(), RuleEvent.ActionType.UPDATED_CONSTANT));
		} else {
			evtAIRule.fire(new RuleEvent(rule1.getPackageName(), RuleEvent.ActionType.UPDATED));
		}
		return rule1;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public AIRuleEntity createRule(int ruleType, String packageName,String description,String body, String kbase) {
		AIRuleEntity rule = new AIRuleEntity();
		rule.setRuleType(ruleType);
		rule.setPackageName(packageName);
		rule.setDescription(description);
		rule.setBody(body);
		rule.setKbase(kbase);
		rule = rulesDAO.saveRule(rule);
		// TODO послать событие
		return rule;
	}	
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveConstant(Integer ruleId, String name, String dataType, String datavalue, 
    		String description,String datavaluetext) {
        AIRuleEntity rule = rulesDAO.getAIRule(ruleId);
        AIConstantEntity ent = rulesDAO.findConstant(rule.getPackageName(), name);
        if (ent == null) {
        	ent = new AIConstantEntity();
        	ent.setAiRule(rule);
        	ent.setName(name);
        }
        ent.setDataValue(datavalue);
        ent.setDataType(dataType);
        ent.setDescription(description);
        ent.setDataValueText(datavaluetext);
        ent = rulesDAO.saveConstant(ent);
        
        evtAIRule.fire(new RuleEvent(rule.getPackageName(), RuleEvent.ActionType.UPDATED_CONSTANT));

        // TODO послать сообщение
    }
    
	@Override
	public void updateConstantValue(String packageName, String name,
			String datavalue) {
		 AIRuleEntity rule = rulesDAO.findRuleByName(packageName);
	        AIConstantEntity ent = rulesDAO.findConstant(rule.getPackageName(), name);
	        if (ent == null) {
	        	return;
	        }
	        ent.setDataValue(datavalue);
	        ent = rulesDAO.saveConstant(ent);
	        
	        evtAIRule.fire(new RuleEvent(rule.getPackageName(), RuleEvent.ActionType.UPDATED_CONSTANT));
	}
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveConstants(String packageName, Map<String, Object> values) {
        List<AIRuleEntity> lstRules = rulesDAO.listRules(packageName, null, null, null);
        if (lstRules.size() == 0) {
            return;
        }
        AIRuleEntity rule = lstRules.get(0);
        int ruleId = rule.getId();
        rulesDAO.deleteRuleConstants(ruleId);

        rule = rulesDAO.getAIRule(ruleId);
        // TODO надо сохранять не только название и значение, но и остальные поля из констант
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            AIConstantEntity ent = new AIConstantEntity();
            ent.setAiRule(rule);

            ent.setName(entry.getKey());
            if (entry.getValue() == null) {
                ent.setDataType("C");
            } else if (entry.getValue() instanceof String) {
                ent.setDataType("C");
                ent.setDataValue(entry.getValue().toString());
            } else if (entry.getValue() instanceof Integer || entry.getValue() instanceof Long) {
                ent.setDataType("N");
                ent.setDataValue(entry.getValue().toString());
            } else if (entry.getValue() instanceof Date) {
                ent.setDataType("D");
                long time = ((Date) entry.getValue()).getTime();
                ent.setDataValue(String.valueOf(time));
            } else if (entry.getValue() instanceof Double || entry.getValue() instanceof Float || entry.getValue() instanceof BigDecimal) {
                ent.setDataType("F");
                ent.setDataValue(entry.getValue().toString());
            }

            rule.getConstants().add(ent);
        }
        rulesDAO.saveRule(rule);
        // TODO послать событие
    }

    @Override
    public Map<String, String> getPluginConfigurations() {
        List<AIRuleEntity> lstRules = rulesDAO.listRules(RulesKeys.PLUGINS_PREFIX + "%", null, null, null);
        HashMap<String, String> res = new HashMap<String, String>(lstRules.size());
        for (AIRuleEntity ent : lstRules) {
            String skey = ent.getPackageName().replace(RulesKeys.PLUGINS_PREFIX, "");
            if (skey.startsWith(".")) {
                skey = skey.substring(1);
            }
            String svalue = ent.getDescription();
            if (StringUtils.isBlank(svalue) && StringUtils.isBlank(skey)) {
                svalue = "Рабочие настройки";
            }
            res.put(skey, svalue);
        }
        return res;
    }

    @Override
    public Map<String, Object> getCryptoCommonSettings() {
        return fireConstantRules(RulesKeys.CRYPTO_COMMON);
    }

    @Override
    public List<String> getPaymentSystemOrder(int accountTypeCode) {
        Map<String, Object> prefs = fireConstantRules(RulesKeys.PAYMENT_SYSTEM_ORDER);

        String sprefs = (String) prefs.get(String.valueOf(accountTypeCode));
        if (StringUtils.isBlank(sprefs)) {
            return Utils.listOf(ManualPayBeanLocal.SYSTEM_NAME);
        }

        String[] sarr = sprefs.split(",");
        ArrayList<String> lst = new ArrayList<String>(sarr.length);
        for (String ss : sarr) {
            lst.add(ss.trim());
        }
        return lst;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteConstant(Integer constantId) {
    	// TODO send BisinessEvent
    	rulesDAO.deleteConstant(constantId);
    }
    
    @Override
    public Map<String,Object> getMainWebConstant() {
    	return fireConstantRules(RulesKeys.AC_MAIN);
    }

	@Override
	public Map<String, Object> getSmsConstants() {
		return fireConstantRules(RulesKeys.SMS_COMMON);
	}

	@Override
	public Map<String, Object> getEmailConstants() {
		return fireConstantRules(RulesKeys.EMAIL_COMMON);
	}
	

	@Override
	public Map<String, Object> getSocNetworkConstants() {
		return fireConstantRules(RulesKeys.SOCNETWORK_COMMON);
	}

	@Override
	public Map<String, Object> getSiteConstants() {
		return fireConstantRules(RulesKeys.SITE_COMMON);
	}

	@Override
	public NameValueRuleResult calcCreditParams(Map<String, Object> creditLimits) {
        NameValueRuleResult res = new NameValueRuleResult();
	    StatelessKieSession ks = ruleSet.newCreditCalcParamsSession();
		ks.execute(Arrays.asList(res, creditLimits));
		return res;	
	}
	
	@Override
	public Map<String, Object> getBonusConstants() {
		return fireConstantRules(RulesKeys.LIMITS_BONUS);
	}

	@Override
	public Map<String, Object> getCallbackConstants() {
		return fireConstantRules(RulesKeys.CALLBACK);
	}

	@Override
	public NameValueRuleResult calcBonusPaymentSum(Double sumBack,Double bonusSum, Map<String, Object> limits) {
		
        NameValueRuleResult res = new NameValueRuleResult();
		
		StatelessKieSession ks = ruleSet.newBonusCalcSession();
		ks.execute(Arrays.asList(res, limits, new Double(sumBack),new Double(bonusSum)));
			
		return res;		
	}


	@Override
	public NameValueRuleResult calcProlongDays(Credit credit, Date dateLong,
			Map<String, Object> limits) {
        NameValueRuleResult res = new NameValueRuleResult();
		
		StatelessKieSession ks = ruleSet.newLongdaysCalcSession();
		ks.execute(Arrays.asList(res, limits, credit, dateLong));
		
		return res;	
	
	}


	@Override
	public Map<String, Object> getLoginConstants() {
		return fireConstantRules(RulesKeys.LOGIN_COMMON);
	}

	@Override
	public Map<String, Object> getAntifraudConstants() {
		return fireConstantRules(RulesKeys.ANTIFRAUD_SETTINGS);
	}

	@Override
	public Map<String, Object> getGenerateConstants() {
		return fireConstantRules(RulesKeys.GENERATE_COMMON);
	}

	

}
