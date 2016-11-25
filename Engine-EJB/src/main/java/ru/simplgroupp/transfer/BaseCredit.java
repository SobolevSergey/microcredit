package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.DatesUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

abstract public class BaseCredit<E extends CreditEntity> extends BaseTransfer<E> implements Serializable, Initializable, Identifiable {
    
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -8527065369233061351L;

	public enum Options {
        INIT_PAYMENTS,
        INIT_PROLONGS,
        INIT_HISTORY,
        INIT_SCHEDULES,
        INIT_LOGS,
        INIT_DEBTS,
        INIT_CREDIT_REQUEST,
        INIT_PEOPLE,
        INIT_REFINANCES,
        INIT_OFFICIAL_DOCUMENT,
        INIT_OWNER, // при рефинансировании - основной кредит
        INIT_DETAILS // кредитные операции
        ; 
    }

	//владелец кредита
    public static final int CREDIT_OWNER=1;
    public static final int CREDIT_OWNER_EQUIFAX=0;
    
    //статус кредита
    public static final int CREDIT_CLOSED=0;
    public static final int CREDIT_ACTIVE=1;
    public static final int CREDIT_SELLED=2;
    public static final int CREDIT_HOPELESS=3;
    public static final int CREDIT_REFINANCE=4;
    public static final int CREDIT_COLLECTOR=5;
    public static final int CREDIT_CANCELLED=7;
    public static final int CREDIT_OVERDUE=8;
    public static final int CREDIT_COURT=9;
    public static final int CREDIT_INNER_COLLECTOR=10;
    //дополнительные статусы для выгрузки
    public static final int CREDIT_OVERDUE5=10;
    public static final int CREDIT_OVERDUE30=11;
    public static final int CREDIT_OVERDUE60=12;
    public static final int CREDIT_OVERDUE90=13;
    public static final int CREDIT_OVERDUE120=14;
    public static final int CREDIT_OVERDUE150=15;
    public static final int CREDIT_OVERDUE180=16;
    public static final int CREDIT_OVERDUE210=17;
    
    public static final String CREDIT_CLOSED_NBKI="13";
    public static final String CREDIT_ACTIVE_NBKI="00";
    public static final String CREDIT_COURT_NBKI="21";
    public static final String CREDIT_OVERDUE_NBKI="52";
    
    //виды кредитов
    public static final String COMMON_CREDIT_CODE="02";
    public static final int COMMON_CREDIT=5;
    public static final int CREDIT_CARD=4;
    public static final int CREDIT_CARD_NBKI=7;
    public static final int MICRO_CREDIT=19;
    public static final int MICRO_CREDIT_NBKI=16;
    public static final int MICRO_CREDIT_TYPE=1;
    public static final int MICRO_CREDIT_ORGANIZATION=2;
    public static final int COMMON_CREDIT_UPLOADING_RSTANDART=9;
    public static final int COMMON_CREDIT_UPLOADING_EQUIFAX=401;
    public static final int MICRO_CREDIT_UPLOADING_RSTANDART=17;
    public static final int MICRO_CREDIT_ONLINE_UPLOADING_RSTANDART=18;
    public static final String COMMON_CREDIT_UPLOADING_RSTANDART_TEXT="Потребительский кредит";
    public static final String MICRO_CREDIT_UPLOADING_RSTANDART_TEXT="Микрокредит";
    public static final String DOGOVOR_UPLOADING_RSTANDART="1";
    public static final int ANOTHER_CREDIT=99;
    
    //виды изменений кредита
    public static final String CHANGES_REASON_TEXT="Реструктуризация по причине избыточной долговой нагрузки";
    public static final String CHANGES_REASON="1";
    
    //частота платежей	
    public static final int FREQ_PAYMENT=7;
    public static final String FREQ_PAYMENT_MONTH="03";
    public static final String FREQ_PAYMENT_STRING="98";
    public static final String FREQ_PAYMENT_ONCE="00";
    
    //валюта кредита
    public static final String CURRENCY_RUR_TEXT="Российский рубль";
    public static final String CURRENCY_RUR="RUR";
    public static final int CURRENCY_RUR_ID=810;
    public static final String CURRENCY_RUB="RUB";
    public static final String CURRENCY_UNKNOWN="00";
    
