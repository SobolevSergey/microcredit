package ru.simplgroupp.interfaces;

import org.activiti.engine.runtime.ProcessInstance;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.exception.WorkflowRuntimeException;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Users;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;
import ru.simplgroupp.workflow.WorkflowObjectState;
import ru.simplgroupp.workflow.WorkflowObjectStateDef;

import javax.ejb.Local;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Local
public interface WorkflowBeanLocal {

	/**
	 * список состояний кредита
	 */
    List<WorkflowObjectStateDef> getCreditProcessStatesForRequest();

    /**
     * запускаем или находим уже запущенный процесс кредита
     * @param creditRequestId - id заявки
     * @param clientUserId - id пользователя
     * @throws WorkflowException
     */
    ProcessInstance startOrFindProcCR(int creditRequestId, Integer clientUserId) throws WorkflowException;

    /**
     * 
     * @param creditRequestId
     * @return
     */
    List<String> getTopLevelActivityIds(int creditRequestId);

    /**
     * удаляем процесс кредита
     * @param creditRequestId - id заявки
     * @throws WorkflowException
     */
    void removeProcCR(int creditRequestId) throws WorkflowException;

    /**
     * возвращает действия из процесса кредита
     * @param creditRequestId - id заявки
     * @param variableNames - переменные
     * @param bIncludeWithActions
     */
    List<WorkflowObjectStateDef> getProcCRWfActions(int creditRequestId, Set<String> variableNames, final boolean bIncludeWithActions);

    /**
     * возвращает действия из процесса кредита оффлайн
     * @param creditRequestId - id заявки
     * @param variableNames - переменные
     * @param bIncludeWithActions
     */
    List<WorkflowObjectStateDef> getProcCROfflineWfActions(int creditRequestId, Set<String> variableNames, final boolean bIncludeWithActions);
    
    /**
     * изменение настроек для БП
     * @param productId - id продукта
     * @param bpName - название процесса
     */
    void optionsChanged(Integer productId,String bpName);

    /**
     * заканчиваем пакетную обработку
     * @param procInstanceId - процесс
     * @param pluginName - плагин
     */
    void endPacketProcess(String procInstanceId, String pluginName);

    /**
     * идем в процесс кредита
     * @param creditRequestId - id заявки
     * @param signalRef - сигнал
     * @param params - параметры
     * @throws WorkflowException
     */
    void goProcCR(int creditRequestId, String signalRef, Map<String, Object> params) throws WorkflowException;

    /**
     * ищем процесс плагина
     * @param pluginName - название плагина
     * @param businessObjectClass - объект бизнес-класс
     * @param businessObjectId - id объекта
     */
    ProcessInstance findPluginProcess(String pluginName, String businessObjectClass, Object businessObjectId);

    /**
     * обработать результаты пакетного запроса
     * @param lstRes - параметры
     * @param pluginName - плагин
     * @param bSync - синхронно или нет
     */
    void handlePacketResults(List<BusinessObjectResult> lstRes, String pluginName, boolean bSync);
    /**
     * получаем объект workflow по ключу
     * @param primaryKey - ключ
     */
    WorkflowObjectState getWFOByPrimaryKey(String primaryKey);

    /**
     * получен результат платежа яндекс
     * @param creditRequestId - id заявки
     * @param data - параметры
     * @throws WorkflowException
     */
    void repaymentReceivedYandex(int creditRequestId, Map<String, Object> data)
            throws WorkflowException;

    /**
     * получен результат платежа киви
     * @param creditRequestId - id заявки
     * @param data - параметры
     * @throws WorkflowException
     */
    void repaymentReceivedQiwi(int creditRequestId, Map<String, Object> data)
            throws WorkflowException;

    /**
     * получен результат платежа payonline
     * @param creditRequestId - id заявки
     * @param data - параметры
     * @throws WorkflowException
     */
    void repaymentReceivedPayonline(int creditRequestId, Map<String, Object> data)
            throws WorkflowException;

