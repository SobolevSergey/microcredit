package ru.simplgroupp.rules;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.simplgroupp.transfer.*;

abstract public class AbstractContext {

	protected Map<String,Object> params  = new HashMap<String,Object>(1);
	/**
	 * человек
	 */
	protected PeopleMain client;
	/**
	 * заявка
	 */
	protected CreditRequest ccRequest;	
	/**
	 * активный кредит
	 */
	protected Credit credit;
	/**
	 * продление
	 */
	protected Prolong prolong;
	/**
	 * оплата
	 */
	protected Payment payment;
	/**
	 * текущая дата
	 */
	protected Date currentDate = new Date();
	/**
	 * пользователь
	 */
	protected Users currentUser;
	/**
	 * рефинансирование
	 */
	protected Refinance refinance;
	/**
	 * последняя отказанная заявка
	 */
	protected CreditRequest lastRefusedCreditRequest;
	/**
	 * Запущено автоматически или вручную
	 */
	protected boolean autoRun = true;
	/**
	 * Событие, на которое сработал запуск. Если не на событие, то null.
	 */
	protected Integer eventCode;
	/**
	 * Коллектор
	 */
	protected Collector collector;
    /**
     * система-партнер
     */
	protected Partner partner;
	/**
	 * платежная система
     */
	protected Reference paymentSystem;

	public CreditRequest getCreditRequest() {
		return ccRequest;
	}

	public void setCreditRequest(CreditRequest ccRequest) {
		this.ccRequest = ccRequest;
	}

	public Map<String,Object> getParams() {
		return params;
	}

	public PeopleMain getClient() {
		return client;
	}

	public void setClient(PeopleMain client) {
		this.client = client;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public Prolong getProlong() {
		return prolong;
	}

	public void setProlong(Prolong prolong) {
		this.prolong = prolong;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Users getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Users currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isAutoRun() {
		return autoRun;
	}

	public void setAutoRun(boolean autoRun) {
		this.autoRun = autoRun;
	}

	public Integer getEventCode() {
		return eventCode;
	}

	public void setEventCode(Integer eventCode) {
		this.eventCode = eventCode;
	}

	public Refinance getRefinance() {
		return refinance;
	}

	public void setRefinance(Refinance refinance) {
		this.refinance = refinance;
	}

	public CreditRequest getLastRefusedCreditRequest() {
		return lastRefusedCreditRequest;
	}

	public void setLastRefusedCreditRequest(CreditRequest lastRefusedCreditRequest) {
		this.lastRefusedCreditRequest = lastRefusedCreditRequest;
	}

	public Collector getCollector() {
		return collector;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}

	public Reference getPaymentSystem() {
		return paymentSystem;
	}

	public void setPaymentSystem(Reference paymentSystem) {
		this.paymentSystem = paymentSystem;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	
}
