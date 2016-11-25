package ru.simplgroupp.ejb.plugins.payment;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.AlfaBankMarengoBeanLocal;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.Pair;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;
import javax.inject.Inject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Реальный модуль для работы с Marengo
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(AlfaBankMarengoBeanLocal.class)
public class AlfaBankMarengoBean extends PaymentProcessorBean implements AlfaBankMarengoBeanLocal {

	@Inject Logger logger;

    private static final String FORMAT_VERSION = "1.05";
    @EJB
    private ServiceBeanLocal servBean;

    @EJB
    private ICryptoService cryptoBean;

    @EJB
    private KassaBeanLocal kassaBean;

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    private ICryptoService cryptoService;
    
	@EJB
	WorkflowBeanLocal workflow;

	@EJB
    private PaymentDAO paymentDAO;
	
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat tf = new SimpleDateFormat("HH:mm:ss");
    private static DecimalFormat nf;

    {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        nf = new DecimalFormat("##############.00", otherSymbols);
    }
    @Override
    public String getSystemName() {
        return AlfaBankMarengoBeanLocal.SYSTEM_NAME;
    }

    @Override
    public boolean isFake() {
        return false;
    }
    
	@Override
	public Set<String> getModelTargetsSupported() {
		return Utils.setOf();
	}    

    @Override
    public EnumSet<Mode> getModesSupported() {
        return EnumSet.of(Mode.PACKET);
    }

