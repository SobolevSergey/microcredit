package ru.simplgroupp.ejb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.io.KieResources;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.dao.interfaces.RulesDAO;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.interfaces.RuleSetBeanLocal;
import ru.simplgroupp.persistence.AIRuleEntity;
import ru.simplgroupp.persistence.ProductRulesEntity;
import ru.simplgroupp.transfer.AIRule;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.annotation.ABusinessEvent;
import ru.simplgroupp.util.RulesKeys;
import ru.simplgroupp.util.XmlUtils;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Local(RuleSetBeanLocal.class)
@Singleton
public class RuleSetBean implements RuleSetBeanLocal {
	
	private KieServices kieServices;
	private KieContainer kieContainer;
	private KieModuleModel kieModuleModel;
	
	private List<ExceptionInfo> stateMessages = new ArrayList<ExceptionInfo>(15);
	
	@Inject Logger logger;
	
	@EJB
	RulesDAO rulesDAO; 
	
	@EJB
	BizActionBeanLocal bizBean;
	
	@EJB
	BizActionDAO bizDAO;
    
	@EJB
	ProductDAO productDAO;
	
	@PostConstruct
	public void init() {
		kieServices = KieServices.Factory.get();
		
		reloadRules();
	}
	
	private void reloadProcessActionsKB(KieModuleModel kieModuleModel) {
		
		List<BizActionEntity> lstAct = bizDAO.listBPActions();
		for (BizActionEntity actEnt: lstAct) {
			BizAction act = new BizAction(actEnt);
/*			
			if (StringUtils.isBlank(act.getRuleEnabled()) && StringUtils.isBlank(act.getRuleAction())) {
				continue;
			}
*/			
			String kbaseName = "kbPA" + act.getProcessDefKey() + "_" + StringUtils.defaultString(act.getPlugin(), "").trim() + "_" + act.getSignalRef();
			KieBaseModel kbaseModel = kieModuleModel.newKieBaseModel( kbaseName);
			
			String spkg = act.getRulePackageName();
			kbaseModel.addPackage(spkg);
		}
	}
	
	private void reloadProcessActions(KieModuleModel kieModuleModel, KieResources kieResources, KieFileSystem kieFileSystem) {
	
		List<BizActionEntity> lstAct = bizDAO.listBPActions();
		for (BizActionEntity actEnt: lstAct) {
			BizAction act = new BizAction(actEnt); 
			if (StringUtils.isBlank(act.getRuleEnabled()) && StringUtils.isBlank(act.getRuleAction())) {
				continue;
			}
			
			String sPackageName = act.getRulePackageName();
			
			// правило доступности действия
			String ruleText = null;
			if (! StringUtils.isBlank(act.getRuleEnabled())) {
				String ruleNamePrefix = sPackageName + "::" + act.getSignalRef() + ".enabled";
				ruleText = act.getRuleEnabled().replace("[$RULE_NAME_PREFIX$]", ruleNamePrefix);
				ruleText = "package " + sPackageName + "; dialect \"java\"; " + ruleText;				
			}	
			if (ruleText != null) {
				InputStream scoreDrlIn = new ByteArrayInputStream(ruleText.getBytes());
				String path = "src/main/resources/" + sPackageName.replace('.', '/') + "/" + act.getId().toString() + "e.drl";
				kieFileSystem.write(path, kieResources.newInputStreamResource(scoreDrlIn, "UTF-8"));				
			}
			
			// правило выполнения действия
			ruleText = null;
			if (! StringUtils.isBlank(act.getRuleAction())) {
				String ruleNamePrefix = sPackageName + "::" + act.getSignalRef() + ".bizaction";
				ruleText = act.getRuleAction().replace("[$RULE_NAME_PREFIX$]", ruleNamePrefix);
				ruleText = "package " + sPackageName + "; dialect \"java\"; " + ruleText;				
			}	
			if (ruleText != null) {
				InputStream scoreDrlIn = new ByteArrayInputStream(ruleText.getBytes());
				String path = "src/main/resources/" + sPackageName.replace('.', '/') + "/" + act.getId().toString() + "a.drl";
				kieFileSystem.write(path, kieResources.newInputStreamResource(scoreDrlIn, "UTF-8"));				
			}			
		}
	}
	
