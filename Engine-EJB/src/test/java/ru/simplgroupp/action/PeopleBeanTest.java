package ru.simplgroupp.action;

import org.apache.openejb.api.LocalClient;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.service.CreditRequestWizardService;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.transfer.creditrequestwizard.Step0Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step1Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step6Data;
import ru.simplgroupp.workflow.SignalRef;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import static junit.framework.Assert.assertNotNull;


@LocalClient
public class PeopleBeanTest {
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @EJB
    private WorkflowBeanLocal workflowBean;

    @EJB
    private KassaBeanLocal kassaBean;

    @EJB
    private CreditRequestWizardService wizardService;

    @EJB
    private ReferenceBooksLocal refBooks;

    @EJB
    private CreditBeanLocal creditBean;

    @EJB
    private CreditRequestDAO crDAO;

    @EJB
    private PeopleDAO peopleDAO;

    @EJB
    private PeopleBeanLocal peopleBean;

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
    private Integer creditRequestId;


    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));
        Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        assertNotNull(kassaBean);
        assertNotNull(wizardService);
        assertNotNull(workflowBean);
        assertNotNull(refBooks);
    }

    @After
    public void tearDown() throws Exception {
        collector.checkThat(creditRequestId, CoreMatchers.notNullValue());
        kassaBean.deleteCreditRequest(creditRequestId);
    }

    private Integer createTestCreditReq() {
        Integer creditRequestId = null;

        try {
            // step0
            Step0Data step0 = wizardService.step0(0, true);
            creditRequestId = wizardService.saveStep0(null, step0);
            workflowBean.startOrFindProcCR(creditRequestId, null);


            // step1
            Step1Data step1 = wizardService.step1(creditRequestId);
            step1.setFirstName("Testfirstname");
            step1.setMiddleName("Testmiddlename");
            step1.setLastName("Testlastname");
            step1.setPhone("+70000000000");
            step1.setEmail("testemail@testemail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date date = sdf.parse("08.08.1988");
            step1.setBirthday(date);
            wizardService.saveStep1(creditRequestId, step1);


            // step2
            Map<String, Object> step2 = kassaBean.paramsStep2(creditRequestId);
            step2.put("gender", 1);
            step2.put("place", "TestPlace");
            step2.put("seria", "1337");
            step2.put("nomer", "031337");
            step2.put("whoIssued", "TestIssued");
            step2.put("whenIssued", "01.01.2001");
            step2.put("code_dep", "031337");
            step2.put("registrationFias", "fcb6ee3f-0c83-42f9-8533-e753c44705a9");
            step2.put("home", "13371");
            step2.put("builder", "13372");
            step2.put("korpus", "13373");
            step2.put("apartment", "13374");
            step2.put("RealtyDate", "01.01.2001");
            step2.put("marriage", 1);
            step2.put("children", 2);
            step2.put("name", "Testspousename");
            step2.put("surname", "Testspousesurname");
            step2.put("midname", "Tesspousemidname");
            step2.put("mobilephone", "+70000000001");
            step2.put("birthdate", "01.01.1990");
            step2.put("typework", 0);
            step2.put("databeg", "01.01.2013");
            step2.put("inn", "000000000000");
            step2.put("snils", "11111111111");
            kassaBean.saveStep2(step2);


            // step3
            Map<String, Object> step3 = kassaBean.paramsStep3(creditRequestId);
            step3.put("matuche", 0);
            step3.put("livingFias", "fcb6ee3f-0c83-42f9-8533-e753c44705a9");
            step2.put("home", "23371");
            step2.put("builder", "23372");
            step2.put("korpus", "23373");
            step2.put("apartment", "23374");
            step3.put("RegDate", "01.01.2001");
            step3.put("ground", 1);
            step3.put("regphone", "+70000000002");
            step3.put("phone", "+70000000003");
            step3.put("available", false);
            step3.put("regavailable", false);
            kassaBean.saveStep3(step3);


            // step4
            Map<String, Object> step4 = kassaBean.paramsStep4(creditRequestId);
            step4.put("education", 0);
            step4.put("employment", 0);
            step4.put("experience", "01.01.2001");
            step4.put("profession", 1);
            step4.put("workplace", "TestWorkPlace");
            step4.put("datestartwork", "01.01.2001");
            step4.put("monthlyincome", 101);
            step4.put("jobFias", "fcb6ee3f-0c83-42f9-8533-e753c44705a9");
            step4.put("home", "13371");
            step4.put("builder", "13372");
            step4.put("korpus", "13373");
            step4.put("occupation", "TestDirector");
            step4.put("workphone", "+70000000004");
            step4.put("extsalarysource", 1);
            step4.put("extsalarysource", 101);
            step4.put("available", false);
            step4.put("car", true);
            kassaBean.saveStep4(step4);


            // step5
            Map<String, Object> step5 = kassaBean.paramsStep5(creditRequestId);
            step5.put("prevcredits", 1);
            step5.put("creditorganization", 604);
            step5.put("credittype", 1);
            step5.put("creditdate", "01.01.2001");
            step5.put("creditisover", 1);
            step5.put("currencytype", 643);
            step5.put("creditsumprev", 1000);
            step5.put("overdue", "1");
            wizardService.saveStep5(step5);


            // step6
            Step6Data step6 = wizardService.step6(creditRequestId);
            step6.setOption(Account.CONTACT_TYPE);
            wizardService.saveStep6(creditRequestId, step6);


            // step7
            Map<String, Object> step7 = kassaBean.paramsStep7(creditRequestId);
            step7.put("creditsum", 1000);
            step7.put("creditdays", 2);
//            step7.put("stake", 0.04);
//            step7.put("stakeMin", 0.01);
//            step7.put("stakeMax", 0.04);
//            step7.put("creditDaysMin", 2);
//            step7.put("creditDaysMax", 31);
//            step7.put("creditSumMin", 1000);
//            step7.put("creditSumMax", 10000);
            kassaBean.saveStep7(step7);
            workflowBean.goProcCR(creditRequestId, SignalRef.toString("procNewCR", null, "msgMore"), null);


            // step 8
            kassaBean.paramsStep8(creditRequestId);
        } catch (Throwable e) {
            collector.addError(e);
        }

        return creditRequestId;
    }

    @Test
    public void testAddPeopleMisc() {
        try {
            creditRequestId = createTestCreditReq();

            PeopleMainEntity peopleMainEntity = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            PeopleMiscEntity peopleMiscEntity = peopleBean.findPeopleMiscActive(peopleMainEntity);

            // change values
            Boolean isCar = peopleMiscEntity.getCar();
            peopleMiscEntity.setCar(!isCar);

            ReferenceEntity marriageRef = peopleMiscEntity.getMarriageId();
            Integer marriageId;
            if (marriageRef.getCodeinteger() > 0) {
                marriageId = marriageRef.getCodeinteger() - 1;
            } else {
                marriageId = marriageRef.getCodeinteger() + 1;
            }
            peopleMiscEntity.setMarriageId(refBooks.findByCodeIntegerEntity(RefHeader.MARITAL_STATUS_SYSTEM, marriageId));

            Integer children = peopleMiscEntity.getChildren();
            children = children > 1 ? 0 : 1;
            peopleMiscEntity.setChildren(children);

            Date realtyDate = dateFormat.parse("11.11.2011");
            peopleMiscEntity.setRealtyDate(realtyDate);

            Date regDate = dateFormat.parse("11.11.2011");
            peopleMiscEntity.setRegDate(regDate);

            Integer groundId = peopleMiscEntity.getRealtyId().getCodeinteger() > 0 ? 0 : 1;
            peopleMiscEntity.setRealtyId(refBooks.findByCodeIntegerEntity(RefHeader.REALTY_TYPE, groundId));


            PeopleMisc peopleMisc = peopleBean.addPeopleMisc(new PeopleMisc(peopleMiscEntity), peopleMainEntity.getId(), new Date());


            collector.checkThat(peopleMisc.getCar(), CoreMatchers.equalTo(!isCar));
            collector.checkThat(peopleMisc.getMarriage().getCodeInteger(), CoreMatchers.equalTo(marriageId));
            collector.checkThat(peopleMisc.getChildren(), CoreMatchers.equalTo(children));
            collector.checkThat(peopleMisc.getRealtyDate(), CoreMatchers.equalTo(realtyDate));
            collector.checkThat(peopleMisc.getRegDate(), CoreMatchers.equalTo(regDate));
            collector.checkThat(peopleMisc.getRealty().getCodeInteger(), CoreMatchers.equalTo(groundId));
            System.out.println("peopleMisc = " + peopleMisc);
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testAddPeoplePersonal() {
        try {
            creditRequestId = createTestCreditReq();

            PeopleMainEntity peopleMainEntity = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            PeoplePersonalEntity peoplePersonalEntity = peopleBean.findPeoplePersonalActive(peopleMainEntity);
            PeoplePersonal peoplePersonal = new PeoplePersonal(peoplePersonalEntity);

            // change values
            Integer gender = peoplePersonal.getGender().getCodeInteger();
            gender = gender == 1 ? 2 : 1;
            peoplePersonal.setGender(refBooks.getGender(gender));

            String name = "Testpersonalsetname";
            peoplePersonal.setName(name);


            PeoplePersonal peoplePersonal1 = peopleBean.addPeoplePersonal(peoplePersonal, peopleMainEntity.getId(), new Date());

            collector.checkThat(peoplePersonal1.getGender().getCodeInteger(), CoreMatchers.equalTo(gender));
            collector.checkThat(peoplePersonal1.getName(), CoreMatchers.equalTo(name));
            System.out.println("peoplePersonal = " + peoplePersonal1);
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testAddDocument() {
        try {
            creditRequestId = createTestCreditReq();

            PeopleMainEntity peopleMainEntity = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            DocumentEntity documentEntity = peopleBean.findPassportActive(peopleMainEntity);
            Documents documents = new Documents(documentEntity);


            //change values
            String series = new StringBuilder(documents.getSeries()).reverse().toString();
            documents.setSeries(series);

            String number = new StringBuilder(documents.getNumber()).reverse().toString();
            documents.setNumber(number);

            Date docDate = dateFormat.parse("11.11.2011");
            documents.setDocDate(docDate);

            String docOrg = "TestAddDocuments";
            documents.setDocOrg(docOrg);


            Documents documents1 = peopleBean.addDocument(documents, peopleMainEntity.getId());


            collector.checkThat(documents1.getSeries(), CoreMatchers.equalTo(series));
            collector.checkThat(documents1.getNumber(), CoreMatchers.equalTo(number));
            collector.checkThat(documents1.getDocDate(), CoreMatchers.equalTo(docDate));
            collector.checkThat(documents1.getDocOrg(), CoreMatchers.equalTo(docOrg));
            System.out.println("documents = " + documents1);
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testAddAddressFias() {
        try {
            creditRequestId = createTestCreditReq();

            PeopleMainEntity peopleMainEntity = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            AddressEntity addressEntity = peopleBean.findAddressActive(peopleMainEntity, 0);
            FiasAddress fiasAddress = new FiasAddress(addressEntity);


            // change values
//            String region = "";
//            fiasAddress.setRegionGUID(region);

            String house = Integer.toString(new Integer(fiasAddress.getHouse()) > 2 ? 1 : 2);
            fiasAddress.setHouse(house);

            String building = Integer.toString(new Integer(fiasAddress.getBuilding()) > 2 ? 1 : 2);
            fiasAddress.setBuilding(building);


            FiasAddress fiasAddress1 = peopleBean.addAddressFias(fiasAddress, peopleMainEntity.getId(), "fcb6ee3f-0c83-42f9-8533-e753c44705a9", new Date());


//            collector.checkThat(fiasAddress.getRegion(), CoreMatchers.equalTo(docOrg));
            collector.checkThat(fiasAddress.getHouse(), CoreMatchers.equalTo(house));
            collector.checkThat(fiasAddress.getBuilding(), CoreMatchers.equalTo(building));
            System.out.println("fiasAddress entity = " + fiasAddress1);
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testAddPeopleContact() {
        try {
            creditRequestId = createTestCreditReq();

            PeopleMainEntity peopleMainEntity = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();

            PeopleMainEntity people = peopleDAO.getPeopleMainEntity(peopleMainEntity.getId());
            PeopleContactEntity contact = peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_EMAIL, people.getId());
            PeopleContact peopleContactEmail = new PeopleContact(contact);


            // change values
            String newEmail = "testnewemail@new.com";
            peopleContactEmail.setValue(newEmail);


            PeopleContact peopleContact1 = peopleBean.addPeopleContact(peopleContactEmail, people.getId());


            collector.checkThat(peopleContact1.getValue(), CoreMatchers.equalTo(newEmail));
            System.out.println("peopleContact = " + peopleContact1);
        } catch (Throwable e) {
            collector.addError(e);
        }
    }

    @Test
    public void testAddAccount() {
        try {
            creditRequestId = createTestCreditReq();


            PeopleMainEntity peopleMainEntity = crDAO.getCreditRequestEntity(creditRequestId).getPeopleMainId();
            PeopleMain peopleMain = peopleDAO.getPeopleMain(peopleMainEntity.getId(), Utils.setOf(PeopleMain.Options.INIT_ACCOUNTACTIVE,
                    PeopleMain.Options.INIT_PEOPLE_CONTACT, PeopleMain.Options.INIT_PEOPLE_PERSONAL,
                    PeopleMain.Options.INIT_DOCUMENT, PeopleMain.Options.INIT_ADDRESS, PeopleMain.Options.INIT_PEOPLE_MISC));

            AccountEntity accountEntity = peopleBean.findAccountActive(peopleMainEntity, Account.CONTACT_TYPE);
            Account account = new Account(accountEntity);


            Account account1 = peopleBean.addAccount(account, peopleMain.getId());
            System.out.println("account = " + account1);
        } catch (Throwable e) {
            collector.addError(e);
        }
    }
}
