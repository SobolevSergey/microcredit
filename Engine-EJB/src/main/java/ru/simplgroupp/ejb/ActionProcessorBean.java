package ru.simplgroupp.ejb;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.dao.interfaces.*;
import ru.simplgroupp.exception.*;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.plugins.identity.IdentityBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.*;
import ru.simplgroupp.interfaces.service.*;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.script.*;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.*;
import ru.simplgroupp.workflow.*;
import ru.simplgroupp.workflow.DecisionState.ExternalSystemState;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * разные действия для работы в процессах
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ActionProcessorBeanLocal.class)
public class ActionProcessorBean implements ActionProcessorBeanLocal {

	@Inject Logger logger;

    @EJB
    PaymentDAO payDAO;

    @EJB
    CreditDAO creditDAO;

    @EJB
    ProlongDAO prolongDAO;

    @EJB
    CreditRequestDAO crDAO;

	@EJB
    PeopleDAO peopleDAO;

	@EJB
	OfficialDocumentsDAO officialDocumentDAO;

    @EJB
    private ServiceBeanLocal serviceBean;

    @EJB
    private KassaBeanLocal kassaBean;

    @EJB
    private CreditBeanLocal creditBean;

    @EJB
    private PaymentService paymentService;

    @EJB
    private ModelBeanLocal modelBean;

    @EJB
    private ReferenceBooksLocal refBooks;

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    private MailBeanLocal mailBean;

    @EJB
    private RulesBeanLocal rulesBean;

    @EJB
    private WorkflowBeanLocal wfBean;

    @EJB
    private WorkflowEngineBeanLocal wfEngine;

    @EJB
    private QuestionBeanLocal questBean;

    @EJB
    private CreditCalculatorBeanLocal creditCalc;

    @EJB
    private HolidaysService holiServ;

    @EJB
    private BizActionBeanLocal bizBean;

    @EJB
    private BizActionDAO bizDAO;

    @EJB
    private PaymentService paymentBean;

    @EJB
    private UsersBeanLocal userBean;

    @EJB
    private EventLogService eventLog;

    @EJB
    ReportBeanLocal reportBean;    
    
    @EJB
    BusinessEventSender evtSender;

    @EJB
    AppServiceBeanLocal appServ;

    @EJB
    ProductBeanLocal productBean;

    @EJB
    IdentityQuestionLocal identityQuestion;

    @EJB
    ProductDAO productDAO;

    @EJB
    CreditInfoService creditInfo;

    @EJB
    IdentityBeanLocal identityBean;

    @EJB
    DocumentsDAO documentsDAO;

    @EJB
    RefAntifraudRulesService antifraudRulesService;

    @EJB(beanName="ScoringEquifaxBean")
    ScoringEquifaxBeanLocal equifaxBean;

    @EJB(beanName="ScoringNBKIBean")
    ScoringNBKIBeanLocal nbkiBean;

    @EJB(beanName="ScoringOkbCaisBean")
    ScoringOkbCaisBeanLocal okbCaisBean;

    @EJB(beanName="ScoringRsBean")
    ScoringRsBeanLocal rsBean;

    @EJB(beanName="ScoringSkbBean")
    ScoringSkbBeanLocal skbBean;

    @EJB
    private CollectorDAO collectorDAO;

    @EJB
    private AntifraudOccasionDAO antifraudOccasionDAO;

    @EJB
    private OrganizationService organizationService;


//    private Map<String, Map<String, Object>> bpMessagesCache = new HashMap<String, Map<String, Object>>(1);

    @PostConstruct
    public void init() {

    }

    @Override
    public void test(Integer creditRequestId) {
        logger.log(Level.ALL, "test " + creditRequestId);
    }

    @Override
    public void removeCreditRequest(Integer creditRequestId) {
        try {
            kassaBean.deleteCreditRequest(creditRequestId);
        } catch (KassaException e) {
            logger.log(Level.SEVERE, "Не удалось удалить заявку "+creditRequestId.toString()+",ошибка "+e.getMessage(), e);
        }
    }

    @Override
    public String calcISOduration(long ms) {
    	return WorkflowUtil.calcISOduration(ms);
    }

    @Override
    public String calcISOduration(String schedule, long ms) {
    	if (StringUtils.isBlank(schedule)) {
    		return WorkflowUtil.calcISOduration(ms);
    	} else {
    		return schedule;
    	}
    }

    @Override
    public String calcISOcycle(long ms) {
        return WorkflowUtil.calcISOcycle(ms);
    }

    @Override
    public String calcISOCycleBizAction(Integer bizActionId) {
    	BizActionEntity ent = bizDAO.getBizActionEntity(bizActionId);
    	return ent.getSchedule();
    }

    @Override
    public String calcISODateProlong(Integer prolongId, int days) {
    	ProlongEntity ent = creditBean.getProlongEntity(prolongId);
    	if (ent!=null){
    	    int ndays = holiServ.getDaysOfHolidays(ent.getLongdate()) + days;
    	    Date dtEnd = DateUtils.addDays(ent.getLongdate(), ndays);
            return WorkflowUtil.calcISOdate(dtEnd);
    	} else {
    		logger.severe("Не найдено продление "+prolongId);
    		return null;
    	}
    }

    @Override
    public String calcISODateRefinance(Integer refinanceId, int days) {
    	RefinanceEntity ent = creditDAO.getRefinanceEntity(refinanceId);
    	if (ent!=null){
    	    int ndays = holiServ.getDaysOfHolidays(ent.getRefinanceDate()) + days;
    	    Date dtEnd = DateUtils.addDays(ent.getRefinanceDate(), ndays);
            return WorkflowUtil.calcISOdate(dtEnd);
    	} else {
    		logger.severe("Не найдено продление "+refinanceId);
    		return null;
    	}
    }

    @Override
    public String calcISOdate(Date source) {
    	logger.info("--------> calcISOdate "+source);
    	String ss="";
    	if (source!=null){
    	  try {
    		ss=WorkflowUtil.calcISOdate(source);
    	  } catch (Exception e){
    		logger.severe("Не удалось посчитать ISO date "+e);
    	  }
    	}
        return ss;
    }

    @Override
    public String calcISOdateTime(Date source) {
    	logger.info("--------> calcISOdateTime "+source);
    	String ss="";
    	if (source!=null){
    	  try {
    		ss=WorkflowUtil.calcISOdateTime(source);
    	  } catch (Exception e){
    		logger.severe("Не удалось посчитать ISO dateTime "+e);
    	  }
    	}
        return ss;
    }

    @Override
    public boolean isModelEnabled(String target) {
        AIModel model = modelBean.getActiveModel(target);
        return (model != null);
    }

    @Override
    public boolean crAccepted(Integer creditRequestId) throws ActionException{
        CreditRequestEntity ent = crDAO.getCreditRequestEntity(creditRequestId);
        logger.info("проверяем, одобрена ли заявка "+creditRequestId.toString());
        if (ent==null){
        	logger.severe("Не найдена заявка с id "+creditRequestId.toString());
        	throw new ActionException(0,"Не найдена заявка с id "+creditRequestId.toString(), Type.TECH, ResultType.FATAL,null);
        }
        return (ent.getAccepted() != null && ent.getAccepted() == CreditRequest.ACCEPTED);
    }

    @Override
    public boolean crOfertaAccepted(Integer creditRequestId) throws ActionException{
        CreditRequestEntity ent = crDAO.getCreditRequestEntity(creditRequestId);
        logger.info("проверяем, подписана ли оферта по завке "+creditRequestId.toString());
        if (ent==null){
        	logger.severe("Не найдена заявка с id "+creditRequestId.toString());
        	throw new ActionException(0,"Не найдена заявка с id "+creditRequestId.toString(), Type.TECH, ResultType.FATAL,null);
        }
        OfficialDocumentsEntity document=officialDocumentDAO.findOfficialDocumentCreditRequestDraft(ent.getPeopleMainId().getId(),
                creditRequestId, OfficialDocuments.OFERTA_CREDIT);
        if (document!=null){
            logger.info("подписана ли оферта по завке "+creditRequestId.toString()+" - "+document.isOfertaSigned());
            return document.isOfertaSigned();
        } else {
        	logger.info("подписана ли оферта по завке "+creditRequestId.toString()+" false");
        	return false;
        }
    }

    @Override
    public boolean crOfertaRejected(Integer creditRequestId) throws ActionException {
        CreditRequestEntity ent = crDAO.getCreditRequestEntity(creditRequestId);
        logger.info("проверяем, был ли отказ клиента от оферты по завке "+creditRequestId.toString());
        if (ent==null){
        	logger.severe("Не найдена заявка с id "+creditRequestId.toString());
        	throw new ActionException(0,"Не найдена заявка с id "+creditRequestId.toString(), Type.TECH, ResultType.FATAL,null);
        }
        return (ent.getStatusId().getId() == CreditStatus.CLIENT_REFUSE);
    }

    @Override
    public boolean crVerificatorMustEdit(Integer creditRequestId) throws ActionException {
        CreditRequestEntity ent = crDAO.getCreditRequestEntity(creditRequestId);
        logger.info("проверяем, нужно ли верификатору заполнять анкету по заявке "+creditRequestId.toString());

        if (ent==null){
        	logger.severe("Не найдена заявка с id "+creditRequestId.toString());
        	throw new ActionException(0,"Не найдена заявка с id "+creditRequestId.toString(), Type.TECH, ResultType.FATAL,null);
        }

        Map<String,Object> config=productBean.getOverdueProductConfig(ent.getProductId().getId());
	    Integer i = Convertor.toInteger(config.get(ProductKeys.VERIFICATOR_MUST_EDIT));

        return (i == 1);
    }


    /**
     * была ли оплачена комиссия для продления, сейчас не используем, удалить???
     */
    @Override
    public boolean prolongPayed(Integer creditRequestId){
    	PaymentEntity pay=paymentService.getPaymentFromClient(creditRequestId, 0, Payment.COMISSION, new Date());
    	if (pay!=null)
    		return true;
    	else
    		return false;
    }

    public Refinance findRefinanceCompleted(Integer creditId) {
    	List<RefinanceEntity> lst = creditDAO.findRefinance(creditId, null, ActiveStatus.ACTIVE);
    	if (lst.size() == 0) {
    		return null;
    	} else {
    		return new Refinance(lst.get(0));
    	}
    }

    @Override
    public Refinance getRefinance(int id) {
    	return creditDAO.getRefinance(id, null);
    }

