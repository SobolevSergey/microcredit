package ru.simplgroupp.ejb.plugins.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.util.HashMap;
import java.util.Map;

import org.admnkz.crypto.CryptoException;
import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.simplgroupp.crypto.CryptoSettings;
import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.PayonlinePayBeanLocal;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.CountryEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.persistence.PaymentStatus;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.BaseAddress;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.transfer.PeoplePersonal;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.net.ssl.SSLContext;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

/**
 * Плугин для передачи займа через payonline
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PayonlinePayBean extends PaymentProcessorBean implements PayonlinePayBeanLocal {

    private static final Logger logger = LoggerFactory.getLogger(PayonlinePayBean.class);

    private static final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();

    static {
        decimalFormatSymbols.setDecimalSeparator('.');
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00", decimalFormatSymbols);

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    private ICryptoService cryptoService;

    @EJB
    private PaymentDAO paymentDAO;
    
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,
            PluginExecutionContext context) throws ActionException {
        logger.info("Вызов sendSingleRequest " + businessObjectId);
        PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
        if (payment == null) {
            logger.warn("PaymentEntity with id " + businessObjectId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,
                    null);
        }
        return sendSingleRequest(payment, context);
    }

    public boolean sendSingleRequest(PaymentEntity payment, PluginExecutionContext context) throws ActionException {

        if (context.getNumRepeats() > 5) {
            handleError(payment, new Date(),
                    new ExceptionInfo(ErrorKeys.MAX_REPEAT_EXCEEDED, "Превышено число попыток",
                            Type.BUSINESS, ResultType.FATAL));
            throw new ActionException(ErrorKeys.MAX_REPEAT_EXCEEDED, "Превышено число попыток",
                    Type.BUSINESS, ResultType.FATAL, null);
        }

        String request;
        String response;
        try {
            request = buildSingleRequest(payment, context);
            logger.info("Request: " + request);
        } catch (ActionException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка формирования запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, e.getMessage(), Type.TECH, ResultType.FATAL, e);
        }

        try {
            response = doSingleRequest(request, context);
        } catch (Exception e) {
            logger.error("Ошибка отправки запроса. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.NO_CONNECTION, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        try {
            parseSingleResponse(payment, response);
        } catch (ActionException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка разбора ответа. Cause: " + e.getMessage(), e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, e.getMessage(), Type.TECH, ResultType.NON_FATAL, e);
        }

        return true;
    }

    private String buildSingleRequest(PaymentEntity payment, PluginExecutionContext context) throws ActionException {

        PayonlinePayPluginConfig config = (PayonlinePayPluginConfig) context.getPluginConfig();

        CreditEntity credit = payment.getCreditId();
        AccountEntity account = credit.getCreditRequestId().getAccountId();
        String personalInformationId;
        try {
            personalInformationId = getPersonalInformationId(credit.getPeopleMainId(), config);
        } catch (CryptoException e) {
            logger.error("Не удалось создать sslContext ", e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, null, Type.TECH, ResultType.FATAL, e);
        } catch (Exception e) {
            logger.error("Ошибка получения PersonalInformationId", e);
            throw new ActionException(ErrorKeys.WAIT, null, Type.TECH, ResultType.NON_FATAL, e);
        }

        StringBuilder params = new StringBuilder();
        String amount = decimalFormat.format(payment.getAmount());

        String signedData =
                "MerchantId=" + config.getMerchantId() + "&RebillAnchor=" + account.getRebillAnchor() + "&OrderId=" +
                        payment.getId() + "&Amount=" + amount + "&Currency=RUB" + "&Operation=Refill" +
                        "&PrivateSecurityKey=" + config.getPrivateSecurityKey();
        String securityKey = DigestUtils.md5Hex(signedData);

        try {
            params.append("MerchantId=").append(
                    URLEncoder.encode(String.valueOf(config.getMerchantId()), "UTF-8")).append("&RebillAnchor=").append(
                    URLEncoder.encode(String.valueOf(account.getRebillAnchor()), "UTF-8")).append("&OrderId=").append(
                    URLEncoder.encode(String.valueOf(payment.getId()), "UTF-8")).append("&Amount=").append(amount)
                    .append("&Currency=RUB").append(
                    "&SecurityKey=").append(URLEncoder.encode(securityKey, "UTF-8")).append("&ContentType=xml")
                    .append("&PaymentInformationId=").append(URLEncoder.encode(personalInformationId, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Ошибка формирования запроса", e);
            throw new ActionException(ErrorKeys.BAD_REQUEST, null, Type.TECH, ResultType.FATAL, e);
        }

        return params.toString();
    }

    private String doSingleRequest(String request, PluginExecutionContext context) throws Exception {
        PayonlinePayPluginConfig config = (PayonlinePayPluginConfig) context.getPluginConfig();

        SSLContext sslContext = HTTPUtils.createEmptyTrustSSLContext();

        String url = config.getRefillUrl();
        return new String(HTTPUtils.sendHttp("POST", url, request.getBytes(), null, sslContext));
    }

    protected void parseSingleResponse(PaymentEntity payment, String response)
            throws ActionException {
        logger.info("Response: " + response);
        try {
            Document doc = XmlUtils.getDocFromString(response, "<?xml version='1.0' encoding='UTF-8'?>");
            Element root = XmlUtils.findElement(true, doc, "transaction");

            String result = XmlUtils.findElement(true, root, "result").getTextContent();

            if ("OK".equalsIgnoreCase(result)) {
                payment.setExternalId(XmlUtils.findElement(true, root, "id").getTextContent());
                handleSuccess(payment, new Date());
            } else if ("Error".equalsIgnoreCase(result)) {
                String errorCode = XmlUtils.findElement(true, root, "errorCode").getTextContent();
                if ("1".equals(errorCode) || "3".equals(errorCode)) {
                    logger.error("Произошла нефатальная ошибка при проведении платежа, повторяем ");
                    handleError(payment, new Date(),
                            new ExceptionInfo(ErrorKeys.WAIT, "", Type.BUSINESS, ResultType.NON_FATAL));
                    throw new ActionException(ErrorKeys.WAIT, "", Type.BUSINESS, ResultType.NON_FATAL, null);
                } else {
                    logger.error("Произошла ошибка при проведении платежа {}: {}, {}", payment.getId(), errorCode,
                            "транзакция отклоняется банком-эмитентом");
                    handleError(payment, new Date(),
                            new ExceptionInfo(ErrorKeys.BANK_ERROR, "транзакция отклоняется банком-эмитентом",
                                    Type.BUSINESS, ResultType.FATAL));
                    throw new ActionException(ErrorKeys.BANK_ERROR, "транзакция отклоняется банком-эмитентом",
                            Type.BUSINESS, ResultType.FATAL, null);
                }
            } else {
                logger.error("Неверный XML");
                throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL,
                        null);
            }
        } catch (ActionException e) {
            logger.error("Ошибка разбора", e);
            throw e;
        } catch (Exception e) {
            logger.error("Неверный XML", e);
            throw new ActionException(ErrorKeys.BAD_RESPONSE, "Неверный XML", Type.TECH, ResultType.NON_FATAL, e);
        }
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId,
            PluginExecutionContext context) throws ActionException {
    	PaymentEntity payment = paymentDAO.getPayment(Convertor.toInteger(businessObjectId));
        return payment.getStatus() == PaymentStatus.SUCCESS;
    }

    @Override
    public String getSystemName() {
        return PayonlinePayBeanLocal.SYSTEM_NAME;
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
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context)
            throws ActionException {

    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[]{Account.PAYONLINE_CARD_TYPE};
    }

    @Override
    public String getBusinessObjectClass() {
        return Payment.class.getName();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context)
            throws ActionException {
        logger.warn("Метод executeSingle не поддерживается");
    }

    @Override
    public String getSystemDescription() {
        return PayonlinePayBeanLocal.SYSTEM_DESCRIPTION;
    }

    @Override
    public Integer getPartnerId() {
        return Partner.PAYONLINE;
    }

    private String getPersonalInformationId(PeopleMainEntity people, PayonlinePayPluginConfig config)
            throws IOException, KassaException, CryptoException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.PASCAL_CASE_TO_CAMEL_CASE);

        PersonalInformation personalInformation = new PersonalInformation();
        PeoplePersonalEntity peoplePersonal = peopleBean.findPeoplePersonalActive(people);

        if (peoplePersonal != null) {
            personalInformation.setName(peoplePersonal.getName());
            personalInformation.setSurname(peoplePersonal.getSurname());
            personalInformation.setSecondName(peoplePersonal.getMidname());
            if (peoplePersonal.getBirthdate() != null) {
                personalInformation.setBirthday(dateFormat.format(peoplePersonal.getBirthdate()));
            }
            personalInformation.setGender(mapGender(peoplePersonal.getGender()));
        }

        personalInformation.setCellular(peopleBean.findContactClient(PeopleContact.CONTACT_CELL_PHONE, people.getId()));
        personalInformation.setPhone(peopleBean.findContactClient(PeopleContact.CONTACT_HOME_PHONE, people.getId()));

        AddressEntity registeredAddress = peopleBean.findAddressActive(people, BaseAddress.REGISTER_ADDRESS);
        if (registeredAddress != null) {
            personalInformation.setOfficialAddress(registeredAddress.getDescription());
        }

        AddressEntity residentialAddress = peopleBean.findAddressActive(people, BaseAddress.RESIDENT_ADDRESS);
        if (residentialAddress != null) {
            personalInformation.setPostalAddress(residentialAddress.getDescription());
        }

        personalInformation.setCountry(mapCountry(peoplePersonal.getCitizen()));

        DocumentEntity document = peopleBean.findPassportActive(people);
        if (document != null) {
            String series = document.getSeries();
            if (series != null && series.length() >= 4) {
                series = series.substring(0, 2) + " " + series.substring(2);
            }
            DocumentData documentData = new DocumentData();
            documentData.setDocumentType(mapDocumentType(document.getDocumenttypeId()));
            documentData.setNumber(series + " " + document.getNumber());
            documentData.setDate(dateFormat.format(document.getDocdate()));
            documentData.setCustomData(document.getDocorgcode());
            documentData.setIssuedBy(document.getDocorg());

            personalInformation.getDocuments().add(documentData);
        }

        PersonalInformationRequest personalInformationRequest = new PersonalInformationRequest(personalInformation);

        SSLContext sslContext = cryptoService.createX509SSLContext(CryptoSettings.PAYONLINE_PAY_CLIENT,
                CryptoSettings.PAYONLINE_PAY_SERVER);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        byte[] body = objectMapper.writeValueAsBytes(personalInformationRequest);
        logger.info("getPersonalInformationId request: " + new String(body));

        byte[] response = HTTPUtils.sendHttp("POST", config.getPersonalInfoUrl(), body
                , headers, sslContext);
        if (response == null) {
            throw new KassaException("Cant get personal info id");
        }
        logger.info("getPersonalInformationId response: " + new String(response));

        PersonalInformationResponse personalInformationResponse = objectMapper.readValue(response, PersonalInformationResponse.class);
        return personalInformationResponse.getPersonalInformationId();
    }

    private String mapCountry(CountryEntity country) {
        if (country == null || BaseAddress.COUNTRY_RUSSIA.equals(country.getCode())) {
            return "643";
        }

        return  "643";
    }

    private String mapGender(ReferenceEntity gender) {
        if (gender == null) {
            return "0";
        }

        if (PeoplePersonal.GENDER_MAN == gender.getCodeinteger()) {
            return "1";
        }
        if (PeoplePersonal.GENDER_WOMAN == gender.getCodeinteger()) {
            return "2";
        }

        return "0";
    }

    private String mapDocumentType(ReferenceEntity documentType) {
        return "2";
    }

    public static class PersonalInformationResponse implements Serializable {

        private static final long serialVersionUID = -1758492576983189386L;

        private String personalInformationId;

        public String getPersonalInformationId() {
            return personalInformationId;
        }

        public void setPersonalInformationId(String personalInformationId) {
            this.personalInformationId = personalInformationId;
        }
    }

    private class PersonalInformationRequest implements Serializable {

        private static final long serialVersionUID = 3654557534777931172L;

        private PersonalInformation personalInformation;

        public PersonalInformationRequest(PersonalInformation personalInformation) {
            this.personalInformation = personalInformation;
        }

        public PersonalInformation getPersonalInformation() {
            return personalInformation;
        }

        public void setPersonalInformation(PersonalInformation personalInformation) {
            this.personalInformation = personalInformation;
        }
    }

    private class PersonalInformation implements Serializable {

        private static final long serialVersionUID = -3420194864507633033L;

        private String name;

        private String surname;

        private String secondName;

        private String birthday;

        private String gender;

        private String cellular;

        private String phone;

        private String officialAddress;

        private String postalAddress;

        private String country;

        private String customData;

        private List<DocumentData> documents = new ArrayList<>(1);

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getSecondName() {
            return secondName;
        }

        public void setSecondName(String secondName) {
            this.secondName = secondName;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCellular() {
            return cellular;
        }

        public void setCellular(String cellular) {
            this.cellular = cellular;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getOfficialAddress() {
            return officialAddress;
        }

        public void setOfficialAddress(String officialAddress) {
            this.officialAddress = officialAddress;
        }

        public String getPostalAddress() {
            return postalAddress;
        }

        public void setPostalAddress(String postalAddress) {
            this.postalAddress = postalAddress;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCustomData() {
            return customData;
        }

        public void setCustomData(String customData) {
            this.customData = customData;
        }

        public List<DocumentData> getDocuments() {
            return documents;
        }

        public void setDocuments(List<DocumentData> documents) {
            this.documents = documents;
        }
    }

    private class DocumentData implements Serializable {

        private static final long serialVersionUID = -1808346881385788459L;

        private String documentType;

        private String number;

        private String issuedBy;

        private String date;

        private String customData;

        public String getDocumentType() {
            return documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getIssuedBy() {
            return issuedBy;
        }

        public void setIssuedBy(String issuedBy) {
            this.issuedBy = issuedBy;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCustomData() {
            return customData;
        }

        public void setCustomData(String customData) {
            this.customData = customData;
        }
    }
}
