package ru.simplgroupp.interfaces;


import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.persistence.AIRuleEntity;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.rules.BooleanRuleResult;
import ru.simplgroupp.rules.CreditRequestContext;
import ru.simplgroupp.rules.NameValueRuleResult;
import ru.simplgroupp.rules.watched.FieldInfo;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;

import javax.ejb.Local;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Local
public interface RulesBeanLocal {

    /**
     * сохранить правило
     *
     * @param rule правило
     */
    AIRuleEntity saveRule(AIRuleEntity rule);

    /**
     * новое правило
     *
     * @param ruleType    вид правила
     * @param packageName название пакета
     * @param description описание
     * @param body        само правило
     * @param kbase
     */
    AIRuleEntity createRule(int ruleType, String packageName, String description, String body, String kbase);

    /**
     * сохраняем все константы
     *
     * @param packageName название пакета
     * @param values      параметры констант
     */
    void saveConstants(String packageName, Map<String, Object> values);

    /**
     * сохраняем константу
     *
     * @param ruleId        id правила
     * @param name          название
     * @param dataType      тип данных
     * @param datavalue     значение
     * @param description   описание
     * @param datavaluetext значение текст
     */
    void saveConstant(Integer ruleId, String name, String dataType, String datavalue, String description, String datavaluetext);

    /**
     * обновляем значение константы
     *
     * @param packageName
     * @param name        название
     * @param datavalue   значение
     */
    void updateConstantValue(String packageName, String name, String datavalue);

    /**
     * удаляем константу
     *
     * @param constantId id константы
     */
    void deleteConstant(Integer constantId);

    List<FieldInfo> calcWatchedFields();


    /**
     * константы для смс-рассылки
     *
     * @return
     */
    Map<String, Object> getSmsConstants();

    /**
     * константы для email-рассылки
     *
     * @return
     */
    Map<String, Object> getEmailConstants();

    /**
     * константы для логина
     *
     * @return
     */
    Map<String, Object> getLoginConstants();
    /**
     * константы для генерации разных кодов и паролей
     *
     * @return
     */
    Map<String, Object> getGenerateConstants();
    /**
     * константы для работы с соц.сетями
     *
     * @return
     */
    Map<String, Object> getSocNetworkConstants();

    /**
     * константы для гео-локации
     *
     * @return
     */
    Map<String, Object> getSiteConstants();

    /**
     * возвращает константы для мошеннической системы
     */
    Map<String, Object> getAntifraudConstants();

    /**
     * можно ли подавать кредитную заявку
     *
     * @param context контекст для выполнения
     */
    BooleanRuleResult canCreateRequest(CreditRequestContext context);

    /**
     * можно ли продлять кредитную заявку
     *
     * @param context контекст для выполнения
     */
    BooleanRuleResult canProlongRequest(CreditRequestContext context);

    /**
     * можно ли оплачивать кредитную заявку
     *
     * @param context контекст для выполнения
     */
    BooleanRuleResult canCompleteRequest(CreditRequestContext context);

    /**
     * можно ли подписывать оферту
     *
     * @param context контекст для выполнения
     */
    BooleanRuleResult canSignOferta(CreditRequestContext context);

    /**
     * возвращает отформатированные сообщения
     *
     * @return
     */
    Map<String, Object> getFormattedMessages();

    /**
     * возвращает отформатированное сообщение по ключу
     *
     * @param messageKey ключ
     * @return
     */
    String getFormattedMessage(String messageKey);

    /**
     * возвращает отформатированные сообщения для БП
     *
     * @param bpName имя БП
     * @return
     */
    Map<String, Object> getFormattedMessagesForBP(String bpName);

    /**
     * возвращает параметры для БП
     *
     * @param bpName имя БП
     * @return
     */
    Map<String, Object> getOptionsForBP(String bpName);

    /**
     * возвращает опции для крипто-работы
     */
    Map<String, Object> getCryptoCommonSettings();

    /**
     * возвращает опции плагина
     *
     * @param pluginName название плагина
     * @param customKey  ключ
     */
    Map<String, Object> getPluginsOptions(String pluginName, String customKey);

    /**
     * Возвращает названия платёжных систем для заданного вида платежа в порядке предпочтения
     *
     * @param accountTypeCode вид платежа
     */
    List<String> getPaymentSystemOrder(int accountTypeCode);

    /**
     * возвращает конфигурации плагина
     */
    Map<String, String> getPluginConfigurations();

    /**
     * возвращает опции плагина
     *
     * @param customKey
     */
    Map<String, Object> getPluginsOptions(String customKey);

    /**
     * устанавливает опции плагина
     *
     * @param customKey
     * @param description описание
     * @param source      опции
     */
    void setPluginsOptions(String customKey, String description, Map<String, Object> source);

