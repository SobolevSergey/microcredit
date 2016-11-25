package ru.simplgroupp.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.script.ScriptEngineManager;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.persistence.BizActionEventEntity;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.ApplicationEventListener;
import ru.simplgroupp.interfaces.BizActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectRef;
import ru.simplgroupp.interfaces.Closeable;
import ru.simplgroupp.interfaces.QuestionBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.ssl.SSLContextFactory;
import ru.simplgroupp.transfer.AIModel;
import ru.simplgroupp.transfer.CreditStatus;
import ru.simplgroupp.transfer.QuestionAnswer;
import ru.simplgroupp.transfer.RefSummary;
import ru.simplgroupp.transfer.RefuseReason;
import ru.simplgroupp.util.AppUtil;
import ru.simplgroupp.util.DecisionKeys;
import ru.simplgroupp.util.EventKeys;
import ru.simplgroupp.util.MimeTypeKeys;

@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ServiceBeanLocal.class)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
public class ServiceBean implements ServiceBeanLocal {
	
	@Inject Logger log;
	
	private Map<String, List<ApplicationEventListener>> listeners = new HashMap<String, List<ApplicationEventListener>>(1);
	
	private Map<String,CachedHolder> stateCache = new HashMap<String, CachedHolder>(20);
	
	private List<ActionContext> actionContexts = new ArrayList<ActionContext>(5);
	private int actionContextsMax = 5;
	
	private GenericKeyedObjectPool<String[],SSLContext> sslPool;
	
	private Map<Integer, ApplicationAction> runningActions = new HashMap<Integer, ApplicationAction>(20);
	
	private Map<Integer, List<BizActionEntity>> mapEvents = new HashMap<>(20);
  
    private ScriptEngineManager scriptFactory;
    
    private ExpressionFactory elFactory;
    
    @Resource
    private SessionContext sessionCtx;

	@EJB
	RulesBeanLocal rulesBean;
	
	@EJB
	WorkflowBeanLocal wfBean;
	
	@EJB
	QuestionBeanLocal questBean;
	
	@EJB
	ICryptoService cryptoServ;
	
    @EJB
    ReferenceBooksLocal refBooks;	
    
    @EJB
    BizActionDAO bizDAO;
    
    @EJB
    BizActionProcessorBeanLocal bizProc;
	
	@PostConstruct
	public void init() {
        scriptFactory = new ScriptEngineManager();
		
		SSLContextFactory factory = new SSLContextFactory(cryptoServ);
		sslPool = new GenericKeyedObjectPool<String[],SSLContext>(factory);
		sslPool.setMaxTotal(10);
		sslPool.setMaxIdle(7);
		reloadMapActions();
		
		elFactory = new ExpressionFactoryImpl();
	}
	
	private void reloadMapActions() {
		List<BizActionEventEntity> lst = bizDAO.listBizActionEvents();
		
		mapEvents.clear();
		Integer currentEventCode = -1;
		for (BizActionEventEntity evt: lst) {
			List<BizActionEntity> lstCur = null;
			if (! evt.getEventCode().equals(currentEventCode)) {
				lstCur = new ArrayList<BizActionEntity>(3);				
				currentEventCode = evt.getEventCode();
				mapEvents.put(currentEventCode, lstCur);
			} else {
				lstCur = mapEvents.get(currentEventCode);
			}
			lstCur.add(evt.getBizAction());
		}
	
	}