    //платежная дисциплина
    public static final String CODE_PAY_DISC="831";
    public static final String CODE_PAY_SUM="705";
    public static final String CODE_PAY_MONTH="706";
    
    //просрочки
    public static final String REF_RSTANDART="4";
    public static final String NEW_OVERDUE_RSTANDART="0";
    public static final String WITHOUT_OVERDUE="1";
    public static final String OVERDUE_BEFORE_MONTH="A";
    public static final String OVERDUE_MONTH="2";
    public static final String OVERDUE_TWO_MONTH="3";
    public static final String OVERDUE_THREE_MONTH="4";
    public static final String OVERDUE_FOUR_MONTH="5";
    public static final String OVERDUE_FIVE_MONTH="6";
    
    //страна
    public static final String COUNTRY_RU="RU";

    //долг по кредиту, переданный на взыскание
    public static final int CREDIT_DEBT_UPLOADING=5;
    
    //виды операций по кредиту
    public static final int OPERATION_CREDIT=1;
    public static final int OPERATION_PROLONG=2;
    public static final int OPERATION_OVERDUE=3;
    public static final int OPERATION_PAYMENT=4;
    public static final int OPERATION_REFINANCE=5;
    
    public static final int MONEY_BACK=18;
    
    //причина прекращения передачи дела
    public static final int CREDIT_CANCEL_UPLOAD_COLLECTOR=1;
    public static final String CREDIT_CANCEL_UPLOAD_COLLECTOR_TEXT="Уступка прав требования по договору";
    public static final int CREDIT_CANCEL_UPLOAD_COURT=10;
    public static final int CREDIT_CANCEL_UPLOAD=10;
    public static final String CREDIT_CANCEL_UPLOAD_COURT_TEXT="Прекращение передачи информации в связи с постановлением суда";
    public static final String CREDIT_CANCEL_UPLOAD_REFUSE_TEXT="Клиент не забрал деньги, заем аннулирован";
    
    //виды событий для выгрузки
    public static final int CREDIT_TYPE_COLLECTOR=4;
    //операции с суммами
    public static final int OPERATION_IN=1;
	public static final int OPERATION_OUT=2;
	
	//суммы подробно
	public static final int SUM_MAIN=1;
	public static final int SUM_PERCENT=2;
	public static final int SUM_PENALTY=3;
	public static final int SUM_COMISSION=4;
	public static final int SUM_RETURN=5;
	public static final int SUM_BACK=6;
	public static final int SUM_INSURANCE=7;
	public static final int SUM_REFINANCE=8;
	public static final int SUM_UNKNOWN=9;
	
	
	protected Reference accountType;
    protected Reference creditStatus;
    protected Reference creditRelation;
    protected Reference creditFreqPayment;
    protected Reference creditType;
    protected Reference currency;
    protected Reference creditPurpose;
    protected Reference overdueState;
    protected Reference worstOverdueState;
    protected Organization creditOrganization;
    protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
	protected Prolong draftProlong;
	protected Prolong lastProlong;
	protected Refinance lastRefinance;
	protected Debt courtDebt;
	/**
	 * платежи
	 */
    protected List<Payment> pays; 
    /**
     * продления
     */
    protected List<Prolong> longs; 
    /**
     * рефинансирование
     */
    protected List<Refinance> refinances; 
    /**
     * графики платежей
     */
    protected List<RepaymentSchedule> schedules;
    /**
     * история платежей из КБ
     */
    protected List<CreditHistoryPay> historyPays;
    /**
     * логи
     */
    protected List<EventLog> events;
    /**
     * Кредитные операции
     */
    protected List<CreditDetails> creditDetails;
    /**
     * документы, подписанные клиентом
     */
    protected List<OfficialDocuments> officialDocuments;
    /**
     * долги
     */
    protected List<Debt> debts;
    
    protected EventLog accidentLog;
    /**
     * продукт
     */
    protected Products product;
    /**
     * кредит-родитель в случае рефинансирования
     */
    protected Credit creditOwner;
    /**
     * платеж Контакта не получен
     */
    protected Boolean paymentContactNotReceived=false;
    /**
     * платеж Контакта отменен
     */
    protected Boolean paymentContactRevoked=false;

