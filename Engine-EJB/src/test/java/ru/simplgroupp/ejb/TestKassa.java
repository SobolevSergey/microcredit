package ru.simplgroupp.ejb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import junit.framework.Assert;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.openejb.api.LocalClient;
import org.junit.Before;



import org.junit.Test;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.fias.ejb.IFIASService;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.CreditCalculatorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.MailBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.QuestionBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.ScoringEquifaxBeanLocal;
import ru.simplgroupp.interfaces.ScoringNBKIBeanLocal;
import ru.simplgroupp.interfaces.ScoringOkbCaisBeanLocal;
import ru.simplgroupp.interfaces.ScoringOkbIdvBeanLocal;
import ru.simplgroupp.interfaces.ScoringRsBeanLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.HolidaysService;
import ru.simplgroupp.interfaces.service.RequestsService;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.Misc;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.CreditStatus;
import ru.simplgroupp.transfer.QuestionVariant;
import ru.simplgroupp.util.DatesUtils;


@LocalClient
public class TestKassa {

    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

    @EJB
    KassaBeanLocal kassa;
    
    @EJB
    CreditBeanLocal credit;

    @EJB
    HolidaysService hs;
    
    @EJB
    EventLogService es;

    @EJB
    ReferenceBooksLocal refBook;

    @EJB
    PeopleBeanLocal ppl;

    @EJB
    IFIASService fiasServ;

    @EJB
    ScoringEquifaxBeanLocal equifax;

    @EJB
    ScoringRsBeanLocal rs;

    @EJB
    ScoringNBKIBeanLocal nbki;
    
    @EJB
    ScoringOkbIdvBeanLocal okbIdv;

    @EJB
    ScoringOkbCaisBeanLocal okbCais;

    @EJB
    MailBeanLocal mail;

    @EJB
    UsersBeanLocal user;

    @EJB
    QuestionBeanLocal quest;

    @EJB
    CreditCalculatorBeanLocal creditCalc;

    @EJB
    RulesBeanLocal rules;

    @EJB
	RequestsService reqService;
    
    @EJB
	CreditRequestDAO creditRequestDAO;
    
    @Before
    public void setUp() throws Exception {
        
        System.setProperty("openejb.validation.output.level","VERBOSE");
		final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        credit = (CreditBeanLocal) context.lookup("java:global/Engine-EJB/CreditBean!ru.simplgroupp.interfaces.CreditBeanLocal");
        reqService=(RequestsService) context.lookup("java:global/Engine-EJB/RequestsServiceImpl!ru.simplgroupp.interfaces.service.RequestsService");
        hs = (HolidaysService) context.lookup("java:global/Engine-EJB/HolidaysServiceImpl!ru.simplgroupp.interfaces.service.HolidaysService");
        es = (EventLogService) context.lookup("java:global/Engine-EJB/EventLogServiceImpl!ru.simplgroupp.interfaces.service.EventLogService");
        equifax= (ScoringEquifaxBeanLocal) context.lookup("java:global/Engine-EJB/ScoringEquifaxBean!ru.simplgroupp.interfaces.ScoringEquifaxBeanLocal");
        refBook = (ReferenceBooksLocal) context.lookup("java:global/Engine-EJB/ReferenceBooks!ru.simplgroupp.interfaces.ReferenceBooksLocal");
        ppl = (PeopleBeanLocal) context.lookup("java:global/Engine-EJB/PeopleBean!ru.simplgroupp.interfaces.PeopleBeanLocal");
        fiasServ=(IFIASService) context.lookup("java:global/Fias/FiasService!ru.simplgroupp.fias.ejb.IFIASService");
        mail = (MailBeanLocal) context.lookup("java:global/Engine-EJB/MailBean!ru.simplgroupp.interfaces.MailBeanLocal");
        quest = (QuestionBeanLocal) context.lookup("java:global/Engine-EJB/QuestionBean!ru.simplgroupp.interfaces.QuestionBeanLocal");
        rs= (ScoringRsBeanLocal) context.lookup("java:global/Engine-EJB/ScoringRsBean!ru.simplgroupp.interfaces.ScoringRsBeanLocal");
        nbki= (ScoringNBKIBeanLocal) context.lookup("java:global/Engine-EJB/ScoringNBKIBean!ru.simplgroupp.interfaces.ScoringNBKIBeanLocal");
        okbIdv= (ScoringOkbIdvBeanLocal) context.lookup("java:global/Engine-EJB/ScoringOkbIdvBean!ru.simplgroupp.interfaces.ScoringOkbIdvBeanLocal");
        okbCais= (ScoringOkbCaisBeanLocal) context.lookup("java:global/Engine-EJB/ScoringOkbCaisBean!ru.simplgroupp.interfaces.ScoringOkbCaisBeanLocal");
        user = (UsersBeanLocal) context.lookup("java:global/Engine-EJB/UsersBean!ru.simplgroupp.interfaces.UsersBeanLocal");
    }