	@PreDestroy
	public void destroy() {
		try {
			sslPool.close();
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	@Asynchronous
	public void firedEvent(String eventName, Map<String, Object> params) {
		
		List<ApplicationEventListener> lst = listeners.get(eventName);
		if (lst == null) {
			return;
		}
		
		RuntimeServices rserv = new RuntimeServices(this, rulesBean, wfBean, questBean);
		for (ApplicationEventListener lsn: lst) {
			try {
				lsn.firedEvent(eventName, params, rserv);
			} catch (Throwable ex) {
				log.log(Level.SEVERE, "Ошибка в обработке события " + eventName, ex);
			}
		}
		
	}
	
	/**
	 * Получаем объект из кэша, если он там есть и начинаем использовать
	 */
	@Override
	@Lock(LockType.WRITE)
	public Object startStateCached(String name) {
		synchronized(stateCache) {
			CachedHolder cur = stateCache.get(name);
			if (cur == null) {
				return null;
			} else {
				cur.counter++;
				return cur.content;
			} 
		}
	}
	
	/**
	 * Помещаем объект в кэш, но не начинаем использовать
	 */
	@Override
	@Lock(LockType.WRITE)
	public void putStateCached(String name, Object source) {
		synchronized(stateCache) {
			CachedHolder cur = stateCache.get(name);
			if (cur == null) {
				cur = new CachedHolder();
				cur.content = source;
				cur.counter = 0;
			} else {
				cur.content = source;
			} 
			stateCache.put(name, cur);
		}
	}
	
	/**
	 * Завершаем пользование объектом
	 */
	@Override
	@Lock(LockType.WRITE)
	public void finishStateCached(String name) {
		RuntimeServices rserv = new RuntimeServices(this, rulesBean, wfBean, questBean);
		synchronized(stateCache) {
			CachedHolder cur = stateCache.get(name);
			if (cur != null) {
				cur.counter--;
				if (cur.counter == 0) {
					if (cur.content instanceof Closeable) {
						((Closeable) cur.content).close(rserv);
					}
					stateCache.remove(name);
				}
			}
		}
	}
	
	@Override
	public void subscribe(String eventName, ApplicationEventListener lsn) {
		List<ApplicationEventListener> lst = listeners.get(eventName);
		if (lst == null) {
			lst = new ArrayList<ApplicationEventListener>(3);
			listeners.put(eventName, lst);
		}
		lst.add(lsn);
	}
	
	/**
	 * Отписаться от уведомлений о событиях
	 * @param eventName - имя события, от которого отписаться. Если null, отписаться от всех событий
	 * @param lsn - объект, который отписывается от рассылки
	 */
	@Override
	public void unsubscribe(String eventName, ApplicationEventListener lsn) {
		if (StringUtils.isBlank(eventName)) {
			for (List<ApplicationEventListener> lst : listeners.values()) {
				lst.remove(lsn);				
			}
		} else {
			List<ApplicationEventListener> lst = listeners.get(eventName);
			if (lst != null) {
				lst.remove(lsn);
				if (lst.size() == 0) {
					listeners.remove(eventName);
				}
			}
		}
	}
	
	class CachedHolder {
		int counter = 0;
		Object content;
	}
	
	private int internalFindActionContext(String customKey) {
		String skey = customKey;
		if (StringUtils.isBlank(skey)) {
			skey = "";
		}
		int n = -1;
		for (ActionContext ctx: actionContexts) {
			n++;
			String skey1 = ctx.getCustomizationKey();
			if (StringUtils.isBlank(skey1)) {
				skey1 = "";
			}
			if (skey.equals(skey1)) {
				return n;
			}
		}
		return -1;
	}
	
	protected RuntimeServices getRuntimeServices() {
//		ServiceBeanLocal svc = sessionCtx.getBusinessObject(ServiceBeanLocal.class);
		return new RuntimeServices(this, rulesBean, wfBean, questBean);
	}
	
	@Override
	public ActionContext createActionContext(String customizationKey, boolean bPersistent ) {
        if (StringUtils.isBlank(customizationKey)) {
            bPersistent = true;
        }

        ActionContext con = getActionContext(customizationKey);
        if (con == null) {
            con = new ActionContext(customizationKey, bPersistent);
            initPlugins(customizationKey, con.getPlugins(), bPersistent);
            con.initPluginStates(getRuntimeServices());
            putActionContext(con);
            subscribe(EventKeys.EVENT_PLUGINS_SETTINGS_CHANGED, con);
        }
        return con;		
	}
	
    /**
     * инициализируем плагины
     * @param customKey
     * @param pls
     * @param bPersistent
     */
    private void initPlugins(String customKey, PluginsSupport pls, boolean bPersistent) {
        RuntimeServices runtimeServices = getRuntimeServices();
        for (PluginConfig plc : pls.getPluginConfigs()) {
            plc.customizationKey = customKey;
            plc.init(runtimeServices);
        }

        if (bPersistent) {
            Map<String, Object> pluginOpts = rulesBean.getPluginsOptions(customKey);
            if (pluginOpts != null) {
                pls.load(customKey, pluginOpts);
            }
        }
        for (PluginConfig plc : pls.getPluginConfigs()) {
            plc.partner = refBooks.findPartnerByName(plc.getPartnerName());
        }
        pls.applyConfigs();

    }	
	
	@Override
	public ActionContext getActionContext(String customKey) {
		int idx = internalFindActionContext(customKey);
		if (idx < 0) {
			return null;
		} else {
			return actionContexts.get(idx);
		}
	}
	
	@Override
	public void putActionContext(ActionContext source) {
		String skey = source.getCustomizationKey();
		if (StringUtils.isBlank(skey)) {
			skey = "";
		}		
		int idx = internalFindActionContext(skey);
		if (idx >= 0) {
			actionContexts.set(idx, source);
		} else {
			actionContexts.add(source);
			if (actionContexts.size() > actionContextsMax) {
				actionContexts.remove(0);
			}
		}
	}

	@Override
	public Object getPluginConfigObject(String pluginName, String customKey) {
		ActionContext context = getActionContext(customKey);
		if (context == null) {
			return null;
		}
		return context.getPlugins().getPluginConfig(pluginName);
	}
	
	@Override
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void removeAction(ApplicationAction action) {
		int key = action.hashCode();
		synchronized(runningActions) {
			runningActions.remove(key);
		}
	}
	
	@Override
	@Lock(LockType.WRITE)
	public void removeAction(int key) {
		synchronized(runningActions) {
			runningActions.remove(key);
		}
	}

	@Override
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ApplicationAction putAction(ApplicationAction action) {
		int key = action.hashCode();
		ApplicationAction res = null;
		synchronized(runningActions) {
			res = runningActions.get(key);
			if (res == null) {
				runningActions.put(key, action);
				res = action;
			} else if (! res.isExclusive()) {
				runningActions.put(key, action);
				res = action;				
			}
		}
		return res;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ApplicationAction getAction(int key) {
		return runningActions.get(new Integer(key));
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ApplicationAction> findActions(String prmActionId, Boolean prmExclusive, BusinessObjectRef[] prmBizRefs) {
		List<ApplicationAction> lstRes = new ArrayList<ApplicationAction>(5);
		for (ApplicationAction act: runningActions.values()) {
			if (prmActionId != null && act.getId() != prmActionId) {
				continue;
			}
			if ( prmExclusive != null && act.isExclusive() != prmExclusive.booleanValue()) {
				continue;
			}
			
			if (prmBizRefs != null && ! act.contains(prmBizRefs, true)) {
				continue;
			}
			lstRes.add(act);
		}
		return lstRes;
	}
	
    private String initJSEngine() {
    	String jsPreface = "";

        // формируем константы
        jsPreface = "var MConst = {decision: {";
        jsPreface = jsPreface + "RESULT_ACCEPT:" + String.valueOf(DecisionKeys.RESULT_ACCEPT) + ",";
        jsPreface = jsPreface + "RESULT_MANUAL:" + String.valueOf(DecisionKeys.RESULT_MANUAL) + ",";
        jsPreface = jsPreface + "RESULT_UNDEFINED:" + String.valueOf(DecisionKeys.RESULT_UNDEFINED)+ ",";
        jsPreface = jsPreface + "RESULT_ACCEPT_AUTO:" + String.valueOf(DecisionKeys.RESULT_ACCEPT_AUTO);
        jsPreface = jsPreface + "}";
        jsPreface = jsPreface + ", answerStatus : {";
        jsPreface = jsPreface + "ANSWER_STATUS_NOT_ASKED:" + String.valueOf(QuestionAnswer.ANSWER_STATUS_NOT_ASKED) +
                ",";
        jsPreface = jsPreface + "ANSWER_STATUS_DENIAL:" + String.valueOf(QuestionAnswer.ANSWER_STATUS_DENIAL) + ",";
        jsPreface = jsPreface + "ANSWER_STATUS_ANSWERED:" + String.valueOf(QuestionAnswer.ANSWER_STATUS_ANSWERED);
        jsPreface = jsPreface + "}";
        jsPreface = jsPreface + ", creditRequestStatus : {";
        jsPreface = jsPreface + "FILLED:" + String.valueOf(CreditStatus.FILLED) + ",";        
        jsPreface = jsPreface + "REJECTED:" + String.valueOf(CreditStatus.REJECTED) + ",";
        jsPreface = jsPreface + "FOR_COURT:" + String.valueOf(CreditStatus.FOR_COURT) + ",";
        jsPreface = jsPreface + "DECISION:" + String.valueOf(CreditStatus.DECISION) + ",";
        jsPreface = jsPreface + "IN_PROCESS:" + String.valueOf(CreditStatus.IN_PROCESS) + ",";
        jsPreface = jsPreface + "FOR_COLLECTOR:" + String.valueOf(CreditStatus.FOR_COLLECTOR) + ",";
        jsPreface = jsPreface + "CLOSED:" + String.valueOf(CreditStatus.CLOSED);
        jsPreface = jsPreface + "}";
        jsPreface = jsPreface + ", refuseCode : {";
        List<RefuseReason> lst = refBooks.getRefuseReasons();
        int n = 0;
        for (RefuseReason ref : lst) {
            if (n > 0) {
                jsPreface = jsPreface + ",";
            }
            String scname = ref.getConstantName();
            if (StringUtils.isBlank(scname)) {
                scname = "CODE_" + ref.getId().toString();
            }
            jsPreface = jsPreface + scname + ":" + ref.getId().toString();
            n++;
        }
        lst = null;
        jsPreface = jsPreface + "}";
        jsPreface = jsPreface + ", search : {";        
        jsPreface = jsPreface + "ANY: SV_ANY "; 
        jsPreface = jsPreface + "}";
        
        jsPreface = jsPreface + ", summary : {";
        List<RefSummary> lstR = refBooks.getRefSummaryList();
        n = 0;
        for (RefSummary ref: lstR) {
            if (n > 0) {
                jsPreface = jsPreface + ",";
            } 
            String scname = ref.getName();
            if (StringUtils.isBlank(scname)) {
                scname = "CODE_" + ref.getId().toString();
            }
            jsPreface = jsPreface + scname + ":" + ref.getId().toString();
            n++;            
        }
        
        jsPreface = jsPreface + "}};";
        return jsPreface;
    }

    @Override
    public ScriptEngineInfo selectEngine(AIModel model) {
        if (MimeTypeKeys.JAVASCRIPT.equalsIgnoreCase(model.getMimeType())) {
        	try {
        	    ScriptEngineInfo info = new ScriptEngineInfo();
        	    info.scriptEngine = scriptFactory.getEngineByName("JavaScript");
        	    info.jsPreface = initJSEngine();
                return info;
        	} catch (Exception e) {
        		log.severe("Произошла ошибка при выборе js движка "+e.getMessage());
        	}
        }
        return null;
    }
    
    @Override
    @Lock(LockType.WRITE)
    @AccessTimeout(value=1200000)
    public SSLContext borrowSSLContext(String[] settings) throws Exception {
    	SSLContext con = null;
    	synchronized(sslPool) {
    		con = sslPool.borrowObject(settings);
    	}
    	return con;
    }
    
    @Override
    @Lock(LockType.WRITE)
    @AccessTimeout(value=1200000)
    public void returnSSLContext(String[] settings, SSLContext sslCon) throws Exception {
    	synchronized(sslPool) {
    		sslPool.returnObject(settings, sslCon);
    	}
    }
    
    @Override
    public List<BizActionEntity> getBizActionsForEvent(int eventCode) {
    	return mapEvents.get(eventCode);
    }
    
    @Override
    public Object evaluateEL(String expression, Map<String, Object> vars) {
    	ELContext elCtx = new SimpleContext();
    	for (Map.Entry<String, Object> entry: vars.entrySet() ) {
    		elCtx.getVariableMapper().setVariable(entry.getKey(), elFactory.createValueExpression(entry.getValue(), Object.class));
    	}
    	
    	ValueExpression ex = elFactory.createValueExpression(elCtx, expression, Object.class);
    	
    	Object ares = ex.getValue(elCtx);
    	return ares;
    }
    
	@Override
	@Asynchronous	
	@Lock(LockType.WRITE)
	public void anyBizActionEvent(@Observes BusinessEvent event) {
		if ("ru.simplgroupp.transfer.BizAction".equals(event.getBusinessObjectClass()) ) {
			reloadMapActions();
		}
		
		List<BizActionEntity> lstAct = mapEvents.get(event.getEventCode());
		if (lstAct == null || lstAct.size() == 0) {
			return;
		}
		
		log.info("Обрабатываем событие " + String.valueOf(event.getEventCode()) + " для " + event.getBusinessObjectClass() + " " + event.getBusinessObjectId().toString());
		AbstractContext ctx = AppUtil.getDefaultContext(event.getBusinessObject(), event.getBusinessObjectClass());
		ctx.setEventCode(event.getEventCode());
		ctx.getParams().putAll(event.getParams());
		ctx.setAutoRun(true);
		for (BizActionEntity biz: lstAct) {
			if (event.getBusinessObjectClass().equals(biz.getBusinessObjectClass())) {
				try {
					bizProc.executeBizAction(biz, event.getBusinessObjectId(), ctx);
				} catch (ActionException e) {
					log.log(Level.SEVERE, "Не выполнено бизнес-действие " + biz.getId().toString() + " на событие " + String.valueOf(event.getEventCode()) , e);
				}
			}
		}
	}
}
