package ru.simplgroupp.ejb.service.impl;

import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.service.OdinSServiceRastorop_v2;
import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.odins.model.rastorop_v2.*;
import ru.simplgroupp.odins.model.rastorop_v2.Bank;
import ru.simplgroupp.odins.model.rastorop_v2.Organization;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ProductKeys;

import javax.ejb.*;
import javax.inject.Inject;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервис для запросов к бд из сервлета отчетов для 1С
 */
@Stateless(name = "OdinSServiceRastorop_v2Impl")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class OdinSServiceRastorop_v2Impl implements OdinSServiceRastorop_v2 {

	@Inject Logger logger;

    private static final String VERSION="2.00";
    
    @EJB
    private PaymentService paymentService;

    @EJB
    private PeopleBeanLocal peopleBean;

    @EJB
    ReferenceBooksLocal refBooks;

    @EJB
    private CreditBeanLocal creditBean;

    @EJB
    protected CreditCalculatorBeanLocal creditCalc;

    @EJB
    OrganizationService orgService;

    @EJB
    PaymentDAO payDAO;

    @EJB
    ProductBeanLocal productBean;

    @EJB
    ProductDAO productDAO;


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OnlineCreditExchange generateXMLResponse(Date date) {
        if(date == null){
            date = new Date();
        }
        logger.severe("******OdinSServiceImpl!!!!!!!!!! STARTED!!!!****************" + date);

        OnlineCreditExchange result = null;
        try {
            result = new OnlineCreditExchange();
            result.setVersion(VERSION);

            DateRange dateRange=DatesUtils.makeOneDayDateRange(date);
            List<PaymentExpense> paymentsExpense = new ArrayList<PaymentExpense>();
            List<PaymentReceipt> paymentsReceipt = new ArrayList<PaymentReceipt>();
            result.setPaymentsExpense(paymentsExpense);
            result.setPaymentsReceipt(paymentsReceipt);

            Organization rastoropOrg = findRastoropOrg();

            //платежи системы клиенту
            List<CreditDetailsEntity> paymentsToClient = creditBean.findCreditDetailsWithUnit(null, BaseCredit.OPERATION_CREDIT, dateRange);
            if(paymentsToClient!=null && paymentsToClient.size()>0){
                for(CreditDetailsEntity detailsEntity: paymentsToClient) {
                    if(detailsEntity.getAnotherId() != null){
                        CreditEntity creditEntity = detailsEntity.getCreditId();
                        //кредит
                        Contract contract = genContract(creditEntity,date);
                        //счет клиента
                        AccountEntity borrowerAccount = creditEntity.getCreditRequestId().getAccountId();
                        PaymentExpense paymentExpense = new PaymentExpense();
                        paymentExpense.setDate(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(detailsEntity.getEventDate()));
                        //id платежа
                        paymentExpense.setNum(String.valueOf(detailsEntity.getAnotherId()));
                        //выплаченная сумма
                        paymentExpense.setSum(CalcUtils.sumToString(detailsEntity.getAmountMain()));
                        paymentExpense.setPayer(rastoropOrg);
                        //contract
                        paymentExpense.setContract(contract);
                        //client,who takes money
                        paymentExpense.setPayee(genPerson(creditEntity, borrowerAccount));
                        //через какую систему платили, их реквизиты
                        paymentExpense.setAgent(genAgent(detailsEntity.getAnotherId()));

                        Unit unit = new Unit();
                        unit.getName().add(creditEntity.getCreditRequestId().getWorkplace().getName());
                        unit.setId(Convertor.toString(creditEntity.getCreditRequestId().getWorkplace().getId()));
                        paymentExpense.setUnit(unit);
                        paymentsExpense.add(paymentExpense);
                    }
                }
            }else{
                logger.severe("******DEBUG ONLY******* paymentsToClient list is empty" + date);
            }

            //платежи клиента системе
            List<CreditDetailsEntity> paymentsToSystem = creditBean.findCreditDetailsWithUnit(null, BaseCredit.OPERATION_PAYMENT, dateRange);
            if(paymentsToSystem!=null && paymentsToSystem.size()>0){
                for(CreditDetailsEntity detailsEntity: paymentsToSystem) {
                    if(detailsEntity.getAnotherId() != null){
                        CreditEntity creditEntity = detailsEntity.getCreditId();
                        //кредит
                        Contract contract = genContract(creditEntity,date);
                        //??? возможно это неправильно
                        AccountEntity borrowerAccount = creditEntity.getCreditRequestId().getAccountId();

                        PaymentReceipt paymentReceipt = new PaymentReceipt();
                        paymentReceipt.setDate(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(detailsEntity.getEventDate()));
                        //id платежа
                        paymentReceipt.setNum(String.valueOf(detailsEntity.getAnotherId()));
                        //Сумма всего
                        paymentReceipt.setSum(CalcUtils.sumToString(detailsEntity.getAmountOperation()));
                        //Сумма процентов
                        paymentReceipt.setSumOfInterest(CalcUtils.sumToString(detailsEntity.getAmountPercent()));
                        paymentReceipt.setPayee(rastoropOrg);
                        paymentReceipt.setContract(contract);
                        //клиент
                        paymentReceipt.setPayer(genPerson(creditEntity,borrowerAccount));
                        //через какую систему платил клиент, их реквизиты
                        paymentReceipt.setAgent(genAgent(detailsEntity.getAnotherId()));

                        Unit unit = new Unit();
                        unit.getName().add(creditEntity.getCreditRequestId().getWorkplace().getName());
                        unit.setId(Convertor.toString(creditEntity.getCreditRequestId().getWorkplace().getId()));
                        paymentReceipt.setUnit(unit);
                        paymentsReceipt.add(paymentReceipt);
                    }
                }
            }else{
                logger.severe("******DEBUG ONLY******* paymentsToSystem list is empty" + date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("******DEBUG ONLY******* EXCEPTION!!! " + e.getMessage());

        }
        return result;
    }


    private Organization genRastoropOrganization(OrganizationEntity org){
        Organization organization = new Organization();
   
        organization.setName(org.getName());
        AccountEntity accountEntity = null;
        if(org.getAccounts() !=null && org.getAccounts().size()>0){
            accountEntity = org.getAccounts().get(0);
        }
        if(accountEntity != null){
            organization.setBankAccount(genBankAccount(accountEntity));
        }
        return organization;
    }
    
    private BankAccount genBankAccount(AccountEntity accountEntity){
        BankAccount bankAccount = null;
        if(accountEntity!=null){
            bankAccount = new BankAccount();
            bankAccount.setNum(accountEntity.getAccountnumber());
            bankAccount.setBank(genRastoropBank(accountEntity));
        }
        return bankAccount;
    }

    private Bank genRastoropBank(AccountEntity accountEntity){
        Bank bank = null;
        if(accountEntity != null){
            bank = new Bank();
            bank.setBic(accountEntity.getBik());
            bank.setCorrespAccount(accountEntity.getCorraccountnumber());
        }
        return bank;
    }
    
    private Contract genContract(CreditEntity credit,Date date){
        Contract contract = null;
        if(credit!=null){
          
            contract = new Contract();
            contract.setId(credit.getId().toString());
            contract.setNum(credit.getCreditAccount());
            contract.setEndDate(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(credit.getCreditdataend()));
            contract.setStartDate(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(credit.getCreditdatabeg()));
            contract.setRate(CalcUtils.pskToString(credit.getCreditpercent()));
            contract.setSum(CalcUtils.sumToString(credit.getCreditsum()));
            
            boolean overdue=false;
            
            	if (credit.getCreditdataendfact()==null 
            			&& DatesUtils.daysDiff(date, credit.getCreditdataend())>14){
            		//если заявка не закрыта и дата окончания кредита просрочена на 2 недели
            		overdue=true;
            		
            	} else {
                    if (credit.getCreditdataendfact()!=null
                		&&credit.getCreditdataendfact().after(date)
                		&&credit.getCreditdataend().before(date)
                		&& DatesUtils.daysDiff(date, credit.getCreditdataend())>14){
                	    //если заявка закрыта и дата окончания кредита просрочена на 2 недели
            		    overdue=true;
                    }
            	}
            contract.setOverdue(overdue);
        }
        return contract;
    }

    private Person genPerson(CreditEntity credit, AccountEntity borrowerAccount){
        Person person = null;
        if(credit!=null){
            PeopleMainEntity borrower = credit.getPeopleMainId();
            PeoplePersonalEntity borrowerPersonal = peopleBean.findPeoplePersonalActive(borrower);
            person = new Person();
            person.setId(borrower.getId().toString());
            person.setName(borrowerPersonal.getName());
            person.setSurname(borrowerPersonal.getSurname());
            person.setPatronymic(borrowerPersonal.getMidname());
            person.setBirthDate(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(borrowerPersonal.getBirthdate()));
            person.setPlaceOfBirth(borrowerPersonal.getBirthplace());

            person.setSex(Sex.fromValue(borrowerPersonal.getGender().getCodeinteger()==PeoplePersonal.GENDER_MAN?"Male":"Female"));

            EmploymentEntity emp = peopleBean.findLastArchiveEmployment(borrower.getId());
            if(emp!=null) {
                PlaceOfWork pw = new PlaceOfWork();
                pw.setName(emp.getPlaceWork());
                pw.setPosition(emp.getOccupation());
                //pw.setIncome(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(emp.getDatestartwork()));
                person.getPlacesOfWork().add(pw);
            }


            //document
            DocumentEntity document = peopleBean.findPassportActive(borrower);
            if (document == null){
                try {
                    if(borrower.getDocuments()!=null && borrower.getDocuments().size()>0){
                        document = borrower.getDocuments().get(0);
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE,"Документ не определен",e);
                }
            }

            if(document!=null){
                person.setPassportIssued(document.getDocorg());
                person.setPassportIssueDate(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(document.getDocdate()));
                person.setPassportNumber(document.getNumber());
                person.setPassportSeries(document.getSeries());
            }

            DocumentOtherEntity doc = peopleBean.findDocumentOtherActive(borrower);
            if(doc!=null){
                AdditionalDocument ad = new AdditionalDocument();
                ad.getType().add(doc.getDocumenttypeId().getName());
                ad.getNumber().add(doc.getNumber());
                ad.setIssueDate(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(doc.getDocdate()));
                person.getAdditionalDocuments().add(ad);
            }

            ContactInformation info = new ContactInformation();
            info.setType("email");
            info.setValue(peopleBean.findContactClient(PeopleContact.CONTACT_EMAIL,credit.getPeopleMainId().getId()));
            person.getContactInformation().add(info);

            info = new ContactInformation();
            info.setType("телефон");
            info.setValue(peopleBean.findContactClient(PeopleContact.CONTACT_CELL_PHONE,credit.getPeopleMainId().getId()));
            person.getContactInformation().add(info);

            AddressEntity addr = peopleBean.findAddressActive(credit.getPeopleMainId(),BaseAddress.REGISTER_ADDRESS);
            info = new ContactInformation();
            info.setType("адрес");
            info.setValue(addr.getAddressText());
            person.getContactInformation().add(info);

            PeopleMiscEntity pm = peopleBean.findPeopleMiscActive(credit.getPeopleMainId());
            if(pm!=null){
                person.setAmountOfChildren(Convertor.toString(pm.getChildren()));
                person.setAmountOfChildrenMinors(Convertor.toString(pm.getUnderage()));
                person.setMaritalStatus(pm.getMarriageId().getName());
            }

//            info = new ContactInformation();
//            info.getType().add("адрес");
//            info.getValue().add(peopleBean.findContactClient(PeopleContact.,credit.getPeopleMainId().getId()));
//            person.getContactInformation().add(info);


//            peopleBean.findContactClient()

            //BankAccount
            //if(borrower.getAccounts()!=null && borrower.getAccounts().size()>0){
            person.setBankAccount(genBankAccount(borrowerAccount));
            //}
        }
        return person;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OnlineCreditExchange generateXMLAccruedInterest(Date date) {
        if(date == null){
            date = new Date();
        }
        ProductsEntity product = productDAO.getProductDefault();
        Map<String, Object> limits = productBean.getNewRequestProductConfig(product.getId());
        Integer addDays = Convertor.toInteger(limits.get(ProductKeys.ADDITIONAL_DAYS_FOR_CALCULATE));
        logger.severe("******OdinSServiceImpl!!!!!!!!!! STARTED!!!! generateXMLAccruedInterest ****************" + date);

        OnlineCreditExchange result = null;
        try {
            result = new OnlineCreditExchange();
            result.setVersion(VERSION);

            //все имеющиеся кредиты на дату 
            List<CreditEntity> credits =creditBean.listCreditsForCalcPercentWithUnit(date);
            if(credits!=null&&credits.size()>0){
                List<TotalsRecord> totalsRecords  =  new ArrayList<TotalsRecord>();
                Organization rastoropOrg = findRastoropOrg();

                for(CreditEntity creditEntity : credits){
                    try {
                      
                    	Double sumBackMin =0D;
                        DateRange creditRange=new DateRange(creditEntity.getCreditdatabeg(),date);
                        Credit credit=new Credit(creditEntity); 
                       
                        //просрочка на последний день - если кредит был уже просрочен
                        if (credit.isOverdue(date)){
                        	DateRange overdueRange=new DateRange(DateUtils.addDays(date, -1),date);
                            List<CreditDetailsEntity> overdue=creditBean.findCreditDetails(creditEntity.getId(), BaseCredit.OPERATION_OVERDUE, overdueRange);
                            if (overdue.size()>0){
                        	    sumBackMin =overdue.get(0).getAmountAll()-overdue.get(0).getAmountMain();
                            } 
                        } else {
                        	//платежи в период
                            List<CreditDetailsEntity> pay=creditBean.findCreditDetails(creditEntity.getId(), BaseCredit.OPERATION_PAYMENT, creditRange);
                            //не было платежей
                            if (pay.size()==0){
                            	sumBackMin =CalcUtils.calcSumPercentSimple(creditEntity.getCreditsum(), DatesUtils.daysDiff(date, creditEntity.getCreditdatabeg()),
                            			(addDays!=null&&addDays==1), creditEntity.getCreditpercent());
                            } else {
                            	CreditDetailsEntity creditDetails=pay.get(0);
                            	sumBackMin =creditDetails.getAmountAll()-creditDetails.getAmountMain()+
                            			CalcUtils.calcSumPercentSimple(creditDetails.getAmountMain(), DatesUtils.daysDiff(date, creditDetails.getEventDate()),
                            			false, creditEntity.getCreditpercent());
                            }
                        }
                        
                        if(sumBackMin==null){
                            sumBackMin = 0D;
                        }
                        
                        TotalsRecord theRecord = new TotalsRecord();
                        theRecord.setSumOfInterest(CalcUtils.sumToString(sumBackMin));

                        Contract contract = genContract(creditEntity,date);
                        theRecord.setContract(contract);

                        theRecord.setOrganization(rastoropOrg);

                        AccountEntity borrowerAccount = creditEntity.getCreditRequestId().getAccountId();
                        theRecord.setPerson(genPerson(creditEntity,borrowerAccount));

                        totalsRecords.add(theRecord);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.severe("******DEBUG ONLY******* generateXMLAccruedInterest cycle EXCEPTION!!! " + e.getMessage());
                    }
                }
                result.setTotalsRecords(totalsRecords);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("******DEBUG ONLY******* generateXMLAccruedInterest EXCEPTION!!! " + e.getMessage());

        }
        return result;
    }

    private Organization findRastoropOrg(){
        OrganizationEntity org=orgService.getOrganizationActive();
        ru.simplgroupp.transfer.Organization orgOur = orgService.findOrganization(org.getId());
        if(orgOur!=null){
            org = orgOur.getEntity();
        }

        Organization rastoropOrg = genRastoropOrganization(org);
        return rastoropOrg;
    }


    private Companies genAgent(Integer anotherId){
        if(anotherId==null) return null;

        Companies agent = null;
        PaymentEntity paymentEntity = payDAO.getPayment(anotherId);
        if(paymentEntity!=null){
            PartnersEntity partnersEntity = paymentEntity.getPartnersId();
            if(partnersEntity!=null){
                agent = new Companies();
                agent.setName(partnersEntity.getName());
                OrganizationEntity organization = orgService.getOrganizationPartnerActive(partnersEntity.getId());
                if(organization != null){
                	agent.setId(organization.getId().toString());
                    agent.setName(organization.getName());
                    agent.setInn(organization.getInn());
                    agent.setKpp(organization.getKpp());
                }
            }
        }
        return agent;
    }


}
