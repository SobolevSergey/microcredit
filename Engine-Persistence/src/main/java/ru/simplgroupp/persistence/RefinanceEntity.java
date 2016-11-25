package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * рефинансирование
 *
 */
public class RefinanceEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2238754948595329364L;

	 protected Integer txVersion = 0;
	
	 /**
	 * дата рефинансирования
	 */
	 private Date refinanceDate;
	 /**
	  * дней рефинансирования
	  */
	 private Integer refinanceDays;
	 /**
	  * ставка рефинансирования
	  */
	 private Double refinanceStake;
	 /**
	  * сумма рефинансирования (идет потом на другой кредит)
	  */
	 private Double refinanceAmount;
	 /**
	  * кредит
	  */
	 private CreditEntity creditId;
	 /**
	  * новый кредит
	  */
	 private CreditEntity creditNewId;
	 /**
	  * код смс
	  */
	 private String smsCode;
	  /**
	   * соглашение текстовое
	   */
	 private String agreement;
	 /**
	  * уникальный номер - номер кредита-Р
	  */
	 private String uniquenomer;
	 /**
	  * запись активна или нет
	  */
	 private Integer isactive;
	 
	public RefinanceEntity(){
		
	}
	
	public Date getRefinanceDate() {
		return refinanceDate;
	}

	public void setRefinanceDate(Date refinanceDate) {
		this.refinanceDate = refinanceDate;
	}

	public Integer getRefinanceDays() {
		return refinanceDays;
	}

	public void setRefinanceDays(Integer refinanceDays) {
		this.refinanceDays = refinanceDays;
	}

	public Double getRefinanceStake() {
		return refinanceStake;
	}

	public void setRefinanceStake(Double refinanceStake) {
		this.refinanceStake = refinanceStake;
	}

	public Double getRefinanceAmount() {
		return refinanceAmount;
	}

	public void setRefinanceAmount(Double refinanceAmount) {
		this.refinanceAmount = refinanceAmount;
	}

	public CreditEntity getCreditId() {
		return creditId;
	}

	public void setCreditId(CreditEntity creditId) {
		this.creditId = creditId;
	}

	public CreditEntity getCreditNewId() {
		return creditNewId;
	}

	public void setCreditNewId(CreditEntity creditNewId) {
		this.creditNewId = creditNewId;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getAgreement() {
		return agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public String getUniquenomer() {
		return uniquenomer;
	}

	public void setUniquenomer(String uniquenomer) {
		this.uniquenomer = uniquenomer;
	}

	public Integer getIsactive() {
		return isactive;
	}

	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}
	
}
