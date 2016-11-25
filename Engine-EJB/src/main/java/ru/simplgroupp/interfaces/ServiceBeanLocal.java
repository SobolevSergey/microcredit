package ru.simplgroupp.interfaces;

import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.ApplicationAction;
import ru.simplgroupp.ejb.BusinessEvent;
import ru.simplgroupp.ejb.ScriptEngineInfo;
import ru.simplgroupp.transfer.AIModel;

public interface ServiceBeanLocal extends ServiceBeanMini {

	public void firedEvent(String eventName, Map<String, Object> params);

	public void subscribe(String eventName, ApplicationEventListener lsn);

	public void unsubscribe(String eventName, ApplicationEventListener lsn);

	public Object startStateCached(String name);
	public void putStateCached(String name, Object source);
	public void finishStateCached(String name);

	public ActionContext getActionContext(String customKey);

	public void putActionContext(ActionContext source);

	public void removeAction(ApplicationAction action);

	public void removeAction(int key);

	public ApplicationAction putAction(ApplicationAction action);

	public ApplicationAction getAction(int key);

	List<ApplicationAction> findActions(String prmActionId,
			Boolean prmExclusive, BusinessObjectRef[] prmBizRefs);

	ScriptEngineInfo selectEngine(AIModel model);

	SSLContext borrowSSLContext(String[] settings) throws Exception;

	void returnSSLContext(String[] settings, SSLContext sslCon)
			throws Exception;

	void anyBizActionEvent(BusinessEvent event);

	List<BizActionEntity> getBizActionsForEvent(int eventCode);

	Object evaluateEL(String expression, Map<String, Object> vars);

	/**
	 * Возвращает или создаёт контекст для запуска плагинов
	 * @param customizationKey
	 * @param bPersistent
	 * @return
	 */
	public ActionContext createActionContext(String customizationKey, boolean bPersistent);

}
