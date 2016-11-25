package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Платежи за телефон
 */
public class PhonePayEntity extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1276688357220028269L;

	protected Integer txVersion = 0;
	
	/**
	 * суммарная запись
	 */
	private PhonePaySummaryEntity summaryId;
	/**
	 * кол-во платежей
	 */
	private Integer paymentCount;
	/**
	 * вид платежей
	 */
	private ReferenceEntity paymentType;
	/**
	 * сумма
	 */
	private Double paymentSum;
	/**
	 * номер периода
	 */
	private Integer periodNumber;
	/**
	 * дата начала
	 */
	private Date periodFirstDate;
	
    public PhonePayEntity(){
		
	}

	public PhonePaySummaryEntity getSummaryId(){
    	return summaryId;
    }
    
    public void setSummaryId(PhonePaySummaryEntity summaryId){
    	this.summaryId=summaryId;
    }
    
    public Date getPeriodFirstDate(){
    	return periodFirstDate;
    }
    
    public void setPeriodFirstDate(Date periodFirstDate){
    	this.periodFirstDate=periodFirstDate;
    }
    
    public Integer getPeriodNumber(){
    	return periodNumber;
    }
    
    public void setPeriodNumber(Integer periodNumber){
    	this.periodNumber=periodNumber;
    }
    
    public Double getPaymentSum(){
    	return paymentSum;
    }
    
    public void setPaymentSum(Double paymentSum){
    	this.paymentSum=paymentSum;
    }
    
    public Integer getPaymentCount(){
    	return paymentCount;
    }
    
    public void setPaymentCount(Integer paymentCount){
    	this.paymentCount=paymentCount;
    }
    
    public ReferenceEntity getPaymentType(){
    	return paymentType;
    }
    
    public void setPaymentType(ReferenceEntity paymentType){
    	this.paymentType=paymentType;
    }
    
   
}
