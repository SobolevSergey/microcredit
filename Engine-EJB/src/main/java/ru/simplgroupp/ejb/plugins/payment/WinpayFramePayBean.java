package ru.simplgroupp.ejb.plugins.payment;

import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.payment.WinpayFramePayLocal;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WinpayFramePayBean extends PaymentProcessorBean implements WinpayFramePayLocal {

	@Inject Logger LOGGER;

    @Override
    public double balance(PluginExecutionContext context) throws ActionException, KassaException {
        WinpayPayPluginConfig config = (WinpayPayPluginConfig) context.getPluginConfig();

        String date = DatesUtils.DATE_FORMAT_YYYY_MM_dd_HHmmss.format(new Date());
        String secretKey = config.getSecretKey();
        String hash = DigestUtils.md5Hex(date + secretKey);

        String request;
        try {
            request = "DT=" + URLEncoder.encode(date, "UTF-8") + "&HASH=" + URLEncoder.encode(hash, "UTF-8");

            LOGGER.info(request);
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE,"Ошибка формирования запроса ", e);
            throw new KassaException("Ошибка формирования запроса", e);
        }


        SSLContext sslContext = HTTPUtils.createEmptyTrustSSLContext();

        String url = config.getBalanceUrl();

        String response;
        try {
            response = new String(HTTPUtils.sendHttp("POST", url, request.getBytes(), null, sslContext));
            LOGGER.info(response);
        } catch (IOException e) {
            LOGGER.severe("Ошибка отправки запроса " + e);
            throw new KassaException("Ошибка отправки запроса", e);
        }

        try {
            Document doc = XmlUtils.getDocFromString(response);
            Element element = XmlUtils.findElement(true, doc, "balance");

            if (element != null) {
                double balance = Convertor.toDouble(element.getTextContent());
                paymentService.saveBalance(null, Partner.WINPAY, Account.CARD_TYPE, balance, new Date());
                return balance;
            } else {
                LOGGER.severe("Неуспешный запрос баланса ");
                throw new KassaException("Неуспешный запрос баланса");
            }

        } catch (KassaException e) {
            LOGGER.log(Level.SEVERE,"Произошла ошибка ", e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Неверный XML ", e);
            throw new KassaException("Неверный XML", e);
        }
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[]{Account.CARD_TYPE};
    }

    @Override
    public String getSystemName() {
        return WinpayFramePayLocal.SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return WinpayFramePayLocal.SYSTEM_DESCRIPTION;
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
    public Integer getPartnerId() {
        return Partner.WINPAY;
    }


    @Override
    public String getBusinessObjectClass() {
        return Payment.class.getName();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context)
            throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context)
            throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,
            PluginExecutionContext context) throws ActionException {
        PaymentEntity payment = emMicro.find(PaymentEntity.class, businessObjectId);
        switch (payment.getStatus()) {
            case SUCCESS:
                return true;
            case ERROR:
                ExceptionInfo exceptionInfo = payment.getErrorInfo();
                throw new ActionException(exceptionInfo.getCode(), exceptionInfo.getMessage(), exceptionInfo.getType(),
                exceptionInfo.getResultType(), null);
        }

        return false;
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId,
            PluginExecutionContext context) throws ActionException {
        return true;
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        throw new UnsupportedOperationException();
    }
}
