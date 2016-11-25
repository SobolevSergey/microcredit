package ru.simplgroupp.ejb.plugins.payment;

import org.admnkz.crypto.CryptoException;
import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.contact.protocol.v2.IAppServAssembler;
import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;
import ru.simplgroupp.contact.protocol.v2.response.CdtrnCXR;
import ru.simplgroupp.contact.protocol.v2.response.ContactResponse;
import ru.simplgroupp.contact.protocol.v2.response.responseparsing.CheckRestResponseParser;
import ru.simplgroupp.contact.protocol.v2.response.responseparsing.ResponseParser;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.ResponseUnp;
import ru.simplgroupp.contact.protocol.v2.tmoneyorderobject.GetMethodAssembler;
import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.SoapUtils;
import ru.simplgroupp.util.XmlUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.persistence.EntityManager;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author aro
 */
/*
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
   хотел сделать бин, но передумал, ибо чтобы проверить надо сервер пускать
*/
public class ContactBaseBean {
	private static final Logger logger = Logger.getLogger(ContactBaseBean.class.getName());
    private ReferenceBooksLocal refBooks;
    private ICryptoService cryptoService;

    private ResponseParser responseParser;
    private CheckRestResponseParser checkRestResponseParser;

    private PaymentProcessorBean bean;
    private EntityManager emMicro;



    /** Общий метод запроса к Контакту. Все методы его вызывают.
     * @param data
     * @param assembler
     * @param sign
     * @throws ru.simplgroupp.exception.ActionException
     */
    public CdtrnCXR doCall(Map<String,String> data, IAppServAssembler assembler,boolean sign,Map<String, String> childrenData, ContactPayPluginConfig pluginConfig,String testKeysDir) throws ActionException {

        Partner partner = refBooks.getPartner(Partner.CONTACT);
        boolean test = !pluginConfig.isUseWork();
        String url = test ? partner.getUrlTest() : partner.getUrlWork();
        return doCall(data,assembler,sign,childrenData,url,pluginConfig.getKeyStorePassword(),pluginConfig.getKeyStoreAlias(),pluginConfig.getKeyStoreName(),testKeysDir);

    }

    public CdtrnCXR doCall(Map<String,String> data, IAppServAssembler assembler,boolean sign,Map<String, String> childrenData,String url, String password,String alias,String keyStoreName,String testKeysDir
            ) throws ActionException {

        URL contactEndpoint = SoapUtils.generateURLForSOAPConnection(url,0,0);
        return doCall(data,assembler,sign,childrenData,contactEndpoint,password,alias,keyStoreName,testKeysDir);
    }

    /** Общий метод запроса к Контакту. Все методы его вызывают.
     * @param data
     * @param assembler
     * @param sign
     * @throws ru.simplgroupp.exception.ActionException
     */
    public CdtrnCXR doCall(Map<String,String> data, IAppServAssembler assembler,boolean sign,Map<String, String> childrenData,
                            URL url, String password,String alias,String keyStoreName,String testKeysDir
                                    ) throws ActionException {

        logger.info("************DEBUG***********doCall started url=" + url + "; password=" + password + "; sign=" + sign);

        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = null;
        SOAPConnection soapConnection = null;
        CdtrnCXR cdtrn = null;
        try {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
            soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server

            String ppCode = data.get(BaseDoc.POINT_CODE);
            byte[] appServBytes = assembler.createAppServ(data,sign,childrenData);
            logger.severe("************DEBUG***********AppServ started");
            logger.severe("************DEBUG***********AppServ!!!!!!!!!!!!!!!!!!!!!!!!!!! =" + new String(appServBytes,XmlUtils.ENCODING_UTF));
            String dtuString = null;
            if(sign){
                byte[] sig = null;
                try {
                    sig = cryptoService.signContactRequest(appServBytes,password,alias,keyStoreName,testKeysDir);
                    dtuString = assembler.createDTUWithSignature(appServBytes, sig, ppCode);
                } catch (CryptoException e) {
                    logger.log(Level.SEVERE,"Not signing request!",e);
                }
            }else{
                dtuString = assembler.createDTUWithoutSignature(appServBytes,ppCode);
            }
            //logger.error("************DEBUG***********dtuString!!!!!!!!!!!!!!!!!!!!!!!!!!! =" + dtuString);

            SOAPMessage sm = SoapUtils.createSOAPRequest(dtuString);

//            logger.severe("SOAPMessage request = !!!!!!!!!!!!!!!!!!!!!!!!!!! =" + Convertor.soapToString(sm,XmlUtils.ENCODING_WINDOWS));

            SSLSocketFactory fact = HTTPUtils.createEmptyTrustSSLContext().getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(fact);
    	    HttpsURLConnection.setDefaultHostnameVerifier(HTTPUtils.allHostsValid());

            SOAPMessage soapResponse = soapConnection.call(sm, url);

            logger.info("!!!!!!! Response SOAP Message from Contact:\n" + Convertor.soapToString(soapResponse,XmlUtils.ENCODING_WINDOWS));

            ContactResponse contactResponse = SoapUtils.processSoapResponse(soapResponse);
            cdtrn = SoapUtils.parseOutXML(contactResponse);
            SoapUtils.processResponseData(cdtrn);


        } catch (SOAPException e) {
            logger.log(Level.SEVERE,"Ошибка,метод doCall ",e);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Ошибка,метод doCall ", e);
        } catch (Exception e) {
        	logger.log(Level.SEVERE,"Ошибка,метод doCall ", e);
        } finally {
            try {
                soapConnection.close();
            } catch (SOAPException e) {
                logger.severe("Не удалось закрыть Soap connection "+ e);
            }
        }
        return cdtrn;
    }


