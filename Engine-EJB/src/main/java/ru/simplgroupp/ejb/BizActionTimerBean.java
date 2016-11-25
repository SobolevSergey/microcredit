package ru.simplgroupp.ejb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.interfaces.ApplicationEventListener;
import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectRef;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.WorkflowEngineBeanLocal;
import ru.simplgroupp.interfaces.service.AppServiceBeanLocal;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.annotation.ABusinessEvent;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.EventKeys;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;

@Singleton
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class BizActionTimerBean implements ApplicationEventListener {
	
	@Inject Logger logger;
	
    @Resource
    TimerService timerService;
    
	@EJB
	BizActionBeanLocal bizBean;	
	
	@EJB
	WorkflowEngineBeanLocal wfEngine;
	
	@EJB
	ServiceBeanLocal servBean;
	
	@EJB
	BizActionDAO bizDAO;
	
	@EJB
	AppServiceBeanLocal appServ;
	
    @Resource
	UserTransaction tx;	
    
    HashMap<String,Date> runningAction = new HashMap<String,Date>(50);
    
    public void init() {
    	servBean.subscribe(EventKeys.EVENT_BIZACT_COMPLETED, this);
    }
    
	@PreDestroy
	public void destroy() {
		servBean.unsubscribe(EventKeys.EVENT_BIZACT_COMPLETED, this);
	}    
    
	public void startBizTimers() {
		List<BizActionEntity> lst = bizDAO.listBOActions(null, null, true, null);
		for (BizActionEntity bizAct: lst) {
			if (bizAct.getIsActive() == ActiveStatus.ACTIVE && !StringUtils.isBlank(bizAct.getSchedule())) {
				createTimer(bizAct);
			}
		}		
	}
	
	private void removeTimer(String timerName) {
		for (Timer tmr: timerService.getTimers()) {
			if (timerName.equals(tmr.getInfo().toString())) {
				tmr.cancel();
			}
		}
	}
	
	public void updateBizTimer(BizActionEntity bizAct) {
		removeTimer(BusinessObjectRef.toString(BizAction.class.getName(), bizAct.getId()));
		
		if (bizAct.getIsActive() == ActiveStatus.ACTIVE && !StringUtils.isBlank(bizAct.getSchedule())) {
			createTimer(bizAct);
		}
	}
	
	private void createTimer(BizActionEntity bizAct) {
		TimerConfig cfg = new TimerConfig(BusinessObjectRef.toString(BizAction.class.getName(), bizAct.getId()), true);
		
		ScheduleExpression schedule = DatesUtils.parseFromCron(bizAct.getSchedule());

		Timer tmr = timerService.createCalendarTimer(schedule, cfg);
	}
	
	public BizActionEntity getBizActionEntity(int aid) {
		BizActionEntity ent = null;
		try {
			try {
				tx.begin();
				ent = bizDAO.getBizActionEntityLock(aid);
			} catch (Throwable ex) {
				tx.setRollbackOnly();
			} finally {
				tx.commit();
			}
		}
		catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			ent = null;
		}
		return ent;
	}
	
	public BizActionEntity saveBizActionEntity(BizActionEntity ent) {
		try {
			try {
				tx.begin();
				ent = bizDAO.saveBizActionEntity(ent);
			} catch (Throwable ex) {
				tx.setRollbackOnly();
			} finally {
				tx.commit();
			}
		}
		catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			ent = null;
		}
		return ent;
	}
	
	private void internalTimeout(Timer timer) {
    	Date dtNow = new Date();
    	BusinessObjectRef ref = BusinessObjectRef.valueOf(timer.getInfo().toString());
    	BizActionEntity ent = getBizActionEntity(ref.getIdAsInteger());
    	if (ent == null) {
    		return;
    	}
    	
    	if (ent.getLastStart() != null) {
    		// чтобы таймер не запускался каждую миллисекунду
    		long delta = dtNow.getTime() - ent.getLastStart().getTime();
    		if (delta < 10000) {
    			return;
    		}
    	}
    	
    	try {
        	ent.setLastStart(dtNow);
        	bizDAO.saveBizActionEntity(ent);    		
    	} catch ( Throwable e) {
			logger.log(Level.SEVERE, "Ошибка при вызове таймера " + timer.getInfo().toString(), e);
			dtNow = new Date();
        	ent.setLastEnd(dtNow);
        	ent.setErrorMessage(e.getMessage());
        	saveBizActionEntity(ent);
        	return;
		}
    	
        try {      	
			wfEngine.messageProcessByBusinessKey(ProcessKeys.DEF_ACT_STANDART, timer.getInfo().toString(), SignalRef.toString(ProcessKeys.DEF_ACT_STANDART, null, ProcessKeys.MSG_TIMER), null);
			
			dtNow = new Date();
        	ent.setLastEnd(dtNow);
        	bizDAO.saveBizActionEntity(ent);        	
//        	saveBizActionEntity(ent);			
		} catch ( Throwable e) {
			logger.log(Level.SEVERE, "Ошибка при вызове таймера " + timer.getInfo().toString(), e);
			dtNow = new Date();
        	ent.setLastEnd(dtNow);
        	ent.setErrorMessage(e.getMessage());
        	saveBizActionEntity(ent);				
		}		
	}
	
    @Timeout
    @Lock(LockType.READ)
    public void programmaticTimeout(Timer timer) {
    	synchronized(runningAction) {
    		Date dts = runningAction.get(timer.getInfo().toString());
    		Date dtNow = new Date();
    		if (dts != null && (dtNow.getTime() - dts.getTime()) < 10000 ) {
	        	logger.info("Таймер для " + timer.getInfo().toString() + " уже запущен");
	    		return;    			
    		}
	    	logger.info("Таймер для " + timer.getInfo().toString());
	    	runningAction.put(timer.getInfo().toString(), dtNow);
	    	
	    	/*ApplicationAction action = appServ.startApplicationAction(timer.getInfo().toString(), true, timer.getInfo().toString());
	    	if (action == null) {
	        	logger.info("Действие " + timer.getInfo().toString() + " уже выполняется");
	    		return;    	
	    	}
	    	
	    	logger.info("Выполняем действие " + timer.getInfo().toString());*/
	    	internalTimeout(timer);
	    	//logger.info("Действие " + timer.getInfo().toString() + " завершено");
    	}
   		
    }

	@Override
	@Lock(LockType.WRITE)
	public void firedEvent(String eventName, Map<String, Object> params, RuntimeServices runtimeServices) {
		if (EventKeys.EVENT_BIZACT_COMPLETED.equals(eventName)) {
			BusinessObjectRef ref = new BusinessObjectRef( (String) params.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS), params.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID));
			
    		synchronized(runningAction) { 
    			runningAction.remove(ref.toString());
    			//appServ.endApplicationAction(ref.toString());
    		}			
		}	
	}
	
	public void actionChangedEvent(@Observes @ABusinessEvent(className = "ru.simplgroupp.transfer.BizAction", eventCode = EventCode.BIZACTION_CHANGED) BusinessEvent event) {
		BizActionEntity ent = null;
		if (event.getBusinessObject() == null) {
			ent = ((BizAction) event.getBusinessObject()).getEntity();
		} else {
			ent = bizDAO.getBizActionEntity(((Number) event.getBusinessObjectId()).intValue());
		}
		updateBizTimer(ent);
	}
}
