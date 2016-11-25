package ru.simplgroupp.ejb;

import org.admnkz.crypto.CryptoException;
import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.io.IOUtils;
import org.apache.openejb.api.LocalClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;
import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.ejb.plugins.payment.ContactPayPluginConfig;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.fixtures.AccountFixture;
import ru.simplgroupp.fixtures.CreditFixture;
import ru.simplgroupp.fixtures.CreditRequestFixture;
import ru.simplgroupp.fixtures.PaymentFixture;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.plugins.payment.ContactPayBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.YandexPayBeanLocal;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;
import ru.simplgroupp.workflow.ProcessKeys;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Khodyrev DS
 */
@LocalClient
public class ContactPayBeanTest {

    @EJB
    ActionProcessorBeanLocal actProc;

    @EJB
    WorkflowEngineBeanLocal wfEng;

    @EJB
    WorkflowBeanLocal wfBean;

    @EJB
    ContactPayBeanLocal contactPay;

    @EJB
    ReferenceBooksLocal refBooks;

    @EJB
    KassaBeanLocal kassaBean;

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    private CreditBeanLocal creditBean;
    
    @EJB
    CreditDAO creditDAO;

    @EJB
    ICryptoService cryptoService;

    @EJB
    PaymentService paymentService;


    @PersistenceContext(unitName="MicroPU")
    protected EntityManager emMicro;

    ActionContext actionContext;
    private PluginExecutionContext plctx;
    private ContactPayPluginConfig config;