    /**
     * получен результат платежа qiwi gate
     * @param creditRequestId - id заявки
     * @param data - параметры
     * @throws WorkflowException
     */
    void repaymentReceivedQiwiGate(Integer creditRequestId, Map<String, Object> data) throws WorkflowException;

    /**
     * получен результат платежа winpay
     * @param creditRequestId - id заявки
     * @param data - параметры
     * @throws WorkflowException
     */
    void repaymentReceivedWinpay(int creditRequestId, Map<String, Object> data)
            throws WorkflowException;

    /**
     * добавление в пакет
     * @param pluginName - название плагина
     * @param businessObjectClass - объект бизнес-класс
     * @param businessObjectId - id объекта
     */
    void messageAddToPacket(String businessObjectClass, Object businessObjectId, String pluginName);

    /**
     * ищем процесс 
     * @param processDefKey - название процесса
     * @param businessObjectClass - объект бизнес-класс
     * @param businessObjectId - id объекта
     */
    ProcessInstance findProcess(String processDefKey, String businessObjectClass, Object businessObjectId);

    /**
     * возвращаем переменные процесса 
     * @param processDefKey - название процесса
     * @param businessObjectClass - объект бизнес-класс
     * @param businessObjectId - id объекта
     */
    Map<String, Object> getProcessVariables(String processDefKey,
                                            String businessObjectClass, Object businessObjectId);

    /**
     * запускаем отладочную модель
     * @param ccRequest - кредитная заявка
     * @param actionContext - контекст
     * @param varsResult - переменные
     * @param modelId - id модели
     * @throws WorkflowException
     */
    void runDebugModelCR(CreditRequest ccRequest, ActionContext actionContext,
                         Map<String, Object> varsResult, int modelId) throws WorkflowException;

    /**
     * запускаем плагин
     * @param plc - конфиг плагина
     * @param businessObjectClass - объект бизнес-класс
     * @param businessObjectId - id объекта
     * @param params - параметры
     * @param context - контекст
     * @throws WorkflowException
     */
    ProcessInstance startPluginProcess(PluginConfig plc,
                                       String businessObjectClass, Object businessObjectId,
                                       Map<String, Object> params, ActionContext context)
            throws WorkflowException;

    /**
     * назначить задачу пользователю
     * @param taskId - id задачи
     * @param user - пользователь
     */
    public void assignTaskToUser(String taskId, Users user);

    /**
     * возвращает действия из процесса кредита
     * @param creditRequestId - id заявки
     * @param variableNames - переменные
     * @param bIncludeWithActions
     */
    public List<WorkflowObjectState> getProcCRWfActions1(int creditRequestId,
                                                         Set<String> variableNames, final boolean bIncludeWithActions);

    /**
     * идем в процесс
     * @param businessObjectClass - объект бизнес-класс
     * @param businessObjectId - id объекта
     * @param params - параметры
     * @param actionDef - действие
     * @param signalRef - сигнал
     * @throws WorkflowException
     */
    public void goProc(String businessObjectClass, Object businessObjectId, WorkflowObjectActionDef actionDef,
                       String signalRef, Map<String, Object> params) throws WorkflowException;

    /**
     * возвращает действия из процесса
     * @param businessObjectClass - объект бизнес-класс
     * @param businessObjectId - id объекта
     * @param variableNames - переменные
     * @param bIncludeWithActions
     */
    public List<WorkflowObjectState> getProcWfActions(String businessObjectClass,
                                                      Object businessObjectId, Set<String> variableNames,
                                                      boolean bIncludeWithActions);

    void dummy();

    /**
     * запускаем или ищем пакетный процесс
     * @param plc - плагин
     * @param actionContext - контекст
     * @throws WorkflowException
     */
    ProcessInstance startOrFindPacketProcess(PluginConfig plc, ActionContext actionContext) throws WorkflowException;
    /**
     * получен результат платежа контакта
     * @param creditRequestId - id заявки
     * @param data - параметры
     * @throws WorkflowException
     */
	public void repaymentReceivedContact(int creditRequestId, Map<String, Object> data)
			throws WorkflowRuntimeException, WorkflowException;