	/**
	 * Кредит оплачен системой
	 */
	protected Boolean paymentSuccessPaid = false;
    
    public BaseCredit() {
        super();
    }
    
    public BaseCredit(E entity) {
        super(entity);
        if (entity.getAccountTypeId()!=null){
          accountType = new Reference(entity.getAccountTypeId());
        }
        if (entity.getCredittypeId()!=null){
            creditType = new Reference(entity.getCredittypeId());
        }
        if (entity.getCreditPurposeId()!=null){
        	creditPurpose=new Reference(entity.getCreditPurposeId());
        }
        if (entity.getCreditStatusId()!=null){
        	creditStatus=new Reference(entity.getCreditStatusId());
        }
        if (entity.getCreditrelationId()!=null){
        	creditRelation=new Reference(entity.getCreditrelationId());
        }
        if (entity.getCreditfreqpaymentId()!=null){
        	creditFreqPayment=new Reference(entity.getCreditfreqpaymentId());
        }
        if (entity.getOverduestateId()!=null){
        	overdueState=new Reference(entity.getOverduestateId());
        }
        if (entity.getWorstOverdueStateId()!=null){
        	worstOverdueState=new Reference(entity.getWorstOverdueStateId());
        }
        if (entity.getIdCurrency()!=null){
          currency = new Reference(entity.getIdCurrency());
        }
        if (entity.getCreditOrganizationId()!=null){
        	creditOrganization=new Organization(entity.getCreditOrganizationId());
        }
        if (entity.getProductId() != null) {
        	product = new Products(entity.getProductId());
        }
        if (entity.getOwner() != null) {
        	creditOwner = new Credit(entity.getOwner());
        }
        peopleMain=new PeopleMain(entity.getPeopleMainId());
        partner=new Partner(entity.getPartnersId());
     
        debts=new ArrayList<>(0);
        pays = new ArrayList<>(0);
        longs = new ArrayList<>(0);
        schedules = new ArrayList<>(0);
        historyPays=new ArrayList<>(0);
        events = new ArrayList<>(0);
        creditDetails = new ArrayList<>(0);
        refinances = new ArrayList<>(0);
        officialDocuments=new ArrayList<>(0);
    }    
    
    public List<Refinance> getRefinances() {
    	return refinances;
    }

    public boolean isRefinance() {
    	return (entity.getCreditStatusId().getCodeinteger() == CREDIT_REFINANCE);
    }
    
