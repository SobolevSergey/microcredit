package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.simplgroupp.toolkit.common.Utils;

/**
 * Кредиты
 */
public class CreditEntity extends BaseEntity implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8252980648009520567L;

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    
    protected Integer txVersion = 0;
   
    /**
	 * вид счета
	 */
    private ReferenceEntity accountTypeId;
    /**
	 * валюта кредита
	 */
    private ReferenceEntity idCurrency;
    /**
	 * статус кредита
	 */
    private ReferenceEntity creditStatusId;
    /**
	 * вид кредита
	 */
    private ReferenceEntity credittypeId;
    /**
	 * отношение к кредиту
	 */
    private ReferenceEntity creditrelationId;
    /**
	 * частота платежей
	 */
    private ReferenceEntity creditfreqpaymentId;
    /**
	 * просрочка по кредиту
	 */
    private ReferenceEntity overduestateId;
    /**
	 * худшая просрочкя по кредиту
	 */
    private ReferenceEntity worstOverdueStateId;
    
    private String bankname;
    /**
	 * кол-во продлений
	 */
    private Integer creditlong;
    /**
	 * дата начала
	 */
    private Date creditdatabeg;
    /**
	 * дата окончания по графику
	 */
    private Date creditdataend;
    /**
	 * дата отсчета, если человек погасил не всю сумму
	 */
    private Date creditDateDebt;
    /**
	 * сумма
	 */
    private Double creditsum;
    /**
	 * сумма возврата
	 */
    private Double creditsumback;
    /**
	 * процент кредита
	 */
    private Double creditpercent;
    /**
	 * завершен ли кредит
	 */
    private Boolean isover;
    /**
	 * текущий долг
	 */
    private Double currentDebt;
    /**
	 * текущий просроченный долг
	 */
    private Double currentOverdueDebt;
    /**
	 * максимальный просроченный долг
	 */
    private Double maxOverdueDebt;
    /**
	 * неиспользованный кредитный лимит
	 */
    private Double creditlimitunused;
    /**
	 * номер кредита
	 */
    private String idCredit;
    /**
	 * номер счета кредита
	 */
    private String creditAccount;
    /**
	 * максимальная задержка по кредиту в днях
	 */
    private Integer maxDelay;
    /**
	 * лимит кредитной карты
	 */
    private Double creditcardlimit;
    /**
	 * выдан своей организацией
	 */
    private Boolean issameorg;
    /**
	 * сумма задолженности
	 */
    private Double creditsumdebt;
    /**
	 * фактическая дата окончания
	 */
    private Date creditdataendfact;
    /**
	 * человек
	 */
    private PeopleMainEntity peopleMainId;
    /**
	 * партнер
	 */
    private PartnersEntity partnersId;
    /**
	 * заявка
	 */
    private CreditRequestEntity creditRequestId;
    /**
	 * кредитная организация
	 */
    private OrganizationEntity creditOrganizationId;
    
    /**
	 * просрочек до 5 дней
	 */
    private Integer delay5;
    /**
	 * просрочек до 30 дней
	 */
    private Integer delay30;
    /**
	 * просрочек до 60 дней
	 */
    private Integer delay60;
    /**
	 * просрочек до 90 дней
	 */
    private Integer delay90;
    /**
	 * просрочек до 120 дней
	 */
    private Integer delay120;
    /**
	 * просрочек более 120 дней
	 */
    private Integer delaymore;
    /**
	 * просрочка в днях, возможно не нужно
	 */
    private Integer delayindays;
    /**
	 * погашен за счет обеспечения
	 */
    private Integer creditmoneyback;
    /**
	 * дата последнего обновления
	 */
    private Date datelastupdate;
    /**
	 * платежная дисциплина
	 */
    private String payDiscipline;
    /**
	 * выплаченная сумма
	 */
    private Double creditSumPaid;
    /**
	 * платеж за месяц
	 */
    private Double creditMonthlyPayment;
    /**
     * дата изменения статуса
     */
    private Date dateStatus; 
    /**
	 * запись активная или нет
	 */
	private Integer isactive;
	 /**
     * был ли выгружен кредит
     */
    private Boolean isUploaded;
    /**
     * изменялась ли сумма, нужно для пересчета при платеже
     */
    private Integer changedSum;
    /**
     * родительский кредит
     */
    private CreditEntity owner;
    
    /**
     * кредитный продукт
     */
    private ProductsEntity productId;
    /**
     * полная стоимость займа
     */
    private Double creditFullCost;
    /**
     * цель кредитования
     */
    private ReferenceEntity creditPurposeId;
    /**
     * платежи
     */
    private List<PaymentEntity> pays=new ArrayList<>(0);
    /**
     * кредитные операции
     */
    private List<CreditDetailsEntity> creditDetailsId = new ArrayList<>(0);

    /**
     * продления
     */
    private List<ProlongEntity> longs=new ArrayList<>(0);
    
    /**
     * графики выплат
     */
    private List<RepaymentScheduleEntity> schedules=new ArrayList<>(0);
    
    /**
     * история выплат кредита из КБ
     */
    private List<CreditHistoryPayEntity> historypays=new ArrayList<>(0);
    
    /**
     * логи
     */
    private List<EventLogEntity> logs = new ArrayList<>(0);
    /**
     * рефинансирование
     */
    private List<RefinanceEntity> refinances = new ArrayList<>(0); 
    /**
     * подписанные человеком документы
     */
    private List<OfficialDocumentsEntity> officialDocuments = new ArrayList<>(0);
    /**
     * долги
     */
    private List<DebtEntity> debts = new ArrayList<>(0);
    
    public CreditEntity() {
    }

    public ReferenceEntity getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(ReferenceEntity accountTypeId) {
        this.accountTypeId=accountTypeId;
    }

    public ReferenceEntity getCreditStatusId() {
        return creditStatusId;
    }

    public void setCreditStatusId(ReferenceEntity creditStatusId) {
        this.creditStatusId=creditStatusId;
    }
    
    public ReferenceEntity getOverduestateId() {
        return overduestateId;
    }

    public void setOverduestateId(ReferenceEntity overduestateId) {
        this.overduestateId=overduestateId;
    }
    
    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public Date getCreditdatabeg() {
        return creditdatabeg;
    }

    public void setCreditdatabeg(Date creditdatabeg) {
        this.creditdatabeg = creditdatabeg;
    }

       
    public Date getCreditdataend() {
        return creditdataend;
    }

    public void setCreditdataend(Date creditdataend) {
        this.creditdataend = creditdataend;
    }

    public Date getCreditDateDebt() {
        return creditDateDebt;
    }

    public void setCreditDateDebt(Date creditDateDebt) {
        this.creditDateDebt = creditDateDebt;
    }

    
    public Date getDatelastupdate() {
        return datelastupdate;
    }

    public void setDatelastupdate(Date datelastupdate) {
        this.datelastupdate = datelastupdate;
    }
    
    public Double getCreditsum() {
        return creditsum;
    }

    public void setCreditsum(Double creditsum) {
        this.creditsum = creditsum;
    }

    public Double getCreditSumPaid() {
        return creditSumPaid;
    }

    public void setCreditSumPaid(Double creditSumPaid) {
        this.creditSumPaid = creditSumPaid;
    }

    public Double getCreditMonthlyPayment() {
        return creditMonthlyPayment;
    }

    public void setCreditMonthlyPayment(Double creditMonthlyPayment) {
        this.creditMonthlyPayment = creditMonthlyPayment;
    }

    
    public Double getCreditsumback() {
        return creditsumback;
    }

    public void setCreditsumback(Double creditsumback) {
        this.creditsumback = creditsumback;
    }
    
    public Integer getDelayindays() {
        return delayindays;
    }

    public void setDelayindays(Integer delayindays) {
        this.delayindays = delayindays;
    }
    
    public Double getCreditpercent() {
        return creditpercent;
    }

    public void setCreditpercent(Double creditpercent) {
        this.creditpercent = creditpercent;
    }
    
    public Integer getCreditmoneyback() {
        return creditmoneyback;
    }

    public void setCreditmoneyback(Integer creditmoneyback) {
        this.creditmoneyback = creditmoneyback;
    }
    
    public Boolean getIsover() {
        return isover;
    }

    public void setIsover(Boolean isover) {
        this.isover = isover;
    }

    public ReferenceEntity getCredittypeId() {
        return credittypeId;
    }

    public void setCredittypeId(ReferenceEntity credittypeId) {
        this.credittypeId = credittypeId;
    }

    public ReferenceEntity getCreditrelationId() {
        return creditrelationId;
    }

    public void setCreditrelationId(ReferenceEntity creditrelationId) {
        this.creditrelationId = creditrelationId;
    }
    
    public ReferenceEntity getCreditfreqpaymentId() {
        return creditfreqpaymentId;
    }

    public void setCreditfreqpaymentId(ReferenceEntity creditfreqpaymentId) {
        this.creditfreqpaymentId = creditfreqpaymentId;
    }
    
    public String getIdCredit() {
        return idCredit;
    }

    public void setIdCredit(String idCredit) {
        this.idCredit = idCredit;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }
    
    public String getPayDiscipline() {
        return payDiscipline;
    }

    public void setPayDiscipline(String payDiscipline) {
        this.payDiscipline = payDiscipline;
    }
    
    public Integer getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(Integer maxDelay) {
        this.maxDelay = maxDelay;
    }
    
    public Double getCreditcardlimit() {
        return creditcardlimit;
    }

    public void setCreditcardlimit(Double creditcardlimit) {
        this.creditcardlimit = creditcardlimit;
    }

    public Double getCreditlimitunused() {
        return creditlimitunused;
    }

    public void setCreditlimitunused(Double creditlimitunused) {
        this.creditlimitunused = creditlimitunused;
    }
    
    public Boolean getIssameorg() {
        return issameorg;
    }

    public void setIssameorg(Boolean issameorg) {
        this.issameorg = issameorg;
    }

    public ReferenceEntity getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(ReferenceEntity idCurrency) {
        this.idCurrency = idCurrency;
    }

    public Double getCreditsumdebt() {
        return creditsumdebt;
    }

    public void setCreditsumdebt(Double creditsumdebt) {
        this.creditsumdebt = creditsumdebt;
    }

    public Integer getCreditlong() {
        return creditlong;
    }

    public void setCreditlong(Integer creditlong) {
        this.creditlong = creditlong;
    }
    
    public Date getCreditdataendfact() {
        return creditdataendfact;
    }

    public void setCreditdataendfact(Date creditdataendfact) {
        this.creditdataendfact = creditdataendfact;
    }
    
    public Double getMaxOverdueDebt() {
        return maxOverdueDebt;
    }

    public void setMaxOverdueDebt(Double maxOverdueDebt) {
        this.maxOverdueDebt = maxOverdueDebt;
    }
    
    public Double getCurrentDebt() {
        return currentDebt;
    }

    public void setCurrentDebt(Double currentDebt) {
        this.currentDebt = currentDebt;
    }

    public List<CreditDetailsEntity> getCreditDetailsId() {
        return creditDetailsId;
    }

    public void setCreditDetailsId(List<CreditDetailsEntity> creditDetailsId) {
        this.creditDetailsId = creditDetailsId;
    }

    public List<PaymentEntity> getPays() {
    	return pays;
    }
    
    public void setPays(List<PaymentEntity> pays) {
    	this.pays=pays;
    }
    
    public List<CreditHistoryPayEntity> getHistorypays(){
    	return historypays;
    }
    
    public void setHistorypays(List<CreditHistoryPayEntity> historypays){
    	this.historypays=historypays;
    }
    
    public List<ProlongEntity> getLongs() {
    	return longs;
    }
    
    public void setLongs(List<ProlongEntity> longs) {
    	this.longs=longs;
    }
    
    public List<RepaymentScheduleEntity> getSchedules() {
    	return schedules;
    }
    
    public void setSchedules(List<RepaymentScheduleEntity> schedules){
    	this.schedules=schedules;
    }
    
    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        this.creditRequestId = creditRequestId;
    }
    
    public OrganizationEntity getCreditOrganizationId() {
        return creditOrganizationId;
    }

    public void setCreditOrganizationId(OrganizationEntity creditOrganizationId) {
        this.creditOrganizationId = creditOrganizationId;
    }
    
    public Integer getDelay5() {
        return delay5;
    }

    public void setDelay5(Integer delay5) {
        this.delay5 = delay5;
    }

    public Integer getDelay30() {
        return delay30;
    }

    public void setDelay30(Integer delay30) {
        this.delay30 = delay30;
    }

    public Integer getDelay60() {
        return delay60;
    }

    public void setDelay60(Integer delay60) {
        this.delay60 = delay60;
    }

    public Integer getDelay90() {
        return delay90;
    }

    public List<EventLogEntity> getLogs() {
        return logs;
    }

    public Integer getDelay120() {
        return delay120;
    }

    public void setDelay120(Integer delay120) {
        this.delay120 = delay120;
    }
    
    public Integer getDelaymore() {
        return delaymore;
    }

    public void setDelaymore(Integer delaymore) {
        this.delaymore = delaymore;
    }
    
    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }
    
    public void setLogs(List<EventLogEntity> logs) {
        this.logs = logs;
    }
    
    public void setDelay90(Integer delay90) {
        this.delay90 = delay90;
    }
    
    public Boolean getIsUploaded() {
		return isUploaded;
	}

	public void setIsUploaded(Boolean isUploaded) {
		this.isUploaded = isUploaded;
	}

	public Integer getHasPays() {
    	if (getPays().size()>0) {
    		return getPays().size();
    	} else {
    		return 0;
    	}
    }
    
    public Integer getHasHistoryPays() {
    	if (getHistorypays().size()>0) {
    		return getHistorypays().size();
    	} else {
    		return 0;
    	}
    }
    
        
    public String getDescription() {
    	return "дата начала "+creditdatabeg.toString()+", дата окончания "+creditdataend.toString()+", сумма "+creditsum.toString()+", ставка "+creditpercent.toString()+(isover) != null?", кредит закрыт":", кредит открыт";
    }
    
    public String getDatabeg() {
    	return sdf.format(creditdatabeg);
    }
    
    public String getDataend() {
    	return sdf.format(creditdataend);
    }
    
    public String getDataendFact() {
    	return sdf.format(creditdataendfact);
    }
    
    public Double getCurrentOverdueDebt() {
        return currentOverdueDebt;
    }

    public void setCurrentOverdueDebt(Double currentOverdueDebt) {
        this.currentOverdueDebt = currentOverdueDebt;
    }
    
    public Date getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(Date dateStatus) {
        this.dateStatus = dateStatus;
    }
    
    public CreditEntity getOwner() {
		return owner;
	}

	public void setOwner(CreditEntity owner) {
		this.owner = owner;
	}

	public int getHasLongs() {
    	if (getLongs().size()>0) {
    		return getLongs().size();
    	} else {
    		return 0;
    	}
    }
    
    public int getHasLogs() {
    	if (getLogs().size()>0) {
    		return getLogs().size();
    	} else {
    		return 0;
    	}
    }
    
    public int getHasSchedules() {
    	if (getSchedules().size()>0) {
    		return getSchedules().size();
    	} else {
    		return 0;
    	}
    }
    
    public Integer getHasOfficialDocuments() {
        if (getOfficialDocuments().size() > 0) {
            return getOfficialDocuments().size();
        } else {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return "кредит на сумму "+creditsum.toString();
    }
    
    /**
     * Сумма оставшегося основного долга
     * @return
     */
    public Double getSumMainRemain() {
    	if (creditsumdebt != null && creditsumdebt > 0) {
    		return creditsumdebt;
    	} else {
    		return creditsum;
    	}
    }
    
    /**
	 * была ли изменена основная сумма
	 * @param mainSum
	 * @return
	 */
	public boolean isChangedMainSum(Double mainSum){
		return(!getSumMainRemain().equals(mainSum));
	}
	
    /**
     * Дата, на которую рассчитан основной оставшийся долг
     * @return
     */
    public Date getDateMainRemain() {
    	if (creditDateDebt != null) {
    		return creditDateDebt;
    	} else {
    		return creditdatabeg;
    	}
    }
    
    /**
     * Дата, когда был проведен и учтен последний платеж
     * @return
     */
    public Date getDateLastPayment() {
    	if (datelastupdate != null) {
    		return datelastupdate;
    	} else {
    		return getDateMainRemain();
    	}
    }
    
    @Override
	public Boolean equalsContent(BaseEntity other) {
    	CreditEntity ent=(CreditEntity) other;
    	return Utils.equalsNull(creditsum,ent.getCreditsum())
    			&&Utils.equalsNull(creditdatabeg!=null?creditdatabeg.getTime():null,ent.getCreditdatabeg()!=null?ent.getCreditdatabeg().getTime():null)
    			&&Utils.equalsNull(creditOrganizationId,ent.getCreditOrganizationId())&&Utils.equalsNull(isover,ent.getIsover())
    			&&Utils.equalsNull(credittypeId,ent.getCredittypeId())&&Utils.equalsNull(idCurrency,ent.getIdCurrency())
    			&&Utils.equalsNull(overduestateId,ent.getOverduestateId());
    }

	public ProductsEntity getProductId() {
		return productId;
	}

	public void setProductId(ProductsEntity productId) {
		this.productId = productId;
	}

	public List<RefinanceEntity> getRefinances() {
		return refinances;
	}

	public void setRefinances(List<RefinanceEntity> refinances) {
		this.refinances = refinances;
	}

	public Double getCreditFullCost() {
		return creditFullCost;
	}

	public void setCreditFullCost(Double creditFullCost) {
		this.creditFullCost = creditFullCost;
	}

	public ReferenceEntity getCreditPurposeId() {
		return creditPurposeId;
	}

	public void setCreditPurposeId(ReferenceEntity creditPurposeId) {
		this.creditPurposeId = creditPurposeId;
	}

	public Integer getChangedSum() {
		return changedSum;
	}

	public void setChangedSum(Integer changedSum) {
		this.changedSum = changedSum;
	}

	public List<OfficialDocumentsEntity> getOfficialDocuments() {
		return officialDocuments;
	}

	public void setOfficialDocuments(List<OfficialDocumentsEntity> officialDocuments) {
		this.officialDocuments = officialDocuments;
	}

	public List<DebtEntity> getDebts() {
		return debts;
	}

	public void setDebts(List<DebtEntity> debts) {
		this.debts = debts;
	}

	public ReferenceEntity getWorstOverdueStateId() {
		return worstOverdueStateId;
	}

	public void setWorstOverdueStateId(ReferenceEntity worstOverdueStateId) {
		this.worstOverdueStateId = worstOverdueStateId;
	}
	
	
	
		
}