    /**
     * удаляет опции плагина
     *
     * @param customKey название плагина
     * @return
     */
    boolean deletePluginOptions(String customKey);

    public NameValueRuleResult getReturnLimits(Credit credit, Map<String, Object> creditParams);

    /**
     * считаем первоначально кредит
     *
     * @param creditParams параметры для кредита
     * @return
     */
    public NameValueRuleResult calcCreditInitial(Map<String, Object> creditParams);

    /**
     * считаем параметры кредита
     *
     * @param creditLimits параметры для кредита
     * @return
     */
    public NameValueRuleResult calcCreditParams(Map<String, Object> creditLimits);

    /**
     * считаем первоначально кредит
     *
     * @param ccRequest заявка на кредит
     * @param addDays - добавочных дней
     * @return
     */
    public NameValueRuleResult calcCreditInitial(CreditRequest ccRequest,Integer addDays);

    /**
     * считаем первоначальную ставку по кредиту
     *
     * @param creditSum  сумма кредита
     * @param creditDays дней кредита
     * @param limits     параметры для рассчета
     * @return
     */
    public NameValueRuleResult calcInitialStake(double creditSum, int creditDays, Map<String, Object> limits);

    /**
     * посчитать максимальную сумму бонусов к оплате
     *
     * @param sumBack    сумма к возврату
     * @param sumPercent сумма процентов
     * @param bonusSum   сумма бонусов
     * @param limits     константы
     * @return
     */
    public NameValueRuleResult calcBonusPaymentSum(Double sumBack, Double bonusSum, Map<String, Object> limits);

    /**
     * какие действия возможны
     *
     * @param actionRef сигнал
     * @param context   контекст
     * @return
     */
    public BooleanRuleResult getProcessActionEnabled(final SignalRef actionRef, AbstractContext context);

    /**
     * расчитываем данные по кредиту
     *
     * @param credit   кредит
     * @param dateCalc дата расчета
     * @param limits   необходимые параметры
     * @return map со значениями расчета
     */
    public NameValueRuleResult calcCreditData(Credit credit, Date dateCalc, Map<String, Object> limits);

    /**
     * Расчет вида деятельности коллектора
     *
     * @param params необходимые параметры
     * @param credit кредит
     * @return map со значениями расчета
     */
    NameValueRuleResult calcCreditToCollector(Credit credit, Map<String, Object> params);

    /**
     * расчитываем кол-во дней для продления
     *
     * @param credit   кредит
     * @param dateLong дата расчета
     * @param limits   необходимые параметры
     * @return map со значениями расчета
     */
    public NameValueRuleResult calcProlongDays(Credit credit, Date dateLong, Map<String, Object> limits);

    /**
     * возвращает действия объекта
     *
     * @param businessObjectClass калсс бизнес-объекта
     * @param forOne              выполняется для одного
     * @param forMany             выполняется для многих
     * @return
     */
    public List<WorkflowObjectActionDef> getObjectActions(String businessObjectClass, Boolean forOne, Boolean forMany);

    /**
     * возвращает, доступно ли действие по данному объекту
     *
     * @param actionDef действие
     * @param context   контекст для выполнения
     * @return
     */
    public BooleanRuleResult getObjectActionEnabled(WorkflowObjectActionDef actionDef, AbstractContext context);


    public Map<String, Object> getOptionsForBOP(String actionRef);

	/**
	 * возвращаем константы для бонусов
	 * 
	 * @return
	 */
	Map<String, Object> getBonusConstants();

    /**
     * возвращаем константы для обратной связи
     *
     * @return константы
     */
    Map<String, Object> getCallbackConstants();

    /**
     * выполняет действие по объекту
     *
     * @param actionDef действие
     * @param actProc   ActionProcessor
     * @param context   контекст для выполнения
     * @return
     * @throws ActionException
     */
    public NameValueRuleResult executeObjectAction(WorkflowObjectActionDef actionDef,
                                                   ActionProcessorBeanLocal actProc, AbstractContext context) throws ActionException;

    /**
     * выполняет старт действия
     *
     * @param actionDef - 
     * @param actProc - ejb для выполнения действий в БП  
     * @param context - контекст
     * @return
     */
   	NameValueRuleResult executeObjectActionStart(WorkflowObjectActionDef actionDef,
			ActionProcessorBeanLocal actProc, AbstractContext context)	throws ActionException;

    Map<String, Object> getMainWebConstant();
    
    /**
     * Приоритет в погашении платежей. Коды из payment.sum_id
     *
     * @return
     */
    List<Integer> getPaymentToOrder();

   
}