    public ReferenceBooksLocal getRefBooks() {
        return refBooks;
    }

    public void setRefBooks(ReferenceBooksLocal refBooks) {
        this.refBooks = refBooks;
    }

    public ICryptoService getCryptoService() {
        return cryptoService;
    }

    public void setCryptoService(ICryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    public ResponseParser getResponseParser() {
        return responseParser;
    }

    public void setResponseParser(ResponseParser responseParser) {
        this.responseParser = responseParser;
    }

    public CheckRestResponseParser getCheckRestResponseParser() {
        return checkRestResponseParser;
    }

    public void setCheckRestResponseParser(CheckRestResponseParser checkRestResponseParser) {
        this.checkRestResponseParser = checkRestResponseParser;
    }

    public EntityManager getEmMicro() {
        return emMicro;
    }

    public void setEmMicro(EntityManager emMicro) {
        this.emMicro = emMicro;
    }

    /**
     * Если ответ ошибочный, выйдет exception, иначе дальше идем. Состояние ошибки не записывается в payment
     * @param response
     * @throws ActionException
     */
    public void checkError(ResponseUnp response) throws ActionException {
    	if (response==null ||response.getRE() ==null ) {
    		String errorString = "Неверный XML, или не работает сервер";
    		throw new ActionException(ErrorKeys.BAD_RESPONSE,errorString,Type.TECH,ResultType.FATAL, null);
    	}
        Integer status = Convertor.toInteger(response.getRE());
        if(status<0){
            String message = response.getERR_TEXT();
            ExceptionInfo errorInfo = new ExceptionInfo(ErrorKeys.BAD_RESPONSE,message,Type.BUSINESS, ResultType.FATAL);
            throw new ActionException(status, message, errorInfo.getType(),errorInfo.getResultType(), null);
        }
    }

    /**
     * Если ответ ошибочный, выйдет exception, иначе дальше идем. Состояние ошибки записывается в payment
     * @param payment
     * @param response
     * @throws ActionException
     */
    public void parseError(PaymentEntity payment, ResponseUnp response) throws ActionException {
        if (response==null ||response.getRE() ==null ){
            String errorString = "Неверный XML, или не работает сервер";
            bean.handleError(payment, new Date(), new ExceptionInfo(ErrorKeys.BAD_RESPONSE,errorString, Type.TECH, ResultType.FATAL));
            throw new ActionException(ErrorKeys.BAD_RESPONSE,errorString,Type.TECH,ResultType.FATAL, null);
        }
        Integer status = Convertor.toInteger(response.getRE());
        if(status<0){
            String message = response.getERR_TEXT();
            ExceptionInfo errorInfo = new ExceptionInfo(ErrorKeys.BAD_RESPONSE,message,Type.BUSINESS, ResultType.FATAL);
            bean.handleError(payment, new Date(), errorInfo);
            throw new ActionException(status, message, errorInfo.getType(),errorInfo.getResultType(), null);
        }
    }

    public PaymentProcessorBean getBean() {
        return bean;
    }

    public void setBean(PaymentProcessorBean bean) {
        this.bean = bean;
    }


    public String doGetPaymentInfoString(Integer paymentId,ContactPayPluginConfig pluginConfig) throws ActionException {

        PaymentEntity payment = emMicro.find(PaymentEntity.class, paymentId);
        if (payment == null) {
            logger.info("PaymentEntity with id " + paymentId + " not found");
            throw new ActionException(ErrorKeys.PAYMENT_NOT_FOUND, "Платёж не найден", Type.TECH, ResultType.FATAL,null);
        }

        Map<String, String> data = new HashMap<String, String>();
        String ppCode = pluginConfig.getPPcode();
        data.put(BaseDoc.POINT_CODE, ppCode);

        String docId = payment.getExternalId();
        if(StringUtils.isNotEmpty(docId)){
            data.put(BaseDoc.DOC_ID, docId);
        }else{
            data.put(BaseDoc.trnSendPoint, ppCode);

            String trnReference = "";
            CreditEntity credit = payment.getCreditId();
            if(credit != null){
                CreditRequestEntity requestCredit = credit.getCreditRequestId();
                if(requestCredit!=null){
                    trnReference = requestCredit.getUniquenomer();
                }
            }
            data.put(BaseDoc.trnReference, trnReference);

            data.put(BaseDoc.trnDate, DatesUtils.DATE_FORMAT_YYYYMMdd.format(payment.getCreateDate()));
        }

        CdtrnCXR cdtrn = doCall(data, new GetMethodAssembler(), true, null, pluginConfig, null);
        logger.info("doGetPaymentInfoString completed");
        if(cdtrn != null){
            return cdtrn.getData().getResponseAsXml();
        }
        return null;
    }

}