    // @Test
    public void testSaveCCRequest() throws Exception {

        Assert.assertNotNull(kassa);

        HashMap<String, Object> params = new HashMap<String, Object>(8);
        params.put("id", null);
        params.put("birthday", "1987-02-28");
        params.put("isagree", 1);
        params.put("cellphone", "11223344");
        params.put("isagreeasp", 1);
        params.put("creditsum", 10000);
        params.put("creditdays", 10);
        params.put("surname", "Петрова");
        params.put("email", "tyg@inbox2.ru");
        params.put("citizen", "RU");
        params.put("ipAddress", "127.0.0.1");
        params.put("ipAttr", "Калуга");
        params.put("name", "Ирина");
        params.put("midname", "Сергеевна");
        params.put("isagreepersdates", 1);
        params.put("idman",306);
        params.put("inn", "");
        params.put("snils", "");
        params.put("gender", 1);
        params.put("seria", "2222");
        params.put("nomer", "222555");
        params.put("whenIssued", "2014-03-03");
        params.put("whoIssued", "ОВД");
        params.put("code_dep", "4200");
        params.put("lplace", "город");
        params.put("region", "eab9fe2f-bb3e-4497-99fd-458961f92ec9");
        params.put("area", "");
        params.put("city", "b8afe638-7f01-4ec3-9853-051b44d70ddb");
        params.put("street", "");
        params.put("home", "1");
        params.put("korpus", "");
        params.put("builder", "");
        params.put("apartment", "2");
        params.put("children", 2);
        params.put("marriage", 2);
        params.put("databeg", null);
        params.put("surname", "");
        kassa.saveStep2(params);
        params.put("matuche", 1);
        params.put("RealtyDate", "2014-03-14");
        params.put("RegDate", "");
        params.put("measurement", 1);
        params.put("phone", "123456");
        params.put("ground", 1);
        kassa.saveStep3(params);
        params.put("employment", 1);
        params.put("workplace", "ФНС");
        params.put("occupation", "должность");
        params.put("monthlyincome", 12345);
        params.put("salarydate", 1);
        params.put("education", 1);
        params.put("freqType", 1);
        params.put("monthlyincome", 10000);
        params.put("car", 1);
        params.put("profession", 1);
        params.put("workphone", "234567");
        kassa.saveStep4(params);
        params.put("option", 1);
        params.put("bankaccount", "233454565666677");
        params.put("bik", "7766554433");
        params.put("prolong", 10);
        params.put("creditbik", "7766554433");
        params.put("prevcredits", 1);
        params.put("credittype", 1);
        params.put("currencytype", 810);
        params.put("creditsumprev", 20000);
        params.put("creditcardlimit", 0);
        params.put("creditdate", "2014-03-14");
        params.put("creditisover", 1);
        //kassa.saveStep5(params);
        //wi.saveStep6(params);
        params.put("agreement", "Я согласен");
        params.put("stake",0.0205);
        params.put("smsCode",123456);
        kassa.saveStep7(params);
    }

   //@Test
    public void testCRequestNew() throws Exception, KassaException
    {
        Assert.assertNotNull(kassa);

    }
 

    //@Test
    public void testParseEquifax()
    {
        RequestsEntity req=reqService.getRequest(21156);
   
        String st=req.getResponsebodystring();

        try {
            equifax.saveAnswer(req, st);
        } catch (KassaException e) {
             e.printStackTrace();
        }

    }