    @Override
    public void init(Set options) {
   
    	//продления
        if (options != null && options.contains(Options.INIT_PROLONGS)) {
            Utils.initCollection(entity.getLongs(), options, Options.INIT_PROLONGS);
            Date maxDate = null;
            lastProlong = null;
            for (ProlongEntity payEnt: entity.getLongs()) {
                Prolong prolong = new Prolong(payEnt);
                if (prolong instanceof Initializable) {
                    ((Initializable) prolong).init(options);
                }
                if (prolong.getIsActive()==ActiveStatus.DRAFT){
                	draftProlong=prolong;
                }
                if (lastProlong == null || lastProlong.getLongDate().after(maxDate)) {
                	lastProlong = prolong;
                	maxDate = prolong.getLongDate();
                } 
                longs.add(prolong);
            }
        }
        
        //рефинансирование
        lastRefinance = null;
        if (options != null && options.contains(Options.INIT_REFINANCES)) {
        	Utils.initCollection(entity.getRefinances(), options);
        	Date maxDate = null;
        	lastRefinance = null;
        	for (RefinanceEntity ent: entity.getRefinances()) {
        		Refinance refin = new Refinance(ent);
        		refin.init(options);
        		refinances.add(refin);
                if (lastRefinance == null || lastRefinance.getRefinanceDate().after(maxDate)) {
                	lastRefinance = refin;
                	maxDate = refin.getRefinanceDate();
                } 
        		
        	}
        }
        
        //родитель кредита (для рефинансирования)
        if (options != null && options.contains(Options.INIT_OWNER) && creditOwner != null) {
        	entity.getOwner().getCreditAccount();
        	creditOwner.init(options);
        }
        
        //платежи
        if (options != null && options.contains(Options.INIT_PAYMENTS)) {
            Utils.initCollection(entity.getPays(), options, Options.INIT_PAYMENTS);
            for (PaymentEntity payEnt: entity.getPays()) {
                Payment cr = new Payment(payEnt);
                if (cr instanceof Initializable) {
                    ((Initializable) cr).init(options);
                }
                pays.add(cr);
                if (!cr.getIsPaid()&&cr.getPaymentType().getCodeInteger()==Payment.FROM_SYSTEM
                		&&cr.getPartner().getId()==Partner.CONTACT&&cr.getStatus().equals(PaymentStatus.SENDED)){
                	paymentContactNotReceived=true;
                }
                if (!cr.getIsPaid()&&cr.getPaymentType().getCodeInteger()==Payment.FROM_SYSTEM
                		&&cr.getPartner().getId()==Partner.CONTACT&&cr.getStatus().equals(PaymentStatus.REVOKED)){
                	paymentContactRevoked=true;
                }

                if (cr.getIsPaid() && cr.getStatus().equals(PaymentStatus.SUCCESS)&&cr.getPaymentType().getCodeInteger()==Payment.FROM_SYSTEM) {
                    paymentSuccessPaid = true;
                }
            }            
        }
        
        //долги
        if (options != null && options.contains(Options.INIT_DEBTS)) {
            Utils.initCollection(entity.getDebts(), options, Options.INIT_DEBTS);
            for (DebtEntity ent: entity.getDebts()) {
                Debt cr = new Debt(ent);
                if (cr instanceof Initializable) {
                    ((Initializable) cr).init(options);
                }
                debts.add(cr);
                if (ent.getAuthorityId() != null && ent.getPartnersId().getId() == Partner.SYSTEM 
                		&& ent.getAuthorityId().getCodeinteger() == Debt.DEBT_COURT &&ent.getIsActive()==ActiveStatus.ACTIVE) {
                    courtDebt = new Debt(ent);
                    courtDebt.init(options);
                }
            }            
        }
        
        //подписанные документы
      	if (options != null && options.contains(Options.INIT_OFFICIAL_DOCUMENT)) {
      		Utils.initCollection(entity.getOfficialDocuments(), options);
      		for (OfficialDocumentsEntity doc: entity.getOfficialDocuments()) {
      		   	OfficialDocuments ofdoc = new OfficialDocuments(doc);
      			ofdoc.init(options);
      			officialDocuments.add(ofdoc);
      				       
      	   	}
      	}	
      	
        //история кредита
        if (options != null && options.contains(Options.INIT_HISTORY)) {
            Utils.initCollection(entity.getHistorypays(), options, Options.INIT_HISTORY);
            for (CreditHistoryPayEntity payEnt: entity.getHistorypays()) {
                CreditHistoryPay cr = new CreditHistoryPay(payEnt);
                if (cr instanceof Initializable) {
                    ((Initializable) cr).init(options);
                }
                historyPays.add(cr);
            }
        }    
        
        //логи
        if (options != null && options.contains(Options.INIT_LOGS)) {
            Utils.initCollection(entity.getLogs(), options);
            for (EventLogEntity empEnt : entity.getLogs()) {
                EventLog evt = new EventLog(empEnt);
                evt.init(options);
                events.add(evt);
            }
        }
        
        //график платежей
        if (options != null && options.contains(Options.INIT_SCHEDULES)) {
            Utils.initCollection(entity.getSchedules(), options, Options.INIT_SCHEDULES);
            for (RepaymentScheduleEntity payEnt: entity.getSchedules()) {
                RepaymentSchedule repay = new RepaymentSchedule(payEnt);
                if (repay instanceof Initializable) {
                    ((Initializable) repay).init(options);
                }
                schedules.add(repay);
            }
        }

        //заявки
        if (options != null && options.contains(Options.INIT_CREDIT_REQUEST)) {
            creditRequest = new CreditRequest(entity.getCreditRequestId());
            creditRequest.init(Utils.excludeOptions(options, 
            		Options.INIT_PAYMENTS,
            		Options.INIT_PROLONGS,
            		Options.INIT_HISTORY,
            		Options.INIT_SCHEDULES,
            		Options.INIT_LOGS,
            		Options.INIT_OFFICIAL_DOCUMENT));
        }

        // кредитные операции
        if (options != null && options.contains(Options.INIT_DETAILS)) {
            List<CreditDetailsEntity> details = entity.getCreditDetailsId();
            for (CreditDetailsEntity e : details) {
                CreditDetails crd = new CreditDetails(e);
                creditDetails.add(crd);
            }
        }
        
        //человек
        Utils.initRelation(peopleMain, Utils.excludeOptions(options, PeopleMain.Options.INIT_CREDIT, PeopleMain.Options.INIT_CREDIT_REQUEST), Options.INIT_PEOPLE);
       
  }

    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public Reference getAccountType() {
        return accountType;
    }