	private void reloadObjectActionsKB(KieModuleModel kieModuleModel) {
		
		List<BizActionEntity> lstAct = bizDAO.listBOActions();
		for (BizActionEntity actEnt: lstAct) {			
			BizAction act = new BizAction(actEnt);
/*			
			if (StringUtils.isBlank(act.getRuleEnabled()) && StringUtils.isBlank(act.getRuleAction())) {
				continue;
			}
*/			
			reloadObjectActionKB(kieModuleModel, act);
			reloadObjectActionKBStart(kieModuleModel, act);

		}
	}
	
	private static String getKBModelName(BizAction act) {
		String kbaseName = "kbOA" + act.getBusinessObjectClass().replace('.', '_') + "_" + act.getSignalRef();
		return kbaseName;
	}
	
	private static String getKBModelNameStart(BizAction act) {
		String kbaseName = "kbOAS" + act.getBusinessObjectClass().replace('.', '_') + "_" + act.getSignalRef();
		return kbaseName;
	}
	
	private KieBaseModel reloadObjectActionKB(KieModuleModel kieModuleModel, BizAction act) {
		String kbaseName = getKBModelName(act);
		KieBaseModel kbaseModel = kieModuleModel.newKieBaseModel( kbaseName);
		
		String spkg = act.getRulePackageName();
		kbaseModel.addPackage(spkg);
		return kbaseModel;
	}
	
	private KieBaseModel reloadObjectActionKBStart(KieModuleModel kieModuleModel, BizAction act) {
		String kbaseName = getKBModelNameStart(act);
		KieBaseModel kbaseModel = kieModuleModel.newKieBaseModel( kbaseName);
		
		String spkg = act.getRulePackageName() + ".start";
		kbaseModel.addPackage(spkg);
		return kbaseModel;
	}	
	
	/**
	 * перезагружаем правило  
	 * @param kieResources
	 * @param kieFileSystem
	 * @param rule
	 */
	private void reloadRule(KieResources kieResources, KieFileSystem kieFileSystem, AIRuleEntity rule) {
		InputStream scoreDrlIn = new ByteArrayInputStream(rule.getBody().getBytes());
		String path = "src/main/resources/" + rule.getPackageName().replace('.', '/') + "/" + rule.getId().toString() + ".drl";
		kieFileSystem.write(path, kieResources.newInputStreamResource(scoreDrlIn, XmlUtils.ENCODING_UTF));		
	}
	
	/**
	 * перезагружаем правило для продукта
	 * 
	 * @param kieResources - knowlegde ресурс
	 * @param kieFileSystem - knowledge файловая система
	 * @param rule - правило
	 */
	private void reloadProductRule(KieResources kieResources, KieFileSystem kieFileSystem, ProductRulesEntity rule) {
		InputStream scoreDrlIn = new ByteArrayInputStream(rule.getRuleBody().getBytes());
		String path = "src/main/resources/" + rule.getName().replace('.', '/') + "/" + rule.getId().toString() + ".drl";
		kieFileSystem.write(path, kieResources.newInputStreamResource(scoreDrlIn, XmlUtils.ENCODING_UTF));		
	}
	
