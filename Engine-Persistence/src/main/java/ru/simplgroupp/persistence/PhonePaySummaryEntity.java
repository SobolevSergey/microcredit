package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Платежи за телефон суммарно
 */
public class PhonePaySummaryEntity extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5525245086219599101L;
	protected Integer txVersion = 0;
	
	/**
	 * человек
	 */
	private PeopleMainEntity peopleMainId;
	/**
	 * заявка
	 */
	private CreditRequestEntity creditRequestId;
	/**
	 * партнер
	 */
	private PartnersEntity partnersId;
	/**
	 * кол-во периодов
	 */
	private Integer periodCount;
	/**
	 * вид платежа по справочнику
	 */
	private ReferenceEntity paymentType;
	/**
	 * вид периода по справочнику
	 */
	private ReferenceEntity paymentPeriodType;
	/**
	 * платежи
	 */
	private List<PhonePayEntity> phonepays=new ArrayList<PhonePayEntity>(0);
	
	public PhonePaySummaryEntity(){
		
	}

	public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
       this.creditRequestId = creditRequestId;
    }
    
    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }
    
    public Integer getPeriodCount() {
        return periodCount;
    }

    public void setPeriodCount(Integer periodCount) {
        this.periodCount = periodCount;
    }
    
    public ReferenceEntity getPaymentType(){
    	return paymentType;
    }
    
    public void setPaymentType(ReferenceEntity paymentType){
    	this.paymentType=paymentType;
    }
    
    public ReferenceEntity getPaymentPeriodType(){
    	return paymentPeriodType;
    }
    
    public void setPaymentPeriodType(ReferenceEntity paymentPeriodType){
    	this.paymentPeriodType=paymentPeriodType;
    }
    
    public List<PhonePayEntity> getPhonepays(){
    	return phonepays;
    }
    
    public void setPhonepays(List<PhonePayEntity> phonepays){
    	this.phonepays=phonepays;
    }
    
   
}