    /**
     * получен результат платежа через Ариус
     * @param creditRequestId - id заявки
     * @param data - параметры
     * @throws WorkflowException
     */
    void repaymentReceivedArius(int creditRequestId, Map<String, Object> data) throws WorkflowRuntimeException, WorkflowException;
	/**
	 * 
	 * @param actionId
	 * @param signalRef
	 * @throws WorkflowException
	 */
	public void messageProcMA(int actionId, String signalRef) throws WorkflowException;

	/**
	 * 
	 * @param actionId
	 * @param signalRef
	 * @throws WorkflowException
	 */
	public void messageProcMAtTx(int actionId, String signalRef) throws WorkflowException;

	/**
     * запускаем или находим уже запущенный процесс кредита
     * @param creditRequestId - id заявки
     * @param clientUserId - id пользователя
     * @param startPoint - с какого места запускаем
     * @throws WorkflowException
     */
	public ProcessInstance startOrFindProcCR(int creditRequestId,
			Integer clientUserId, String startPoint)
			throws WorkflowRuntimeException, WorkflowException;

	/**
	 * сигналим в процесс кредита
	 * @param creditRequestId - id заявки
	 * @throws WorkflowRuntimeException
	 * @throws WorkflowException
	 */
	void signalProcCR(int creditRequestId) throws WorkflowRuntimeException,
			WorkflowException;

	/**
	 * запускаем или находим уже запущенный процесс оплаты
	 * @param paymentId - id платежа
	 * @param accountTypeCode - id вида платежа
	 * @param creditId - id кредита
	 * @return
	 * @throws WorkflowException
	 */
	public ProcessInstance startOrFindProcPayment(int paymentId, int accountTypeCode,
			int creditId) throws WorkflowException;

	/**
	 * идем в процесс отказа клиента от кредита
	 * @param creditRequestId - id заявки
	 * @param clientUserId - id пользователя
	 * @throws WorkflowRuntimeException
	 * @throws WorkflowException
	 */
	void goProcClientReject(int creditRequestId, Integer clientUserId)
			throws WorkflowRuntimeException, WorkflowException;

	boolean isProcessRunning(String processDefKey, String businessObjectClass,
			Object businessObjectId);

	void goProcAll(String businessObjectClass, Object businessObjectId,
			String signalRef, Map<String, Object> params, boolean bTop)
			throws WorkflowRuntimeException, WorkflowException;
	
	 /**
     * получен результат платежа 
     * @param creditRequestId - id заявки
     * @param accountType - id вида платежа
     * @param data - параметры
     * @throws WorkflowException
     */
	void repaymentReceived(int creditRequestId, int accountType,
			Map<String, Object> data) throws WorkflowRuntimeException,
			WorkflowException;

	/**
	 * запускаем или находим процесс оффлайн
	 * 
	 * @param creditRequestId - id заявки
	 * @param clientUserId - id пользователя
	 * @return
	 * @throws WorkflowRuntimeException
	 * @throws WorkflowException
	 */
	ProcessInstance startOrFindProcCROffline(int creditRequestId,
			Integer clientUserId) throws WorkflowRuntimeException,
			WorkflowException;

	/**
	 * работаем с процессом оффлайн
	 * 
	 * @param creditRequestId - id заявки
	 * @param signalRef - сигнал
	 * @param params - параметры
	 * @throws WorkflowRuntimeException
	 * @throws WorkflowException
	 */
	void goProcCROffline(int creditRequestId, String signalRef,
			Map<String, Object> params) throws WorkflowRuntimeException,
			WorkflowException;
	
	/**
	 * удаляем задачу у пользователя
	 * @param taskId - id задачи
	 */
	public void removeTaskFromUser(String taskId);

	/**
	 * Проходит по иерархии процессов вверх, начиная с exId. Ищет execution, в которой есть переменная по имени varName. 
	 * Возвращает все локальные переменные из этого execution. 
	 * @param exId
	 * @param varName
	 * @return
	 */
	public Map<String, Object> findVariableInParents(String exId, String varName);
}
