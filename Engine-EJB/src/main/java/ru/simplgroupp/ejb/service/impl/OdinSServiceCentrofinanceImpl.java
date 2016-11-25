package ru.simplgroupp.ejb.service.impl;

import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.service.OdinSServiceCentrofinance;
import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.odins.model.centrofinance.*;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.Partner;
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
@Stateless(name = "OdinSServiceCentrofinanceImpl")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class OdinSServiceCentrofinanceImpl implements OdinSServiceCentrofinance {

	@Inject Logger logger;

    private static final String VERSION="1.01";
    
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
    public SputnikExchange generateXMLResponse(Date date) {
        if(date == null){
            date = new Date();
        }
        logger.severe("******OdinSServiceImpl!!!!!!!!!! STARTED!!!!****************" + date);

        SputnikExchange result = null;
        try {
            result = new SputnikExchange();
            result.setVersion(VERSION);

            DateRange dateRange=DatesUtils.makeOneDayDateRange(date);
            List<PaymentExpense> paymentsExpense = new ArrayList<PaymentExpense>();
            List<PaymentReceipt> paymentsReceipt = new ArrayList<PaymentReceipt>();
            result.setPaymentsExpense(paymentsExpense);
            result.setPaymentsReceipt(paymentsReceipt);

            Organization rastoropOrg = findRastoropOrg();

            //платежи системы клиенту
            List<CreditDetailsEntity> paymentsToClient = creditBean.findCreditDetails(null, BaseCredit.OPERATION_CREDIT, dateRange);
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

                        paymentsExpense.add(paymentExpense);
                    }
                }
            }else{
                logger.severe("******DEBUG ONLY******* paymentsToClient list is empty" + date);
            }

            //платежи клиента системе
            List<CreditDetailsEntity> paymentsToSystem = creditBean.findCreditDetails(null, BaseCredit.OPERATION_PAYMENT, dateRange);
            if(paymentsToSystem!=null && paymentsToSystem.size()>0){
                for(CreditDetailsEntity detailsEntity: paymentsToSystem) {
                    if(detailsEntity.getAnotherId() != null){
                        CreditEntity creditEntity = detailsEntity.getCreditId();
                        PaymentEntity pay = payDAO.getPayment(detailsEntity.getAnotherId());
                        //кредит
                        Contract contract = genContract(creditEntity,date);
                        //??? возможно это неправильно
                        AccountEntity borrowerAccount = creditEntity.getCreditRequestId().getAccountId();
           
                        PaymentReceipt paymentReceipt = new PaymentReceipt();
                        //через какую систему платил клиент, их реквизиты
                        Сompany agent = genAgent(detailsEntity.getAnotherId());
                        paymentReceipt.setAgent(agent);
                        //если был платеж системы
                        paymentReceipt.setType(CalcUtils.sumToString(0d));
                        if(agent.getId().equals(String.valueOf(Partner.SYSTEM))) { 
                        	//бонусы
                            List<PeopleBonusEntity> bonuses = peopleBean.findCreditPayBonus(detailsEntity.getCreditId().getId(), detailsEntity.getEventDate());
                            Double bonusSum = 0D;
                            if (bonuses.size() > 0) {
                                bonusSum = Math.abs(bonuses.get(0).getAmountrub());
                                paymentReceipt.setBonus(CalcUtils.sumToString(bonusSum));
                            }
                            //если это сумма из копилки
                            if(creditBean.checkPeopleSums(detailsEntity.getCreditId().getId(),DatesUtils.makeOneDayDateRange(detailsEntity.getEventDate()),(-1*detailsEntity.getAmountOperation()))){
                                paymentReceipt.setBonus(CalcUtils.sumToString(0d));
                                paymentReceipt.setType(CalcUtils.sumToString(1d)); // выставляем что это из переплаты
                            }
                        }else{
                            paymentReceipt.setBonus(CalcUtils.sumToString(0D));
                        }
                        paymentReceipt.setDate(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(detailsEntity.getEventDate()));
                        //id платежа
                        paymentReceipt.setNum(String.valueOf(detailsEntity.getAnotherId()));
                        //Сумма всего
                        paymentReceipt.setSum(CalcUtils.sumToString(detailsEntity.getAmountOperation()));
                        //Сумма процентов
                        paymentReceipt.setSumOfInterest(CalcUtils.sumToString(detailsEntity.getAmountPercent()));
                        paymentReceipt.setPayee(rastoropOrg);
                        paymentReceipt.setContract(contract);
                        paymentReceipt.setAmountOverpay(CalcUtils.sumToString(detailsEntity.getAmountOverpay()));
                        //клиент
                        paymentReceipt.setPayer(genPerson(creditEntity,borrowerAccount));
                        //проверка суммы, в обмене не участвует
                        if(pay!=null){
                            paymentReceipt.setPaySum(CalcUtils.sumToString(pay.getAmount()));
                        }else{
                            paymentReceipt.setPaySum(CalcUtils.sumToString(0d));
                        }

                        paymentsReceipt.add(paymentReceipt);
                    }
                }
            }else{
                logger.severe("******DEBUG ONLY******* paymentsToSystem list is empty" + date);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,"******DEBUG ONLY******* EXCEPTION!!! ",e);
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
            person.setName(borrowerPersonal.getFullName());
            person.setBirthDate(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(borrowerPersonal.getBirthdate()));

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
                person.setPassportIssued(DatesUtils.DATE_FORMAT_YYYY_MM_dd.format(document.getDocdate()));
                person.setPassportIssueDate(person.getPassportIssued());
                person.setPassportNumber(document.getNumber());
                person.setPassportSeries(document.getSeries());
            }

            //BankAccount
            //if(borrower.getAccounts()!=null && borrower.getAccounts().size()>0){
            person.setBankAccount(genBankAccount(borrowerAccount));
            //}
        }
        return person;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public SputnikExchange generateXMLAccruedInterest(Date date) {
        if(date == null){
            date = new Date();
        }
        ProductsEntity product = productDAO.getProductDefault();
        Map<String, Object> limits = productBean.getNewRequestProductConfig(product.getId());
        Integer addDays = Convertor.toInteger(limits.get(ProductKeys.ADDITIONAL_DAYS_FOR_CALCULATE));
        logger.severe("******OdinSServiceImpl!!!!!!!!!! STARTED!!!! generateXMLAccruedInterest ****************" + date);

        SputnikExchange result = null;
        try {
            result = new SputnikExchange();
            result.setVersion(VERSION);

            //все имеющиеся кредиты на дату 
            List<CreditEntity> credits =creditBean.listCreditsForCalcPercent(date);
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
                        logger.log(Level.SEVERE,"******DEBUG ONLY******* generateXMLAccruedInterest cycle EXCEPTION!!! ",e);
                    }
                }
                result.setTotalsRecords(totalsRecords);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,"******DEBUG ONLY******* generateXMLAccruedInterest EXCEPTION!!! ",e);
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


    private Сompany genAgent(Integer anotherId){
        if(anotherId==null) return null;

        Сompany agent = null;
        PaymentEntity paymentEntity = payDAO.getPayment(anotherId);
        if(paymentEntity!=null){
            PartnersEntity partnersEntity = paymentEntity.getPartnersId();
            if(partnersEntity!=null){
                agent = new Сompany();
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
