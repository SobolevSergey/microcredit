package ru.simplgroupp.ejb;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.hunter.hashing.SCL;
import ru.simplgroupp.hunter.onlinematching.wsdl.Match;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ScoringOkbHunterBeanLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.RequestsService;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Класс-заглушка для работы с ОКБ Национальный Хантер
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ScoringOkbHunterBeanLocal.class)
public class ScoringOkbHunterFakeBean extends AbstractPluginBean implements ScoringOkbHunterBeanLocal {
	@Inject Logger LOG;

    @EJB
    private CreditRequestDAO creditRequestDAO;

    @EJB
    private ReferenceBooksLocal refBooks;

    @EJB
    private RequestsService requestsService;

    @EJB
    private EventLogService eventLog;


    @Override
    public String getSystemName() {
        return SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return SYSTEM_DESCRIPTION;
    }

    @Override
    public boolean isFake() {
        return true;
    }

    @Override
    public EnumSet<Mode> getModesSupported() {
        return EnumSet.of(Mode.SINGLE);
    }

    @Override
    public EnumSet<ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(ExecutionMode.MANUAL);
    }

    @Override
    public EnumSet<SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.SYNC);
    }

    @Override
    public Set<String> getModelTargetsSupported() {
        return Utils.setOf(ModelKeys.TARGET_CREDIT_DECISION);
    }

    @Override
    public String getBusinessObjectClass() {
        return CreditRequest.class.getName();
    }
    //sync - executeSingle
    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        Integer creditRequestId = Convertor.toInteger(businessObjectId);
        LOG.info("ОКБ Национальный Хантер, эмуляция. Заявка " + creditRequestId + " передана на скоринг.");
        CreditRequest ccRequest;
        try {
            ccRequest = creditRequestDAO.getCreditRequest(creditRequestId, Utils.setOf());
        } catch (Exception ex) {
            throw new ActionException(ErrorKeys.CANT_INIT_OBJECT, "Не удалось инициализировать кредитную заявку",
                    Type.TECH, ResultType.NON_FATAL, ex);
        }
        if (ccRequest == null){
            throw new ActionException(ErrorKeys.CANT_INIT_OBJECT, "Не удалось инициализировать кредитную заявку, заявка null",
                    Type.TECH, ResultType.NON_FATAL, null);
        }
        try {
            newRequest(ccRequest.getEntity(), false, null);
        } catch  (ActionException ex) {
            LOG.info("ОКБ Национальный Хантер, эмуляция. Заявка " + creditRequestId + " не обработана.");
            throw new ActionException("Произошла ошибка ", ex);
        }
        LOG.info("ОКБ Национальный Хантер, эмуляция. Заявка " + creditRequestId + " обработана.");
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork, Integer cacheDays) throws ActionException {
        RequestsEntity result = requestsService.addRequest(cre.getPeopleMainId().getId(), cre.getId(),
                Partner.OKB_HUNTER, RequestStatus.STATUS_IS_DONE_WITH_SESSION, new Date());

        //save request
        try {
            result.setResponsemessage("Пустой метод, нет данных");
            result = requestsService.saveRequest(result);
        } catch (KassaException ex){
            throw new ActionException(ErrorKeys.CANT_SAVE_REQUEST, "Не удалось сохранить запрос", Type.BUSINESS,
                    ResultType.NON_FATAL, null);
        }
        //save log
        try {
            eventLog.saveLog(EventType.INFO, EventCode.OUTER_SCORING_OKB_IDV,
                    "Запрос-загоушка к сервису ОКБ Нацоональный Хантер", null, new Date(), cre, null, null);
        } catch (KassaException ex) {
            throw new ActionException(ErrorKeys.CANT_WRITE_LOG, "Не удалось записать лог", Type.TECH,
                    ResultType.NON_FATAL, ex);
        }
        return result;
    }

    @Override
    public RequestsEntity saveAnswer(RequestsEntity req, String answer) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void uploadHistory(UploadingEntity uploading, Date sendingDate, Boolean isWork) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkUploadStatus(UploadingEntity uploading, Boolean isWork) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork, List<Integer> creditIds) throws KassaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Match createRequest(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner,
            boolean isWork, SCL scl) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Match createMatch(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner,
            boolean isWork, SCL scl) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void hash(Match match) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void changePassword(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void uploadCreditRequests(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void uploadCreditRequests(List<CreditRequestEntity> creditRequestEntities, PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }
}
