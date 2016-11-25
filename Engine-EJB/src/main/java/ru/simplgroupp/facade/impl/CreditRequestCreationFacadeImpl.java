package ru.simplgroupp.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.facade.CreditRequestCreationFacade;
import ru.simplgroupp.facade.PeopleFacade;
import ru.simplgroupp.facade.PersonalDataDto;
import ru.simplgroupp.interfaces.CreditCalculatorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.UserAlreadyExistException;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.ProductsEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.services.CreditDataDto;
import ru.simplgroupp.services.CreditRequestService;
import ru.simplgroupp.services.DocumentService;
import ru.simplgroupp.services.PeopleContactService;
import ru.simplgroupp.services.PeoplePersonalService;
import ru.simplgroupp.services.PeopleService;
import ru.simplgroupp.toolkit.common.GenUtils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.CreditStatus;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.Products;
import ru.simplgroupp.transfer.RefCreditRequestWay;
import ru.simplgroupp.util.WorkflowUtil;
import ru.simplgroupp.workflow.StateRef;
import ru.simplgroupp.workflow.WorkflowObjectStateDef;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Credit request creation service implementation
 */
@Singleton
@TransactionManagement
public class CreditRequestCreationFacadeImpl implements CreditRequestCreationFacade {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private PeopleService peopleService;

    @Inject
    private PeopleFacade peopleFacade;

    @Inject
    private PeoplePersonalService peoplePersonalService;

    @Inject
    private DocumentService documentService;

    @Inject
    private CreditRequestService creditRequestService;

    @Inject
    private PeopleContactService peopleContactService;

    @Inject
    private UsersBeanLocal usersBean;

    @Inject
    private KassaBeanLocal kassaBean;

    @Inject
    private ProductBeanLocal productBean;

    @Inject
    private WorkflowBeanLocal wfBean;

    @Inject
    private CreditCalculatorBeanLocal creditCalc;

    @Inject
    private ProductDAO productDAO;

    @Inject
    private WorkflowBeanLocal workflow;

    @Inject
    EventLogService eventLog;

    @Override
    @TransactionAttribute
    public CreditRequestEntity createCreditRequest(CreditDataDto creditData, Integer peopleId, PersonalDataDto
            personalData, boolean isNewPeople)
            throws WorkflowException, ObjectNotFoundException {
        PeopleMainEntity peopleMain;
        if (peopleId == null) {
            peopleMain = peopleService.createPeople();
        } else {
            peopleMain = peopleService.findPeople(peopleId);
        }

        peopleFacade.savePersonalData(peopleMain.getId(), personalData, isNewPeople);

        // User
        UsersEntity user = usersBean.findUserByLogin(personalData.getEmail());
        if (user != null) {
            if (user.getPeopleMainId().getId() != peopleId) {
                throw new UserAlreadyExistException();
            } else {
                user.setUsername(personalData.getEmail());
            }
        } else {
            usersBean.addUserClient(peopleMain, personalData.getEmail());
        }


        CreditRequest creditRequest = kassaBean.createOffline(creditData.getAmount(), creditData.getDays(),
                new PeopleMain(peopleMain), Products.ZAEM_CODE, null);

        wfBean.startOrFindProcCROffline(creditRequest.getId(), null);

        return creditRequest.getEntity();
    }

    @Override
    @TransactionAttribute
    public void activateCreditRequest(Integer creditRequestId, UsersEntity manager) throws KassaException, WorkflowException {
        CreditRequestEntity creditRequest = creditRequestService.findCreditRequest(creditRequestId);

        Map<String, Object> limits;
        if (creditRequest.getProductId() != null) {
            limits = productBean.getNewRequestProductConfig(creditRequest.getProductId().getId());
        } else {
            ProductsEntity product = productDAO.getProductDefault();
            limits = productBean.getNewRequestProductConfig(product.getId());
            creditRequest.setProductId(product);
        }

        double stake = creditCalc.calcInitialStake(creditRequest.getCreditsum(), creditRequest.getCreditdays(), limits);
        Integer i = kassaBean.findMaxCreditRequestNumber(new Date());

        creditRequest = kassaBean.newCreditRequest(creditRequest,
                creditRequest.getPeopleMainId().getId(), CreditStatus.FILLED,
                true, true, true, true, true, Partner.SYSTEM, null, new Date(), new Date(), new Date(),
                i, creditRequest.getCreditsum(), creditRequest.getCreditdays(),
                stake, "", null, GenUtils.genUniqueNumber(new Date(), i, stake * 100), null, null, RefCreditRequestWay.DIRECT, null);
        CreditRequest creditRequestDto = new CreditRequest(creditRequest);

        generateAgreement(creditRequestDto, new Date());
        //сохранили данные
        int id = kassaBean.saveCreditRequest(creditRequestDto, "127.0.0.1", "");

        logger.info("Сохранили заявку " + creditRequestId);

        //если заявка заполнена, можно идти дальше
        List<WorkflowObjectStateDef> lstActO = workflow.getProcCROfflineWfActions(id, null, true);
        WorkflowObjectStateDef taskDef = WorkflowUtil.findWFState(lstActO, new StateRef("*", "*", "taskFillCROffline"));
        if (taskDef != null) {
            String msgMore = lstActO.get(0).getActions().get(0).getSignalRef();
            workflow.goProcCROffline(id, msgMore, null);
        }

        //пишем лог
        CreditRequestEntity crequest = creditRequestService.findCreditRequest(id);
        if (crequest != null) {
            eventLog.saveLog("localhost", eventLog.getEventType(EventType.INFO).getEntity(),
                    eventLog.getEventCode(EventCode.MANAGER_CALL).getEntity(), "создание заявки менеджером ",
                    crequest, null, manager, null, "", "", "", "");
        }
    }

    private void generateAgreement(CreditRequest creditRequest, Date dateOferta) throws KassaException {
        String agreement = kassaBean.generateAgreement(creditRequest, dateOferta,0);
        creditRequest.setAgreement(agreement);
    }
}