	private void reloadObjectAction(KieResources kieResources, KieFileSystem kieFileSystem, BizAction act) {
		String sPackageName = act.getRulePackageName();
		
		// правило доступности действия
		String ruleText = null;
		if (! StringUtils.isBlank(act.getRuleEnabled())) {
			String ruleNamePrefix = act.getBusinessObjectClass() + "::" + act.getSignalRef() + ".enabled";
			ruleText = act.getRuleEnabled().replace("[$RULE_NAME_PREFIX$]", ruleNamePrefix);
			ruleText = "package " + sPackageName + "; dialect \"java\"; " + ruleText;				
		}	
		if (ruleText != null) {
			InputStream scoreDrlIn = new ByteArrayInputStream(ruleText.getBytes());
			String path = "src/main/resources/" + sPackageName.replace('.', '/') + "/" + act.getId().toString() + "e.drl";
			kieFileSystem.write(path, kieResources.newInputStreamResource(scoreDrlIn, XmlUtils.ENCODING_UTF));				
		}
		
		// правило выполнения действия
		ruleText = null;
		if (! StringUtils.isBlank(act.getRuleAction())) {
			String ruleNamePrefix = act.getBusinessObjectClass() + "::" + act.getSignalRef() + ".bizaction";
			ruleText = act.getRuleAction().replace("[$RULE_NAME_PREFIX$]", ruleNamePrefix);
			ruleText = "package " + sPackageName + "; dialect \"java\"; " + ruleText;				
		}	
		if (ruleText != null) {
			InputStream scoreDrlIn = new ByteArrayInputStream(ruleText.getBytes());
			String path = "src/main/resources/" + sPackageName.replace('.', '/') + "/" + act.getId().toString() + "a.drl";
			kieFileSystem.write(path, kieResources.newInputStreamResource(scoreDrlIn, XmlUtils.ENCODING_UTF));				
		}	
		
		// правило перед началом действия
		ruleText = null;
		sPackageName =sPackageName + ".start";
		if (! StringUtils.isBlank(act.getRuleStart())) {
			String ruleNamePrefix = act.getBusinessObjectClass() + "::" + act.getSignalRef() + ".bizaction";
			ruleText = act.getRuleStart().replace("[$RULE_NAME_PREFIX$]", ruleNamePrefix);
			ruleText = "package " + sPackageName + "; dialect \"java\"; " + ruleText;				
		}	
		if (ruleText != null) {
			InputStream scoreDrlIn = new ByteArrayInputStream(ruleText.getBytes());
			String path = "src/main/resources/" + sPackageName.replace('.', '/') + "/" + act.getId().toString() + "a.drl";
			kieFileSystem.write(path, kieResources.newInputStreamResource(scoreDrlIn, XmlUtils.ENCODING_UTF));				
		}		
	}
	
	private void reloadObjectActions(KieModuleModel kieModuleModel, KieResources kieResources, KieFileSystem kieFileSystem) {
		List<BizActionEntity> lstAct = bizDAO.listBOActions();
		
		for (BizActionEntity actEnt: lstAct) {
			BizAction act = new BizAction(actEnt); 
			if (StringUtils.isBlank(act.getRuleEnabled()) && StringUtils.isBlank(act.getRuleAction())) {
				continue;
			}
			
			reloadObjectAction(kieResources, kieFileSystem, act);			
		}
	}	
	
	private void reloadRules() {
		
		logger.info("Начали перезагрузку правил");
		
		KieResources kieResources = kieServices.getResources();
		KieRepository kieRepository = kieServices.getRepository();
		
		kieModuleModel = kieServices.newKieModuleModel();
		//ищем названия правил продуктов, которые нужно компилировать		
		List<String> kbases = productDAO.listKbase();
		kbases.remove("kbProcessActions");
		kbases.remove("kbObjectActions");
		for (String kbase: kbases) {
			KieBaseModel kbaseModel = kieModuleModel.newKieBaseModel(kbase);
			//ищем сами правила
			List<ProductRulesEntity> rules=productDAO.findAllRulesActiveByKbase(kbase, new Date());
			//если правила есть, добавим их в модель knowledge base
			if (rules.size()>0){
			    for (ProductRulesEntity rule:rules){
				    kbaseModel.addPackage(rule.getName());
			    }
			}
		}
		
		reloadProcessActionsKB(kieModuleModel);
		reloadObjectActionsKB(kieModuleModel);

		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		String sxml = kieModuleModel.toXML();
		kieFileSystem.write("src/main/resources/META-INF/kmodule.xml", sxml);
		
				
		for (String kbase: kbases) {
			//ищем сами правила
			List<ProductRulesEntity> rules=productDAO.findAllRulesActiveByKbase(kbase, new Date());
			//если правила есть, перезагружаем их
			if (rules.size()>0){
			    for (ProductRulesEntity rule:rules){
				    reloadProductRule(kieResources,kieFileSystem, rule);
			    }
			}
		}
		
		reloadProcessActions(kieModuleModel, kieResources, kieFileSystem);
		reloadObjectActions(kieModuleModel, kieResources, kieFileSystem);
		
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();
		Results results = kieBuilder.getResults();
		if (results.hasMessages(Message.Level.ERROR)) {
			// критические ошибки при инициализации
			List<Message> lstm = results.getMessages(Message.Level.ERROR);
			for (Message msg: lstm) {
				stateMessages.add(new ExceptionInfo(0, msg.getText(), Type.BUSINESS, ResultType.FATAL));
			}
		} else if (results.hasMessages(Message.Level.WARNING)) { 
			logger.warning("Предупреждения при перезагрузке правил");
		}
		kieContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
		logger.info("Закончили перезагрузку правил");
	}
	