    //@Test
    public void testParseOkbCais()
    {
        RequestsEntity req=reqService.getRequest(18060);
        String st=req.getResponsebodystring();

        try {
            okbCais.saveAnswer(req, st);
        } catch (KassaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

   // @Test
    public void testParseRs()
    {
        RequestsEntity req=reqService.getRequest(27527);
        String st=req.getResponsebodystring();
        try {
            rs.saveAnswer(req, st);
        } catch (KassaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    //@Test
    public void testTakeRequest() {
        RequestsEntity req=reqService.getRequest(42612);
        String st=new String(req.getRequestbody());
        System.out.println(st);
        req=reqService.getRequest(42616);
        st=new String(req.getRequestbody());
        System.out.println(st);
    }
    
   // @Test
    public void testParseNbki()
    {
        RequestsEntity req=reqService.getRequest(26590);
        String st=req.getResponsebodystring();
        try {
            nbki.saveAnswer(req, st);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    //  @Test
    public void testParseError()
    {
        RequestsEntity req=reqService.getRequest(1);
        StringBuilder sb=new StringBuilder();
        sb.append("<?xml version='1.0' encoding='windows-1251'?>");
        sb.append("<bki_response version='3.0' partnerid='001' datetime='01.01.2008 12:30:23' package_id='1'>");
        sb.append("<responsecode>18</responsecode>");
        sb.append("<responsestring>XML-пакет находится в обработке</responsestring>");
        sb.append("</bki_response>");

        String st=sb.toString();

        try {
            equifax.saveAnswer(req, st);
        } catch (KassaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // @Test
    public void testSearch() throws Exception {
        Assert.assertNotNull(kassa);
        List<CreditRequest> lst=kassa.listCreditRequests(0, 20, null, null, null, null, null, null,null, null, null, null, null ,null, null, null, null, "1111", null, null, null, null, null, null, null,null);
        if (lst.size() > 0) {

        }
    }

    //@Test
    public void testDelete() throws Exception, Exception
    {
        Assert.assertNotNull(kassa);
        kassa.deleteCreditRequest(849);
     
    }

    //@Test
    public void testSendSms() {
  
        mail.sendSMSV2("79059610016", "Проверка");
    }

//    @Test
    public void testSendSmsV3() {

        mail.sendSMSV3("79206112485", "Проверка");
    }

//    @Test
    public void sendSMSV4() {

        mail.sendSMSV4("79206112485", "Проверка");
    }

    //@Test
    public void testSendMail()
    {
        String mailContent="Тестовое сообщение";
	    mail.send("Смена пароля", mailContent, "helen19711@yandex.ru");

    }

     //@Test
    public void testSendMailAttachment()
    {
        mail.sendAttachment("отчет", "отчет", "helen19711@yandex.ru", "c:\\1\\request.txt");
    }

    //  @Test
    public void testOverview()
    {
        Map<Integer,Integer> mp=kassa.overview();
        int i1=mp.get(CreditStatus.FILLED);
        int i2=mp.get(CreditStatus.REJECTED);
        int i3=mp.get(CreditStatus.FOR_COURT);
        int i4=mp.get(CreditStatus.DECISION);
        int i5=mp.get(CreditStatus.IN_PROCESS);
        int i6=mp.get(CreditStatus.FOR_COLLECTOR);
        int i7=mp.get(CreditStatus.CLOSED);
        System.out.println(i1);
        System.out.println(i2);
        System.out.println(i3);
        System.out.println(i4);
        System.out.println(i5);
        System.out.println(i6);
        System.out.println(i7);
        Map<Integer,Double> sm=kassa.overviewSum();
        double sm1=sm.get(1);
        double sm2=sm.get(2);
        System.out.println(sm1);
        System.out.println(sm2);
    }

    @Test
    public void testOverviewSum()
    {
       
        Map<Integer,Double> sm=kassa.overviewSum(DatesUtils.makeDate(2015, 11, 26));
        double sm1=sm.get(1);
        double sm2=sm.get(2);
        double sm3=sm.get(3);
        double sm4=sm.get(4);
        double sm5=sm.get(5);
        System.out.println(sm1);
        System.out.println(sm2);
        System.out.println(sm3);
        System.out.println(sm4);
        System.out.println(sm5);
    }
    
    //@Test
    public void testAddQA()
    {

        List<QuestionVariant> lq=quest.getQuestionVariants(1216);
        if (lq!=null)
            System.out.println(lq.size());
        else
            System.out.println("нет");
    }

    //@Test
    public void testListPeople(){
        int i=ppl.countPeopleMain( null, "", "", "", "", "", "", "","","");
        System.out.print(i);
    }

    //@Test
    public void testFindMaxNumber(){
        Integer i=kassa.findMaxCreditRequestNumber(new Date());
        System.out.print(i);
    }


   
    //@Test
    public void testCalendar(){
        Calendar cl=Calendar.getInstance();
        cl.setTime(new Date());
        hs.getHolidays(new Date());
        boolean b=hs.isHoliday(new Date());
        int i=hs.getDaysOfHolidays(new Date());
        System.out.println(b);
        System.out.println(i);
        hs.getHolidays(DateUtils.addDays(new Date(),-2));
        b=hs.isHoliday(DateUtils.addDays(new Date(),-2));
        i=hs.getDaysOfHolidays(DateUtils.addDays(new Date(),-2));
        System.out.println(b);
        System.out.println(i);
        hs.getHolidays(DateUtils.addDays(new Date(),2));
        b=hs.isHoliday(DateUtils.addDays(new Date(),2));
        i=hs.getDaysOfHolidays(DateUtils.addDays(new Date(),2));
        System.out.println(b);
        System.out.println(i);

    }

    
   //@Test
   public void testCopy(){
	  String st=kassa.execCopyToCsv("", "address","","");
	  System.out.println(st); 
   }
   
   //@Test
   public void testGeoLocation(){
	   CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(24749);
	   String geoPlace=kassa.saveGeoLocation(creditRequest, "185.52.140.86");
	   System.out.print(geoPlace);
   }
  
  // @Test
   public void testFindMiscVars(){
	   List<Misc> lst=kassa.findMiscVarsCreditRequest(9994);
	   System.out.println(lst.size());
	   Assert.assertTrue(lst.size()>0);
   }
}