    @Override
    public Map<String, Object> calcRefinanceData(Integer refinanceId, Integer creditId) {

    	RefinanceEntity refinance = creditDAO.findRefinanceDraft(creditId);
    	if (refinance == null) {
    		return new HashMap<String,Object>(0);
    	}

    	logger.info("Рассчитываем параметры рефинансирования "+creditId);
    	Map<String, Object> res = creditCalc.calcRefinance(creditId, refinance.getRefinanceDate());
    	logger.info("Сумма для начала рефинансирования "+refinance.getRefinanceAmount());
    	// выплаченная сумма со дня рефинансирования
    	double sumPays = 0d;

    	String dateString=String.valueOf(DatesUtils.getDay(refinance.getRefinanceDate()))
				+"."+String.valueOf(DatesUtils.getMonth(refinance.getRefinanceDate()))
						+"."+String.valueOf(DatesUtils.getYear(refinance.getRefinanceDate()));
        sumPays = paymentBean.getCreditPaymentSum(creditId, null, new DateRange(DateUtils.addDays(Convertor.toDate(dateString, DatesUtils.FORMAT_ddMMYYYY),-1),
        		new Date()));

    	logger.info("Выплаченная сумма "+sumPays);
    	//необходимая сумма для рефинансирования
    	double sumNeed =refinance.getRefinanceAmount();
    	res.put(ProductKeys.SUM_REFINANCE_REMAIN, new Double(sumNeed - sumPays));
    	for(Map.Entry<String, Object> param:res.entrySet()){
    		logger.info("Параметры для рефинансирования "+param.getKey()+" "+param.getValue());
    	}
    	return res;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Map<String, Object> calcProlongData(Integer prolongId, Integer creditId) {

    	ProlongEntity prolong = creditBean.findProlongDraft(creditId);
    	if (prolong == null) {
    		return new HashMap<String,Object>(0);
    	}

    	logger.info("Рассчитываем параметры продления "+creditId);

    	Map<String, Object> res = creditCalc.calcCredit(creditId, prolong.getLongdate());
    	logger.info("Сумма процентов "+(Convertor.toDouble(res.get(CreditCalculatorBeanLocal.SUM_PERCENT))).toString());
     	//сумма процентов - считаем после оплаты, должен остаться 0
    	double sumPercent = Convertor.toDouble(res.get(CreditCalculatorBeanLocal.SUM_PERCENT));
    	//оставшаяся сумма процентов - равна сумме процентов
    	res.put(CreditCalculatorBeanLocal.SUM_PERCENT_REMAIN, new Double(sumPercent));
       	for(Map.Entry<String, Object> param:res.entrySet()){
    		logger.info("Параметры для продления "+param.getKey()+" "+param.getValue());
    	}
    	return res;
    }

    @Override
    public Integer getProlongNew(Integer creditId) {
    	ProlongEntity prolong = creditBean.findProlongDraft(creditId);
    	if (prolong == null) {
    		return null;
    	} else {
    		return prolong.getId();
    	}
    }

    @Override
    public Integer getRefinanceNew(Integer creditId) {
    	RefinanceEntity refinance = creditDAO.findRefinanceDraft(creditId);
    	if (refinance == null) {
    		return null;
    	} else {
    		return refinance.getId();
    	}
    }

    /**
     * Отменить продление
     * @param creditId id кредита
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean prolongCRCancel(Integer creditId) {
    	try {
			creditBean.cancelProlongDraft(creditId);
			return true;
		} catch (KassaException e) {
			logger.log(Level.SEVERE, "Ошибка при отмене продления по кредиту " + creditId.toString(), e);
			return false;
		}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean refinanceCancel(Integer creditId) {
    	try {
			creditBean.cancelRefinanceDraft(creditId);
			return true;
		} catch (KassaException e) {
			logger.log(Level.SEVERE, "Ошибка при отмене рефинансирования по кредиту " + creditId.toString(), e);
			return false;
		}
    }

    @Override
    public boolean prolongCRSave(Integer creditId) {
    	try {
    		logger.info("Делаем активным продление по кредиту "+creditId);
			kassaBean.startProlong(creditId);
			return true;
		} catch (KassaException e) {
			logger.log(Level.SEVERE, "Ошибка при продлении кредита " + creditId.toString(), e);
			return false;
		}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean refinanceSave(Integer creditId) {
    	try {
    		logger.info("Делаем активным рефинансирование по кредиту "+creditId);
			creditBean.startRefinance(creditId);
			return true;
		} catch (KassaException e) {
			logger.log(Level.SEVERE, "Ошибка при рефинансировании кредита " + creditId.toString(), e);
			return false;
		}
    }

    @Override
    public PluginConfig refreshPluginConfig(PluginConfig plc, ActionContext context) {
        return context.getPlugins().getPluginConfig(plc.getPluginName());
//		PluginSystemLocal plg = context.getPlugins().getPluginByName(plc.getPluginName());
//		plc.setPlugin(plg);
    }

    /**
     * Начислить кредит, но не выплачивать его. Возвращает id кредита.
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int createCredit(Integer creditRequestId) throws ActionException, ActionRuntimeException {
        try {
            return kassaBean.saveCreditApprove(creditRequestId, null);
        } catch (KassaException e) {
        	logger.severe("Не удалось начислить кредит "+e);
            throw new ActionException(0, "Не удалось начислить кредит", Type.TECH, ResultType.FATAL, e);
        }
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
  //  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public int createCreditNew(Integer creditRequestId) throws ActionException, ActionRuntimeException {
        try {
            return kassaBean.startCredit(creditRequestId);
        } catch (KassaException e) {
        	logger.severe("Не удалось начислить кредит "+e);
            throw new ActionException(0, "Не удалось начислить кредит", Type.TECH, ResultType.FATAL, e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean sendLowBalanceSms(String number, Integer paymentSystemCode, Double balance) {
        if (StringUtils.isEmpty(number)||paymentSystemCode==null){
    		return false;
    	}
		Boolean exist = (mailBean.findPaymentMessageByPaymentCode(paymentSystemCode) != null);
		logger.info("paymentSystemCode for balance "+paymentSystemCode);
		logger.info("message exists in db "+exist);
        Boolean result = false;
        if (!exist) {
            Reference paySystemName = refBooks.findByCodeInteger(RefHeader.ACCOUNT_TYPE, paymentSystemCode);
            ProductMessagesEntity message = productDAO.findMessageByName(ProductKeys.SMS_LOW_PAYMENT_TEXT);
            Object[] params = new Object[]{paySystemName.getName(), balance};
            result = sendSMSCommon(null, number, message.getBody(), params);
            if (result) {
                mailBean.savePaymentMessage(paymentSystemCode, new Date());
            }
        }
        return result;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deletePaymentMessageByPaymentCode(Integer paymentSystemCode) {
        mailBean.deletePaymentMessageByPaymentCode(paymentSystemCode);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeReferenceStatus(Integer refHeaderID, Integer codeInteger, Integer status) {
        refBooks.changeReferenceStatus(refHeaderID, codeInteger, status);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void checkPaymentSystems(Map<String, Double> systemBalances, Map<String, Object> params) {
        Double requiredBalance = Convertor.toDouble(params.get("requiredBalance"));
        Double smsRequiredBalance = Convertor.toDouble(params.get("smsRequiredBalance"));
        Integer sendSmsRequiredBalance = Convertor.toInteger(params.get("sendSmsRequiredBalance"));
        String phoneNumber = Convertor.toString(params.get("smsSendPhoneNumber"));

        for (String systemName : systemBalances.keySet()) {
            Double balance = systemBalances.get(systemName);
            if (balance != null) {
                if (YandexPayBeanLocal.SYSTEM_NAME.equals(systemName)) {
                    ReferenceEntity ref = refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, Account.YANDEX_TYPE);
                    // если система включена и баланс меньше суммы необходимой для отправки смс и отправка смс включена, то шлем смс о низком балансе
                    if (ref.getIsactive() == ActiveStatus.ACTIVE && balance < smsRequiredBalance && sendSmsRequiredBalance == ActiveStatus.ACTIVE) {
                        sendLowBalanceSms(phoneNumber, ref.getCodeinteger(), balance);
                    }
                    // если баланс больше, удаляем запись об отправленой смс
                    if (balance > smsRequiredBalance) {
                        deletePaymentMessageByPaymentCode(ref.getCodeinteger());
                    }

                    // если баланс меньше отключаемой суммы
                    if (balance < requiredBalance) {
                        // отключаем кошелек яндекса
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.YANDEX_TYPE, ActiveStatus.ARCHIVE);
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.BANK_TYPE, ActiveStatus.ARCHIVE);
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.YANDEX_CARD_TYPE, ActiveStatus.ARCHIVE);
                        // проверка всех систем какие работают с картами
                        Double winPayBalance = systemBalances.get(WinpayPayBeanLocal.SYSTEM_NAME);
                        if (winPayBalance == null || winPayBalance < requiredBalance) {
                            // отключаем карты
                            changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.CARD_TYPE, ActiveStatus.ARCHIVE);
                        }
                    } else {
                        // включаем все
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.YANDEX_TYPE, ActiveStatus.ACTIVE);
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.YANDEX_CARD_TYPE, ActiveStatus.ACTIVE);
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.CARD_TYPE, ActiveStatus.ACTIVE);
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.BANK_TYPE, ActiveStatus.ACTIVE);
                    }
                } else if (ContactPayBeanLocal.SYSTEM_NAME.equals(systemName)) {
                    ReferenceEntity ref = refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, Account.CONTACT_TYPE);
                    // если система включена и баланс меньше суммы необходимой для отправки смс и отправка смс включена, то шлем смс о низком балансе
                    if (ref.getIsactive() == ActiveStatus.ACTIVE && balance < smsRequiredBalance && sendSmsRequiredBalance == ActiveStatus.ACTIVE) {
                        sendLowBalanceSms(phoneNumber, ref.getCodeinteger(), balance);
                    }
                    // если баланс больше, удаляем запись об отправленой смс
                    if (balance > smsRequiredBalance) {
                        deletePaymentMessageByPaymentCode(ref.getCodeinteger());
                    }

                    if (balance < requiredBalance) {
                        // отключаем контакт
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.CONTACT_TYPE, ActiveStatus.ARCHIVE);
                    } else {
                        // включаем
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.CONTACT_TYPE, ActiveStatus.ACTIVE);
                    }
                } else if (WinpayPayBeanLocal.SYSTEM_NAME.equals(systemName)) {
                    ReferenceEntity ref = refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, Account.CARD_TYPE);
                    // если система включена и баланс меньше суммы необходимой для отправки смс и отправка смс включена, то шлем смс о низком балансе
                    if (ref.getIsactive() == ActiveStatus.ACTIVE && balance < smsRequiredBalance && sendSmsRequiredBalance == ActiveStatus.ACTIVE) {
                        sendLowBalanceSms(phoneNumber, ref.getCodeinteger(), balance);
                    }
                    // если баланс больше, удаляем запись об отправленой смс
                    if (balance > smsRequiredBalance) {
                        deletePaymentMessageByPaymentCode(ref.getCodeinteger());
                    }

                    if (balance < requiredBalance) {
                        // отключаем карты
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.CARD_TYPE, ActiveStatus.ARCHIVE);
                    } else {
                        // включаем
                        changeReferenceStatus(RefHeader.ACCOUNT_TYPE, Account.CARD_TYPE, ActiveStatus.ACTIVE);
                    }
                }
            }
        }
    }

    @Override
    public Integer daysWaitToSignOferta(CreditRequest request) {
        Map<String, Object> params = productBean.getAllProductConfig(request.getProduct().getId());
        String param = Convertor.toString(params.get(ProductKeys.CREDIT_WAIT_SIGN_OFERTA));
        logger.info("CREDIT_WAIT_SIGN_OFERTA param value: " + param);
        Integer result = Convertor.toInteger(param.replaceAll("\\D", ""));
        logger.info("CREDIT_WAIT_SIGN_OFERTA param value after convert: " + result);
        return result;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Map<String, Double> checkBalancePaymentSystem() {
        PluginExecutionContext plctx;
        ActionContext actionContext = createActionContext(null, true);

        Map<String, Double> systemBalances = new HashMap<>();
        // баланс Яндекс
        plctx = new PluginExecutionContext(actionContext.getPlugins().getPluginConfig(YandexPayBeanLocal.SYSTEM_NAME), null, 0, Collections.<String, Object>emptyMap(), actionContext.getPluginState(YandexPayBeanLocal.SYSTEM_NAME));
        if (plctx.getPluginConfig().getIsActive()) {
            YandexPayBeanLocal yandexPay = actionContext.getPlugins().getYandexPay();
            try {
                Double balance = yandexPay.balance(plctx);
                systemBalances.put(YandexPayBeanLocal.SYSTEM_NAME, balance);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Не удалось получить баланс по Яндекс деньги");
            }
        }

        // баланс Контакта
        plctx = new PluginExecutionContext(actionContext.getPlugins().getPluginConfig(ContactPayBeanLocal.SYSTEM_NAME), null, 0, Collections.<String, Object>emptyMap(), actionContext.getPluginState(ContactPayBeanLocal.SYSTEM_NAME));
        if (plctx.getPluginConfig().getIsActive()) {
            ContactPayBeanLocal contactPay = actionContext.getPlugins().getContactPay();
            try {
                Double balance = contactPay.doCheckRest(plctx);
                systemBalances.put(ContactPayBeanLocal.SYSTEM_NAME, balance);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Не удалось получить баланс по Контакту");
            }
        }

        // баланс WinPay
        plctx = new PluginExecutionContext(actionContext.getPlugins().getPluginConfig(WinpayPayBeanLocal.SYSTEM_NAME), null, 0, Collections.<String, Object>emptyMap(), actionContext.getPluginState(WinpayPayBeanLocal.SYSTEM_NAME));
        if (plctx.getPluginConfig().getIsActive()) {
            WinpayPayBeanLocal winpayPay = actionContext.getPlugins().getWinpayPay();
            try {
                Double balance = winpayPay.balance(plctx);
                systemBalances.put(WinpayPayBeanLocal.SYSTEM_NAME, balance);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Не удалось получить баланс по WinPay");
            }
        }

        return systemBalances;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void refuseCredit(Integer creditRequestId, String comment, Integer reason) throws ActionException,
            ActionRuntimeException {
        try {
            kassaBean.saveCreditRefuse(creditRequestId, comment, reason);
        }  catch (KassaException e) {
        	logger.severe("Не удалось выполнить действие отказать в кредите");
            throw new ActionException(0, "Не удалось выполнить действие отказать в кредите", Type.TECH, ResultType.FATAL, e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Integer findContactPaymentId(Object businessObjectId) {
    	int creditRequestId = Convertor.toInteger(businessObjectId);
    	CreditRequestEntity cred = crDAO.getCreditRequestEntity(creditRequestId);
    	if (cred.getAcceptedcreditId() == null) {
    		return null;
    	}
    	PaymentEntity pay = payDAO.findPaymentToClient(cred.getAcceptedcreditId().getId());
    	if (pay == null) {
    		return null;
    	}
    	if (pay.getAccountId() != null) {
	    	if (pay.getAccountId().getAccountTypeId().getCodeinteger() == Account.CONTACT_TYPE ) {
	    		return pay.getId();
	    	} else {
	    		return null;
	    	}
    	}
    	if (pay.getAccountTypeId() != null && pay.getAccountTypeId().getCodeinteger() == Account.CONTACT_TYPE) {
    		return pay.getId();
    	}
    	return null;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveClientRefuse(Integer creditRequestId, Integer userId) throws ActionException, WorkflowException {
    	try {
            logger.info("Save client refuse creditRequestID: " + creditRequestId);
    		kassaBean.saveClientRefuse(creditRequestId,userId);
    	}  catch (KassaException e) {
        	logger.severe("Не удалось выполнить действие отказать в кредите");
            throw new ActionException(0, "Не удалось выполнить действие отказать в кредите", Type.TECH, ResultType.FATAL, e);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Payment getPayment(int paymentId) {
    	return payDAO.getPayment(paymentId, Utils.setOf());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean isActionAvailable(String businessObjectClass, Integer businessObjectId, String actionName) {
    	if (CreditRequest.class.getName().equals(businessObjectClass) && "cancel".equals(actionName)) {
    		// ищем, остались ли неотозванные платежи
    		CreditRequestEntity cred = crDAO.getCreditRequestEntity(businessObjectId);

    		PaymentFilter filter = new PaymentFilter();
    		filter.setPaymentTypeId(refBooks.findByCodeInteger(RefHeader.PAYMENT_TYPE, Payment.FROM_SYSTEM).getId());
    		List<PaymentEntity> lstPay = paymentService.findPayments(0, 0, null, filter);
    		for (PaymentEntity pay: lstPay) {
    			if (! EnumSet.of(PaymentStatus.REVOKED, PaymentStatus.ERROR, PaymentStatus.DELETED).contains(pay.getStatus()) ) {
    				return false;
    			}
    		}
    		return true;
    	}
    	// TODO
    	return false;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void refuseClient(int creditRequestId, int userId) throws ActionException {
    	try {
	    	try {
	    		BusinessObjectResult bres = new BusinessObjectResult(CreditRequest.class.getName(), new Integer(creditRequestId), true, null);
	    		CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(creditRequestId);
	    		if (creditRequest.getWayId().getCodeinteger()==RefCreditRequestWay.DIRECT){
	    			wfBean.goProcCROffline(creditRequestId, SignalRef.toString(ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE, null, ProcessKeys.MSG_CLIENT_REJECT), Utils.<String, Object>mapOf(ProcessKeys.VAR_TASK_RESULT, bres));
	    		} else {
	    		    wfBean.goProcCR(creditRequestId, SignalRef.toString(ProcessKeys.DEF_CREDIT_REQUEST, null, ProcessKeys.MSG_CLIENT_REJECT), Utils.<String, Object>mapOf(ProcessKeys.VAR_TASK_RESULT, bres));
	    		}
	    		wfBean.goProcClientReject(creditRequestId, userId);
	    		eventLog.saveLog(EventType.INFO, EventCode.CLIENT_REFUSE, "Отказ клиента от заявки " + String.valueOf(creditRequestId), userId, new Date(), creditRequest, null, null);
	    	}  catch (WorkflowException e) {
	        	logger.severe("Не удалось выполнить действие отказ клиента " + String.valueOf(creditRequestId));
	            throw new ActionException(0, "Не удалось выполнить действие отказать в кредите", Type.TECH, ResultType.FATAL, e);
	        }
    	} catch (Exception ex) {
    		logger.severe("Не удалось выполнить действие отказ клиента " + String.valueOf(creditRequestId));
    	}

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void approveCredit(Integer creditRequestId, String comment) throws ActionException {
        try {
            kassaBean.saveCreditApproveWithoutCredit(creditRequestId, comment);
        }  catch (KassaException e) {
        	logger.severe("Не удалось выполнить действие одобрить кредит");
            throw new ActionException(0, "Не удалось выполнить действие одобрить кредит", Type.TECH, ResultType.FATAL, e);
        }
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveCreditRequestDecision(Integer creditRequestId, boolean bAccepted,
                                          Integer rejectCode) throws ActionException, ActionRuntimeException {
        try {
            kassaBean.saveCreditDecision(creditRequestId, bAccepted, rejectCode);
        } catch (KassaException e) {
            throw new ActionException(0, "Не удалось сохранить состояние заявки", Type.TECH, ResultType.FATAL, e);
        }
    }
    
    @Override
    public Integer findActiveModelId(String modelKey, Integer productId, Integer wayId) {
    	AIModel model = modelBean.findActiveModel(modelKey, productId, wayId);
        if (model == null) {
        	return null;
        } else {
        	return model.getId();
        }
    }
    
    @Override
    public void deleteResults(String businessObjectClass, Integer businessObjectId) {
    	modelBean.deleteResults(businessObjectClass, businessObjectId);
    }

    @Override
    public Map<String,Object> calcModel(String modelKey, DecisionState decisionState, String businessObjectClass,
                             Object businessObjectId, ActionContext actionContext, Integer modelId,Integer productId, Integer wayId, Map<String, Object> mpVars) {
    	Map<String,Object> mapRes = Utils.mapOf();
    	List<AIModel> models = null;
    	AIModel model = null;
    	//ищем активную модель
        if (modelId == null) {
        	model = modelBean.findActiveModel(modelKey, productId, wayId);
        } else {
            model = modelBean.getModel(modelId, Utils.setOf());
        }
        //нет модели для расчета, возвращаемся
        if (model == null) {
            logger.severe("Нет активной модели для расчёта");
            decisionState.finish(false, DecisionKeys.RESULT_UNDEFINED);
            return mapRes;
        }

        //ищем движок для расчета модели
        ScriptEngineInfo engineInfo = serviceBean.selectEngine(model);
        ScriptEngine engine = engineInfo.getScriptEngine();
        //нет движка для расчета, возвращаемся
        if (engine == null) {
            logger.severe("Не найден движок для типа " + model.getMimeType());
            decisionState.finish(false, DecisionKeys.RESULT_UNDEFINED);
            return mapRes;
        }

        Integer creditRequestId = 0;
        logger.info("Класс бизнес-объекта "+businessObjectClass);
        logger.info("Id бизнес-объекта "+businessObjectId);
        //расчеты по заявке
        if (CreditRequest.class.getName().equals(businessObjectClass)) {
            // ищем заявку
            creditRequestId = Convertor.toInteger(businessObjectId);
            CreditRequest ccRequest;
            try {
                ccRequest = crDAO.getCreditRequest(creditRequestId, Utils.setOf(
                		PeopleMain.Options.INIT_DEBT,
                		PeopleMain.Options.INIT_DOCUMENT,
                		PeopleMain.Options.INIT_BLACKLIST,
                		PeopleMain.Options.INIT_EMPLOYMENT,
    	       			PeopleMain.Options.INIT_PEOPLE_PERSONAL,
    	    			PeopleMain.Options.INIT_ADDRESS,
    	    			PeopleMain.Options.INIT_ACCOUNT,
    	    			PeopleMain.Options.INIT_NEGATIVE,
    	    			PeopleMain.Options.INIT_PEOPLE_CONTACT,
    	    			PeopleMain.Options.INIT_PEOPLE_MISC,
    	    			PeopleMain.Options.INIT_SPOUSE,
    	    			PeopleMain.Options.INIT_CREDIT,
    	    			PeopleMain.Options.INIT_SCORING,
    	    			PeopleMain.Options.INIT_VERIF,
    	    			PeopleMain.Options.INIT_PHONESUMMARY,
    	    			CreditRequest.Options.INIT_DEBT,
    	    			CreditRequest.Options.INIT_DOCUMENT,
    	    			CreditRequest.Options.INIT_SCORING,
    	    			CreditRequest.Options.INIT_CREDIT,
    	    			CreditRequest.Options.INIT_CREDIT_HISTORY,
    	    			CreditRequest.Options.INIT_NEGATIVE,
    	    			CreditRequest.Options.INIT_SUMMARY,
    	    			CreditRequest.Options.INIT_REQUESTS,
    	    			CreditRequest.Options.INIT_VERIF,
    	    			CreditRequest.Options.INIT_DEVICEINFO,
                        CreditRequest.Options.INIT_ANTIFRAUD,
                        CreditRequest.Options.INIT_ANTIFRAUD_SUSPICION,
                        BaseCredit.Options.INIT_PAYMENTS
    	    			));
            } catch (Exception e) {
                logger.severe("Заявка " + creditRequestId + " не найдена");
                decisionState.finish(false, DecisionKeys.RESULT_UNDEFINED);
                return mapRes;
            }
            //инициализируем заявку для модели
            engine.put(ProcessKeys.VAR_CREDIT_REQUEST, ccRequest);
            //инициализируем вопросы верификатора для модели
            VerifyScriptObject vers = new VerifyScriptObject(creditRequestId, actionContext.getPlugins().getVerify(), questBean, actionContext);
            engine.put("verify", vers);


            //инициализируем скрипт для вопросов идетификации
            IdentityScriptObject identityScript = new IdentityScriptObject(creditRequestId,
                    actionContext.getPlugins().getPluginConfig(IdentityBeanLocal.SYSTEM_NAME), identityQuestion);
            engine.put("identity", identityScript);

        } else if (Credit.class.getName().equals(businessObjectClass)) {
            // кредит
        	// TODO - если модель будет работать с кредитами
        }

        // очищаем что нужно вызывать
        for (String skey : decisionState.getExternalSystems().keySet()) {
            decisionState.getExternalSystems().get(skey).setNeedCall(false);
            if (skey.startsWith("plugin_")) {
            	String plname = skey.replace("plugin_", "");
            	//ищем плагин по названию
            	PluginConfig plc = actionContext.getPlugins().getPluginConfig(plname);
            	//если не нашли, берем фейк
            	if (plc == null) {
            		decisionState.getExternalSystems().get(skey).isWork = false;
            	} else {
            		decisionState.getExternalSystems().get(skey).isWork = (! plc.isUseFake());
            	}
            }
        }


        //инициализируем скрипты и переменные для модели
        engine.put(ProcessKeys.VAR_DECISION_STATE, decisionState);
        engine.put(ProcessKeys.VAR_PEOPLE_SEARCH, new PeopleSearchScriptObject(peopleBean));
        engine.put(ProcessKeys.VAR_CREDIT_SEARCH, new CreditSearchScriptObject(creditBean));
        engine.put(ProcessKeys.VAR_CREDIT_REQUEST_SEARCH, new CreditRequestSearchScriptObject(kassaBean));
        engine.put(ProcessKeys.VAR_SCORING_SEARCH, new ScoringSearchScriptObject());
        engine.put(ProcessKeys.VAR_VERIFICATION_SEARCH, new VerificationSearchScriptObject());
        engine.put(ProcessKeys.VAR_DEBT_SEARCH, new DebtSearchScriptObject());
        engine.put(ProcessKeys.VAR_NEGATIVE_SEARCH, new NegativeSearchScriptObject());
        engine.put(ProcessKeys.VAR_SUMMARY_SEARCH, new SummarySearchScriptObject());
        engine.put(ProcessKeys.VAR_EVENTLOG_SEARCH, new EventLogSearchScriptObject(eventLog));
        engine.put(ProcessKeys.VAR_DATESUTILS, new DatesUtils());
        engine.put(ProcessKeys.VAR_ANTIFRAUD_SEARCH, new AntifraudSearchScriptObject());
        engine.put(ProcessKeys.VAR_IDENTITY_QUESTION_SEARCH, new IdentitySearchScriptObject());
        engine.put("SV_ANY", SearchValue.ANY);
        
        // TODO
        engine.put(ProcessKeys.VAR_NO_AUTO_REFUSE, new Integer(0));
        engine.put(ProcessKeys.VAR_SAVE_VARIABLES, new Integer(0));

        // записываем стандартные переменные
        for (Map.Entry<String, Object> entry: mpVars.entrySet()) {
        	engine.put(entry.getKey(), entry.getValue());
        }

        try {
        	//выполняем скрипт
        	String scr = engineInfo.getJsPreface() + model.getBody();
        	engine.eval(scr);

            VerifyScriptObject vers = (VerifyScriptObject) engine.get("verify");
            if (vers != null) {
                ArrayList<String> lst = new ArrayList<>(vers.getQuestions().size());
                lst.addAll(vers.getQuestions().keySet());
                mapRes.put("verify", lst);
            }
        } catch (ScriptException e) {
            if (CreditRequest.class.getName().equals(businessObjectClass)) {
                logger.log(Level.SEVERE, "Ошибка при расчётах по заявке " + creditRequestId.toString(), e);
            } else if (Credit.class.getName().equals(businessObjectClass)) {
                // TODO - если модель будет работать с кредитами
                logger.log(Level.SEVERE, "Ошибка при расчётах модели " + modelKey, e);
            } else {
                logger.log(Level.SEVERE, "Ошибка при расчётах модели " + modelKey, e);
            }
            decisionState.internalFinish(false, DecisionKeys.RESULT_UNDEFINED);
            decisionState.addError(String.valueOf(ErrorKeys.MODEL_SYSTEM_ERROR), e.getMessage());
        } catch (Throwable e) {
            decisionState.internalFinish(false, DecisionKeys.RESULT_UNDEFINED);
            decisionState.addError(String.valueOf(ErrorKeys.MODEL_SYSTEM_ERROR), e.getMessage());
        }
        
        // вытаскиваем стандартные переменные
        for (String vname: mpVars.keySet()) {
        	Object avalue = engine.get(vname);
        	mpVars.put(vname, avalue);
        }
        // для обратной совместимости добавляем все переменные, сохранённые в DecisionState
        mpVars.putAll(decisionState.getVars());
        return mapRes;
    }
    
    @Override
    public Map<String, Object> getAIModelParams(int modelId, ActionContext actionContext) {
        Map<String, Object> mpVars = new HashMap<String, Object>(10);
        modelBean.loadModelParams(modelId, actionContext.getCustomizationKey(), mpVars); 
        return mpVars;
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)	
    public void setAIModelParamValues(int modelId, ActionContext actionContext, Map<String, Object> mpVars, boolean bSaveValues) {
    	modelBean.saveModelParams(modelId, actionContext.getCustomizationKey(), mpVars);
    	if (bSaveValues) {
    		modelBean.deleteResults((String) mpVars.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS), (Integer) mpVars.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID));
    		modelBean.saveModelParamValues(modelId, actionContext.getCustomizationKey(), mpVars, null);
    	}
    }  
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)	
    public int setAIModelParamValues(int modelId, ActionContext actionContext, Map<String, Object> mpVars, Integer run_id, boolean bSaveValues) {
    	modelBean.saveModelParams(modelId, actionContext.getCustomizationKey(), mpVars);
    	if (bSaveValues) {
    		run_id = modelBean.saveModelParamValues(modelId, actionContext.getCustomizationKey(), mpVars, run_id);
    	}
    	return run_id;
    }     

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public void checkDefaultModelParams(int modelId, ActionContext actionContext) {
    	modelBean.checkDefaultModelParams(modelId, actionContext.getCustomizationKey());
    }

    @Override
    public boolean sendSMS(Integer peopleMainId, String smsText) {
       return sendSMS(peopleMainId,smsText,null);
    }

    @Override
    public boolean sendSMS(Integer peopleMainId, String smsText,String phone) {
        if (peopleMainId==null&&StringUtils.isEmpty(phone)){
        	return false;
        }
        if (peopleMainId!=null){
            //ищем активный телефон человека
    	    PeopleContactEntity phoneContact = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, peopleMainId);

            if (phoneContact == null || StringUtils.isBlank(phoneContact.getValue())) {
        	    return false;
            }
            phone=phoneContact.getValue();
        }
        try {
        	boolean bres = mailBean.sendSMSV2(phone, smsText);
        	if (peopleMainId!=null){
        	    mailBean.saveMessage(peopleMainId, null, Messages.SMS, Reference.AUTO_EXEC, new Date(), smsText, null);
        	}
        	return bres;
        } catch (Throwable ex) {
        	logger.log(Level.SEVERE, "СМС не было отправлено на " + phone, ex);
        	return false;
        }

    }
    
    @Override
    public boolean sendEmail(Integer peopleMainId, String subject, String body) {
    	return sendEmail(peopleMainId,null,subject,body);
    }

    @Override
    public boolean sendEmail(Integer peopleMainId,String email, String subject, String body) {
    	//если нет ни адреса, ни id человека
    	if (peopleMainId==null&&StringUtils.isEmpty(email)){
        	return false;
        }
    	//ищем активный email человека, если у нас есть id человека
    	if (peopleMainId!=null){
    	    PeopleContactEntity contact = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, peopleMainId);
            if (contact == null || StringUtils.isBlank(contact.getValue()) ) {
        	    return false;
            }
            email=contact.getValue();
    	}
    	try {
        	mailBean.send(subject, body, email);
        	//если человек есть в системе, пишем запись об отправке письма в БД
        	if (peopleMainId!=null){
        	    mailBean.saveMessage(peopleMainId, null, Messages.EMAIL, Reference.AUTO_EXEC, new Date(), subject, null);
        	}
        } catch (Throwable ex) {
        	logger.log(Level.SEVERE, "Письмо не было отправлено на " + email, ex);
        	return false;
        }
        return true;
    }

    @Override
    public boolean sendSMSByCreditRequest(int creditRequestId, String smsText) {
        CreditRequestEntity ent = crDAO.getCreditRequestEntity(creditRequestId);
        if (ent==null){
        	return false;
        }
        return sendSMS(ent.getPeopleMainId().getId(), smsText);
    }

    @Override
    public boolean sendSMSKByCreditRequest(Integer productId,int creditRequestId, String bpName, String smsMessageKey, Object... params) {
        String formattedText =null;
        if (productId==null){
    		productId=productDAO.getProductDefault().getId();
    	}
        if (bpName.equals(ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE)){
    		bpName=ProcessKeys.DEF_CREDIT_REQUEST;
    	}
    	if (bpName.equals(ProcessKeys.DEF_CREDIT_CONSIDER_OFFLINE)){
    		bpName=ProcessKeys.DEF_CREDIT_CONSIDER;
    	}
        ProductMessagesEntity message=wfEngine.getProductMessageForBP(productId, bpName, smsMessageKey);

    	if (message!=null){
    	    formattedText =message.getBody();
    	}
        if (formattedText == null) {
            return false;
        } else {
            String smsg = formattedText;
            if (params != null && params.length > 0) {
            	  try{
                      logger.info("Строка до преобразования "+smsg);
                      smsg = String.format(formattedText, params);
                      logger.info("Строка после преобразования "+smsg);
                   } catch (IllegalFormatException e){
                 		logger.severe("Не удалось преобразовать строку "+smsg+" ошибка "+e);
                 	}
            }
            return sendSMSByCreditRequest(creditRequestId, smsg);
        }
    }

    @Override
    public boolean sendSMSKProduct(Integer productId,int peopleMainId, String bpName, String smsMessageKey, Object[] params){
    	if (productId==null){
    		productId=productDAO.getProductDefault().getId();
    	}

    	if (bpName.equals(ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE)){
    		bpName=ProcessKeys.DEF_CREDIT_REQUEST;
    	}
    	if (bpName.equals(ProcessKeys.DEF_CREDIT_CONSIDER_OFFLINE)){
    		bpName=ProcessKeys.DEF_CREDIT_CONSIDER;
    	}
    	ProductMessagesEntity message=wfEngine.getProductMessageForBP(productId, bpName, smsMessageKey);
    	if (message!=null){
    	    String formattedText =message.getBody();
    	    return sendSMSCommon(peopleMainId, formattedText, params);
    	}
    	return false;
    }

    @Override
    public boolean sendSMSCommon(Integer peopleMainId, String formattedText, Object... params) {
        return sendSMSCommon(peopleMainId,null,formattedText,params);
    }

    @Override
    public boolean sendSMSCommon(Integer peopleMainId, String phone,String formattedText, Object... params){
    	if (StringUtils.isEmpty(formattedText)) {
            return false;
        } else {
            String smsg = formattedText;
            if (params != null && params.length > 0) {
               try{
                   logger.info("Строка до преобразования "+smsg);
                   smsg = String.format(formattedText, params);

                } catch (IllegalFormatException e){
              		logger.severe("Не удалось преобразовать строку "+smsg+" ошибка "+e);
              	}
            }
            logger.info("Строка после преобразования "+smsg);
            return sendSMS(peopleMainId, smsg,phone);
        }
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void toCollector(int creditId) {
    	creditBean.creditToCollector(creditId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void toCourt(int creditId) {
    	logger.info("Начали передачу кредита " + String.valueOf(creditId) + " в суд");
    	try {
    		creditBean.creditToCourt(creditId, new Date());
			logger.info("Успешно передали кредит " + String.valueOf(creditId) + " в суд");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Не передали кредит " + String.valueOf(creditId) + " в суд по причине", e);
		}

    }

    @Override
    public boolean sendEmailCommon(Integer peopleMainId, String formattedTextSubject, String formattedTextBody, Object... params) {
       return sendEmailCommon(peopleMainId,null,formattedTextSubject,formattedTextBody,params);
    }

    @Override
    public boolean sendEmailCommon(Integer peopleMainId, String email,String formattedTextSubject, String formattedTextBody, Object... params) {
        if (StringUtils.isEmpty(formattedTextBody) && StringUtils.isEmpty(formattedTextSubject)) {
            return false;
        } else {
            String smsgBody = formattedTextBody;
            if (smsgBody == null) {
            	smsgBody = "";
            }
            if (params != null && params.length > 0) {
               try {
            	 //  logger.info("Строка до преобразования, body "+smsgBody);
            	   smsgBody = String.format(smsgBody, params);

                 //  logger.info("Строка после преобразования, body "+smsgBody);

               } catch (Exception e){
           		   logger.log(Level.SEVERE,"Не удалось преобразовать строку  ",e);
           	  }
            }

            try {
                //генерируем письмо с версткой
                Map<String,Object> mp=new HashMap<String,Object>();
                // TODO: преобразовать smsgBody, используя EL-Processor и параметры
                // Object atext = serviceBean.evaluateEL(smsgBody, vars);
                mp.put("description", smsgBody);
                
                ReportGenerated rgen = reportBean.generateReport(null, Report.EMAIL_ID, mp);
                smsgBody = rgen.getAsString();
                if (peopleMainId!=null){
                    logger.info("Сгенерировали письмо для "+peopleMainId);
                } else {
                	logger.info("Сгенерировали письмо для "+email);
                }
            } catch (Exception e){
        		logger.severe("Не удалось сгенерировать письмо "+smsgBody+" ошибка "+e);
            }

            String smsgSubject = formattedTextSubject;
            if (smsgSubject == null) {
            	smsgSubject = "";
            }
            if (params != null && params.length > 0) {
            	try {
            	    logger.info("Строка до преобразования, subject "+smsgSubject);
               	    smsgSubject = String.format(smsgSubject, params);
               	    logger.info("Строка после преобразования, subject "+smsgSubject);
            	} catch (IllegalFormatException e){
            		logger.severe("Не удалось преобразовать строку, subject "+smsgSubject+" ошибка "+e);
            	}
            }

            return sendEmail(peopleMainId, email,smsgSubject, smsgBody);
        }
    }

    @Override
    public boolean sendEmailKProduct(Integer productId, int peopleMainId, String bpName, String emailMessageKey, Object... params) {
    	if (productId==null){
    		productId=productDAO.getProductDefault().getId();
    	}
    	//ProductMessagesEntity message=productDAO.findProductMessageByName(productId, bpName, emailMessageKey);
    	if (bpName.equals(ProcessKeys.DEF_CREDIT_REQUEST_OFFLINE)){
    		bpName=ProcessKeys.DEF_CREDIT_REQUEST;
    	}
    	if (bpName.equals(ProcessKeys.DEF_CREDIT_CONSIDER_OFFLINE)){
    		bpName=ProcessKeys.DEF_CREDIT_CONSIDER;
    	}
    	ProductMessagesEntity message=wfEngine.getProductMessageForBP(productId, bpName, emailMessageKey);
    	if (message!=null){
    	    String formattedTextBody =message.getBody();
    	    String formattedTextSubject =message.getSubject();
    	    return sendEmailCommon(peopleMainId, formattedTextSubject, formattedTextBody, params);
    	}
    	return false;

    }

    @Override
    public RulesBeanLocal getRulesBean() {
        return rulesBean;
    }

    @Override
    public ProductBeanLocal getProductBean() {
        return productBean;
    }

    @Override
    public boolean needPluginCalls(DecisionState ds, ActionContext actionContext) {
        String prefix = ProcessKeys.PREFIX_PLUGIN;
        for (Map.Entry<String, ExternalSystemState> entry : ds.getExternalSystems().entrySet()) {
            if (entry.getKey().startsWith(prefix) && entry.getValue().isNeedCall()) {
                String pluginName = entry.getKey().substring(prefix.length());
                if (actionContext.getPlugins().getPluginConfig(pluginName).getIsActive()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> getActivePluginCalls(DecisionState ds, String modelKey, ActionContext actionContext) {
        String prefix = ProcessKeys.PREFIX_PLUGIN;
        ArrayList<String> lst = new ArrayList<String>(ds.getExternalSystems().size());
        for (Map.Entry<String,ExternalSystemState> entry: ds.getExternalSystems().entrySet()) {
            if (entry.getKey().startsWith(prefix) && entry.getValue().isNeedCall() ) {
                String pluginName = entry.getKey().substring(prefix.length() );
                PluginConfig plc = actionContext.getPlugins().getPluginConfig(pluginName);
                if (plc.getIsActive() && plc.getPlugin().getModelTargetsSupported().contains(modelKey)) {
                    lst.add(pluginName);
                    logger.info("Добавили в ActivePluginCalls "+pluginName);
                }
            }
        }
        return lst;
    }

    @Override
    public void bpmnError(String errorCode) throws Exception {
    	throw new BpmnError(errorCode);
    }

    @Override
    public String findPaymentSystemPay(int paymentId, ActionContext actionContext) throws ActionException {
    	PaymentEntity pay = payDAO.getPayment(paymentId);

        logger.info("Ищем платёжный плугин для обработки платежа " + paymentId);

        int accountType = pay.getAccountTypeId().getCodeinteger();
		logger.info("Вид счета " + accountType);
		List<String> preferedPluginNames = rulesBean.getPaymentSystemOrder(accountType);

		List<String> servingPluginNames = new ArrayList<String>(15);
		for (PluginConfig pluginConfig : actionContext.getPlugins().getPluginConfigs()) {
			logger.info("Плугин " +pluginConfig.getPluginName());
			logger.info("Активность " +pluginConfig.getIsActive());
			logger.info("Системное название " +pluginConfig.getPlugin());
			logger.info("Является платежной системой "+(pluginConfig.getPlugin() instanceof PaymentSystemLocal));
			if (pluginConfig.getIsActive() && pluginConfig.getPlugin() != null && pluginConfig.getPlugin() instanceof PaymentSystemLocal) {

 				if ( pluginConfig.getPlugin().getExecutionModesSupported().contains( PluginSystemLocal.ExecutionMode.AUTOMATIC) ) {
					PaymentSystemLocal plugin = (PaymentSystemLocal) pluginConfig.getPlugin();

                    if (Arrays.binarySearch(plugin.getSupportedAccountTypes(), accountType) >= 0) {
                    	if (plugin.getPartnerId() != null && plugin.getPartnerId().equals(pay.getPartnersId().getId())) {
                    		servingPluginNames.add( pluginConfig.getPluginName() );
                    	}
					}
 				}
			}
		}

		if (servingPluginNames.size() == 0) {
            logger.warning("Не найден плугин для обработки счёта " + accountType);
			return null;
		} else {
			// ищем сначала в порядке предпочтения
			for (String sname: preferedPluginNames) {
				if (servingPluginNames.indexOf(sname) >= 0) {
                    logger.info("Выбран плугин для обработки счёта " + accountType + ": " + sname);
					return sname;
				}
			}
			// или возвращаем первую найденную
            logger.info("Выбран плугин для обработки счёта " + accountType + ": " + servingPluginNames.get(0));
			return servingPluginNames.get(0);
		}
    }

	@Override
	public String findPaymentSystemAcc(int accountType, ActionContext actionContext) throws ActionException {
        logger.info("Ищем платёжный плугин для обработки счёта " + accountType);

		logger.info("Вид счета " +accountType);
		List<String> preferedPluginNames = rulesBean.getPaymentSystemOrder(accountType);

		List<String> servingPluginNames = new ArrayList<String>(15);
		for (PluginConfig pluginConfig : actionContext.getPlugins().getPluginConfigs()) {
			logger.info("Плугин " +pluginConfig.getPluginName());
			logger.info("Активность " +pluginConfig.getIsActive());
			logger.info("Является платежной системой "+(pluginConfig.getPlugin() instanceof PaymentSystemLocal));
			if (pluginConfig.getIsActive() && pluginConfig.getPlugin() != null && pluginConfig.getPlugin() instanceof PaymentSystemLocal) {

 				if ( pluginConfig.getPlugin().getExecutionModesSupported().contains( PluginSystemLocal.ExecutionMode.AUTOMATIC) ) {
					PaymentSystemLocal plugin = (PaymentSystemLocal) pluginConfig.getPlugin();
                    logger.info("Поддерживаемые типы счетов: " + StringUtils.join(ArrayUtils.toObject(plugin.getSupportedAccountTypes()), ", "));
                    if (Arrays.binarySearch(plugin.getSupportedAccountTypes(), accountType) >= 0) {
                        servingPluginNames.add( pluginConfig.getPluginName() );
					}
 				}
			}
		}

		if (servingPluginNames.size() == 0) {
            logger.warning("Не найден плугин для обработки счёта " + accountType);
			return null;
		} else {
			// ищем сначала в порядке предпочтения
			for (String sname: preferedPluginNames) {
				if (servingPluginNames.indexOf(sname) >= 0) {
                    logger.info("Выбран плугин для обработки счёта " + accountType + ": " + sname);
					return sname;
				}
			}
			// или возвращаем первую найденную
            logger.info("Выбран плугин для обработки счёта " + accountType + ": " + servingPluginNames.get(0));
			return servingPluginNames.get(0);
		}
	}

	@Override
	public int getAccountTypeCR(int creditRequestId) {
		CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);
		int accountType = creditRequest.getAccountId().getAccountTypeId().getCodeinteger();
		return accountType;
	}

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Payment createPaymentToClient(int creditId, int accountId, Double creditSum, Date createDate, Integer paysumId) {
    	logger.info("Начинаем новый платёж клиенту по кредиту" + String.valueOf(creditId));
		try {
			Payment payment = kassaBean.createPaymentToClient(creditId, accountId, creditSum, createDate, paysumId);
			wfBean.startOrFindProcPayment(payment.getId(), payment.getAccount().getAccountType().getCodeInteger(), creditId);
			logger.info("Отправили платёж клиенту по кредиту" + String.valueOf(creditId));
			return payment;
		} catch (Exception e) {
			logger.info("Не отправили платёж клиенту по кредиту" + String.valueOf(creditId));
			throw new ActionRuntimeException(-1, "Не выполнен платёж клиенту по кредиту  " + String.valueOf(creditId), Type.BUSINESS, ResultType.FATAL, e);
		}
	}

	@Override
	public String findPaymentSystem(int creditRequestId, ActionContext actionContext) throws ActionException {
        logger.info("Ищем платёжный плугин для обработки заявки " + creditRequestId);

		CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);
		if (creditRequest == null) {
            logger.warning("Не найдена заявка " + creditRequestId);
			throw new ActionException(-1, "Не найдена заявка " + String.valueOf(creditRequestId), Type.TECH, ResultType.FATAL, null);
		}

		int accountType = creditRequest.getAccountId().getAccountTypeId().getCodeinteger();
		logger.info("Вид счета " +accountType);
		List<String> preferedPluginNames = rulesBean.getPaymentSystemOrder(accountType);

		List<String> servingPluginNames = new ArrayList<String>(15);
		for (PluginConfig pluginConfig : actionContext.getPlugins().getPluginConfigs()) {
			logger.info("Плугин " +pluginConfig.getPluginName());
			logger.info("Активность " +pluginConfig.getIsActive());
			logger.info("Является платежной системой "+(pluginConfig.getPlugin() instanceof PaymentSystemLocal));
			if (pluginConfig.getIsActive() && pluginConfig.getPlugin() != null && pluginConfig.getPlugin() instanceof PaymentSystemLocal) {

 				if ( pluginConfig.getPlugin().getExecutionModesSupported().contains( PluginSystemLocal.ExecutionMode.AUTOMATIC) ) {
					PaymentSystemLocal plugin = (PaymentSystemLocal) pluginConfig.getPlugin();

                    if (Arrays.binarySearch(plugin.getSupportedAccountTypes(), accountType) >= 0) {
                        servingPluginNames.add( pluginConfig.getPluginName() );
					}
 				}
			}
		}

		if (servingPluginNames.size() == 0) {
            logger.warning("Не найден плугин для обработки заявки " + creditRequestId);
			return null;
		} else {
			// ищем сначала в порядке предпочтения
			for (String sname: preferedPluginNames) {
				if (servingPluginNames.indexOf(sname) >= 0) {
                    logger.info("Выбран плугин для обработки заявки " + creditRequestId + ": " + sname);
					return sname;
				}
			}
			// или возвращаем первую найденную
            logger.info("Выбран плугин для обработки заявки " + creditRequestId + ": " + servingPluginNames.get(0));
			return servingPluginNames.get(0);
		}
	}

    @Override
    public void runPlugin(String pluginName, String businessObjectClass, Object businessObjectId, Map<String,
            Object> params, ActionContext actionContext) throws ActionException, ActionRuntimeException {
        PluginConfig plc = actionContext.getPlugins().getPluginConfig(pluginName);
        if (!plc.getIsActive()) {
            return;
        }

        try {
            ProcessInstance pinst = wfBean.startPluginProcess(plc, businessObjectClass, businessObjectId, params,
                    actionContext);
            while ((pinst != null) && (!pinst.isEnded())) {
                // TODO
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    logger.severe("Ошибка runPlugin "+pluginName+", "+e);
                }
                pinst = wfEngine.getProcessInstance(pinst.getProcessInstanceId());
            }
        } catch (WorkflowException e) {
        	logger.severe("Ошибка runPlugin "+pluginName+", "+e);
            throw new ActionException("Ошибка при запуске плагина " + pluginName, e);
        }
    }

    @Override
    public void setBusinessPermission(String businessObjectClass, Object bisinessObjectId, String roleName,
                                      String permissions) {
        // TODO
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String restartCredit(int creditId, Object data) {
    	String lsPoint = null;

    	logger.info("Пытаемся возобновить кредит "+creditId);
    	EventLog log = eventLog.findLatestByCreditId(eventLog.getEventCode(EventCode.STOP_CREDIT).getId(), creditId);
    	creditBean.restartCredit(creditId, new Date());
    	if (log != null) {
    		lsPoint = log.getDescription();
    	}
    	if (data != null && data instanceof Map) {
    		lsPoint = (String) (((Map) data).get("lastStopPoint"));
    	}
    	logger.info("Возобновили кредит "+creditId);
    	logger.info("lsPoint "+ StringUtils.defaultString(lsPoint));
		return lsPoint;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void stopCredit(int creditId, String stopPoint) {
    	StateRef ref = null;
    	if (stopPoint != null) {
    		ref = StateRef.valueOf(stopPoint);
    	}
    	logger.info("Пытаемся остановить кредит "+creditId+", stopPoint "+ StringUtils.defaultString(stopPoint, ""));
    	creditBean.stopCredit(creditId, new Date(), ref);
    	logger.info("Остановили кредит "+creditId+", stopPoint " + StringUtils.defaultString(stopPoint, ""));
    }

    @Override
    public boolean hasCRQuestions(int creditRequestId) {
        int n = questBean.countQuestionAnswer(creditRequestId, null);
        return (n > 0);
    }

    @Override
    public Date getCreditDataEndPlan(int creditId) {
    	logger.info("Считаем creditDataEndPlan по кредиту "+creditId);
    	Credit credit = creditDAO.getCredit(creditId, null);
    	if (credit != null) {
    	    Map<String,Object> limits=productBean.getOverdueProductConfig(credit.getProduct().getId());
    	    Integer i=Convertor.toInteger(limits.get(ProductKeys.CREDIT_DAYS_MAX_LGOT));
    	    if (i!=null){
    		    i+=1;
    	    } else {
    		    i=1;
    	    }

            return DateUtils.addDays(credit.getCreditDataEnd(),i);
        } else {
        	logger.severe("Не найден кредит с id "+creditId);
        }
        return null;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void processSuccessPayment(Integer paymentId) {
    	paymentService.processSuccessPayment(paymentId, new Date());
    }

    @Override
    public Integer getPaymentToClientId(Integer creditId) {
        logger.info("getPaymentToClientId credit " + creditId);
        PaymentEntity pay = payDAO.findPaymentToClient(creditId);
        if (pay == null) {
            logger.info("getPaymentToClientId pay is null");
            return null;
        } else {
            logger.info("getPaymentToClientId pay " + pay.getId());
            return pay.getId();
        }
    }

    @Override
    public String findAcquiringSystem(int creditId, int accountTypeCode, Integer payId, ActionContext actionContext) {
    	logger.info("ищем плугин для приема денег по кредиту " + creditId);

    	Payment payment =  null;
    	if (payId == null) {
	    	PaymentFilter filter = new PaymentFilter();
	    	filter.setCreditId(creditId);
	    	filter.setAccountTypeId(refBooks.getAccountType(accountTypeCode).getId());
	    	filter.setPaymentTypeId(refBooks.getPaymentType(Payment.TO_SYSTEM).getId());
	    	filter.setIsPaid(false);
	    	List<Payment> lstPay = paymentService.findPayments(0, -1, null, Utils.setOf(), filter);
	    	if (lstPay.size() == 0) {
	    		logger.severe("не найден платёж для приема денег по кредиту "+creditId + " по типу платежа " + accountTypeCode);
	    		return null;
	    	}
	    	if (lstPay.size() > 1) {
	    		logger.warning("Найдено более одного платёжа для приема денег по кредиту "+creditId + " по типу платежа " + accountTypeCode);
	    		//return null;
	    	}

	    	payment = lstPay.get(0);
    	} else {
    		payment = payDAO.getPayment(payId, Utils.setOf());
    	}
    	logger.info("Пытаемся провести платеж "+payment.getId());

    	List<String> lstEx = new ArrayList<String>(15);
        for (PluginConfig plc : actionContext.getPlugins().getPluginConfigs()) {
            if (plc.getIsActive() && plc.getPlugin() != null && plc.getPlugin() instanceof AcquiringBeanLocal) {
                if (plc.getPlugin().getExecutionModesSupported().contains(PluginSystemLocal.ExecutionMode.AUTOMATIC)) {
                    AcquiringBeanLocal ploc = (AcquiringBeanLocal) plc.getPlugin();
                    if (Arrays.binarySearch(ploc.getSupportedAccountTypes(), accountTypeCode) >= 0) {
                    	if (ploc.getPartnerId() != null && ploc.getPartnerId().equals(payment.getPartner().getId())) {
                    		lstEx.add(plc.getPluginName());
                    	}
                    }
                }
            }
        }
        if (lstEx.size() == 0) {
            logger.warning("не найден плугин для приема денег по кредиту "+creditId + " по типу платежа " + accountTypeCode);
            return null;
        } else {
        	logger.info("найден плугин для приема денег по кредиту " + creditId+ " по типу платежа " + accountTypeCode +", "+lstEx.get(0));
            return lstEx.get(0);
        }
    }

    @Override
    public void recalcOverdue(Integer creditId, Date dateCalc) throws ActionException {
    	logger.info("Начинаем считать просрочку по кредиту "+creditId.toString());
    	try {
    	    creditBean.saveOverdueSum(creditId, dateCalc);
    	} catch (Exception e){
    		logger.severe("Не удалось пересчитать просрочку по кредиту "+creditId.toString());
    		throw new ActionException("Не удалось пересчитать просрочку по кредиту "+creditId.toString(),e);
     	}
    }

    @Override
    public Double getRemainingMoney(int creditId) {
    	CreditEntity credit = creditDAO.getCreditEntity(creditId);
    	if (credit.getIsover()) {
    		// кредит выплачен полностью или больше
    		if (credit.getCreditsumdebt() != null && credit.getCreditsumdebt().doubleValue() > 0) {
    			return -1 * credit.getCreditsumdebt();
    		} else {
    			return new Double(0.0);
    		}
    	} else {
    		return credit.getCreditsumback();
    	}
    }

    @Override
    public Double getCreditSumBack(int creditId) {
    	CreditEntity credit = creditDAO.getCreditEntity(creditId);
    	if (credit!=null){
            return credit.getCreditsumback();
    	} else {
    		return null;
    	}
    }

    @Override
    public Double getCreditSumDebt(int creditId) {
    	CreditEntity credit = creditDAO.getCreditEntity(creditId);
    	if (credit!=null){
            return credit.getCreditsumdebt();
    	} else {
    		return null;
    	}
    }

    @Override
    public Users getGuestUser() {
    	return userBean.findUserByLogin(Users.GUEST_NAME, Utils.setOf());
    }

    /**
     * инициализируем плагины
     * @param customKey
     * @param pls
     * @param bPersistent
     */
    private void initPlugins(String customKey, PluginsSupport pls, boolean bPersistent) {
        RuntimeServices runtimeServices = new RuntimeServices(serviceBean, rulesBean, wfBean, questBean);
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
    public void savePluginConfigs(ActionContext context, Map<String, Object> mpChanged) {
        initPlugins(context.getCustomizationKey(), context.getPlugins(), context.isPersistent());
        context.initPluginStates(new RuntimeServices(serviceBean, rulesBean, wfBean, questBean));
        serviceBean.putActionContext(context);
		serviceBean.firedEvent(EventKeys.EVENT_PLUGINS_SETTINGS_CHANGED, mpChanged);
    }

    @Override
    public ActionContext createActionContext(String customizationKey, boolean bPersistent) {
        if (StringUtils.isBlank(customizationKey)) {
            bPersistent = true;
        }

        ActionContext con = serviceBean.getActionContext(customizationKey);
        if (con == null) {
            con = new ActionContext(customizationKey, bPersistent);
            initPlugins(customizationKey, con.getPlugins(), bPersistent);
            con.initPluginStates(new RuntimeServices(serviceBean, rulesBean, wfBean, questBean));
            serviceBean.putActionContext(con);
            serviceBean.subscribe(EventKeys.EVENT_PLUGINS_SETTINGS_CHANGED, con);
        }
        return con;
    }

    @Override
    public Object createActionContextObject(String customizationKey,
                                            boolean bPersistent) {
        return createActionContext(customizationKey, bPersistent);
    }

    @Override
    public Credit getCredit(int id, Set options) {
    	return creditDAO.getCredit(id, options);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Prolong getProlong(int prolongId, Set options) {
    	return prolongDAO.getProlong(prolongId, options);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Refinance getRefinance(int refinanceId, Set options) {
    	return creditDAO.getRefinance(refinanceId, options);
    }

    @Override
    public BizActionEntity getBizActionEntity(int id) {
    	return bizDAO.getBizActionEntity(id);
    }


    @Override
    public void returnRepayCR(Integer paymentId) {
    	logger.info("Возвращаем лишнее по платежу " + paymentId.toString());
    	// TODO почему по платежу, а не по кредиту? сначала же пересчет
    }

    @Override
	public PaymentService getPaymentService() {
		return paymentService;
	}

    @Override
    public void dummy() {

    }

    @Override
    public void canReceivePaymentContact(int creditId, Map<String, Object> paymentParams) throws ActionException {
    	// TODO
    }

    @Override
    public Users getUserByPeople(int peopleMainId) {
    	return userBean.findUserByPeopleId(peopleMainId, Utils.setOf());
    }

    @Override
    public Users getUserByCreditRequest(int creditRequestId) {
    	logger.info("User by credit request "+creditRequestId);
    	CreditRequestEntity ent = crDAO.getCreditRequestEntity(creditRequestId);
    	logger.info("PeopleMain "+ent.getPeopleMainId().getId());
    	return userBean.findUserByPeopleId(ent.getPeopleMainId().getId(), Utils.setOf());
    }

    @Override
    public boolean checkPassportValidity(int creditRequestId) throws PeopleException {

    	CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(creditRequestId);
    	if (creditRequest==null){
    		return false;
    	}

    	if (creditRequest.getPeopleMainId()==null) {
    		return false;
    	}

    	PeopleMainEntity pmain=peopleDAO.getPeopleMainEntity(creditRequest.getPeopleMainId().getId());
      	DocumentEntity document=peopleBean.findPassportActive(pmain);

    	Integer validity=null;
    	//ищем, действительный ли паспорт
    	if (document!=null&&StringUtils.isNotEmpty(document.getSeries())&&StringUtils.isNotEmpty(document.getNumber())){
          try {
        	validity=CheckRemoteDbUtils.checkPaspValidity(document.getSeries().trim()+document.getNumber().trim(),rulesBean.getSiteConstants());
			document=documentsDAO.saveDocumentValidity(document, validity);
			eventLog.saveLog(EventType.INFO,EventCode.REQUEST_OUTER_DB,
				"Обращение к внешней БД недействительных паспортов, паспорт "+document.getSeries()+" "+document.getNumber(),
				 null, new Date(),  creditRequest, pmain, null);

		  } catch (Exception e1) {
			logger.severe("Не удалось сохранить действительность паспорта");
		  }
    	}
        return (validity!=null&&validity==Documents.VALID);
    }

    @Override
    public boolean checkTerrorism(int clientUserId,int creditRequestId) throws PeopleException {
       
        CreditRequestEntity creditRequest = crDAO.getCreditRequestEntity(creditRequestId);
        if (creditRequest == null) {
            return false;
        }

        PeopleMain ppl = peopleDAO.getPeopleMain(creditRequest.getPeopleMainId().getId(),
                Utils.setOf(PeopleMain.Options.INIT_PEOPLE_CONTACT, PeopleMain.Options.INIT_PEOPLE_PERSONAL, PeopleMain.Options.INIT_DOCUMENT,
                        PeopleMain.Options.INIT_ADDRESS, PeopleMain.Options.INIT_EMPLOYMENT));

        if (ppl == null) {
            return false;
        }

        // сохраняем ли случаи мошенничества вообще или нет
        Map<String, Object> params = rulesBean.getAntifraudConstants();
        boolean transferToExternalSystems = Utils.booleanFromNumber(params.get(RulesKeys.ANTIFRAUD_TRANSFER_EXTERNAL));
        logger.info("передаем данные АМ во внешние системы = " + transferToExternalSystems);

        boolean checkAF = Utils.booleanFromNumber(params.get(RulesKeys.ANTIFRAUD_CHECK_INTERNAL_RULES));
        logger.info("проверяем по внутренним АМ правилам = " + checkAF);
        
        //если проверяем на внутренние правила
        if (checkAF){
        	checkAntifraudRules(creditRequestId);
        }
        
        boolean start = true;
        PeopleMainEntity pmain = peopleDAO.getPeopleMainEntity(ppl.getId());
        PeoplePersonalEntity people = peopleBean.findPeoplePersonalActive(pmain);

        //проверяем на терроризм
        BlacklistEntity blackExternalTerrorist = peopleBean.saveTerrorist(people);
        if (blackExternalTerrorist != null) {
            start = false;
        }

        //пишем лог
        try {

            eventLog.saveLog(EventType.INFO, EventCode.REQUEST_OUTER_DB,
                    "Обращение к внешней БД террористов, ФИО " + people.getFullName(),
                    null, new Date(), creditRequest, pmain, null);

        } catch (Exception e1) {
            logger.severe("Не удалось сохранить обращение к БД террористов, ошибка " + e1);
        }


        boolean innerBlack = false;
        String description = "";
        RefBlacklistEntity refBlacklistEntity = null;
        List<RefBlacklistEntity> re;

        // начинаем сравнивать данные нашего человека с данными которые есть в локальных черных списках
        //ищем в ЧС по телефону
        for (PeopleContact contact : ppl.getPeopleContacts()) {
            re = refBooks.findInRefBlacklistByPhone(contact.getValue());
            if (!re.isEmpty()) {
                if (transferToExternalSystems) {
                    // ищем код хантера под соответствующий контакт
                    String hunterObjectCode = null;
                    if (contact.getContact().getCodeInteger() == PeopleContact.CONTACT_CELL_PHONE) {
                        hunterObjectCode = RefHunterRule.HUNTER_OBJECT_MOBILE_PHONE;
                    } else if (contact.getContact().getCodeInteger() == PeopleContact.CONTACT_WORK_PHONE) {
                        hunterObjectCode = RefHunterRule.HUNTER_OBJECT_EMPLOYMENT_PHONE;
                    } else if (contact.getContact().getCodeInteger() == PeopleContact.CONTACT_HOME_PHONE) {
                        hunterObjectCode = RefHunterRule.HUNTER_OBJECT_RESIDENCE_PHONE;
                    } else if (contact.getContact().getCodeInteger() == PeopleContact.CONTACT_HOME_REGISTER_PHONE) {
                        hunterObjectCode = RefHunterRule.HUNTER_OBJECT_REGISTER_PHONE;
                    }

                    // если нашли код, добавляем событие мошенничества
                    if (hunterObjectCode != null) {
                        String comment = "Добавление из ЧС по контакту " + contact.getValue();
                        antifraudOccasionDAO.saveWithCheck(creditRequestId, pmain.getId(), hunterObjectCode, comment);
                    }
                    description = description+"Добавление из ЧС по контакту " + contact.getValue()+"; ";
                }

                if (!innerBlack) {
                    innerBlack = true;
                    description = "Добавление из ЧС по контакту " + contact.getValue()+"; ";
                    refBlacklistEntity = re.get(0);
                }
            }
        }

        // ищем по паспорту
        re = refBooks.findInRefBlacklistByPassport(ppl.getActivePassport().getSeries(),
                ppl.getActivePassport().getNumber());
        if (!re.isEmpty()) {
            if (transferToExternalSystems) {
                String hunterObjectCode = RefHunterRule.HUNTER_OBJECT_PASSPORT;
                String comment = "Добавление из ЧС по паспорту " +
                        ppl.getActivePassport().getSeries() + " " + ppl.getActivePassport().getNumber();
                antifraudOccasionDAO.saveWithCheck(creditRequestId, pmain.getId(), hunterObjectCode, comment);
                description = description+"Добавление из ЧС по паспорту " + ppl.getActivePassport().getSeries() + " " +
                        ppl.getActivePassport().getNumber()+"; ";
            }

            if (!innerBlack) {
                innerBlack = true;
                description = "Добавление из ЧС по паспорту " + ppl.getActivePassport().getSeries() + " " +
                        ppl.getActivePassport().getNumber()+"; ";
                refBlacklistEntity = re.get(0);

            }
        }

        // ищем по ПД
        re = refBooks.findInRefBlacklistByPersonalData(ppl.getPeoplePersonalActive().getSurname(),
                ppl.getPeoplePersonalActive().getName(), ppl.getPeoplePersonalActive().getMidname(),
                ppl.getPeoplePersonalActive().getBirthDate());
        if (!re.isEmpty()) {
            if (transferToExternalSystems) {
                String hunterObjectCode = RefHunterRule.HUNTER_OBJECT_MAIN_APPLICANT;
                String comment = "Добавление из ЧС по ПД " + ppl.getPeoplePersonalActive().getFullName();
                antifraudOccasionDAO.saveWithCheck(creditRequestId, pmain.getId(), hunterObjectCode, comment);
                description = description+"Добавление из ЧС по ПД " + ppl.getPeoplePersonalActive().getFullName()+"; ";
            }

            if (!innerBlack) {
                innerBlack = true;
                description = "Добавление из ЧС по ПД " + ppl.getPeoplePersonalActive().getFullName()+"; ";
                refBlacklistEntity = re.get(0);
            }
        }

        // ищем по работодателю
        re = refBooks.findInRefBlacklistByEmployer(ppl.getCurrentEmployment().getPlaceWork(), ppl.getCurrentEmployment().getPlaceWork());
        if (!re.isEmpty()) {
            if (transferToExternalSystems) {
                String hunterObjectCode = RefHunterRule.HUNTER_OBJECT_EMPLOYMENT;
                String comment = "Добавление из ЧС по работодателю " + ppl.getCurrentEmployment().getPlaceWork();
                antifraudOccasionDAO.saveWithCheck(creditRequestId, pmain.getId(), hunterObjectCode, comment);
                description = description+"Добавление из ЧС по работодателю " + ppl.getCurrentEmployment().getPlaceWork()+"; ";
            }

            if (!innerBlack) {
                innerBlack = true;
                description = "Добавление из ЧС по работодателю " + ppl.getCurrentEmployment().getPlaceWork()+"; ";
                refBlacklistEntity = re.get(0);
            }
        }

        //ничего не нашлось, ищем по адресу
        for (FiasAddress address : ppl.getAddressesActive()) {
            re = refBooks.findInRefBlacklistByAddress(address == null ? null : address.getRegionName(),
                    address == null ? null : address.getAreaName(), address == null ? null : address.getCityName(),
                    address == null ? null : address.getPlaceName(), address == null ? null : address.getStreetName(),
                    address == null ? null : address.getHouse(), address == null ? null : address.getCorpus(),
                    address == null ? null : address.getBuilding(), address == null ? null : address.getFlat());

            if (!re.isEmpty()) {
                if (transferToExternalSystems) {
                    String hunterObjectCode = null;
                    if (address.getAddrtype() != null) {
                        if (address.getAddrtype().getCodeInteger() == BaseAddress.REGISTER_ADDRESS) {
                            hunterObjectCode = RefHunterRule.HUNTER_OBJECT_REGISTRATION_ADDRESS;
                        } else if (address.getAddrtype().getCodeInteger() == BaseAddress.RESIDENT_ADDRESS) {
                            hunterObjectCode = RefHunterRule.HUNTER_OBJECT_RESIDENCE_ADDRESS;
                        } else if (address.getAddrtype().getCodeInteger() == BaseAddress.WORKING_ADDRESS) {
                            hunterObjectCode = RefHunterRule.HUNTER_OBJECT_EMPLOYMENT_ADDRESS;
                        }
                    }

                    if (hunterObjectCode != null) {
                        String comment = "Добавление из ЧС по адресу " + address.getDescription();
                        antifraudOccasionDAO.saveWithCheck(creditRequestId, pmain.getId(), hunterObjectCode, comment);
                        description = description+"Добавление из ЧС по адресу " + address.getDescription()+"; ";
                    }
                }

                if (!innerBlack) {
                    innerBlack = true;
                    description = "Добавление из ЧС по адресу " + address.getDescription()+"; ";
                    refBlacklistEntity = re.get(0);
                }
            }
        }


        BlacklistEntity blackPeople = peopleBean.findInBlackList(pmain, new Date(), null);

        //есть во внутреннем, нет в списке террористов 
        if (innerBlack && blackPeople == null && blackExternalTerrorist == null) {
            Integer reason = null;
            if (refBlacklistEntity != null && refBlacklistEntity.getReasonId() != null) {
                reason = refBlacklistEntity.getReasonId().getCodeinteger();
            }
            if (refBlacklistEntity != null && refBlacklistEntity.getSourceId() != null) {
                description=description+"источник информации "+refBlacklistEntity.getSourceId().getName();
            }
            peopleBean.saveBlackList(pmain, reason, new Date(), description);

            //пишем лог
            try {

                eventLog.saveLog(EventType.INFO, EventCode.BLACKLIST_ADD,
                        description, null, new Date(), creditRequest, pmain, null);

            } catch (Exception e1) {
                logger.severe("Не удалось сохранить запись в ЧС, ошибка " + e1);
            }
        }

        //есть в черном списке, во внутреннем уже нет, в террористах нет
        if (!innerBlack && blackExternalTerrorist == null && blackPeople != null) {
            peopleBean.removeFromBlackList(pmain, new Date());

            //пишем лог
            try {

                eventLog.saveLog(EventType.INFO, EventCode.BLACKLIST_REMOVE,
                        "Удаление из черного списка", null, new Date(), creditRequest, pmain, null);

            } catch (Exception e1) {
                logger.severe("Не удалось сохранить удаление из черного списка, ошибка " + e1);
            }
        }

        return !start;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveCRMiscVariables(int creditRequestId, Map<String, Object> vars) {
		kassaBean.saveMiscVariables(CreditRequest.class.getName(), creditRequestId, vars);
	}

	@Override
	public boolean hasProcess(Integer creditRequestId) {
		ProcessInstance proc = wfBean.findProcess(ProcessKeys.DEF_CREDIT_REQUEST, CreditRequest.class.getName(), creditRequestId);
		return (proc != null);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void restoreProcess(Integer creditRequestId, String activityId) throws WorkflowRuntimeException, WorkflowException {
		logger.info("Восстанавливаем процесс по заявке "+creditRequestId+" и activityId "+activityId);
		CreditRequestEntity cred = crDAO.getCreditRequestEntity(creditRequestId);
		UsersEntity usr = userBean.findUserByPeopleId(cred.getPeopleMainId().getId());
		if (usr==null){
			logger.severe("Не найден пользователь для заявки, нужно редактирование заявки");
			throw new WorkflowException("Не найден пользователь для заявки, нужно редактирование заявки");
		}
		wfBean.removeProcCR(creditRequestId);

		cred.setStatusId(refBooks.getCreditRequestStatus( CreditStatus.FILLED));
		crDAO.saveCreditRequest(cred);
		try {
		    wfBean.startOrFindProcCR(creditRequestId, usr.getId(), activityId);
		} catch (WorkflowRuntimeException | WorkflowException e) {
			logger.log(Level.SEVERE, "Не удалось восстановить процесс для заявки " + String.valueOf(creditRequestId), e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void restartCreditRequest(int creditRequestId) {
		try {
			wfBean.signalProcCR(creditRequestId);
		} catch (WorkflowRuntimeException | WorkflowException e) {
			logger.log(Level.SEVERE, "Не удалось перезапустить процесс для заявки " + String.valueOf(creditRequestId), e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelPaymentContact(int paymentId) {

		try {
			Payment pay = payDAO.getPayment(paymentId, Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));

			CreditRequest cRequest = pay.getCredit().getCreditRequest();
			UsersEntity usr = userBean.findUserByPeopleId(cRequest.getPeopleMain().getId());
			int creditRequestId = cRequest.getId();

			//ищем другие платежи, которые были сделаны клиенту
			PaymentFilter filter = new PaymentFilter();
	    	filter.setCreditId(pay.getCredit().getId());
	    	filter.setStatus(PaymentStatus.SUCCESS.ordinal());
	    	filter.setPaymentTypeId(refBooks.getPaymentType(Payment.FROM_SYSTEM).getId());
	    	filter.setIsPaid(true);
	    	List<Payment> lstPay = paymentService.findPayments(0, -1, null, Utils.setOf(), filter);
	    	//если платежей нет, заявке тоже поставим статус отказа
			if (lstPay.size()==0){
				CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(creditRequestId);
				if (creditRequest!=null){
					creditRequest.setStatusId(refBooks.getCreditRequestStatus(CreditStatus.CLIENT_REFUSE));
					creditRequest.setDateStatus(new Date());
					crDAO.saveCreditRequest(creditRequest);
					logger.info("Поставили заявке статус отмены после отмены платежа Контакта "+paymentId);
				}
				CreditEntity credit=creditDAO.getCreditEntity(pay.getCredit().getId());
				if (credit!=null){
					credit.setCreditStatusId(refBooks.findByCodeIntegerEntity(RefHeader.CREDIT_STATUS_TYPE, BaseCredit.CREDIT_CANCELLED));
					credit.setIsover(true);
					credit.setDateStatus(new Date());
					//в дату окончания кредита поставим дату начала, дабы он нигде и никак не учитывался
					credit.setCreditdataendfact(credit.getCreditdatabeg());
					creditDAO.saveCreditEntity(credit);
					logger.info("Поставили кредиту статус закрыт после отмены платежа Контакта "+paymentId);
				}
				//удаляем запись у коллектора, если кредит уже успели передать ему
				collectorDAO.removeCollectorRecord(cRequest.getPeopleMain().getId(), pay.getCredit().getId());
			}

			refuseClient(creditRequestId, usr.getId());

			logger.info("Отменили платеж "+paymentId);
		} catch (ActionRuntimeException e) {
			logger.log(Level.SEVERE, "Не удалось отменить платёж " + String.valueOf(paymentId), e);
		} catch (ActionException e) {
			logger.log(Level.SEVERE, "Не удалось отменить платёж " + String.valueOf(paymentId), e);
		}

	}

	@Override
	public void fireBusinessEvent(BusinessEvent event) {
		evtSender.fireEvent(event);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Payment getPayment(int id, Set options) {
		return payDAO.getPayment(id, options);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditRequest getCreditRequest(int id, Set options) {
		return crDAO.getCreditRequest(id, options);
	}

    @Override
    public RefBonus getBonusByCodeInteger(int bonusCode) {
        return refBooks.getBonusByCodeInteger(bonusCode);
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addBonus(Integer peopleMainId, Integer relatedPeopleMainId,
			Integer relatedCreditId, Integer bonusCode, Integer operarionCode) throws ActionException {

        RefBonus refb = refBooks.getBonusByCodeInteger(bonusCode);

		if(refb==null||!refb.getIsactive()){
			return;
		}
		Double amount = refb.getAmount();
		try {
			peopleBean.addBonus(peopleMainId, bonusCode, operarionCode, new Date(), amount, relatedCreditId, relatedPeopleMainId);
		} catch (Exception e) {
			logger.severe("Не удалось добавить бонусы "+e);
			throw new ActionException("Не удалось добавить бонусы ", e);
		}

	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addBonus(Integer peopleMainId, Integer relatedPeopleMainId, Integer relatedCreditId,
                         Integer bonusCode, Double amount, Integer operarionCode) throws ActionException {
        try {
            peopleBean.addBonus(peopleMainId, bonusCode, operarionCode, new Date(), amount, relatedCreditId, relatedPeopleMainId);
        } catch (Exception e) {
            logger.severe("Не удалось добавить бонусы " + e);
            throw new ActionException("Не удалось добавить бонусы ", e);
        }
    }

    @Override
	public Map<String,Object> calcReturnData(Integer creditId, String variablePrefix) {

		Map<String,Object> mp = new HashMap<>(6);

		Double sumBack = 0d, sumBackMin = 0d, sumBonusPay = 0d, sumBonusPayMin = 0d;
		Credit credit = creditDAO.getCredit(creditId, Utils.setOf(BaseCredit.Options.INIT_PROLONGS,BaseCredit.Options.INIT_REFINANCES));
		String description="";
		if (credit.getDraftProlong()!=null){
			Map<String, Object> crparams = creditCalc.calcCredit(credit.getId(),credit.getDraftProlong().getLongDate());
            sumBack = Convertor.toDouble(crparams.get(CreditCalculatorBeanLocal.SUM_PERCENT),CalcUtils.dformat);
            sumBackMin = sumBack;
            description = "Оплата процентов перед продлением";
            mp.put(variablePrefix + ".sumId", RefPaymentTarget.PROLONG);
		} else if (credit.getLastRefinance()!=null){
			sumBack=credit.getLastRefinance().getRefinanceAmount();
    		sumBackMin=sumBack;
    		description = "Оплата суммы для рефинансирования";
    		mp.put(variablePrefix + ".sumId", RefPaymentTarget.REFINANCE);
		} else {
		    //если это просто оплата
            Map<String, Object> resCalc = creditCalc.calcCredit(creditId, new Date());
            sumBack = Convertor.toDouble(resCalc.get(CreditCalculatorBeanLocal.SUM_BACK),CalcUtils.dformat);
            sumBackMin = Convertor.toDouble(resCalc.get(CreditCalculatorBeanLocal.SUM_PERCENT),CalcUtils.dformat);
            logger.info("Ставка "+ resCalc.get(CreditCalculatorBeanLocal.STAKE_CURRENT));
            description = "Погашение займа и процентов по договору займа " + creditId + " от "
                + credit.getDatabeg() + " г.";
            UserPropertiesEntity userProperties=peopleBean.findUserProperties(credit.getPeopleMain().getId());
		    //если в настройках пользователя стоит платеж бонусами
		    if (userProperties!=null&&userProperties.getPayByBonus()==1){
			    Map<String,Object> limits=rulesBean.getBonusConstants();
			    //если в настроках системы система бонусов активна
			    if (Convertor.toInteger(limits.get(CreditCalculatorBeanLocal.BONUS_ISACTIVE))==1){
    	            //считаем бонусы
    	            double sumBonus=peopleDAO.getPeopleBonusInSystem(credit.getPeopleMain().getId());
    	            if (sumBonus>0){

    	            	Double sumForBonus=sumBack;
    			    	if (Convertor.toInteger(limits.get(CreditCalculatorBeanLocal.BONUS_USE_PERCENT_ONLY))==1){
    			    		  sumForBonus=sumBackMin;
    			    	}
    			    	
		                Map<String, Object> bres=creditCalc.calcBonusPaymentSum(sumForBonus, sumBonus, limits);
		                sumBonusPay=CalcUtils.roundFloor(Convertor.toDouble(bres.get(CreditCalculatorBeanLocal.SUM_BONUS_MONEY_FOR_PAYMENT)),0);
		                bres=creditCalc.calcBonusPaymentSum(sumBackMin, sumBonus, limits);
		                sumBonusPayMin=CalcUtils.roundFloor(Convertor.toDouble(bres.get(CreditCalculatorBeanLocal.SUM_BONUS_MONEY_FOR_PAYMENT)),0);

		                mp.put(variablePrefix + ".bonus.sumFrom", sumBonusPayMin);
		                mp.put(variablePrefix + ".bonus.sumTo", sumBonusPay);
    	            }
			    }
		    }

    	    sumBack=sumBack-sumBonusPay;
    	    sumBackMin=sumBackMin-sumBonusPayMin;
    	    mp.put(variablePrefix + ".sumId", RefPaymentTarget.RETURN);
		}
    	mp.put(variablePrefix + ".type", Payment.TO_SYSTEM);
    	mp.put(variablePrefix + ".sumFrom", sumBackMin);
    	mp.put(variablePrefix + ".sumTo", sumBack);
    	mp.put(variablePrefix + ".bonus.sumFrom", sumBonusPayMin);
    	mp.put(variablePrefix + ".bonus.sumTo", sumBonusPay);
    	mp.put(variablePrefix + ".description", description);

    	return mp;
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadScoringEquifax() throws ActionException {

		Date today = new Date();
		try	{
			//займы
			UploadingEntity uploading = equifaxBean.createFileForUpload(today, true);
			equifaxBean.uploadHistory(uploading, today, true);
			//заявки
			UploadingEntity uploadingCR = equifaxBean.createFileForUploadCreditRequest(today, true);
			equifaxBean.uploadHistory(uploadingCR, today, true);

		} catch (Exception e) {

			logger.severe("Не удалось выгрузить данные для Equifax "+e);
			throw new ActionException("Не удалось выгрузить данные для Equifax ", e);
		}

	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadScoringEquifax1() throws ActionException {
       
		ActionContext actionContext = createActionContext(null, true); 
		ScoringEquifaxBeanLocal equifax = actionContext.getPlugins().getEquifax();

		Date today = new Date();
		try	{
			//займы
			UploadingEntity uploading = equifax.createFileForUpload(today, true);
			equifax.uploadHistory(uploading, today, true);
			//заявки
			UploadingEntity uploadingCR = equifax.createFileForUploadCreditRequest(today, true);
			equifax.uploadHistory(uploadingCR, today, true);

		} catch (Exception e) {

			logger.severe("Не удалось выгрузить данные для Equifax "+e);
			throw new ActionException("Не удалось выгрузить данные для Equifax ", e);
		}

	}	

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadScoringNBKI() throws ActionException {

		Date today = new Date();
		try	{
			UploadingEntity uploading = nbkiBean.createFileForUpload(today, true);
			nbkiBean.uploadHistory(uploading, today, true);

		} catch (Exception e) {

			logger.severe("Не удалось выгрузить данные для NBKI "+e);
			throw new ActionException("Не удалось выгрузить данные для NBKI ", e);
		}

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadScoringOkbCais() throws ActionException {

		Date today = new Date();
		try	{
			UploadingEntity uploading = okbCaisBean.createFileForUpload(today, true);
			okbCaisBean.uploadHistory(uploading, today, true);

		} catch (Exception e) {

			logger.severe("Не удалось выгрузить данные для OkbCais "+e);
			throw new ActionException("Не удалось выгрузить данные для OkbCais ", e);
		}

	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadScoringRS() throws ActionException {

		Date today = new Date();
		try	{
			UploadingEntity uploading = rsBean.createFileForUpload(today, true);
			rsBean.uploadHistory(uploading, today, true);

		} catch (Exception e) {

			logger.severe("Не удалось выгрузить данные для RS "+e);
			throw new ActionException("Не удалось выгрузить данные для RS ", e);
		}

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadScoringSkb() throws ActionException {

		Date today = new Date();
		try	{
			UploadingEntity uploading = skbBean.createFileForUpload(today, true);
			skbBean.uploadHistory(uploading, today, true);

		} catch (Exception e) {

			logger.severe("Не удалось выгрузить данные для Skb "+e);
			throw new ActionException("Не удалось выгрузить данные для Skb ", e);
		}

	}

	@Override
	public boolean hasSystemVerification(Integer creditRequestId) {
		CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(creditRequestId);
		if (creditRequest!=null){
			List<VerificationEntity> lst=creditInfo.findVerification(null, creditRequest.getPeopleMainId().getId(),
					Partner.SYSTEM, null);
			return (lst.size()>0);
		}
		return false;
	}

	@Override
	public boolean hasAnsweredIdentityQuestions(Integer creditRequestId) {
		logger.info("Проверяем, есть ли неотвеченные вопросы идентификации по заявке "+creditRequestId);
		return identityQuestion.isComplete(creditRequestId,null,null);
	}

    @Override
    public void changeCollectorType(Integer collectorID, Integer typeID) {
        collectorDAO.changeCollectorCollectionType(collectorID, typeID);
    }

    @Override
    public void closeCollector(Integer collectorID) {
        collectorDAO.closeCollector(collectorID);
    }

    @Override
    public void peopleToBlackList(Integer peopleMainID) {
        // отправляем клиента в блэклист
        PeopleMainEntity peopleMainEntity = peopleDAO.getPeopleMainEntity(peopleMainID);
        try {
            peopleBean.saveBlackList(peopleMainEntity, BlackList.REASON_CREDIT_OVERDUE, new Date(), null);
        } catch (PeopleException e) {
            logger.severe("Ошибка при сохранении в blacklist: " + e);
        }
    }

	@Override
	public void checkAntifraudRules(Integer creditRequestId) {
		CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(creditRequestId);
    	if (creditRequest==null){
    		return;
    	}

    	if (creditRequest.getPeopleMainId()==null) {
    		return;
    	}
    	logger.info("Начали проверку на внутренние АМ правила по заявке "+creditRequestId);
    	PeopleMainEntity pmain=peopleDAO.getPeopleMainEntity(creditRequest.getPeopleMainId().getId());
       	try {
    		antifraudRulesService.getMatchesForAntifraudRules(creditRequest.getPeopleMainId().getId(), creditRequestId);
			eventLog.saveLog(EventType.INFO,EventCode.CHECK_ANTIFRAUD_RULES, "Проверка на внутренние АМ правила", null,
					new Date(),  creditRequest, pmain, null);
		} catch (Exception e) {
			logger.severe("Не удалось сохранить лог о проверке на внутренние АМ правила");
		}
    	logger.info("Закончили проверку на внутренние АМ правила по заявке "+creditRequestId);
	}

	@Override
	public boolean checkPaid(Integer creditId) {
	
		PaymentEntity payment=paymentService.getLastCreditPayment(creditId, null);
		if (payment==null){
			logger.severe("Не удалось найти платеж по кредиту с id "+creditId);
			return false;
		}
		if (payment.getStatus().equals(PaymentStatus.SUCCESS)&&payment.getIsPaid()){
			
			return true;
		}
		logger.severe("Произошла ошибка при проведении платежа по кредиту с id "+creditId);
		return false;
	}

    @Override
    public void sendSmsNotification(Integer productId, Integer peopleMainId, String bpName, String smsMessageKey) {
        // сообщение которое посылаем
        ProductMessagesEntity message = wfEngine.getProductMessageForBP(productId, bpName, smsMessageKey);
        if (message != null) {
            String smsText = message.getBody();

            // ищем телефон для уведомлений
            OrganizationEntity organizationEntity = organizationService.getOrganizationActive();
            if (organizationEntity != null) {
                String phone = organizationEntity.getNotificationPhone();

                if (StringUtils.isNotEmpty(phone)) {
                    phone = Convertor.fromMask(phone);

                    try {
                    	Object[] params=null;
                        if (sendSMSCommon(null,phone,smsText,params)) {
                            mailBean.saveMessage(peopleMainId, null, Messages.SMS, Reference.AUTO_EXEC, new Date(), null, smsText);
                            logger.log(Level.INFO, "СМС уведомление было успешно отправлено на номер " + phone);
                            return;
                        }

                        logger.log(Level.SEVERE, "Не удалось послать СМС уведомление на номер " + phone);

                    } catch (Throwable ex) {
                        logger.log(Level.SEVERE, "Не удалось послать СМС уведомление на номер " + phone, ex);
                    }
                } else {
                    logger.log(Level.SEVERE, "Номер для СМС уведомлений отсутствует");
                }
            }
        }
    }
    @Override
    public String findAcquiringPlasticCardSystem() {
        ActionContext actionContext =  createActionContext(null, true);
        logger.info("ищем плугин для приема денег по типу платежа - пластиковая карта ");

        List<String> lstEx = new ArrayList<String>(15);
        for (PluginConfig plc : actionContext.getPlugins().getPluginConfigs()) {
            if (plc.getIsActive() && plc.getPlugin() != null && plc.getPlugin() instanceof AcquiringBeanLocal) {
                if (plc.getPlugin().getExecutionModesSupported().contains(PluginSystemLocal.ExecutionMode.AUTOMATIC)) {
                    AcquiringBeanLocal ploc = (AcquiringBeanLocal) plc.getPlugin();
                    if (Arrays.binarySearch(ploc.getSupportedAccountTypes(), Account.CARD_TYPE) >= 0) {
                            lstEx.add(plc.getPluginName());
                    }
                }
            }
        }
        if (lstEx.size() == 0) {
            logger.warning("не найден плугин для приема денег по кредиту, по типу платежа  - пластиковая карта ");
            return null;
        } else {
            logger.info("найден плугин для приема денег по кредиту , по типу платежа  - пластиковая карта ");
            return lstEx.get(0);
        }
    }

}