	@Override
	public List<ExceptionInfo> preCompileBizActionRules(BizAction action) {
		KieResources kieResources = kieServices.getResources();
		KieBaseModel kbModel = reloadObjectActionKB(kieModuleModel, action);
		
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		String sxml = kieModuleModel.toXML();
		kieFileSystem.write("src/main/resources/META-INF/kmodule.xml", sxml);	
		
		reloadObjectAction(kieResources, kieFileSystem, action);
		
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();
		Results results = kieBuilder.getResults();
		
		List<ExceptionInfo> lstRes = new ArrayList<ExceptionInfo>(0);
		List<Message> lstm = results.getMessages();
		for (Message msg: lstm) {
			ResultType rtype = ResultType.NON_FATAL;
			if (msg.getLevel().equals(Message.Level.ERROR)) {
				rtype = ResultType.FATAL;
			}
			lstRes.add(new ExceptionInfo(0, msg.getText(), Type.BUSINESS, rtype));
		}	
		return lstRes;
	}
	
	@Override
	public List<ExceptionInfo> preCompileRule(AIRule rule) {
		KieResources kieResources = kieServices.getResources();
		KieBaseModel kbModel = kieModuleModel.newKieBaseModel( rule.getKBase());

		kbModel.addPackage(rule.getPackageName());
		
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		String sxml = kieModuleModel.toXML();
		kieFileSystem.write("src/main/resources/META-INF/kmodule.xml", sxml);	
		
		reloadRule(kieResources, kieFileSystem, rule.getEntity());
		
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();
		Results results = kieBuilder.getResults();
		
		List<ExceptionInfo> lstRes = new ArrayList<ExceptionInfo>(0);
		List<Message> lstm = results.getMessages();
		for (Message msg: lstm) {
			ResultType rtype = ResultType.NON_FATAL;
			if (msg.getLevel().equals(Message.Level.ERROR)) {
				rtype = ResultType.FATAL;
			}
			lstRes.add(new ExceptionInfo(0, msg.getText(), Type.BUSINESS, rtype));
		}	
		return lstRes;
	}	
	
	@Override
	public List<ExceptionInfo> getStateMessages() {
		return stateMessages;
	}
	
	@Override
	public void clearStateMessages() {
		stateMessages.clear();
	}

	@Override
	public StatelessKieSession newCreditCollectorSession() {
		return newSession(RulesKeys.SESSION_CREDIT_COLLETOR);
	}

	@Override
	public StatelessKieSession newSession(String kbaseName) {
		KieBase kbase = kieContainer.getKieBase(kbaseName);
		if (kbase == null) {
			return null;
		}
		
		StatelessKieSession ks = kbase.newStatelessKieSession();
		return ks;
	}
	
	@Override
	public StatelessKieSession newReturnSession() {
		return newSession(RulesKeys.SESSION_RETURN);
	}
	
	@Override
	public StatelessKieSession newWatchedFieldsSession() {
		return newSession(RulesKeys.SESSION_WATCHED_FIELDS);
	}
	