    @Override
    public EnumSet<ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(ExecutionMode.AUTOMATIC);
    }

    @Override
    public EnumSet<SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.SYNC);
    }

    protected List<PaymentEntity> retrieveExecutePayments(PluginExecutionContext context) {
        Partner partner = context.getPluginConfig().getPartner();
        return paymentDAO.findExecutedPayments(PaymentStatus.NEW,partner);
      
    }


    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        List<PaymentEntity> payments = retrieveExecutePayments(context);

        List<BusinessObjectResult> result = new ArrayList<BusinessObjectResult>(payments.size());

        if (payments.size() < 1) {
            return Collections.emptyList();
        }

        String request;
        Object response;

        try {
            request = buildPacketRequest(payments, context);
        } catch (Exception e) {

            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {
            response = doPacketRequest(request, context);
        } catch (Exception e) {

            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        try {
            return parsePacketResponse(payments, response);
        } catch (Exception e) {

            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        return false;
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        return false;
    }

    protected Object doPacketRequest(Object request, PluginExecutionContext context) throws Exception {
        AlfaBankMarengoPluginConfig config = (AlfaBankMarengoPluginConfig) context.getPluginConfig();

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/xml");

        return HTTPUtils.sendHttp("POST", config.isUseWork() ? config.getWorkOrderURL() : config.getTestOrderURL(),
                ((String) request).getBytes(), headers, HTTPUtils.createEmptyTrustSSLContext());
    }

    //* executePacket */

    protected String buildPacketRequest(Collection<PaymentEntity> payments, PluginExecutionContext context) throws ActionException {
        AlfaBankMarengoPluginConfig config = (AlfaBankMarengoPluginConfig) context.getPluginConfig();

        String data = buildOrderXml(payments, context);

//        if (!config.isUseWork()) {
//            return data;
//        }

        byte[] signature;
        try {
            signature = sign(data);
        } catch (Exception e) {
            throw new ActionException(ErrorKeys.CANT_CREATE_SIGN, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><signed xmlns=\"urn:x-obml:1.0\"\n" +
                "xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "xsi:type=\"CMSDETACHED\">");
        xml.append("<data contentType=\"application/xml\">").append(javax.xml.bind.DatatypeConverter.printBase64Binary(data.getBytes())).append("</data>");
        xml.append("<signature>").append(javax.xml.bind.DatatypeConverter.printBase64Binary(signature)).append("</signature>");
        xml.append("</signed>");

        return xml.toString();
    }

    protected List<BusinessObjectResult> parsePacketResponse(Collection<PaymentEntity> payments, Object response) throws ActionException {

        Map<Integer, PaymentEntity> paymentMap = new HashMap<Integer, PaymentEntity>();
        for(PaymentEntity p: payments) {
            paymentMap.put(p.getId(), p);
        }

        List<BusinessObjectResult> result = new ArrayList<BusinessObjectResult>(payments.size());

        try {

            Document doc = XmlUtils.getDocFromString(new String((byte[])response));

            if (doc!=null) {

                Element errorEl = XmlUtils.findElement(false, doc, "error");

                if (errorEl != null) {
                    int errorCode = Integer.valueOf(XmlUtils.findElement(true, errorEl, "code").getTextContent());
                    String errorDescription = XmlUtils.findElement(true, errorEl, "description").getTextContent();
                    Pair<Integer, ResultType> error = mapError(errorCode);
                    throw new ActionException(error.getFirst(), errorDescription, Type.BUSINESS, error.getSecond(), null);
                }

                String data = XmlUtils.findElement(true, doc, "data").getTextContent();
                byte[] dataBytes = null;
                if (data != null) {
                    dataBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(data);
                }

                String signature = XmlUtils.findElement(true, doc, "signature").getTextContent();
                byte[] signatureBytes = null;
                if (signature != null) {
                    signatureBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(signature);
                }

                try {
                    if (checkSign(dataBytes, signatureBytes)) {
                        doc = XmlUtils.getDocFromString(new String(dataBytes));

                        if (XmlUtils.isExistNode(doc, "success")) {

                            for(Node node: XmlUtils.findNodeList(XmlUtils.findElement(true, doc, "success"), "doumentId")) {
                                try {
                                    Integer id = Integer.valueOf(node.getTextContent());
                                    result.add(handleSuccess(paymentMap.get(id), new Date()));

                                    paymentMap.remove(id);
                                } catch (NumberFormatException e) {
                                    logger.severe("Invalid doumentId value "+node.getTextContent());
                                }

                                for(PaymentEntity p: paymentMap.values()) {
                                    result.add(new BusinessObjectResult(p.getClass().getName(), p.getId(), null, new ExceptionInfo(ErrorKeys.BAD_RESPONSE, "Нет ответа", Type.TECH, ResultType.NON_FATAL)));
                                }
                            }
                        } else if (XmlUtils.isExistNode(doc, "error")) {

                            Element errorXML = XmlUtils.findElement(true, doc, "error");
                            int errorCode = Integer.valueOf(XmlUtils.findElement(true, errorXML, "code").getTextContent());
                            String errorDescription = XmlUtils.findElement(true, errorXML, "description").getTextContent();

                            Pair<Integer, ResultType> error = mapError(errorCode);
                            throw new ActionException(error.getFirst(), errorDescription, Type.BUSINESS, error.getSecond(), null);

                        } else {
                            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, null);
                        }

                    }
                } catch (Exception e) {
                    throw new ActionException(ErrorKeys.INVALID_SIGN, "Неверная подпись", Type.TECH, ResultType.NON_FATAL, e);
                }
            } else {
                logger.severe("Invalid xml for payment "+StringUtils.join(paymentMap.keySet(), ","));
                throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, null);
            }

        } catch (KassaException e) {
            logger.severe("Can't parse response for payment \"+businessObjectId+\". Cause: " + e.getMessage());
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, e);
        }

        return result;
    }

    private Pair<Integer, ResultType> mapError(int errorCode) {
        switch (errorCode) {

        }

        return new Pair<Integer, ResultType>(ErrorKeys.UNKNOWN, ResultType.FATAL);
    }

    private String buildOrderXml(Collection<PaymentEntity> payments, PluginExecutionContext context) {

        AlfaBankMarengoPluginConfig config = (AlfaBankMarengoPluginConfig) context.getPluginConfig();
        boolean useWork = config.isUseWork();

        String organization = useWork ? config.getWorkOrganization() : config.getTestOrganization();
        String account = useWork ? config.getWorkAccount() : config.getTestAccount();
        String inn = useWork ? config.getWorkInn() : config.getTestInn();
        String bik = useWork ? config.getWorkBik() : config.getTestBik();

        Date now = new Date();

        /** СекцияПлатежногоДокумента */

        Date startDate = null;
        Date endDate = null;

        double total = 0;


        StringBuilder docs = new StringBuilder();
        for (PaymentEntity payment: payments) {

            Date date = payment.getCreateDate();
            if (!config.isUseWork()) {
            	date=DatesUtils.makeDate(2013, 3, 16);
            }

            CreditEntity credit = payment.getCreditId();

            PeopleMainEntity borrower = credit.getPeopleMainId();
            AccountEntity borrowerAccount = credit.getCreditRequestId().getAccountId();
            PeoplePersonalEntity borrowerPersonal = peopleBean.findPeoplePersonalActive(borrower);

            docs.append("<СекцияПлатежногоДокумента>");

            docs.append("<СекцияДокумент>Платежное поручение</СекцияДокумент>");
            docs.append("<Номер>").append(payment.getId()).append("</Номер>");  // TODO: Номер
            docs.append("<Дата>").append(df.format(date)).append("</Дата>");
            docs.append("<Сумма>").append(nf.format(payment.getAmount())).append("</Сумма>");
            //xml.append("<Квитанция>").append("</Квитанция>");

            /** РеквизитыПлательщика */
            docs.append("<РеквизитыПлательщика>");
            docs.append("<ПлательщикСчет>").append(account).append("</ПлательщикСчет>");
            docs.append("<ДатаСписано ИдПлатежа=\"").append(payment.getId()).append("\">").append(df.format(config.isUseWork() ? now : date)).append("</ДатаСписано>");
            docs.append("<ПлательщикИНН>").append(inn).append("</ПлательщикИНН>");
            docs.append("<Плательщик1>").append(organization).append("</Плательщик1>");
//            docs.append("<ПлательщикБанк1>").append(request.getPayerBank()).append("</ПлательщикБанк1>");
            docs.append("<ПлательщикБИК>").append(bik).append("</ПлательщикБИК>");
//            docs.append("<ПлательщикКорсчет>").append(request.getPayerCorAccount()).append("</ПлательщикКорсчет>");

            docs.append("</РеквизитыПлательщика>");

            /** РеквизитыПолучателя */
            docs.append("<РеквизитыПолучателя>");
            docs.append("<ПолучательСчет>").append(borrowerAccount.getAccountnumber()).append("</ПолучательСчет>");
            docs.append("<Получатель>").append(borrowerPersonal.getFullName()).append("</Получатель>");
            docs.append("<ПолучательИНН>").append(borrower.getInn()).append("</ПолучательИНН>");
            docs.append("<Получатель1>").append(borrowerPersonal.getFullName()).append("</Получатель1>");
            docs.append("<ПолучательБанк1>").append(borrowerAccount.getBankname() == null ? "" : borrowerAccount.getBankname()).append("</ПолучательБанк1>");
            docs.append("<ПолучательБИК>").append(borrowerAccount.getBik()).append("</ПолучательБИК>");
//            docs.append("<ПолучательКорсчет>").append(borrowerAccount.getCorraccountnumber()).append("</ПолучательКорсчет>");
            docs.append("</РеквизитыПолучателя>");

            /** РеквизитыПлатежа */
            docs.append("<РеквизитыПлатежа>");
            docs.append("<ВидПлатежа>Электронно</ВидПлатежа>");
            docs.append("<ВидОплаты>01</ВидОплаты>");
            docs.append("<Очередность>4</Очередность>");
            docs.append("<Код>").append(payment.getId()).append("</Код>");
            docs.append("<НазначениеПлатежа>").append("Предоставление процентного займа № ").append(credit.getId())
                    .append("от ").append(credit.getDatabeg()).append(" г.").append("</НазначениеПлатежа>");
            docs.append("</РеквизитыПлатежа>");

            docs.append("</СекцияПлатежногоДокумента>");

            if (startDate == null || startDate.after(payment.getCreateDate())) {
                startDate = payment.getCreateDate();
            }
            if (endDate == null || endDate.before(payment.getCreateDate())) {
                endDate = payment.getCreateDate();
            }

            total += payment.getAmount();
        }

        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<ClientBankExchange xmlns=\"urn:1C.ru:ClientBankExchange\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"1C-Kl_Exch.xsd\">");
        xml.append("<ВерсияФормата>").append(FORMAT_VERSION).append("</ВерсияФормата>");
        xml.append("<Отправитель>").append("Касса онлайн").append("</Отправитель>");
        xml.append("<Получатель>").append("Альфабанк").append("</Получатель>");
        xml.append("<ДатаСоздания>").append(df.format(now)).append("</ДатаСоздания>");
        xml.append("<ВремяСоздания>").append(tf.format(now)).append("</ВремяСоздания>");

        /** УсловияОтбора */