    public void setAccountType(Reference accountType) {
        this.accountType=accountType;
    }

    public Prolong getDraftProlong() {
      	return draftProlong;
    }
    
    public Prolong getLastProlong() {
    	return lastProlong;
    }    
    
    public Refinance getDraftRefinance() {
    	if (lastRefinance != null && lastRefinance.getIsActive() == ActiveStatus.DRAFT) {
    		return lastRefinance;
    	} else {
    		return null;
    	}
	}

	public Reference getCreditStatus() {
        return creditStatus;
    }

	@XmlElement
    public void setCreditStatus(Reference creditStatus) {
        this.creditStatus = creditStatus;
    }
    
    public Reference getCreditPurpose() {
		return creditPurpose;
	}

	public void setCreditPurpose(Reference creditPurpose) {
		this.creditPurpose = creditPurpose;
	}

	public Reference getOverdueState() {
        return overdueState;
    }

    public void setOverdueState(Reference overdueState) {
        this.overdueState = overdueState;
    }
    
    public Reference getWorstOverdueState() {
		return worstOverdueState;
	}

	public void setWorstOverdueState(Reference worstOverdueState) {
		this.worstOverdueState = worstOverdueState;
	}

	public Reference getCreditRelation() {
        return creditRelation;
    }

    public void setCreditRelation(Reference creditRelation) {
        this.creditRelation = creditRelation;
    }
    
    public Reference getCreditFreqPayment() {
        return creditFreqPayment;
    }

    public void setCreditFreqPayment(Reference creditFreqPayment) {
        this.creditFreqPayment = creditFreqPayment;
    }
    
    public String getBankName() {
        return entity.getBankname();
    }

    public void setBankName(String bankname) {
        entity.setBankname(bankname);
    }

    public Integer getDelay5() {
        return entity.getDelay5();
    }

    @XmlElement
    public void setDelay5(Integer delay5) {
        entity.setDelay5(delay5);
    }

    public Integer getDelay30() {
        return entity.getDelay30();
    }

    @XmlElement
    public void setDelay30(Integer delay30) {
        entity.setDelay30(delay30);
    }

    public Integer getDelay60() {
        return entity.getDelay60();
    }

    @XmlElement
    public void setDelay60(Integer delay60) {
        entity.setDelay60(delay60);
    }

    public Integer getDelay90() {
        return entity.getDelay90();
    }

    @XmlElement
    public void setDelay90(Integer delay90) {
        entity.setDelay90(delay90);
    }

    public Integer getDelay120() {
        return entity.getDelay120();
    }

    @XmlElement
    public void setDelay120(Integer delay120) {
        entity.setDelay120(delay120);
    }
    
    public Integer getDelayMore() {
        return entity.getDelaymore();
    }

    @XmlElement
    public void setDelayMore(Integer delaymore) {
        entity.setDelaymore(delaymore);
    }
    
    public Date getDateLastUpdate() {
        return entity.getDatelastupdate();
    }

    @XmlElement
    public void setDateLastUpdate(Date datelastupdate) {
        entity.setDatelastupdate(datelastupdate);
    }
    
    public Date getCreditDataBeg() {
        return entity.getCreditdatabeg();
    }

    @XmlElement
    public void setCreditDataBeg(Date creditdatabeg) {
        entity.setCreditdatabeg(creditdatabeg);
    }

    public Date getCreditDataEnd() {
        return entity.getCreditdataend();
    }

    @XmlElement
    public void setCreditDataEnd(Date creditdataend) {
        entity.setCreditdataend(creditdataend);
    }

    public Date getCreditDateDebt() {
        return entity.getCreditDateDebt();
    }

