package ru.simplgroupp.ejb;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.ejb.arius.*;
import ru.simplgroupp.ejb.plugins.payment.AriusPayBean;
import ru.simplgroupp.ejb.plugins.payment.AriusPayPluginConfig;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.ejb.service.impl.AriusServiceImpl;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.WebUtils;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class AriusPayBeanSimpleTest {
    private static final Logger logger = LoggerFactory.getLogger(AriusPayBeanSimpleTest.class.getName());


    private String client_orderid;
    private String order_desc;
    private String first_name;
    private String last_name;

    private String login;
    private String ssn;
    private String birthday;
    private String address1;
    private String city;
    private String country;
    private String zip_code;
    private String phone;
    private String cell_phone;
    private String amount;
    private String email;
    private String currency;
    private String ipaddress;
    private String site_url;
    private String credit_card_number;
    private String card_printed_name;
    private String expire_month;
    private String expire_year;

    private String cvv2;
    private String purpose;
    private String redirect_url;
    private String server_callback_url;
    private String merchant_data;
    private String merchantKey;
    private String isresident;

    private String paynetOrderId;


    private String urlSale;
    private String urlTransfer;
    private String urlStatus;

    private AriusPayBean ariusPayBean;
    private AriusServiceImpl ariusService;
    private AriusPayPluginConfig config;
    private CreditEntity credit;
    private CreditRequestEntity creditRequestEntity;
    private PeoplePersonalEntity borrowerPersonal;
    private PeopleContactEntity phoneContact;
    private PeopleContactEntity emailContact;
    private AddressEntity borAddress;
    private CountryEntity borCountry;
    private AccountEntity account;


    @Before
    public void setUp() throws Exception {
        ariusPayBean = new AriusPayBean();
        ariusService = new AriusServiceImpl();
        config       = new AriusPayPluginConfig();
       // config.setUseWork(false);



        urlSale     = "https://sandbox.ariuspay.ru/paynet/api/v2/sale/";
        urlTransfer = "https://sandbox.ariuspay.ru/paynet/api/v2/transfer/";
        urlStatus   = "https://sandbox.ariuspay.ru/paynet/api/v2/status/";


        merchantKey = "AD9B8E2C-DEA1-47AA-AAA3-CF594EE4F093";
        client_orderid = "98373987F489D748";   //paymentId will be
        order_desc = "Arius Transfer";
        card_printed_name = "";
        first_name="Aleksandr";
        last_name="Rogov";
        ssn="1267";
        birthday="19820115";
        address1="Vorovskogo Street 13";
        city="Kozann";
        country = "RU";
        zip_code="422520";
        phone="%2B79872896935";
        cell_phone="%2B79872250540";
        amount="1.00";
        email="john.smith@gmail.com";
        currency="RUB";
        ipaddress= WebUtils.findMyIpAddress();
        site_url="www.google.com";
        credit_card_number="4444555566661111";
        card_printed_name="CARD HOLDER";
        expire_month="12";
        expire_year="2099";
        cvv2="123";
        purpose="microcredit transfer";
        redirect_url="http://doc.ariuspay.ru/doc/dummy.htm";
        server_callback_url="http://doc.ariuspay.ru/doc/dummy.htm";
        merchant_data="VIP customer";

        login  = "mobifinans-sbox";
        isresident = "true";
        paynetOrderId ="12345678";

    }


    @Test
    public void doTest1() {
        StringBuilder params = new StringBuilder();
        params.append(AriusConstants.CLIENT_ORDERID).append(AriusConstants.EQ).append(client_orderid);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.ORDER_DESC).append(AriusConstants.EQ).append(order_desc);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.FIRST_NAME).append(AriusConstants.EQ).append(first_name);
        params.append(AriusConstants.AND).append(AriusConstants.LAST_NAME).append(AriusConstants.EQ).append(last_name);

        params.append(AriusConstants.AND).append(AriusConstants.SSN).append(AriusConstants.EQ).append(ssn);
        params.append(AriusConstants.AND).append(AriusConstants.BIRTHDAY).append(AriusConstants.EQ).append(birthday);
        params.append(AriusConstants.AND).append(AriusConstants.ADDRESS1).append(AriusConstants.EQ).append(address1);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.CITY).append(AriusConstants.EQ).append(city);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.ZIP_CODE).append(AriusConstants.EQ).append(zip_code);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.COUNTRY).append(AriusConstants.EQ).append(country);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.PHONE).append(AriusConstants.EQ).append(phone);
        params.append(AriusConstants.AND).append(AriusConstants.CELL_PHONE).append(AriusConstants.EQ).append(cell_phone);
        params.append(AriusConstants.AND).append(AriusConstants.AMOUNT).append(AriusConstants.EQ).append(amount);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.EMAIL).append(AriusConstants.EQ).append(email);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.CURRENCY).append(AriusConstants.EQ).append(currency);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.IPADDRESS).append(AriusConstants.EQ).append(ipaddress);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.SITE_URL).append(AriusConstants.EQ).append(site_url);
        params.append(AriusConstants.AND).append(AriusConstants.CREDIT_CARD_NUMBER).append(AriusConstants.EQ).append(credit_card_number);//Mandatory

        params.append(AriusConstants.AND).append(AriusConstants.CARD_PRINTED_NAME).append(AriusConstants.EQ).append(card_printed_name);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.EXPIRE_MONTH).append(AriusConstants.EQ).append(expire_month);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.EXPIRE_YEAR).append(AriusConstants.EQ).append(expire_year);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.CVV2).append(AriusConstants.EQ).append(cvv2);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.PURPOSE).append(AriusConstants.EQ).append(purpose);


        params.append(AriusConstants.AND).append(AriusConstants.REDIRECT_URL).append(AriusConstants.EQ).append(redirect_url);//Mandatory
        params.append(AriusConstants.AND).append(AriusConstants.SERVER_CALLBACK_URL).append(AriusConstants.EQ).append(server_callback_url);
        params.append(AriusConstants.AND).append(AriusConstants.MERCHANT_DATA).append(AriusConstants.EQ).append(merchant_data);
        //params.append(AriusConstants.AND).append(AriusConstants.control).append(AriusConstants.EQ).append(control);//Mandatory

        Map<String,String> rparams=new HashMap<String,String>();
        byte[] response=null;
        String theUrl = urlSale + AriusConstants.ENDPOINT3DS;
        try {
            response = HTTPUtils.sendHttp("POST", theUrl , params.toString().getBytes(), rparams, null);
            System.out.println(new String(response));
        } catch (Exception e) {
            logger.error("Не удалось получить ответ от системы arius, site={}",theUrl,e);
            e.printStackTrace();
        }

    }

    private String createControl() {
        StringBuilder sb = new StringBuilder(AriusConstants.ENDPOINT3DS);
        sb.append(client_orderid);
        sb.append(extractFractionalPart(amount));
        sb.append(email);
        sb.append(merchantKey);

        return sb.toString();
    }


    private String extractFractionalPart(String amount){
        BigDecimal bg = new BigDecimal(amount);
        return bg.remainder(BigDecimal.ONE).toString();
    }






    @Test
    public void preauthTest() {

        setAriusDataFormStep1();

        AriusTransferFormParameters parameters = null;
        try {
            parameters = ariusService.fillFormPaymentParameters(
                    config, credit, creditRequestEntity, borrowerPersonal, phoneContact, emailContact, borAddress, borCountry, 3d, AriusConstants.ARIUS_OPERATION_PREAUTH_FORM, config.getEndpointcardreg());
        } catch (AriusParametersException e) {
            e.printStackTrace();
        }
        AriusTransferFormResponse response = new AriusTransferFormResponse();
        ariusService.doAriusCall(parameters, response);
        int i =90;
    }

    private void setAriusDataFormStep1(){
        //creditRequestId=

        credit = null;
        creditRequestEntity = new CreditRequestEntity();
        borrowerPersonal = new PeoplePersonalEntity();
        phoneContact = new PeopleContactEntity();
        emailContact = new PeopleContactEntity();
        borAddress = new AddressEntity();
        account = new AccountEntity();
        creditRequestEntity.setAccountId(account);


        borCountry = null;


        creditRequestEntity.setUniquenomer("873464763");
        creditRequestEntity.setCreditsum(10d);

        borrowerPersonal.setName("Иван");
        borrowerPersonal.setSurname("Петров");

        phoneContact.setValue("79872896935");
        emailContact.setValue("keenrog@yandex.ru");

        borAddress.setIndex("422520");
        borAddress.setCity("Москва");
        borAddress.setAddressText("Ленина 10");

        account.setCardNumberMasked("41611");

    }

    @Test
    public void cardRegTest() {

        setAriusDataFormStep1();
        String orderId="221750";

        AriusCreateCardRefParameters parameters = null;
        try {
            parameters = ariusService.fillCardRefParameters(config,orderId);
        } catch (AriusParametersException e) {
            e.printStackTrace();
        }
        AriusCreateCardRefResponse response = new AriusCreateCardRefResponse();
        ariusService.doAriusCall(parameters, response);
        int i =90;
    }

    @Test
    public void returnTest() {

        setAriusDataFormStep1();
        String orderId="221750";

        AriusReturnParameters parameters = null;
        try {
            parameters = ariusService.fillReturnParameters(config,orderId);
        } catch (AriusParametersException e) {
            e.printStackTrace();
        }
        AriusReturnResponse response = new AriusReturnResponse();
        ariusService.doAriusCall(parameters, response);
        int i =90;
    }



    @Test
    public void testIp() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName("95.213.155.146");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String host = addr.getHostName();
        System.out.println(host);
    }

    @Test
    public void transferTest() {

        setAriusDataFormStep1();

        AriusTransferParameters parameters = null;
        try {
            parameters = ariusService.fillTransferParameters(
                    config, credit, creditRequestEntity,account, borrowerPersonal, phoneContact, emailContact, borAddress, borCountry,
                    100d, AriusConstants.ARIUS_OPERATION_TRANSFER_BY_REF, config.getEndpointdeposit2card());
        } catch (AriusParametersException e) {
            e.printStackTrace();
        }
        AriusTransferResponse response = new AriusTransferResponse();
        ariusService.doAriusCall(parameters, response);
        int i =90;
    }

    @Test
    public void statusTest() {
        AriusTransferStatusParameters parameters = null;
        try {
            parameters = ariusService.fillTransferStatusParameters("222100","00000000-0000-0000-0000-0000007a9f83",config, AriusConstants.ARIUS_OPERATION_STATUS, config.getEndpointdeposit2card());
        } catch (AriusParametersException e) {
            e.printStackTrace();
        }
        AriusTransferStatusResponse response = new AriusTransferStatusResponse();
        ariusService.doAriusCall(parameters, response);
        int i =90;
    }


    @Test
    public void saleTest() {

        setAriusDataFormStep1();

        AriusSaleParameters parameters = null;
        try {
            parameters = ariusService.fillSaleParameters(
                    config, credit, creditRequestEntity, borrowerPersonal, phoneContact, emailContact, borAddress, borCountry, 300d, AriusConstants.ARIUS_OPERATION_SALE_FORM, config.getEndpoint3ds());
        } catch (AriusParametersException e) {
            e.printStackTrace();
        }
        AriusSaleFormResponse response = new AriusSaleFormResponse();
        ariusService.doAriusCall(parameters, response);
        int i =90;
    }


}