//        xml.append("<УсловияОтбора>");
//
//        xml.append("<ДатаНачала>").append(df.format(startDate)).append("</ДатаНачала>");
//        xml.append("<ДатаКонца>").append(df.format(endDate)).append("</ДатаКонца>");
//        xml.append("<РасчСчет>").append(account).append("</РасчСчет>");
//        xml.append("<Документ>").append("</Документ>");
//
//        xml.append("</УсловияОтбора>");

        /** СекцияРасчСчет */
//
//        xml.append("<СекцияРасчСчет>");
//
//        xml.append("<ДатаНачала>").append(df.format(startDate)).append("</ДатаНачала>");
//        xml.append("<ДатаКонца>").append(df.format(endDate)).append("</ДатаКонца>");
//
//        xml.append("<РасчСчета>");
//        xml.append("<РасчСчет>").append("???").append("</РасчСчет>");
//        xml.append("<НачальныйОстаток>").append("???").append("</НачальныйОстаток>");
//        xml.append("<ВсегоПоступило>").append(0).append("</ВсегоПоступило>");
//        xml.append("<ВсегоСписано>").append(nf.format(total)).append("</ВсегоСписано>");
//        xml.append("<КонечныйОстаток>").append("???").append("</КонечныйОстаток>");
//        xml.append("</РасчСчета>");
//
//        xml.append("</СекцияРасчСчет>");
        xml.append(docs);
        xml.append("</ClientBankExchange>");
        return xml.toString();
    }

    @Override
    public void addToPacket(String businessObjectClass,
                            Object businessObjectId, PluginExecutionContext context) throws ActionException {

        Integer paymentId = Convertor.toInteger(businessObjectId);
        // TODO возможно, как-то пометить, что этот платёж входит в пакет

    }

    private byte[] sign(String data) throws Exception {
        return cryptoService.createJCPCMS(data.getBytes(), "sign.alfamorengo.client-director", true);
    }

    private boolean checkSign(byte[] data, byte[] signature) throws Exception {

        return cryptoService.verifyJCPCMS(signature, "ssl.alfamorengo.server");
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        // не поддерживается
        return null;
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        // не поддерживается
        return null;
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[] {Account.BANK_TYPE,Account.CARD_TYPE};
    }
    
	@Override
	public String getBusinessObjectClass() {
		return Payment.class.getName();
	}

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {

    }

	@Override
	public String getSystemDescription() {
		return AlfaBankMarengoBeanLocal.SYSTEM_DESCRIPTION;
	}

}