    public void setCreditDateDebt(Date creditDateDebt) {
        entity.setCreditDateDebt(creditDateDebt);
    }
    
    public Integer getIsActive() {
        return entity.getIsactive();
    }

    public void setIsActive(Integer isactive) {
    	entity.setIsactive(isactive);
    }
    
    public Double getCreditSum() {
        return entity.getCreditsum();
    }

    @XmlElement
    public void setCreditSum(Double creditsum) {
        entity.setCreditsum(creditsum);
    }

    public Double getCreditFullCost() {
        return entity.getCreditFullCost();
    }

    @XmlElement
    public void setCreditFullCost(Double creditFullCost) {
        entity.setCreditFullCost(creditFullCost);
    }
    
    public Double getCreditSumPaid() {
        return entity.getCreditSumPaid();
    }

    public void setCreditSumPaid(Double creditSumPaid) {
        entity.setCreditSumPaid(creditSumPaid);
    }
    
    public Double getCreditMonthlyPayment() {
        return entity.getCreditMonthlyPayment();
    }

    public void setCreditMonthlyPayment(Double creditMonthlyPayment) {
        entity.setCreditMonthlyPayment(creditMonthlyPayment);
    }
    
    public Double getCreditSumBack() {
        return entity.getCreditsumback();
    }

    @XmlElement
    public void setCreditSumBack(Double creditsumback) {
        entity.setCreditsumback(creditsumback);
    }
    
    public Double getCreditPercent() {
        return entity.getCreditpercent();
    }

    @XmlElement
    public void setCreditPercent(Double creditpercent) {
        entity.setCreditpercent(creditpercent);
    }
    
    public Boolean getIsOver() {
        return entity.getIsover();
    }

    @XmlElement
    public void setIsOver(Boolean isover) {
        entity.setIsover(isover);
    }

    public List<EventLog> getEventLogs() {
        return events;
    }
    
    public Reference getCreditType() {
        return creditType;
    }

    @XmlElement
    public void setCreditType(Reference credittype) {
        this.creditType = credittype;
    }

    public String getIdCredit() {
        return entity.getIdCredit();
    }

    public void setIdCredit(String idCredit) {
        entity.setIdCredit(idCredit);
    }

    public String getCreditAccount() {
        return entity.getCreditAccount();
    }

    @XmlElement
    public void setCreditAccount(String creditAccount) {
        entity.setCreditAccount(creditAccount);
    }
    
    public String getPayDiscipline() {
        return entity.getPayDiscipline();
    }

    @XmlElement
    public void setPayDiscipline(String payDiscipline) {
        entity.setPayDiscipline(payDiscipline);
    }
    
    public Integer getMaxDelay() {
        return entity.getMaxDelay();
    	
    }

    @XmlElement
    public void setMaxDelay(Integer maxDelay) {
      entity.setMaxDelay(maxDelay);
    }
    
    public Double getCreditCardLimit() {
        return entity.getCreditcardlimit();
    }

    @XmlElement
    public void setCreditCardLimit(Double creditcardlimit) {
        entity.setCreditcardlimit(creditcardlimit);
    }

    public Integer getCreditMoneyBack() {
        return entity.getCreditmoneyback();
    }

    public void setCreditMoneyBack(Integer creditmoneyback) {
        entity.setCreditmoneyback(creditmoneyback);
    }
    
    public Double getCreditLimitUnused() {
        return entity.getCreditlimitunused();
    }

    public void setCreditLimitUnused(Double creditlimitunused) {
        entity.setCreditlimitunused(creditlimitunused);
    }
    
    public Boolean getIsSameOrg() {
        return entity.getIssameorg();
    }

    public void setIsSameOrg(Boolean issameorg) {
        entity.setIssameorg(issameorg);
    }

    public Reference getCurrency() {
        return currency;
    }

    @XmlElement
    public void setCurrency(Reference currency) {
        this.currency = currency;
    }

    public Organization getCreditOrganization() {
        return creditOrganization;
    }

    public void setCreditOrganization(Organization creditOrganization) {
        this.creditOrganization = creditOrganization;
    }
    
    public Double getCreditSumDebt() {
        return entity.getCreditsumdebt();
    }

    public void setCreditsumdebt(Double creditsumdebt) {
        entity.setCreditsumdebt(creditsumdebt);
    }

