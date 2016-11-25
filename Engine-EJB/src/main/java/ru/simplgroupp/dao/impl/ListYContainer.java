package ru.simplgroupp.dao.impl;

import java.util.Date;

import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.util.AbstractListContainer;
import ru.simplgroupp.util.ListContainer;

/**
 * Поиск по платежам
 * @author irina
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ListYContainer extends AbstractListContainer<Payment> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7189023558621011146L;
	
	private static final Integer NULL_INT_VALUE = -9999;
	
	/**
	 * Дата создания платежа
	 */
	@XmlElement
	protected DateRange prmDateCreate = new DateRange(null, null);

	/**
	 * Дата проведения платежа
	 */
	@XmlElement	
	protected DateRange prmDateComplete = new DateRange(null, null);
 
	@XmlElement
	protected Integer prmPartnerId;

	@XmlElement
	protected Integer prmStatus;

	@XmlElement
	protected String prmLastName;

	@XmlElement
	protected Integer prmPaymentTypeId;
    
	@XmlElement
	protected Integer prmCreditId;
    
	@XmlElement
	protected Integer prmAccountId;
    
	@XmlElement
	protected Integer prmAccountTypeId;
    
	@XmlElement
	protected Integer prmIsPaid;	
	
	/**
	 * сумма платежа
	 */
	@XmlElement
	protected MoneyRange prmSumPay = new MoneyRange(null, null);	

	@Override
	public String buildHQL(int bForList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHQLParams(Query qry, int bForList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nullIfEmpty() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareParams() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearParams() {
		prmDateCreate = new DateRange(null, null);
		prmDateComplete = new DateRange(null, null);
		prmPartnerId = null;
		prmStatus = null;
		prmLastName = null;
		prmPaymentTypeId = null;
		prmCreditId = null;
		prmAccountId = null;
		prmAccountTypeId = null;
		prmIsPaid = null;
        prmSumPay = new MoneyRange(null, null);
	}

	@Override
	public void copyParams(ListContainer<Payment> source) {
		ListYContainer src = (ListYContainer) source;
		
		this.prmAccountId = src.prmAccountId;
		this.prmAccountTypeId = src.prmAccountTypeId;
		this.prmCreditId = src.prmCreditId;
		this.prmDateComplete = src.prmDateComplete;
		this.prmDateCreate = src.prmDateCreate;
		this.prmIsPaid = src.prmIsPaid;
		this.prmLastName = src.prmLastName;
		this.prmPartnerId = src.prmPartnerId;
		this.prmPaymentTypeId = src.prmPaymentTypeId;
		this.prmStatus = src.prmStatus;
		this.prmSumPay = src.prmSumPay;	
	}

	@Override
	public String generateListName() {
		String sname = "Платежи ";
		sname = sname + "от " + new Date().toString();
		return sname;
	}

	@Override
	protected Payment wrapEntity(Object entity) {
		return new Payment((PaymentEntity) entity);
	}

	public DateRange getPrmDateCreate() {
		return prmDateCreate;
	}

	public void setPrmDateCreate(DateRange prmDateCreate) {
		this.prmDateCreate = prmDateCreate;
	}

	public DateRange getPrmDateComplete() {
		return prmDateComplete;
	}

	public void setPrmDateComplete(DateRange prmDateComplete) {
		this.prmDateComplete = prmDateComplete;
	}

	public Integer getPrmPartnerId() {
		return prmPartnerId;
	}

	public void setPrmPartnerId(Integer prmPartnerId) {
		this.prmPartnerId = prmPartnerId;
	}

	public Integer getPrmStatus() {
		return prmStatus;
	}

	public void setPrmStatus(Integer prmStatus) {
		this.prmStatus = prmStatus;
	}

	public String getPrmLastName() {
		return prmLastName;
	}

	public void setPrmLastName(String prmLastName) {
		this.prmLastName = prmLastName;
	}

	public Integer getPrmPaymentTypeId() {
		return prmPaymentTypeId;
	}

	public void setPrmPaymentTypeId(Integer prmPaymentTypeId) {
		this.prmPaymentTypeId = prmPaymentTypeId;
	}

	public Integer getPrmCreditId() {
		return prmCreditId;
	}

	public void setPrmCreditId(Integer prmCreditId) {
		this.prmCreditId = prmCreditId;
	}

	public Integer getPrmAccountId() {
		return prmAccountId;
	}

	public void setPrmAccountId(Integer prmAccountId) {
		this.prmAccountId = prmAccountId;
	}

	public Integer getPrmAccountTypeId() {
		return prmAccountTypeId;
	}

	public void setPrmAccountTypeId(Integer prmAccountTypeId) {
		this.prmAccountTypeId = prmAccountTypeId;
	}

	public Integer getPrmIsPaid() {
		return prmIsPaid;
	}

	public void setPrmIsPaid(Integer prmIsPaid) {
		this.prmIsPaid = prmIsPaid;
	}

	public MoneyRange getPrmSumPay() {
		return prmSumPay;
	}

	public void setPrmSumPay(MoneyRange prmSumPay) {
		this.prmSumPay = prmSumPay;
	}

}
