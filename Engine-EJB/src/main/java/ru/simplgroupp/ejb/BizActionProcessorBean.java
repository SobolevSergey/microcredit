package ru.simplgroupp.ejb;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ActionRuntimeException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.interfaces.service.AppServiceBeanLocal;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.rules.BooleanRuleResult;
import ru.simplgroupp.rules.NameValueRuleResult;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.*;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;

@Stateless
@Local(BizActionProcessorBeanLocal.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class BizActionProcessorBean implements BizActionProcessorBeanLocal {
	
    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;	
	
	@EJB
	BizActionBeanLocal bizBean;	
	
	@EJB
	BizActionDAO bizDAO;
	
	@EJB
	WorkflowEngineBeanLocal wfEngine;	
	
    @EJB
    WorkflowBeanLocal wfBean;
    
    @EJB
    RulesBeanLocal rulesBean;
    
    @EJB
    ActionProcessorBeanLocal actionProcessor;
    
    @EJB
    AppServiceBeanLocal appServ;

    @EJB
    ProductBeanLocal productBean;
    
    @EJB
    ProductDAO productDAO;
    
    @Resource
	UserTransaction tx;

	@EJB
	PeopleDAO peopleDAO;
	
    protected List<BizActionEntity> bizActions=new ArrayList<BizActionEntity>(0);
    
    @Inject Logger logger;
	
    @Override 
	public void startOrFindAllManyActions() throws ActionException, ActionRuntimeException {
		List<BizActionEntity> lst = bizDAO.listBOActions(null, null, true, BizAction.SINGLETON_ACTION);
		for (BizActionEntity bizAct: lst) {
			if (BizActionUtils.needProcess(bizAct)) {
				startOrFindBizAction(bizAct);
			}
		}
	}
    
    @Override
    public void runNow(int bizActionId) throws ActionException {
    	BizActionEntity bizAct = bizDAO.getBizActionEntity(bizActionId);
    	//ищем процесс по бизнес-действию
		ProcessInstance pinst = wfEngine.findProcessByBusinessKey(ProcessKeys.DEF_ACT_STANDART, BizAction.class.getName() + ":" + bizAct.getId().toString());
		//если процесса нет, запустим его
		if (pinst == null ) {
			try {
				pinst = wfEngine.startProcessOptTx(ProcessKeys.DEF_ACT_STANDART, BizAction.class.getName() + ":" + bizAct.getId().toString() , Utils.<String, Object>mapOf(
						ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, BizAction.class.getName(),
						ProcessKeys.VAR_BUSINESS_OBJECT_ID, bizAct.getId()
				));
			} catch (WorkflowException e) {
				throw new ActionException("Ошибка при выполнении действия " + bizAct.getDescription(), e);
			}
		}		
		
		// сигналим в процесс о запуске
		try {
			wfBean.messageProcMAtTx(bizAct.getId(), "msgRunNow");
		} catch (WorkflowException e) {
			throw new ActionException("Ошибка при выполнении действия " + bizAct.getDescription(), e);
		}		
   	
    }
		
	public void startOrFindBizAction(BizActionEntity bizAct)  throws ActionException, ActionRuntimeException {
		//ищем процесс по бизнес-действию
		String bkey = BizActionUtils.getBusinessKey(bizAct, null);	
		
		ProcessInstance pinst = wfEngine.findProcessByBusinessKey(ProcessKeys.DEF_ACT_STANDART, bkey);
		//если действие для выполнения (активно)
		if (bizAct.getIsActive() == ActiveStatus.ACTIVE) {
			//если процесса нет, запустим
			if (pinst == null ) {
				try {
					pinst = wfEngine.startProcessOptTx(ProcessKeys.DEF_ACT_STANDART, bkey , Utils.<String, Object>mapOf(
							ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, BizAction.class.getName(),
							ProcessKeys.VAR_BUSINESS_OBJECT_ID, bizAct.getId()
					));
				} catch (WorkflowException e) {
					throw new ActionException("Ошибка при выполнении действия " + bizAct.getDescription(), e);
				}
			} else {
				// обновить параметры уже запущенного процесса
				try {
					wfBean.messageProcMAtTx(bizAct.getId(), "msgReload");
				} catch (WorkflowException e) {
					throw new ActionException("Ошибка при выполнении действия " + bizAct.getDescription(), e);
				}
			}
		} else {
			if (pinst != null) {
				// остановить процесс
				try {
					wfBean.messageProcMAtTx(bizAct.getId(), "msgStop");
				} catch (WorkflowException e) {
					throw new ActionException("Ошибка при выполнении действия " + bizAct.getDescription(), e);
				}
			}
		}		
	}	
	
	@Override
	public void startOrFindBizAction(BizActionEntity bizAct, Object businessObjectId, AbstractContext externalContext) throws ActionException {
		if (bizAct.getIsActive() != ActiveStatus.ACTIVE) {
			return;
		}
		
		if (BizActionUtils.needProcess(bizAct)) {
			// TODO			
			//ищем процесс по бизнес-действию
			String bkey = BizActionUtils.getBusinessKey(bizAct, null);	
			
			ProcessInstance pinst = wfEngine.findProcessByBusinessKey(ProcessKeys.DEF_ACT_STANDART, bkey);

			//если процесса нет, запустим
			if (pinst == null ) {
				String bclass = null;
				Object bid = null;
				if (bizAct.getIsSingleton() == BizAction.SINGLETON_ACTION) {
					bclass = BizAction.class.getName();
					bid = bizAct.getId();
				} else {
					bclass = bizAct.getBusinessObjectClass();
					bid = businessObjectId;
				}
				Map<String, Object> mp = Utils.mapOfSO(
						ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, bclass,
						ProcessKeys.VAR_BUSINESS_OBJECT_ID, bid);
				if (externalContext != null && externalContext.getParams() != null && externalContext.getParams().size() > 0) {
					mp.putAll(externalContext.getParams());
				}
				try {
					pinst = wfEngine.startProcessOptTx(ProcessKeys.DEF_ACT_STANDART, bkey , mp);
				} catch (WorkflowException e) {
					throw new ActionException("Ошибка при выполнении действия " + bizAct.getDescription(), e);
				}
			} else {
				// обновить параметры уже запущенного процесса
				Map<String, Object> mp = null;
				if (externalContext != null && externalContext.getParams() != null && externalContext.getParams().size() > 0) {
					mp = new HashMap<String,Object>(5);
					mp.putAll(externalContext.getParams());
				}
				
				try {
					wfEngine.messageProcessByBusinessKey(ProcessKeys.DEF_ACT_STANDART, bkey, SignalRef.toString(ProcessKeys.DEF_ACT_STANDART, null, "msgReload"), mp);
				} catch (WorkflowException e) {
					throw new ActionException("Ошибка при выполнении действия " + bizAct.getDescription(), e);
				}
			}
			
		} else {
			executeBizAction(bizAct, businessObjectId, externalContext);
		}
	}
	
	@Override
	public ExceptionInfo executeBizAction(String businessObjectClass, String signalRef, String procDefKey, 
			String plugin, Integer businessObjectId, AbstractContext externalContext) throws ActionException {
		
		BizActionEntity bizAct = null;
		if (StringUtils.isBlank(procDefKey)) {
			bizAct = bizDAO.findBizObjectAction(businessObjectClass, signalRef);
		} else {
			bizAct = bizDAO.findProcessAction(businessObjectClass, procDefKey, plugin, signalRef);
		}
		
		SignalRef sig = new SignalRef(procDefKey, null, signalRef);
    	ApplicationAction action = appServ.startApplicationAction(
    			sig.toString(), true, bizAct.getDescription(),
    			new BusinessObjectRef(businessObjectClass, businessObjectId)
    			);
    	if (action == null) {
    		return new ExceptionInfo(0, bizAct.getDescription() + " уже выполняется", Type.BUSINESS, ResultType.NON_FATAL);
    	}     	
    	
    	try {
    		executeBizAction(bizAct, businessObjectId, externalContext);
    	} catch (ActionException ex) {
    		return ex.getInfo();
    	} catch (Throwable ex) {
    		return new ExceptionInfo(1, ex.getMessage(), Type.TECH, ResultType.FATAL);
    	} finally {
    		appServ.endApplicationAction(action);
    	}
    	return null;
	}
	
	@Override
	//@Asynchronous
    public void executeBizActionAsync(BizActionEntity bizAct, int listId, boolean bExclusive, AbstractContext externalContext) throws ActionException {
		executeBizAction(bizAct, listId, bExclusive, externalContext);
	}
	
	@Override
    public void executeBizAction(BizActionEntity bizAct, int listId, boolean bExclusive, AbstractContext externalContext) throws ActionException {
    	// конструктор для создания бизнес-объекта из entity
		Constructor<? extends BaseTransfer> cons = null;
		try {
			cons = AppUtil.findConstructor(bizAct.getBusinessObjectClass(), AppUtil.bizNameToEntityName(bizAct.getBusinessObjectClass()));
		} catch (Exception ex) {
			throw new ActionException(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
		}

    	ApplicationAction action = appServ.startApplicationAction(
    			bizAct.getSignalRef(), bExclusive, bizAct.getDescription(),
    			new BusinessObjectRef(bizAct.getBusinessObjectClass(), bizAct.getId()),
    			new BusinessObjectRef(BusinessList.class.getName(), new Integer(listId))
    			);
    	if (action == null) {
    		throw new ActionException(0, bizAct.getDescription() + " уже выполняется", Type.BUSINESS, ResultType.NON_FATAL, null);
    	}		
		
    	try {
			WorkflowObjectActionDef actionDef = new WorkflowObjectActionDef( StringUtils.isNotBlank( bizAct.getProcessDefKey() ));
			actionDef.setSignalRef(bizAct.getSignalRef());
			actionDef.setBusinessObjectClass(bizAct.getBusinessObjectClass());
			
			if (bizAct.getIsAtomic() == 1) {
				try {				
					tx.begin();
					executeAtomicBizActionMany(bizAct, cons, actionDef, listId, externalContext);
					tx.commit();
				} catch (RuntimeException ex) {
					try {
						tx.rollback();
					} catch (RuntimeException e) {
						throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
					} catch (SystemException e) {
						throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
					} 
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} catch (ActionException ex) {
					try {
						tx.rollback();
					} catch (RuntimeException e) {
						throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
					} catch (SystemException e) {
						throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
					} 
					throw ex;
				} catch (Exception ex) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				}
			} else {
				executeNonAtomicBizActionMany(bizAct, cons, actionDef, listId, externalContext);
	
			}	
    	} finally {
    		appServ.endApplicationAction(action);
    	}
		
    }
	
    @Override
    public void executeBizAction(BizActionEntity bizAct, Object businezzObjectId, AbstractContext externalContext) throws ActionException {
    	// конструктор для создания бизнес-объекта из entity
		Constructor<? extends BaseTransfer> cons = null;
		try {
			cons = AppUtil.findConstructor(bizAct.getBusinessObjectClass(), AppUtil.bizNameToEntityName(bizAct.getBusinessObjectClass()));
		} catch (Exception ex) {
			throw new ActionException(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
		}

		WorkflowObjectActionDef actionDef = new WorkflowObjectActionDef( StringUtils.isNotBlank( bizAct.getProcessDefKey() ));
		actionDef.setSignalRef(bizAct.getSignalRef());
		actionDef.setBusinessObjectClass(bizAct.getBusinessObjectClass());
		
		if (bizAct.getIsAtomic() == 1) {
			try {				
				tx.begin();
				executeAtomicBizAction(bizAct, cons, actionDef, businezzObjectId, externalContext);
				tx.commit();
			} catch (RuntimeException ex) {
				try {
					tx.rollback();
				} catch (RuntimeException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} catch (SystemException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} 
				throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
			} catch (ActionException ex) {
				try {
					tx.rollback();
				} catch (RuntimeException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} catch (SystemException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} 
				throw ex;
			} catch (Exception ex) {
				throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
			}
		} else {

			//проверяем, что в списке есть действие; нужно, чтобы действие не выполнялось много раз
			if (bizActions.contains(bizAct)){
				
				for (int i=0;i<bizActions.size();i++) {
				
					//если было действие с таймером
					if(bizActions.get(i).getLastStart()!=null){
				      if (!DatesUtils.isSameDay(bizActions.get(i).getLastStart(),new Date())){
					        logger.info("Удалили из списка bizAction "+bizActions.get(i).getDescription());  
					        bizActions.remove(bizActions.get(i));
	
				      } else if (bizActions.get(i).getForOne()!=null&&bizActions.get(i).getForOne()==1&&externalContext!=null&&!externalContext.isAutoRun()){
				    	  //запускали сегодня, но это одиночное действие, надо повторить
				    	  logger.info("Удалили из списка одиночное действие с таймером, запускали сегодня "+bizActions.get(i).getDescription());  
				    	  bizActions.remove(bizActions.get(i));
				      }
					} else {
						//если мы делаем действие по одному объекту, то удаляем в любом случае
						if (bizActions.get(i).getForOne()==1){
							logger.info("Удалили из списка bizAction "+bizActions.get(i).getDescription());  
							bizActions.remove(bizActions.get(i));
						}
					}
				    if (bizActions.size()==0){
				    	logger.severe("нет действий в списке!");
				       	break;
				    }
				}
			}
	
			//выполняем действие
			if (!bizActions.contains(bizAct)){
				logger.info("Выполняем бизнес-действие " + bizAct.getDescription());
			    executeNonAtomicBizAction(bizAct, cons, actionDef, businezzObjectId, externalContext);
			}
				
		}		
    }
    
	public void executeAtomicBizActionMany (
			BizActionEntity bizAct, 
			Constructor<? extends BaseTransfer> cons, 
			WorkflowObjectActionDef actionDef,
			int listId,
			AbstractContext externalContext) throws ActionException, ActionRuntimeException {
		
		String hql = null;
		Map<String, Object> params = Utils.mapOf( "currentDate", new Date());
		if (bizAct.getForMany() == 1) {
			hql = bizBean.buildHQL(bizAct, listId);
		} else {
			hql = bizBean.buildHQLOne(bizAct, listId);
// TODO			params.put("businessObjectId", businezzObjectId);
		}
		
		Set initOptions = AppUtil.getDefaultInitOptions(bizAct.getBusinessObjectClass());
		try {
			Query qry = emMicro.createQuery(hql);
			if (params != null) {
				Set<Parameter<?>> qprms = qry.getParameters();
				for (Parameter<?> prm: qprms) {
					qry.setParameter(prm.getName(), params.get(prm.getName()));
				}
			}
			List lstEntity = qry.getResultList();
			for (Object entity: lstEntity) {					
				Object biz = AppUtil.createBizObject(cons, entity);
				if (biz instanceof Initializable) {
					((Initializable) biz).init(initOptions);
				}
				emMicro.detach(entity);
				if (entity instanceof Identifiable) {
					executeOneBizAction(biz, bizAct, actionDef, externalContext, ((Identifiable) entity).getId(), false);
				}
			}
			
		} catch (ActionException ex) {
			throw new ActionRuntimeException(ex.getInfo().getCode(), ex.getInfo().getMessage(), ex.getInfo().getType(), ex.getInfo().getResultType(), ex);
		} catch (Exception ex) {
			throw new ActionRuntimeException(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
		}

	}     
    
	public void executeAtomicBizAction(
			BizActionEntity bizAct, 
			Constructor<? extends BaseTransfer> cons, 
			WorkflowObjectActionDef actionDef,
			Object businezzObjectId,
			AbstractContext externalContext) throws ActionException, ActionRuntimeException {
		
		String hql = null;
		Map<String, Object> params = Utils.mapOf( "currentDate", new Date());
		if (businezzObjectId == null) {
			hql = bizBean.buildHQL(bizAct);
		} else {
			hql = bizBean.buildHQLOne(bizAct);
			params.put("businessObjectId", businezzObjectId);
		}
		
		Set initOptions = AppUtil.getDefaultInitOptions(bizAct.getBusinessObjectClass());
		try {
			Query qry = emMicro.createQuery(hql);
			if (params != null) {
				Set<Parameter<?>> qprms = qry.getParameters();
				for (Parameter<?> prm: qprms) {
					qry.setParameter(prm.getName(), params.get(prm.getName()));
				}
			}
			List lstEntity = qry.getResultList();
			for (Object entity: lstEntity) {					
				Object biz = AppUtil.createBizObject(cons, entity);
				if (biz instanceof Initializable) {
					((Initializable) biz).init(initOptions);
				}
				emMicro.detach(entity);
				executeOneBizAction(biz, bizAct, actionDef, externalContext, businezzObjectId, false);
			}
			
		} catch (ActionException ex) {
			throw new ActionRuntimeException(ex.getInfo().getCode(), ex.getInfo().getMessage(), ex.getInfo().getType(), ex.getInfo().getResultType(), ex);
		} catch (Exception ex) {
			throw new ActionRuntimeException(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
		}

	}   
	
	/**
	 * делаем список, соответствующий условиям для выполнения бизнес-действия
	 * 
	 * @param hql - условие для запроса
	 * @param params - параметры
	 * @param cons - конструктор для класса бизнес-объекта 
	 * @param initOptions - что инициализируем
	 * @return
	 * @throws ActionException
	 */
	protected List listAndInit(String hql, Map<String, Object> params, Constructor<? extends BaseTransfer> cons, Set initOptions ) throws ActionException {
		
		logger.info("Начали проверку на выполнение действий");
		
		Query qry = emMicro.createQuery(hql);
		if (params != null) {
			Set<Parameter<?>> qprms = qry.getParameters();
			for (Parameter<?> prm: qprms) {
				qry.setParameter(prm.getName(), params.get(prm.getName()));
			}
		}
		
		try {
			List lstEntity = qry.getResultList();
			List lstRes = new ArrayList(lstEntity.size());
			for (Object entity: lstEntity) {					
				Object biz = AppUtil.createBizObject(cons, entity);
				if (biz instanceof Initializable) {
					((Initializable) biz).init(initOptions);
				}
				lstRes.add(biz);
			}
			logger.info("Закончили проверку на выполнение действий");
			return lstRes;
		} catch (Exception ex) {
			logger.severe("Не удалось провести проверку на выполнение действий");
			throw new ActionException(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
		}
	}	
	
	/**
	 * выполняем одну итерацию бизнес-действия
	 * 
	 * @param biz
	 * @param bizAct - бизнес-действие
	 * @param actionDef
	 * @param externalContext - контекст, описывающий условия, в которых происходит вызов 
	 * @param businezzObjectId - id бизнес-объекта
	 * @param bStartOnly
	 * @return
	 * @throws Exception
	 */
	protected Map<String,Object> executeOneBizAction(Object biz, BizActionEntity bizAct, WorkflowObjectActionDef actionDef, AbstractContext externalContext, Object businezzObjectId, boolean bStartOnly) throws Exception {
		AbstractContext ctx = AppUtil.getDefaultContext(biz, bizAct.getBusinessObjectClass());
		//если есть внешний контекст, добавим из него данные
		if (externalContext != null) {
			ctx.setCurrentUser(externalContext.getCurrentUser());
			ctx.setAutoRun(externalContext.isAutoRun());
			ctx.getParams().putAll(externalContext.getParams());
			if (externalContext.getCredit()!=null){
			    ctx.setCredit(externalContext.getCredit());
			}
		}
		logger.info("Попали в executeOneBizAction ");
		Map<String,Object>  limits = null;
	
		if (! StringUtils.isBlank(bizAct.getRuleEnabled())) {
			//добавляем константы для разных действий
			if (ctx.getCredit()!=null){
				limits=productBean.getAllProductConfig(ctx.getCredit().getProduct().getId());
			} else {
				if (productDAO.getProductDefault()!=null){
					limits=productBean.getAllProductConfig(productDAO.getProductDefault().getId());
 			    }
			}
			ctx.getParams().putAll(limits);

			BooleanRuleResult bres = rulesBean.getObjectActionEnabled(actionDef, ctx);
			if (bres != null && !bres.getResultValue()) {
				return null;
			}
		}
		
		if (StringUtils.isBlank(bizAct.getRuleAction()) && bizAct.getBizActionType().getId() == BizActionType.TYPE_APPLICATION) {
			return null;
		}
		
		//добавляем константы для рассчета разных действий, если их еще нет
		Integer productId=null;
		if (limits == null) {
			if (ctx.getCredit()!=null){
				productId=ctx.getCredit().getProduct().getId();
				limits=productBean.getAllProductConfig(productId);
			} else {
				if (productDAO.getProductDefault()!=null){
					limits=productBean.getAllProductConfig(productDAO.getProductDefault().getId());
				}
			}
			ctx.getParams().putAll(limits);
		}
		
		
		if (bStartOnly) {
			NameValueRuleResult eres = rulesBean.executeObjectActionStart(actionDef, actionProcessor, ctx);
			return eres.getVariables();
		}
		
		NameValueRuleResult eres = rulesBean.executeObjectAction(actionDef, actionProcessor, ctx);

		switch (bizAct.getBizActionType().getId()) {
			// запустить бизнес-процесс
			case BizActionType.TYPE_PROCESS: {				
				Map<String,Object> params = new HashMap<String, Object>(5);
				params.putAll(eres.getVariables());
				params.putAll(ctx.getParams());
				if (params.containsKey(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS)) {
					actionDef.setBusinessObjectClass(params.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS).toString());
					businezzObjectId = params.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID);
				}
				if (StringUtils.isNotBlank(bizAct.getRunProcessDefKey())) {
					actionDef.setRunProcessDefKey(bizAct.getRunProcessDefKey());
				}				
				wfBean.goProc(bizAct.getBusinessObjectClass(), businezzObjectId, actionDef, null, params);
			};break;
		    //рассылка писем
			case BizActionType.TYPE_SEND_EMAIL: {
				
				logger.info("Попали в таймер по рассылке email ");
				String formattedTextSubject = null, formattedTextBody = null;

				if (bizAct.getParams().isEmpty()) {
					ProductMessagesEntity message=productBean.getMessageForBusinessAction(productId, bizAct.getSignalRef());
					if (message!=null) {
						logger.info("Найден шаблон письма");
						formattedTextSubject = message.getSubject();
						formattedTextBody = message.getBody();
					}									
				} else {
					formattedTextSubject = (String) ctx.getParams().get(BizActionKeys.PREFIX_PARAM_CONTEXT + "subject");
					formattedTextBody = (String) ctx.getParams().get(BizActionKeys.PREFIX_PARAM_CONTEXT + "body");
				}
				
				if (StringUtils.isBlank(formattedTextSubject) && StringUtils.isBlank(formattedTextBody)) {
					logger.severe("Не найден шаблон письма");
				} else {
					logger.info("Попали в рассылку email " + ((ctx.getCredit() == null) ? "" : ctx.getCredit().getId()));
					logger.info("Отправляем письмо");
					PeopleMain peopleMain = peopleDAO.getPeopleMain(ctx.getClient().getId(), Utils.setOf(
							PeopleMain.Options.INIT_PEOPLE_PERSONAL
					));
					//стандартные параметры
					Object[] params = new Object[] {
							//ФИО
							peopleMain.getPeoplePersonalActive().getFullName(),
							//номер договора
							ctx.getCredit().getCreditAccount(),
							//сумма к возврату
							CalcUtils.dformat_XX.format(ctx.getCredit().getCreditSumBack()),
							//сумма основного долга
							CalcUtils.dformat_XX.format(ctx.getCredit().getSumMainRemain()),
							//сумма процентов
							CalcUtils.dformat_XX.format((ctx.getCredit().getCreditSumBack() - ctx.getCredit().getSumMainRemain())),
							//текущая дата
						 	DatesUtils.DATE_FORMAT_ddMMYYYY.format(ctx.getCurrentDate())
					};

					actionProcessor.sendEmailCommon(ctx.getClient().getId(), formattedTextSubject, formattedTextBody, params);
					logger.info("Письмо отправлено");
				}
			};break;
			//рассылка смс
			case BizActionType.TYPE_SEND_SMS: {
			
				logger.info("Попали в таймер по рассылке sms ");
				String formattedTextBody = null;
				Object[] params = new Object[] {};
				
				if (bizAct.getParams().isEmpty()) {
					ProductMessagesEntity message=productBean.getMessageForBusinessAction(productId, bizAct.getSignalRef());
					if (message!=null) {
						logger.info("Найден шаблон СМС");
						formattedTextBody = message.getBody();
					}
				} else {
					formattedTextBody = (String) ctx.getParams().get(BizActionKeys.PREFIX_PARAM_CONTEXT + "body");
				}

				if (StringUtils.isBlank(formattedTextBody)) {
					logger.severe("Не найден шаблон смс");
				} else {
					logger.info("Попали в рассылку sms "+((ctx.getCredit() == null)?"":ctx.getCredit().getId()) );
					logger.info("Отправляем СМС");
					actionProcessor.sendSMSCommon(ctx.getClient().getId(), formattedTextBody, params);					
					logger.severe("СМС отправлено");
				}				
			};break;			
		}
		return null;
	}	
	
	@Override
	public boolean canExecute(Object biz, BizActionEntity bizAct, AbstractContext externalContext) {
		WorkflowObjectActionDef actionDef = new WorkflowObjectActionDef( StringUtils.isNotBlank( bizAct.getProcessDefKey() ));
		actionDef.setSignalRef(bizAct.getSignalRef());
		actionDef.setBusinessObjectClass(bizAct.getBusinessObjectClass());		
		
		AbstractContext ctx = AppUtil.getDefaultContext(biz, bizAct.getBusinessObjectClass());
		if (externalContext != null) {
			ctx.setCurrentUser(externalContext.getCurrentUser());
			ctx.getParams().putAll(externalContext.getParams());
		}
		logger.info("Попали в executeOneBizAction ");
		Map<String,Object>  limits = null;
		if (! StringUtils.isBlank(bizAct.getRuleEnabled())) {
			if (ctx.getCredit()!=null){
				limits=productBean.getOverdueProductConfig(ctx.getCredit().getProduct().getId());
			} else {
				if (productDAO.getProductDefault()!=null){
					limits=productBean.getOverdueProductConfig(productDAO.getProductDefault().getId());
				}
			}
			ctx.getParams().putAll(limits);			
			BooleanRuleResult bres = rulesBean.getObjectActionEnabled(actionDef, ctx);
			if (bres != null && !bres.getResultValue()) {
				return false;
			}
		}
		return true;
	}
	
	public void executeNonAtomicBizActionMany(
			BizActionEntity bizAct, 
			Constructor<? extends BaseTransfer> cons, 
			WorkflowObjectActionDef actionDef,
			int listId,
			AbstractContext externalContext) throws ActionException {
		
		String hql = null;
		Map<String, Object> params = Utils.mapOf( "currentDate", new Date(), "prmListId", listId);
		if (bizAct.getForMany() == 1) {
			hql = bizBean.buildHQL(bizAct, listId);
		} else {
			hql = bizBean.buildHQLOne(bizAct, listId);
// TODO ?			params.put("businessObjectId", businezzObjectId);
		}
		Set initOptions = AppUtil.getDefaultInitOptions(bizAct.getBusinessObjectClass());
		
		int nHandled = 0, nAll = 0;
		ExceptionInfo exInfo = new ExceptionInfo(0, null, Type.BUSINESS, null);
		
		try {
			List lstBiz = null;
			try {
				tx.begin();
				lstBiz = listAndInit(hql, params, cons, initOptions);
				tx.commit();
			} catch (RuntimeException ex) {
				try {
					tx.rollback();
				} catch (RuntimeException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} catch (SystemException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} 
				throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
			} catch (ActionException ex) {
				try {
					tx.rollback();
				} catch (RuntimeException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} catch (SystemException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} 
				throw ex;
			} catch (Exception ex) {
				throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
			}
			
			nAll = lstBiz.size();
			logger.info("Всего найдено "+nAll+"записей по действию "+bizAct.getDescription());
			for (Object biz: lstBiz) {
				if (! (biz instanceof Identifiable)) {
					continue;
				}
				
				ExceptionInfo ex = executeOneBizActionTx(biz, bizAct, actionDef, externalContext, ((Identifiable) biz).getId(), false);
				if (ex == null) {
					nHandled++;
					logger.info("Выполнено "+nHandled+"записей по действию "+bizAct.getDescription());
				} else {
					if (ex.getResultType().equals(ResultType.FATAL)) {
						exInfo.setResultType(ResultType.FATAL);
					}					
				}
			}
			
			if (nHandled==nAll){
				bizActions.add(bizAct);
			}
			
			logger.info("Закончили работу по действию "+bizAct.getDescription());
			
		} catch (ActionException ex) {
			throw ex;				
		} catch (Exception ex) {
			throw new ActionException(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
		}
		
		if (nHandled < nAll) {
			throw new ActionException(ErrorKeys.ACTION_GENERAL_ERROR, "Не все объекты обработаны правильно. Должно было: " + nAll + " , обработано правильно: " + nHandled, exInfo.getType(), exInfo.getResultType(), null);
		}
	}	
	
	public void executeNonAtomicBizAction(
			BizActionEntity bizAct, 
			Constructor<? extends BaseTransfer> cons, 
			WorkflowObjectActionDef actionDef,
			Object businezzObjectId,
			AbstractContext externalContext) throws ActionException {
		
		String hql = null;
		Map<String, Object> params = Utils.mapOf( "currentDate", new Date());
		if (businezzObjectId == null) {
			hql = bizBean.buildHQL(bizAct);
		} else {
			hql = bizBean.buildHQLOne(bizAct);
			params.put("businessObjectId", businezzObjectId);
		}
		Set initOptions = AppUtil.getDefaultInitOptions(bizAct.getBusinessObjectClass());
		
		int nHandled = 0, nAll = 0;
		ExceptionInfo exInfo = new ExceptionInfo(0, null, Type.BUSINESS, null);
		
		try {
			List lstBiz = null;
			try {
				tx.begin();
				lstBiz = listAndInit(hql, params, cons, initOptions);
				tx.commit();
			} catch (RuntimeException ex) {
				try {
					tx.rollback();
				} catch (RuntimeException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} catch (SystemException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} 
				throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
			} catch (ActionException ex) {
				try {
					tx.rollback();
				} catch (RuntimeException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} catch (SystemException e) {
					throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
				} 
				throw ex;
			} catch (Exception ex) {
				throw new ActionException(0, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
			}
			
			nAll = lstBiz.size();
			logger.info("Всего найдено "+nAll+" записей по действию "+bizAct.getDescription());
			for (Object biz: lstBiz) {
				ExceptionInfo ex = executeOneBizActionTx(biz, bizAct, actionDef, externalContext, businezzObjectId, false);
				if (ex == null) {
					nHandled++;
					logger.info("Выполнено "+nHandled+"записей по действию "+bizAct.getDescription());
				} else {
					if (ex.getResultType().equals(ResultType.FATAL)) {
						exInfo.setResultType(ResultType.FATAL);
					}					
				}
			}
			
			if (nHandled==nAll){
				bizActions.add(bizAct);
			}
			
			logger.info("Закончили работу по действию "+bizAct.getDescription());
			
		} catch (ActionException ex) {
			throw ex;				
		} catch (Exception ex) {
			throw new ActionException(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL, ex);
		}
		
		if (nHandled < nAll) {
			throw new ActionException(ErrorKeys.ACTION_GENERAL_ERROR, "Не все объекты обработаны правильно. Должно было: " + nAll + " , обработано правильно: " + nHandled, exInfo.getType(), exInfo.getResultType(), null);
		}
	}
	
	@Override
	public Map<String,Object> executeBizAction(Identifiable biz, BizActionEntity bizAct, AbstractContext externalContext, boolean bStartOnly) 
			throws ActionException, ActionRuntimeException {
		
		WorkflowObjectActionDef actionDef = new WorkflowObjectActionDef( StringUtils.isNotBlank( bizAct.getProcessDefKey() ));
		actionDef.setSignalRef(bizAct.getSignalRef());
		actionDef.setBusinessObjectClass(bizAct.getBusinessObjectClass());	
		
		return executeOneBizActionTx(biz, bizAct, actionDef, externalContext, bStartOnly);
	}
	
	public ExceptionInfo executeOneBizActionTx(Object biz, BizActionEntity bizAct, WorkflowObjectActionDef actionDef, AbstractContext externalContext, Object businezzObjectId, boolean bStartOnly) 
			throws ActionException, ActionRuntimeException {
		ExceptionInfo res = null;
		try {
			try {
				tx.begin();
				executeOneBizAction(biz, bizAct, actionDef, externalContext, businezzObjectId, bStartOnly);
			} catch (Throwable ex) {
				tx.setRollbackOnly();
				if (ex instanceof ActionException) {
					res = ((ActionException) ex).getInfo();
				} else if (ex instanceof ActionRuntimeException) {
					res = ((ActionRuntimeException) ex).getInfo();
				} else {
					res = new ExceptionInfo(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL);
				}
			} finally {
				tx.commit();
			}
		} catch (Exception ex) {
			res = new ExceptionInfo(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL);
		}
		return res;
	}
	
	/**
	 * Выполняем действие над индивидуальным объектом внутри транзакции. Транзакция открывается в начале и закрыватеся в конце
	 * 
	 * @param biz 
	 * @param bizAct - бизнес-действие
	 * @param actionDef 
	 * @param externalContext - контекст, описывающий условия, в которых происходит вызов 
	 * @param bStartOnly
	 * @return
	 * @throws ActionException
	 * @throws ActionRuntimeException
	 */
	public Map<String,Object> executeOneBizActionTx(Identifiable biz, BizActionEntity bizAct, WorkflowObjectActionDef actionDef, AbstractContext externalContext, boolean bStartOnly) 
			throws ActionException, ActionRuntimeException {
		try {
			try {
				tx.begin();
				Map<String,Object> res = executeOneBizAction(biz, bizAct, actionDef, externalContext, biz.getId(), bStartOnly);
				return res;
			} catch (Throwable ex) {
				tx.setRollbackOnly();
				if (ex instanceof ActionException) {
					throw (ActionException) ex;
				} else if (ex instanceof ActionRuntimeException) {
					throw ((ActionRuntimeException) ex);
				} else {
					throw new ActionException(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL, ex );
				}
			} finally {
				tx.commit();
			}
		} catch (Exception ex) {
			throw new ActionException(ErrorKeys.ACTION_GENERAL_ERROR, ex.getMessage(), Type.TECH, ResultType.FATAL, ex );
		}		
	}	

	
}