    @Before
    public void setUp() throws Exception {
/*
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
        System.setProperty("java.util.logging.manager", "org.apache.juli.ClassLoaderLogManager");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties.aro"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
*/


        System.setProperty("openejb.validation.output.level","VERBOSE");
        final Properties p = new Properties();
        //p.load(this.getClass().getResourceAsStream("/test.properties"));
        p.load(this.getClass().getResourceAsStream("/test.properties.aro"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
//       Context context = new InitialContext(p);
        context.bind("inject", this);


        wfEng = (WorkflowEngineBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowEngineBean!ru.simplgroupp.interfaces.WorkflowEngineBeanLocal");
        wfBean = (WorkflowBeanLocal) context.lookup("java:global/Engine-EJB/WorkflowBean!ru.simplgroupp.interfaces.WorkflowBeanLocal");
        actProc = wfEng.getActionProcessor();
        peopleBean = (PeopleBeanLocal) context.lookup("java:global/Engine-EJB/PeopleBean!ru.simplgroupp.interfaces.PeopleBeanLocal");
        creditBean = (CreditBeanLocal) context.lookup("java:global/Engine-EJB/CreditBean!ru.simplgroupp.interfaces.CreditBeanLocal");
        actionContext= actProc.createActionContext(null, true);
        contactPay = actionContext.getPlugins().getContactPay();
        plctx = new PluginExecutionContext(actionContext.getPlugins().getPluginConfig(ContactPayBeanLocal.SYSTEM_NAME)
                , null, 0, Collections.<String, Object>emptyMap(), actionContext.getPluginState(ContactPayBeanLocal.SYSTEM_NAME));
        config = (ContactPayPluginConfig) plctx.getPluginConfig();

    }

    @Test
    public void sendSingleRequestTest() {
        Account invalidAccount = AccountFixture.getYandexInvalidAccount();
        CreditRequest creditRequest = CreditRequestFixture.createCreditRequest(invalidAccount);

        Credit credit = CreditFixture.createCredit(creditRequest);
        Payment payment = PaymentFixture.createPayment(credit);
        try {
            contactPay.sendSingleRequest(Payment.class.getName(), payment.getEntity().getId(), plctx);
            Assert.assertEquals(1,2);
        } catch (ActionException e) {
              //Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
        Integer creditId = 1830;
        Integer accountId = 1258;
        Integer paySumType = 1;//Основная сумма, выплаченная клиенту
        Integer mode =1; //Система клиенту
        CreditEntity creditEntity = creditDAO.getCreditEntity(creditId);
        CreditRequestEntity creditRequestEntity = creditEntity.getCreditRequestId();
        //creditBean.changeCreditRequestEntityUniquenomer(creditRequestEntity.getId());
        payment = paymentService.createPayment(creditId,Account.CONTACT_TYPE,paySumType, 10000d, mode, Partner.CONTACT);

        try {
            boolean res = contactPay.sendSingleRequest(Payment.class.getName(), payment.getEntity().getId(), plctx);
            if(res){
                querySingleResponseTest(payment.getEntity().getId());
            }
        } catch (ActionException e) {
            Assert.assertEquals(1,2);
        }
    }

    //@Test
    private void querySingleResponseTest(Integer paymentId) {
        try {
            boolean res = contactPay.querySingleResponse("пеймент",paymentId,plctx);
        } catch (ActionException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void querySingleResponseTest() {
        Integer paymentId = 4376;
        querySingleResponseTest(paymentId);
    }

    //@Test
    public void testRun() throws Exception {


    	ActionContext context = actProc.createActionContext(null, true);
        wfEng.removeProcessByBusinessKey(ProcessKeys.DEF_SUB_STANDART, new Integer(1316).toString());
        //actProc.runPlugin(YandexPayBeanLocal.SYSTEM_NAME, Payment.class.getName(), new Integer(1316), null);
        contactPay = context.getPlugins().getContactPay();

        contactPay.sendSingleRequest(Payment.class.getName(), 1318, null);
    }
    @Test
    public void getDictionaries() {
        try {
            contactPay.getDictionaries(false);
            Assert.assertEquals(1,1);
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
    }
    @Test
    public void doPingTest() {
        try {
            contactPay.doPing(false);
            Assert.assertEquals(1,1);
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
    }


    @Test
    public void doNewAndSendOutgoingTest() {
        try {

            ActionContext context = actProc.createActionContext(null, true);
            PluginExecutionContext plctx = new PluginExecutionContext(context.getPlugins().getPluginConfig(YandexPayBeanLocal.SYSTEM_NAME), null, 0, Collections.<String, Object>emptyMap(), context.getPluginState(YandexPayBeanLocal.SYSTEM_NAME));
            contactPay = context.getPlugins().getContactPay();

            Map<String,String> childrenMap = new HashMap<String, String>();

            childrenMap.put(BaseDoc.trnService,"2");
            childrenMap.put(BaseDoc.trnClAmount,"1.00");
            childrenMap.put(BaseDoc.trnClCurrency,"RUR");
            childrenMap.put(BaseDoc.trnFeesClient,"0.02");
            childrenMap.put(BaseDoc.trnFeesClientLocal,"0.02");

            childrenMap.put(BaseDoc.sName,"ALEXANDER");
            childrenMap.put(BaseDoc.sLastName,"ZAHARCHENKO");

            childrenMap.put(BaseDoc.sSurName,"");
            childrenMap.put(BaseDoc.sCountry,"BI");
            childrenMap.put(BaseDoc.sZipCode,"");
            childrenMap.put(BaseDoc.sRegion,"");

            childrenMap.put(BaseDoc.sCity,"MOSCOW");
            childrenMap.put(BaseDoc.sAddress,"MOSCOW");
            childrenMap.put(BaseDoc.sPhone,"");
            childrenMap.put(BaseDoc.sIDtype,"PASSPORT");
            childrenMap.put(BaseDoc.sIDnumber,"1234123456");
            childrenMap.put(BaseDoc.sIDdate,"20080423");
            childrenMap.put(BaseDoc.sIDwhom,"GYVD");
            childrenMap.put(BaseDoc.sIDexpireDate,"");

            childrenMap.put(BaseDoc.bName,"IGOR");
            childrenMap.put(BaseDoc.bLastName,"STRELKOV");
            childrenMap.put(BaseDoc.bSurName,"IVANOVICH");

            childrenMap.put(BaseDoc.sResident,"1");
            childrenMap.put(BaseDoc.sBirthday,"19770112");
            childrenMap.put(BaseDoc.trnCurrency,"RUR");
            childrenMap.put(BaseDoc.trnDate,"201070730");


            childrenMap.put(BaseDoc.bCity,"MOSCOW");
            childrenMap.put(BaseDoc.bRegion,"ALGERIA");
            childrenMap.put(BaseDoc.bZipCode,"");
            childrenMap.put(BaseDoc.bCountry,"DZ");
            childrenMap.put(BaseDoc.bBirthday,"19770706");

            childrenMap.put(BaseDoc.trnAdditionalInfo,"");
            childrenMap.put(BaseDoc.trnAmount,"1.00");
            childrenMap.put(BaseDoc.trnReference,"");


            childrenMap.put(BaseDoc.trnSendPoint,BaseDoc.TEST_PPCODE_VALUE);
            childrenMap.put(BaseDoc.trnPickupPoint,BaseDoc.TEST_PPCODE_VALUE);
            childrenMap.put(BaseDoc.bCountryC,"AL");
            childrenMap.put(BaseDoc.sCountryC,"RU");
            childrenMap.put(BaseDoc.trnFeesClientCurr,"RUR");
            childrenMap.put(BaseDoc.trnRate,"1.0000");
            childrenMap.put(BaseDoc.bResident,"");
            childrenMap.put(BaseDoc.bIDexpireDate,"20170706");
            childrenMap.put(BaseDoc.bIDwhom,"UVD");
            childrenMap.put(BaseDoc.bIDdate,"20170706");
            childrenMap.put(BaseDoc.bIDnumber,"12398748474412");
            childrenMap.put(BaseDoc.bIDtype,"Паспорт моряка");
            childrenMap.put(BaseDoc.bPhone,"123123123");
            childrenMap.put(BaseDoc.bAddress,"SOME STREET");

            contactPay.sendSingleRequest("",1,plctx);//just stub
            Assert.assertEquals(1,1);
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
    }


    @Test
    /**
     * Метод создает хранилище сертификата для ЭЦП
     */
    public void createStorePKCS12() throws CryptoException {
        String FILE_SEPARATOR = File.separator;
        String root = "/Volumes/CLOUDFLASH";
        String certRoot = root + "/CA";
        String clientCertRoot = root + "/OpenKeys";
        String passwd = "DobSamTan22";
        String pkFn = root + FILE_SEPARATOR + "keys" + FILE_SEPARATOR + "00000001.key";

        // Инициализация хранилища
        KeyStore store = null;
        try {
            store = KeyStore.getInstance("PKCS#12", "SC");
            store.load(null, null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Чтение секретного ключа PKCS#8
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("PKCS#8", "SC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        byte[] encoded = null;
        try {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(pkFn);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            encoded = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        KeySpec privkeySpec = new PKCS8EncodedKeySpec(encoded);
        PrivateKey priv = null;
        try {
            priv = keyFac.generatePrivate(privkeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        // Чтение сертификата УЦ
        FileInputStream in = null;
        CertificateFactory cf = null;
        X509Certificate cacert = null;
        try {
            cf = CertificateFactory.getInstance("X.509", "SC");
            in = new FileInputStream(certRoot + FILE_SEPARATOR + "racertca_2.pem"); // CA 7
            cacert = (X509Certificate) cf.generateCertificate(in);
            in.close();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Чтение собственного сертификата
        X509Certificate cert = null;
        try {
            in = new FileInputStream(clientCertRoot + FILE_SEPARATOR + "racert_1500478.pem"); // CA 7
            cert = (X509Certificate) cf.generateCertificate(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Формирование цепочки сертификатов
        java.security.cert.Certificate[] chain = new java.security.cert.Certificate[2];
        chain[0] = cert;
        chain[1] = cacert;

        // Помещение в хранилище секретного ключа с цепочкой сертификатов
        try {
            store.setKeyEntry("kasonkey", priv, passwd.toCharArray(), chain);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        // Помещение в хранилище сертификата УЦ
        try {
            store.setCertificateEntry("ConCA", cacert);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // Запись хранилища
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(root + FILE_SEPARATOR + "contactStore.pfx"));
            store.store(out, passwd.toCharArray());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void extractDateTest(){
        String data1 = "14.08.78 Г. Р.";
        String data2 = "17.05.1987 ‘. ЉЂђЂЊЂ•€ Ѓ“‰ЌЂЉ‘ЉЋѓЋ ђЂ‰ЋЌЂ ђ…‘Џ“Ѓ‹€Љ€ „Ђѓ…‘’ЂЌ";
        System.out.println(Convertor.toDate(data1,DatesUtils.FORMAT_ddMMYYYY));
        System.out.println(Convertor.toDate(data2,DatesUtils.FORMAT_ddMMYYYY));
    }

    //@Test Мои некоторые попытки создать keystore для криптопро
    //Удалю позже
    public void createContactTrustStore() {
        String certPath = "D:\\contcsv\\CERT\\contactTrustStore\\thawte.cer";    // файл, в который был предварительно
        // сохранен корневой сертификат центра


        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("HDImageStore");
            ks.load(null, null);
            CertificateFactory cf = CertificateFactory.getInstance("X509");

            FileInputStream fis = new FileInputStream(certPath);
            java.security.cert.Certificate   cert   =   cf.generateCertificate(new BufferedInputStream(fis));
            ks.setCertificateEntry("certificate", cert);

            FileOutputStream fos = null;
            fos = new FileOutputStream("D:\\contcsv\\CERT\\contactTrustStore\\contactTrust.store");
            ks.store(fos, "changeit".toCharArray());
            fos.close();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Test Мои некоторые попытки создать keystore для криптопро
    //Удалю позже
    public void addCertsToContactTrustStore() {
        String certPath = "D:\\contcsv\\CERT\\contactTrustStore\\thawte.cer";    // файл, в который был предварительно
        String certPath1 = "D:\\contcsv\\CERT\\contactTrustStore\\certca_1900.pem";
        String certPath2 = "D:\\contcsv\\CERT\\contactTrustStore\\certca_1901.pem";
        String certPath3 = "D:\\contcsv\\CERT\\contactTrustStore\\certca_1902.pem";


        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("HDImageStore");
            ks.load(new FileInputStream("D:\\contcsv\\CERT\\contactTrustStore\\contactTrust.store"), "changeit".toCharArray());

            CertificateFactory cf = CertificateFactory.getInstance("X509");
            FileInputStream fis = new FileInputStream(certPath1);
            java.security.cert.Certificate   cert   =   cf.generateCertificate(new BufferedInputStream(fis));
            ks.setCertificateEntry("certificate2", cert);

            fis = new FileInputStream(certPath);
            cert   =   cf.generateCertificate(new BufferedInputStream(fis));
            ks.setCertificateEntry("certificate", cert);

            fis = new FileInputStream(certPath2);
            cert   =   cf.generateCertificate(new BufferedInputStream(fis));
            ks.setCertificateEntry("certificate3", cert);

            fis = new FileInputStream(certPath3);
            cert   =   cf.generateCertificate(new BufferedInputStream(fis));
            ks.setCertificateEntry("certificate4", cert);


            FileOutputStream fos = null;
            fos = new FileOutputStream("D:\\contcsv\\CERT\\contactTrustStore\\contactTrust.store");
            ks.store(fos, "changeit".toCharArray());
            fos.close();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
