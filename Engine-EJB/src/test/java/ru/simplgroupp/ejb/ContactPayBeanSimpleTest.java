package ru.simplgroupp.ejb;

import org.admnkz.crypto.app.CryptoService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;
import ru.simplgroupp.contact.protocol.v2.response.CdtrnCXR;
import ru.simplgroupp.contact.protocol.v2.response.ContactResponse;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.CheckRestResponse;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.Dics;
import ru.simplgroupp.contact.protocol.v2.tabonentobject.GetChangesFullPart0MethodAssembler;
import ru.simplgroupp.contact.protocol.v2.tmoneyorderobject.*;
import ru.simplgroupp.contact.util.AUtils;
import ru.simplgroupp.ejb.plugins.payment.ContactBaseBean;
import ru.simplgroupp.ejb.plugins.payment.ContactPayBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContactPayBeanSimpleTest {

    private ContactBaseBean contactBaseBean;
    private CryptoService cryptoService;
    private ContactPayBean payBean;


    private String PPCODE_VALUE = BaseDoc.PPCODE_VALUE;
    private String key_storage = "/Volumes/DISK";
    //private String key_storage = "/Volumes/CLOUDFLASH";

    @Before
    public void setUp() throws Exception {
        cryptoService = new CryptoService();
        contactBaseBean = new ContactBaseBean();
        contactBaseBean.setCryptoService(cryptoService);

        payBean = new ContactPayBean();
        payBean.setCryptoService(cryptoService);
    }


    @Test
    public void doCheckRest() {

        CheckRestResponse resp = null;
        try {
            //47422810900034000030 это наш счет в контакте Растороп
            //40701810300030000006 это наш счет в контакте Ловизайм
            resp = payBean.doCheckRestTest(false, "47422810900034000030", 0, "https://contactws.contact-sys.com/trans/wsTrans.exe/soap/ITransmitter", key_storage);
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
        double rest = Convertor.toDouble(resp.getAVAIL_REST());
        System.out.println("Balance = " + rest);
    }

    @Test
    public void doPay() {
        String url = "https://contactws.contact-sys.com/trans/wsTrans.exe/soap/ITransmitter";

        try {
            //47422810100034000034 это наш счет в контакте Растороп
            //40701810300030000006 это наш счет в контакте Ловизайм
            String ppCode = PPCODE_VALUE;
            Map<String, String> data = new HashMap<String, String>();
            data.put(BaseDoc.POINT_CODE, ppCode);

            CdtrnCXR cxr = contactBaseBean.doCall(data, new NewAndSendOutgoingMethodAssembler(), true, createChildrenMap(), url, "DobSamTan22", "kasonkey", "KEY#1", key_storage);
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
    }


    @Test
    public void doProcessSoapResponse() {
        String s = "<?xml version=\"1.0\"?>\n" +
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                "\t<SOAP-ENV:Body SOAP-ENC:encodingStyle=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "\t\t<NS1:TransmitResponse xmlns:NS1=\"urn:TransmitterIntf-ITransmitter\">\n" +
                "\t\t\t<return xsi:type=\"xsd:int\">0</return>\n" +
                "\t\t\t<outXML xsi:type=\"xsd:string\">&lt;CDTRN Version=\"1\" CorrespondentID=\"-1\" Code=\"BASE64\" Lang=\"ru\" Stamp=\"2014-12-18T17:58:57+03:00\"&gt;&lt;DATA Pack=\"ZLIB\" Encryption=\"PLAIN\"&gt;eJxljFELgjAUhf/KuO+t3YlU4hSzIZK42NTXPaSEUAoJ2c9vQkEUXDiXcz6+MH7eruTR3ad+HAQgZUC64Ty2/XARMPdDO87TCrmPEEehluakSiOJybPS5pUAh2v5DlNZdXQOIEmT5IVdGgGcbbjPKGPftU1rrT/b32BTdXBSXWsgWaH2SWEbqU2uSmdHipxyhh4h28BDd7+QNVK71+k5Zd7CIkEvQB6wHayjFxh7P7I=&lt;/DATA&gt;&lt;/CDTRN&gt;</outXML>\n" +
                "\t\t</NS1:TransmitResponse>\n" +
                "\t</SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>\n";

        ContactResponse cr = processSoapResponse(s);
        cr.getOutXML();
    }


    private static ContactResponse processSoapResponse(String responseString) {
        ContactResponse contactResponse = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(responseString.getBytes());

            String responseString2 = out.toString("UTF-8");

            InputStream inputSource = new ByteArrayInputStream(responseString2.getBytes("UTF-8"));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();

            org.w3c.dom.Document d = db.parse(inputSource);
            org.w3c.dom.Node outxml = d.getElementsByTagName("outXML").item(0);
            org.w3c.dom.Node res = d.getElementsByTagName("return").item(0);
            contactResponse = new ContactResponse();
            contactResponse.setOutXML(outxml.getTextContent());
            if (res != null) {
                contactResponse.setReturnResult(Integer.valueOf(res.getTextContent()));
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contactResponse;
    }

    private Map<String, String> createChildrenMap() {
        //1 transaction = 16530938738
        //2 transaction = 16530938739
        //ДАТЫ 18 дек, и 19 дек
        //1 номер 63974649  уже выплачен

        Map<String, String> childrenMap = new HashMap<String, String>();
        //ОБЩАЯ ИНФА ********************************************************************************************
        childrenMap.put(BaseDoc.trnClCurrency, BaseCredit.CURRENCY_RUR); //нац. валюта клиента, поставил рубли
        childrenMap.put(BaseDoc.trnCurrency, BaseCredit.CURRENCY_RUR);//Валюта
        childrenMap.put(BaseDoc.trnAmount, Convertor.toDecimalString(CalcUtils.dformat, 0d));//Сумма!!!
        childrenMap.put(BaseDoc.trnReference, "16530938739");//Номер перевода!!! Нужен

        String ppcode = BaseDoc.PPCODE_VALUE;
        childrenMap.put(BaseDoc.trnSendPoint, ppcode);//Филиал отправителя
        childrenMap.put(BaseDoc.trnPickupPoint, BaseDoc.PICKUP_POINT_BEZADR_VALUE); //Филиал получателя
        try {
            childrenMap.put(BaseDoc.trnDate, DatesUtils.DATE_FORMAT_YYYYMMdd.format(new Date()));//Дата перевода, видимо тек. дата
        } catch (Exception e) {
            e.printStackTrace();
        }
        childrenMap.put(BaseDoc.trnService, "2");//Код услуги, пока не знаю что это

        //ОТПРАВИТЕЛЬ *****************************************************************************************
        childrenMap.put(BaseDoc.sCountry, BaseCredit.COUNTRY_RU);//Страна отправителя
        childrenMap.put(BaseDoc.sResident, "1");//Страна резиденства отправителя 0-не резидент, 1-резидент
        childrenMap.put(BaseDoc.sResidentC, BaseCredit.COUNTRY_RU);//Страна резиденства отправителя 0-не резидент, 1-резидент
        childrenMap.put(BaseDoc.sCountryC, BaseCredit.COUNTRY_RU);   //Страна гражданства отправителя
        childrenMap.put(BaseDoc.sPhone, AUtils.parsePhoneToContactForm("79872250540"));//Телефон отправителя


        //ЭТО фейковые данные о нас
        /* это образец
        childrenMap.put(BaseDoc.sIDtype,BaseDoc.sIDtype_Value); //Тип документа отправителя
        childrenMap.put(BaseDoc.sName,"ALEXANDER");      //Фамилия отправителя,это видимо мы ???TODO узнать; должно быть имя???
        childrenMap.put(BaseDoc.sLastName,"ZAHARCHENKO");//Имя отправителя,это видимо мы ???TODO узнать; должна быть фамилия???
        childrenMap.put(BaseDoc.sSurName,"");//Отчество отправителя ???TODO узнать
        childrenMap.put(BaseDoc.sZipCode,"");//Почтовый индекс отправителя
        childrenMap.put(BaseDoc.sRegion,"");//Регион (штат) отправителя
        childrenMap.put(BaseDoc.sCity,"MOSCOW");//Город отправителя
        childrenMap.put(BaseDoc.sAddress,"MOSCOW");//Адрес отправителя (улица, дом, квартира)
        childrenMap.put(BaseDoc.sIDnumber,"1234123456"); //Серия и номер документа отправителя
        childrenMap.put(BaseDoc.sIDdate,"20080423"); //Дата выдачи документа отправителя
        childrenMap.put(BaseDoc.sIDwhom,"GYVD"); //Кем выдан документ отправителя
        childrenMap.put(BaseDoc.sIDexpireDate,""); //Дата истечения срока действия документа отправителя
        childrenMap.put(BaseDoc.sBirthday,"19770112");//Дата рождения отправителя
        */


        childrenMap.put(BaseDoc.sIDtype, "PASSPORT"); //Тип документа отправителя
        childrenMap.put(BaseDoc.sName, "Vladimir");      //Фамилия отправителя,это видимо мы
        childrenMap.put(BaseDoc.sLastName, "Generous");//Имя отправителя,это видимо мы
        childrenMap.put(BaseDoc.sSurName, "");//Отчество отправителя
        childrenMap.put(BaseDoc.sZipCode, "");//Почтовый индекс отправителя
        childrenMap.put(BaseDoc.sRegion, "");//Регион (штат) отправителя
        childrenMap.put(BaseDoc.sCity, "MOSCOW");//Город отправителя
        childrenMap.put(BaseDoc.sAddress, "MOSCOW Kremlin 5");//Адрес отправителя (улица, дом, квартира)
        childrenMap.put(BaseDoc.sIDnumber, "1234123456"); //Серия и номер документа отправителя
        childrenMap.put(BaseDoc.sIDdate, "20020423"); //Дата выдачи документа отправителя
        childrenMap.put(BaseDoc.sIDwhom, "GYVD"); //Кем выдан документ отправителя
        childrenMap.put(BaseDoc.sIDexpireDate, ""); //Дата истечения срока действия документа отправителя
        childrenMap.put(BaseDoc.sBirthday, "19700112");//Дата рождения отправителя
        childrenMap.put(BaseDoc.sBirthPlace, "MOSCOW MOSCOW Kremlin 5");//Место рождения отправителя, для сумм более 15000


        //ПОЛУЧАТЕЛЬ *****************************************************************************************
        childrenMap.put(BaseDoc.bName, "Калашникова"); //Фамилия получателя, странно,должно быть имя
        childrenMap.put(BaseDoc.bLastName, "Марина");//Имя получателя, странно,должно быть имя
        childrenMap.put(BaseDoc.bSurName, "Евгеньевна");//Отчество получателя
        childrenMap.put(BaseDoc.bCity, "8f286b5d-2594-46bb-a03b-b62d2b7a2b39");//Город получателя
        childrenMap.put(BaseDoc.bRegion, "27b89426-1c17-4eb9-8e81-4411c8ecb069");//Регион (штат) получателя
        childrenMap.put(BaseDoc.bZipCode, "");//Почтовый индекс получателя
        childrenMap.put(BaseDoc.bCountry, "RU");//Страна получателя
        childrenMap.put(BaseDoc.bIDwhom, "ОТДЕЛ МВД РФ ПО Г. НОВОЧЕБОКСАРСК");//Кем выдан документ получателя
        childrenMap.put(BaseDoc.bIDnumber, "9704374293");//Паспортные данные получателя
        childrenMap.put(BaseDoc.bIDtype, "паспорт рф");  //Тип документа получателя
        childrenMap.put(BaseDoc.bPhone, "7-9-872250540");  //Телефон получателя      Fake

        childrenMap.put(BaseDoc.bAddress, "Новочебоксарск,бульвар гидростроителей 4-90"); //Адрес получателя (улица, дом, квартира)
        childrenMap.put(BaseDoc.bBirthday, "19600624"); //Дата рождения получателя
        childrenMap.put(BaseDoc.bIDdate, "20051102"); //Дата выдачи документа получателя

        return childrenMap;
    }


    @Test
    public void doCancelPay() {
        String url = "https://contactws.contact-sys.com/trans/wsTrans.exe/soap/ITransmitter";

        try {
            //47422810100034000034 это наш счет в контакте
            String ppCode = PPCODE_VALUE;
            Map<String, String> data = new HashMap<String, String>();
            data.put(BaseDoc.POINT_CODE, ppCode);
            //1 - 63974649
            //2 <?xml version="1.0" encoding="windows-1251"?><RESPONSE SIGN_IT="1" STATE="4" RE="0" ID="63973497" GLOBAL_VERSION="11.12.2013  8:31:31" GLOBAL_VERSION_SERVER="22.03.2011 13:12:09"><trnDate>20141218</trnDate><trnReference>16530938738</trnReference><trnCurrency>RUR</trnCurrency><trnAmount>100.00</trnAmount><trnSendPoint>KSOV</trnSendPoint><trnPickupPoint>CDPA</trnPickupPoint><sName>Vladimir</sName><sLastName>Generous</sLastName><sBirthday>19700112</sBirthday><sCountry>RU</sCountry><sCity>MOSCOW</sCity><sAddress>MOSCOW Kremlin 5</sAddress><sPhone>7-9-872250540</sPhone><sIDtype>PASSPORT</sIDtype><sIDnumber>1234123456</sIDnumber><sIDdate>20020423</sIDdate><sIDwhom>GYVD</sIDwhom><bName>Калашникова</bName><bLastName>Марина</bLastName><bSurName>Евгеньевна</bSurName><bBirthday>19600624</bBirthday><bCountry>RU</bCountry><bRegion>27b89426-1c17-4eb9-8e81-4411c8ecb069</bRegion><bCity>8f286b5d-2594-46bb-a03b-b62d2b7a2b39</bCity><bAddress>Новочебоксарск,бульвар гидростроителей 4-90</bAddress><bPhone>7-9-872250540</bPhone><bIDtype>Паспорт</bIDtype><bIDnumber>9704374293</bIDnumber><bIDdate>20051102</bIDdate><bIDwhom>ОТДЕЛ МВД РФ ПО Г. НОВОЧЕБОКСАРСК</bIDwhom><trnService>2</trnService><trnRate>1.0000</trnRate><trnFeesClientLocal>50.00</trnFeesClientLocal><trnFeesClient>50.00</trnFeesClient><tCardNumber></tCardNumber><sResident>1</sResident><trnClCurrency>RUR</trnClCurrency><sCountryC>RU</sCountryC><trnPayoutAmount>0.0000</trnPayoutAmount><sBirthPlace>MOSCOW MOSCOW Kremlin 5</sBirthPlace><sResidentC>RU</sResidentC><tDebetCardNumber></tDebetCardNumber><trnFeesPart>0.00</trnFeesPart><trnFeesPartRet>0.00</trnFeesPartRet><trnReturnFees>1</trnReturnFees><trnReturnFeesClient>50.00</trnReturnFeesClient><trnReturnFeesClientLocal>50.00</trnReturnFeesClientLocal><trnReturnRsbFees>50.00</trnReturnRsbFees><payRsbFeesLocal>50.00</payRsbFeesLocal></RESPONSE>
            //3 <?xml version="1.0" encoding="windows-1251"?><RESPONSE SIGN_IT="1" STATE="4" RE="0" ID="63974350" GLOBAL_VERSION="11.12.2013  8:31:31" GLOBAL_VERSION_SERVER="22.03.2011 13:12:09"><trnDate>20141219</trnDate><trnReference>16530938738</trnReference><trnCurrency>RUR</trnCurrency><trnAmount>100.00</trnAmount><trnSendPoint>KSOV</trnSendPoint><trnPickupPoint>CDPA</trnPickupPoint><sName>Vladimir</sName><sLastName>Generous</sLastName><sBirthday>19700112</sBirthday><sCountry>RU</sCountry><sCity>MOSCOW</sCity><sAddress>MOSCOW Kremlin 5</sAddress><sPhone>7-9-872250540</sPhone><sIDtype>PASSPORT</sIDtype><sIDnumber>1234123456</sIDnumber><sIDdate>20020423</sIDdate><sIDwhom>GYVD</sIDwhom><bName>Калашникова</bName><bLastName>Марина</bLastName><bSurName>Евгеньевна</bSurName><bBirthday>19600624</bBirthday><bCountry>RU</bCountry><bRegion>27b89426-1c17-4eb9-8e81-4411c8ecb069</bRegion><bCity>8f286b5d-2594-46bb-a03b-b62d2b7a2b39</bCity><bAddress>Новочебоксарск,бульвар гидростроителей 4-90</bAddress><bPhone>7-9-872250540</bPhone><bIDtype>паспорт рф</bIDtype><bIDnumber>9704374293</bIDnumber><bIDdate>20051102</bIDdate><bIDwhom>ОТДЕЛ МВД РФ ПО Г. НОВОЧЕБОКСАРСК</bIDwhom><trnService>2</trnService><trnRate>1.0000</trnRate><trnFeesClientLocal>50.00</trnFeesClientLocal><trnFeesClient>50.00</trnFeesClient><tCardNumber></tCardNumber><sResident>1</sResident><trnClCurrency>RUR</trnClCurrency><sCountryC>RU</sCountryC><trnPayoutAmount>0.0000</trnPayoutAmount><sBirthPlace>MOSCOW MOSCOW Kremlin 5</sBirthPlace><sResidentC>RU</sResidentC><tDebetCardNumber></tDebetCardNumber><trnFeesPart>0.00</trnFeesPart><trnFeesPartRet>0.00</trnFeesPartRet><trnReturnFees>1</trnReturnFees><trnReturnFeesClient>50.00</trnReturnFeesClient><trnReturnFeesClientLocal>50.00</trnReturnFeesClientLocal><trnReturnRsbFees>50.00</trnReturnRsbFees><payRsbFeesLocal>50.00</payRsbFeesLocal></RESPONSE>
            String docId = "";
            //docId="64645083";
            //docId="64645160";
            //docId="64443421";
            //docId="64818919";
            //docId="64014099";
            //docId="64014099";
            docId = "65435394";     //1025012011500035 16.01
            //


            data.put(BaseDoc.DOC_ID, docId);

            CdtrnCXR cxr = contactBaseBean.doCall(data, new ReqForCancelMethodAssembler(), true, null, url, "DobSamTan22", "kasonkey", "KEY#1", key_storage);
            int i = 90;
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
    }

    @Test
    public void doReturnPay() {
        String url = "https://contactws.contact-sys.com/trans/wsTrans.exe/soap/ITransmitter";

        try {
            //47422810100034000034 это наш счет в контакте
            String ppCode = PPCODE_VALUE;
            Map<String, String> data = new HashMap<String, String>();
            data.put(BaseDoc.POINT_CODE, ppCode);
            //1 - 63974649
            //2 <?xml version="1.0" encoding="windows-1251"?><RESPONSE SIGN_IT="1" STATE="4" RE="0" ID="63973497" GLOBAL_VERSION="11.12.2013  8:31:31" GLOBAL_VERSION_SERVER="22.03.2011 13:12:09"><trnDate>20141218</trnDate><trnReference>16530938738</trnReference><trnCurrency>RUR</trnCurrency><trnAmount>100.00</trnAmount><trnSendPoint>KSOV</trnSendPoint><trnPickupPoint>CDPA</trnPickupPoint><sName>Vladimir</sName><sLastName>Generous</sLastName><sBirthday>19700112</sBirthday><sCountry>RU</sCountry><sCity>MOSCOW</sCity><sAddress>MOSCOW Kremlin 5</sAddress><sPhone>7-9-872250540</sPhone><sIDtype>PASSPORT</sIDtype><sIDnumber>1234123456</sIDnumber><sIDdate>20020423</sIDdate><sIDwhom>GYVD</sIDwhom><bName>Калашникова</bName><bLastName>Марина</bLastName><bSurName>Евгеньевна</bSurName><bBirthday>19600624</bBirthday><bCountry>RU</bCountry><bRegion>27b89426-1c17-4eb9-8e81-4411c8ecb069</bRegion><bCity>8f286b5d-2594-46bb-a03b-b62d2b7a2b39</bCity><bAddress>Новочебоксарск,бульвар гидростроителей 4-90</bAddress><bPhone>7-9-872250540</bPhone><bIDtype>Паспорт</bIDtype><bIDnumber>9704374293</bIDnumber><bIDdate>20051102</bIDdate><bIDwhom>ОТДЕЛ МВД РФ ПО Г. НОВОЧЕБОКСАРСК</bIDwhom><trnService>2</trnService><trnRate>1.0000</trnRate><trnFeesClientLocal>50.00</trnFeesClientLocal><trnFeesClient>50.00</trnFeesClient><tCardNumber></tCardNumber><sResident>1</sResident><trnClCurrency>RUR</trnClCurrency><sCountryC>RU</sCountryC><trnPayoutAmount>0.0000</trnPayoutAmount><sBirthPlace>MOSCOW MOSCOW Kremlin 5</sBirthPlace><sResidentC>RU</sResidentC><tDebetCardNumber></tDebetCardNumber><trnFeesPart>0.00</trnFeesPart><trnFeesPartRet>0.00</trnFeesPartRet><trnReturnFees>1</trnReturnFees><trnReturnFeesClient>50.00</trnReturnFeesClient><trnReturnFeesClientLocal>50.00</trnReturnFeesClientLocal><trnReturnRsbFees>50.00</trnReturnRsbFees><payRsbFeesLocal>50.00</payRsbFeesLocal></RESPONSE>
            String docId = "";
            //docId="64645083";
            //docId="64645160";
            //docId="64443421";
            //docId="64818919";
            //docId="64014099";
            //docId="64719175";
            //docId="64885474";         // 1020027121400021   31 дек
            docId = "65435394";     //1025012011500035 16.01


            data.put(BaseDoc.DOC_ID, docId);

            CdtrnCXR cxr = contactBaseBean.doCall(data, new ReturnOutgoingMethodAssembler(), true, null, url, "DobSamTan22", "kasonkey", "KEY#1", key_storage);
            int i = 90;
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
    }

    @Test
    public void doGetPay() {
        String url = "https://contactws.contact-sys.com/trans/wsTrans.exe/soap/ITransmitter";
        try {
            //1- 16530938738
            //2- 16530938739
            //3 - 1030018121400002      // 63974649
            //4 - 1030019121400016
            //1020025121400055
            //1020005011500008 - 7 jan


            //1020025121400055
            //        [14:29:17] Елена Климова: 25.12.2014
            //1020027121400021
            //1025012011500035 16.01
            //1040013011500063
            //1020025121400055

            String ppCode = PPCODE_VALUE;
            Map<String, String> data = new HashMap<String, String>();
            data.put(BaseDoc.POINT_CODE, ppCode);
            data.put(BaseDoc.trnReference, "1020025121400055");
            data.put(BaseDoc.trnSendPoint, ppCode);
            data.put(BaseDoc.trnDate, "20141225");

            CdtrnCXR cxr = contactBaseBean.doCall(data, new GetMethodAssembler(), true, null, url, "DobSamTan22", "kasonkey", "KEY#1", key_storage);
            int i = 90;
        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
    }

    @Test
    public void doGetPay2() {
        String url = "https://contactws.contact-sys.com/trans/wsTrans.exe/soap/ITransmitter";
        try {
            String docId = "";
            //docId="64645083";
            //docId="64645160";
            //docId="64443421";
            //docId="64818919";
            docId = "64794193";

            String ppCode = PPCODE_VALUE;
            Map<String, String> data = new HashMap<String, String>();
            data.put(BaseDoc.POINT_CODE, ppCode);
            data.put(BaseDoc.DOC_ID, docId);

            CdtrnCXR cxr = contactBaseBean.doCall(data, new GetMethodAssembler(), true, null, url, "DobSamTan22", "kasonkey", "KEY#1", key_storage);
            int i = 90;

        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
    }

    @Test
    public void doGetState() {
        String url = "https://contactws.contact-sys.com/trans/wsTrans.exe/soap/ITransmitter";
        String docId = "64563347";
        //docId="64564422";
        //docId ="64569617";
        //docId ="64610537";
        //docId="64648965";
        //docId="64645083";
        //docId="64443421";
        //docId="64645272";
        //docId="64818919";
        //docId="64794193";
//        docId="67156354";


        try {
            String ppCode = BaseDoc.PPCODE_VALUE;
            Map<String, String> data = new HashMap<String, String>();
            data.put(BaseDoc.POINT_CODE, ppCode);
            data.put(BaseDoc.DOC_ID, docId);
            data.put(BaseDoc.INOUT, BaseDoc.OUT_INOUT_VALUE);
            CdtrnCXR cdtrn = contactBaseBean.doCall(data, new GetStateMethodAssembler(), true, null, url, "DobSamTan22", "kasonkey", "KEY#1", key_storage);

            int i = 90;

        } catch (ActionException e) {
            Assert.assertEquals(e.getInfo().getCode(), ErrorKeys.INVALID_ACCOUNT);
        }
    }

    @Test
    public void getFullDicByParts() throws ActionException {
        Dics dics = partialDics(0, "");
        //int index=1;
        while (dics != null && dics.getCurrentPart() < dics.getTotal()) {
            //payBean.puDicsInDatabase(dics);
            dics = partialDics(dics.getCurrentPart() + 1, dics.getBookId());
        }
    }

    private Dics partialDics(Integer part, String bookId) throws ActionException {
        String url = "https://contactws.contact-sys.com/trans/wsTrans.exe/soap/ITransmitter";
        String ppCode = BaseDoc.PPCODE_VALUE;//если ничто не работает, то хардкод всегда рулит!!!

        //<REQUEST OBJECT_CLASS="TAbonentObject" ACTION="GET_CHANGES" POINT_CODE="" VERSION="" TYPE_VERSION="" PORTION =”” PACK="" BOOK_ID=””/>

        Map<String, String> data = new HashMap<String, String>();
        data.put(BaseDoc.POINT_CODE, ppCode);
        data.put(BaseDoc.VERSION, "0");//??? TODO
        data.put(BaseDoc.BOOK_ID, bookId);
        data.put(BaseDoc.PART, String.valueOf(part));
        CdtrnCXR cdtrn = contactBaseBean.doCall(data, new GetChangesFullPart0MethodAssembler(), false, null, url, null, null, null, null);
        Dics dics = AUtils.parseDicsPartialV2(cdtrn);

        return dics;
    }

}