    public Date getCreditDataEndFact() {
        return entity.getCreditdataendfact();
    }

    @XmlElement
    public void setCreditDataEndFact(Date creditdataendfact) {
        entity.setCreditdataendfact(creditdataendfact);
    }
    
    public List<Payment> getPays() {
        return pays;
    }
       
    public List<CreditHistoryPay> getHistoryPays(){
    	return historyPays;
    }
    
    public List<Prolong> getLongs() {
        return longs;
    }
      
    public List<RepaymentSchedule> getRepaymentSchedules() {
        return schedules;
    }
    
    public PeopleMain getPeopleMain() {
        return peopleMain;
    }


    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
    }
    
    public CreditRequest getCreditRequest() {
        return creditRequest;
    }


    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest=creditRequest;
    }
    
    public Partner getPartner() {
        return partner;
    }

    @XmlElement
    public void setPartner(Partner partner) {
        this.partner=partner;
    }
    
    public Date getDateStatus() {
        return entity.getDateStatus();
    }

    @XmlElement
    public void setDateStatus(Date dateStatus) {
        entity.setDateStatus(dateStatus);
    }
    
     
    public String getDescription() {
    	return entity.getDescription();
    }
    
    public String getDatabeg() {
    	return entity.getDatabeg();
    }
    
    public String getDataend() {
    	return entity.getDataend();
    }
    
    public String getDataendFact(){
    	return entity.getDataendFact();
    }
    
    public int getHasLongs(){
    	return entity.getHasLongs();
    }
    
    public int getHasPays() {
    	return entity.getHasPays();
    }
    
    public int getHasHistoryPays(){
    	return entity.getHasHistoryPays();
    }
    
    public int getHasSchedules(){
    	return entity.getHasSchedules();
    }
    
    public Integer getHasOfficialDocuments() {
        return entity.getHasOfficialDocuments();
    }
    
    public Integer getCreditLong() {
        return entity.getCreditlong();
    }

    @XmlElement
    public void setCreditLong(Integer creditlong) {
        entity.setCreditlong(creditlong);
    }
    
    public Double getCurrentDebt() {
        return entity.getCurrentDebt();
    }

    public void setCurrentDebt(Double currentDebt) {
        entity.setCurrentDebt(currentDebt);
    }

    public Double getMaxOverdueDebt() {
        return entity.getMaxOverdueDebt();
    }

    public void setMaxOverdueDebt(Double maxOverdueDebt) {
        entity.setMaxOverdueDebt(maxOverdueDebt);
    }
    
    public Double getCurrentOverdueDebt() {
        return entity.getCurrentOverdueDebt();
    }

    @XmlElement
    public void setCurrentOverdueDebt(Double currentOverdueDebt) {
        entity.setCurrentOverdueDebt(currentOverdueDebt);
    }
    
    public Integer getDelayInDays() {
        return entity.getDelayindays();
    }

    public void setDelayInDays(Integer delayindays) {
        entity.setDelayindays(delayindays);
    }

    public List<CreditDetails> getCreditDetails() {
        return creditDetails;
    }

    public void setCreditDetails(List<CreditDetails> creditDetails) {
        this.creditDetails = creditDetails;
    }

    /**
     * сколько дней кредита
     */
    public int getCreditDays(){
    	if (this.getCreditDataBeg()==null||this.getCreditDataEnd()==null){
    		return 0;
    	}
    	return DatesUtils.daysDiff(getCreditDataEnd(),getCreditDataBeg());
    }
    
    public EventLog getAccidentLog() {
    	return accidentLog;
    }

	public void setAccidentLog(EventLog accidentLog) {
		this.accidentLog = accidentLog;
	}
	
	public Integer getChangedSum() {
        return entity.getChangedSum();
    }

    public void setChangedSum(Integer changedSum) {
        entity.setChangedSum(changedSum);
    }
    
	public List<Payment> getPayToSystem() {
		List<Payment> lst = new ArrayList<Payment>(pays.size());
		for (Payment pay: pays) {
			if (pay.getPaymentType().getCodeInteger() == Payment.TO_SYSTEM) {
				lst.add(pay);
			}
		}
		return lst;
	}	
	
	public List<Payment> getPayFromSystem() {
		List<Payment> lst = new ArrayList<Payment>(pays.size());
		for (Payment pay: pays) {
			if (pay.getPaymentType().getCodeInteger() == Payment.FROM_SYSTEM) {
				lst.add(pay);
			}
		}
		return lst;
	}

    public List<Payment> getClientPayments() {
        List<Payment> clientPaymentList = new ArrayList<Payment>();
        for (Payment payment : getPays()) {
            if (payment.getPaymentType().getCodeInteger() == Payment.TO_SYSTEM) {
                if (payment.getStatus().equals(PaymentStatus.SUCCESS) && payment.getIsPaid() != null && payment.getIsPaid()) {
                    // клиентские платежи
                    clientPaymentList.add(payment);
                }
            }
        }
        return clientPaymentList;
    }

    public List<Payment> getSystemPayments() {
        List<Payment> systemPaymentList = new ArrayList<Payment>();
        for (Payment payment : getPays()) {
            if (payment.getPaymentType().getCodeInteger() == Payment.FROM_SYSTEM) {
                if (payment.getStatus().equals(PaymentStatus.SENDED) 
                		|| payment.getStatus().equals(PaymentStatus.SUCCESS)
                		|| payment.getStatus().equals(PaymentStatus.REVOKED)) {
                    // системные платежи
                    systemPaymentList.add(payment);
                }
            }
        }
        return systemPaymentList;
    }

	public List<OfficialDocuments> getOfficialDocuments() {
		return officialDocuments;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
		this.entity.setProductId(product.getEntity());
	}

	public Refinance getLastRefinance() {
		return lastRefinance;
	}

	public Credit getCreditOwner() {
		return creditOwner;
	}
	
	public List<Debt> getDebts() {
		return debts;
	}

	public void setDebts(List<Debt> debts) {
		this.debts = debts;
	}

	
	public Debt getCourtDebt() {
		return courtDebt;
	}

	public int getOverdueInDays() {
		Date dtNow = new Date();
		if (this.entity.getCreditdataend().after(dtNow)) {
			return 0;
		} else {
			return DatesUtils.daysDiff(dtNow, this.entity.getCreditdataend());
		}
	}
	
	
	public Boolean getPaymentContactNotReceived() {
		return paymentContactNotReceived;
	}

	public void setPaymentContactNotReceived(Boolean paymentContactNotReceived) {
		this.paymentContactNotReceived = paymentContactNotReceived;
	}

	
	public Boolean getPaymentContactRevoked() {
		return paymentContactRevoked;
	}

	public void setPaymentContactRevoked(Boolean paymentContactRevoked) {
		this.paymentContactRevoked = paymentContactRevoked;
	}

    public Boolean getPaymentSuccessPaid() {
        return paymentSuccessPaid;
    }

    public void setPaymentSuccessPaid(Boolean paymentSuccessPaid) {
        this.paymentSuccessPaid = paymentSuccessPaid;
    }

    @Override
	 public Boolean equalsContent(CreditEntity entity){
		 return Utils.equalsNull(this.getCreditSum(),entity.getCreditsum())
				&&DatesUtils.isSameDay(this.getCreditDataBeg(), entity.getCreditdatabeg())
	    		&&Utils.equalsNull(this.getCreditOrganization()!=null?this.getCreditOrganization().getId():null,entity.getCreditOrganizationId()!=null?entity.getCreditOrganizationId().getId():null)
	    		&&Utils.equalsNull(this.getIsOver(),entity.getIsover())
	    		&&Utils.equalsNull(this.getCreditType()!=null?this.getCreditType().getId():null,entity.getCredittypeId()!=null?entity.getCredittypeId().getId():null)
	    		&&Utils.equalsNull(this.getCreditPurpose()!=null?this.getCreditPurpose().getId():null,entity.getCreditPurposeId()!=null?entity.getCreditPurposeId().getId():null)
	    		&&Utils.equalsNull(this.getCurrency()!=null?this.getCurrency().getId():null,entity.getIdCurrency()!=null?entity.getIdCurrency().getId():null)
	    		&&Utils.equalsNull(this.getOverdueState()!=null?this.getOverdueState().getId():null,entity.getOverduestateId()!=null?entity.getOverduestateId().getId():null);
	 }
}