	@Override
	public StatelessKieSession newCreditCalcSession() {
		return newSession(RulesKeys.SESSION_CREDIT_CALC);
	}	
	
	@Override
	public StatelessKieSession newLongdaysCalcSession() {
		return newSession(RulesKeys.SESSION_LONG_DAYS_CALC);
	}	
	
	@Override
	public StatelessKieSession newBonusCalcSession() {
		return newSession(RulesKeys.SESSION_BONUS_CALC);
	}	
	
	@Override
	public StatelessKieSession newCreditCalcParamsSession() {
		return newSession(RulesKeys.SESSION_CREDIT_PARAMS);
	}	
	
	@Override
	public StatelessKieSession newProcessActionsSession(String processDefKey, String pluginName, String name) {
		String kbaseName = "kbPA" + processDefKey + "_" + StringUtils.defaultString(pluginName, "").trim() + "_" + name;
		return newSession(kbaseName);
	}	
	
	@Override
	public StatelessKieSession newObjectActionsSession(String businessObjectClass, String signalRef) {
		String kbaseName = "kbOA" + businessObjectClass.replace('.', '_') + "_" + signalRef;
		return newSession(kbaseName );
	}	
	
	@Override
	public StatelessKieSession newObjectActionsSessionStart(String businessObjectClass, String signalRef) {
		String kbaseName = "kbOAS" + businessObjectClass.replace('.', '_') + "_" + signalRef;
		return newSession(kbaseName );
	}		
	
	@Override
	public void recompileRule(AIRule rule) {
		stateMessages.clear();
		
		KieResources kieResources = kieServices.getResources();
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();		
		
		String kbaseName = rule.getKBase();
		KieBaseModel kbaseModel = kieModuleModel.getKieBaseModels().get(kbaseName);
		
		String spkg = rule.getPackageName();
		kbaseModel.removePackage(spkg);	
		
		reloadRule(kieResources,kieFileSystem, rule.getEntity());
		
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();
		Results results = kieBuilder.getResults();
		if (results.hasMessages(Message.Level.ERROR)) {
			// критические ошибки при инициализации
			List<Message> lstm = results.getMessages(Message.Level.ERROR);
			for (Message msg: lstm) {
				stateMessages.add(new ExceptionInfo(0, msg.getText(), Type.BUSINESS, ResultType.FATAL));
			}
		} else if (results.hasMessages(Message.Level.WARNING)) { 
			
		}		
		kbaseModel.addPackage(spkg);
	}
	
	@Override
	public void recompileRule(BizAction act) {
		stateMessages.clear();
		
		KieResources kieResources = kieServices.getResources();
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		
		String kbaseName = getKBModelName(act);
		KieBaseModel kbaseModel = kieModuleModel.getKieBaseModels().get(kbaseName);
		
		String spkg = act.getRulePackageName();
		kbaseModel.removePackage(spkg);
		
		reloadObjectAction(kieResources, kieFileSystem, act);
		
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();
		Results results = kieBuilder.getResults();
		if (results.hasMessages(Message.Level.ERROR)) {
			// критические ошибки при инициализации
			List<Message> lstm = results.getMessages(Message.Level.ERROR);
			for (Message msg: lstm) {
				stateMessages.add(new ExceptionInfo(0, msg.getText(), Type.BUSINESS, ResultType.FATAL));
			}
		} else if (results.hasMessages(Message.Level.WARNING)) { 
			
		}		
		kbaseModel.addPackage(spkg);
		// TODO
	}

	@Override
	public void actionChangedEvent(@Observes @ABusinessEvent(className = "ru.simplgroupp.transfer.BizAction", eventCode = EventCode.BIZACTION_CHANGED) BusinessEvent event) {
		BizActionEntity ent = null;
		BizAction act = null;
		if (event.getBusinessObject() == null) {
			act = (BizAction) event.getBusinessObject();
			ent = act.getEntity();
		} else {
			ent = bizDAO.getBizActionEntity(((Number) event.getBusinessObjectId()).intValue());
			act = new BizAction(ent);
		}
		
		// перекомпилить правило
		recompileRule(act);
	}	
}
