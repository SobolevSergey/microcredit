package ru.simplgroupp.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ActionRuntimeException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.BizActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.RuleSetBeanLocal;
import ru.simplgroupp.interfaces.StartCheckBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.toolkit.ejb.EJBUtil;

import java.util.List;
import java.util.logging.Logger;


@Singleton
@Startup

//@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class StartBean {

	@Inject Logger log;
	

	@EJB
	ActionProcessorBeanLocal actionProcessor;
	
	@EJB
	RuleSetBeanLocal ruleSet;
	
	@EJB
	WorkflowEngineBeanLocal wfEngine;
	
	@EJB
	WorkflowBeanLocal wfBean;
	
	@EJB
	BizActionProcessorBeanLocal bizProc;
	
	@EJB
	BizActionTimerBean bizTimer;



//	@EJB(mappedName="java:global/microdep/StartCheckBean!ru.simplgroupp.interfaces.StartCheckBeanLocal")
	StartCheckBeanLocal checkBean;


	@PostConstruct
    public void onStartup() {
		checkBean = (StartCheckBeanLocal) EJBUtil.findEJB("java:global/microdep/StartCheckBean!ru.simplgroupp.interfaces.StartCheckBeanLocal");
		if (checkBean == null) {
			return;
		}

		if (!checkBean.isOk()) {
			log.severe("Приложение microcredit не может запуститься по причинам: ");
			List<ExceptionInfo> lst = checkBean.getStatusMessages();
			for (ExceptionInfo info: lst) {
				if (ResultType.FATAL.equals(info.getResultType())) {
					log.severe(info.getMessage());
				} else {
					log.warning(info.toString());
				}
			}
			throw new RuntimeException("Приложение microcredit не может запуститься из-за неготовности БД или процессов");
		}

		log.info("Проинициализировали ActionProcessorBean");
        
        actionProcessor.dummy();        
        wfEngine.dummy();
        wfBean.dummy();
        ActionContext actCtx = actionProcessor.createActionContext(null, true);
        
        List<ExceptionInfo> lst = ruleSet.getStateMessages();
        ruleSet.clearStateMessages();        
        log.info("Проинициализировали RuleSetBean");
        if (lst.size() > 0) {
        	throw new RuntimeException("Критическая ошибка при инициализации RuleSetBean");
        }
        
        log.info("Запускаем стандартные действия");

        try {
			bizProc.startOrFindAllManyActions();
			bizTimer.startBizTimers();
		} catch (ActionRuntimeException e) {
			throw new RuntimeException("Критическая ошибка при запуске действий", e);
		} catch (ActionException e) {
			throw new RuntimeException("Критическая ошибка при запуске действий", e);
		}


    }
	
	 @PreDestroy
     void onShutdown() 
	 { 
		 log.info("Тормозим стандартные действия");
		 // TODO
	 }
}
