package ru.simplgroupp.ejb.plugins.payment;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.ejb.arius.*;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.plugins.payment.AriusPayBeanLocal;
import ru.simplgroupp.interfaces.service.AriusService;
import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

/**
 * @author sniffl
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AriusPayBean extends PaymentProcessorBean implements AriusPayBeanLocal {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AriusPayBean.class.getName());


    @EJB
    ReferenceBooksLocal refBooks;

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    ActionProcessorBeanLocal actProc;

    @EJB
    MailBeanLocal mailBean;

    @EJB
    OrganizationService orgService;

    @EJB
    PaymentDAO paymentDAO;

    @EJB
    AriusService ariusService;


    @Override
    public String getSystemName() {
        return AriusPayBeanLocal.SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return AriusPayBeanLocal.SYSTEM_DESCRIPTION;
    }

    @Override
    public boolean isFake() {
        return false;
    }

    @Override
    public EnumSet<Mode> getModesSupported() {
        return EnumSet.of(Mode.SINGLE);
    }

    @Override
    public EnumSet<ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(ExecutionMode.AUTOMATIC);
    }

    @Override
    public EnumSet<SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.ASYNC);
    }

    @Override
    public String getBusinessObjectClass() {
        return Payment.class.getName();
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId,
                            PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
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
    public int[] getSupportedAccountTypes() {
        return new int[] {Account.CARD_TYPE,Account.ARIUS_TYPE};
    }

    @Override
    public boolean doSale(boolean test) throws ActionException {
        return false;
    }

    @Override
    /**
     * Делает перевод денег(сумма кредита) заемщику через систему Arius
     * @param payment
     * @param context
     * @return
     * @throws ru.simplgroupp.exception.ActionException
     */
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,PluginExecutionContext context) throws ActionException {
        logger.error("Вызов sendSingleRequest paymentId = {} " ,businessObjectId);
        AriusPayPluginConfig config = (AriusPayPluginConfig) context.getPluginConfig();
        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
        if (payment == null) {
            logger.error("PaymentEntity not found id = {} ", businessObjectId);
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,null);
        }

        CreditEntity credit = payment.getCreditId();
        CreditRequestEntity requestCredit = credit.getCreditRequestId();
        PeopleMainEntity borrower = credit.getPeopleMainId();
        PeopleContactEntity pplcont=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, borrower.getId());


        AriusTransferResponse responseV2 = ariusService.doTransfer(requestCredit.getId());

        if(AriusResponseTypeEnum.async_response.equals(responseV2.getType())){
            //Successful
            paymentService.saveExternalId(payment.getId(),
                    responseV2.getVal(AriusConstants.PAYNET_ORDER_ID),
                    responseV2.getVal(AriusConstants.SERIAL_NUMBER));

            //}
            //шлю смс получателю денег
            try {
                String phoneNumber = pplcont.getValue();
                String summa = Convertor.toDecimalString(CalcUtils.dformat, requestCredit.getCreditsum());
                String numdog = requestCredit.getUniquenomer();

                if (StringUtils.isNotEmpty(phoneNumber) && StringUtils.isNotEmpty(summa) && StringUtils.isNotEmpty(numdog)){
                    logger.error("!!!!DEBUG ONLY !!!!! sms sending.... phone=" + Convertor.fromMask(phoneNumber) +"; sum=" + summa +"; numdog="+numdog);
                    mailBean.sendSMSV2(Convertor.fromMask(phoneNumber), "На Вашу пластиковую карту отправлен денежный перевод через систему Arius, сумма " + summa + "; номер договора (перевода) " + numdog);
                }
            } catch (Exception e) {
                logger.error("СМСКА не послана получателю денег. Cause: " + e.getMessage(), e);
            }

            return false;
        }else{
            //Error
            throw new ActionException(ErrorKeys.BAD_RESPONSE, responseV2.getVal(AriusConstants.ERROR_MESSAGE), Type.TECH, ResultType.FATAL, null);
        }

    }


    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId,PluginExecutionContext context) throws ActionException {
        logger.info("querySingleResponse from AriusPayBean with id " + businessObjectId + " Started!");
        AriusPayPluginConfig config = (AriusPayPluginConfig) context.getPluginConfig();
        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
        if (payment == null) {
            logger.info("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,null);
        }

        AriusTransferStatusResponse response = ariusService.doTransferStatus(payment.getExternalId(),payment.getExternalId2());

        AriusStatusEnum statusEnum = response.getStatus();
        if(statusEnum == null){
            logger.error("Arius Transfer Status is null.So shouldn't be.");
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Что то не так, статус не получен.", Type.TECH, ResultType.FATAL,null);
        }else if(statusEnum.equals(AriusStatusEnum.approved)){
            //SUCCESS RESPONSE
            handleSuccess(payment, new Date());
            return true;
        }else if(statusEnum.equals(AriusStatusEnum.declined)){
            //Transfer declined
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Transaction is declined", Type.TECH, ResultType.NON_FATAL, null);
        }else if(statusEnum.equals(AriusStatusEnum.error)){
            //Transaction is declined but something went wrong, please inform your account manager, final status
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Transaction is declined,status is error", Type.TECH, ResultType.NON_FATAL, null);
        }else if(statusEnum.equals(AriusStatusEnum.filtered)){
            //Transaction is declined by fraud internal or external control systems, final status
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Transaction is declined,status is filtered", Type.TECH, ResultType.NON_FATAL, null);
        }else if(statusEnum.equals(AriusStatusEnum.processing)){
            //Transaction is being processed, you should continue polling, non final status
            return false;
        }else if(statusEnum.equals(AriusStatusEnum.unknown)){
            logger.error("Arius Transfer Status is unknown.Please inform your account manager");
            //The status of transaction is unknown, please inform your account manager, non final status
            return false;
        }

        return false;
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId,PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

	@Override
	public Integer getPartnerId() {
		return Partner.ARIUS;
	}


}
