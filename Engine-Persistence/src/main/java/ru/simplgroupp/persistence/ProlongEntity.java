package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Продления
 */
public class ProlongEntity extends BaseEntity implements Serializable {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = -1715663107008817759L;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    protected Integer txVersion = 0;
 
    /**
     * дата продления
     */
    private Date longdate;
    /**
     * дней продления
     */
    private Integer longdays;
    /**
     * ставка продления
     */
    private Double longstake;
    /**
     * сумма продления
     */
    private Double longamount;
    /**
     * кредит
     */
    private CreditEntity creditId;
    /**
     * код смс
     */
    private String smsCode;
    /**
     * соглашение текстовое
     */
    private String agreement;
    /**
     * уникальный номер
     */
    private String uniquenomer;
    
    /**
     * запись активна или нет
     */
    private Integer isactive;
    
    public ProlongEntity() {
    }

    public Date getLongdate() {
        return longdate;
    }

    public void setLongdate(Date longdate) {
        this.longdate = longdate;
    }

    public Integer getLongdays() {
        return longdays;
    }

    public void setLongdays(Integer longdays) {
        this.longdays = longdays;
    }

    public Double getLongstake() {
        return longstake;
    }

    public void setLongstake(Double longstake) {
        this.longstake = longstake;
    }

    public Double getLongamount() {
        return longamount;
    }

    public void setLongamount(Double longamount) {
        this.longamount = longamount;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }
    
    @Override
    public String toString() {
        return "продление от "+sdf.format(this.longdate);
    }

    public CreditEntity getCreditId() {
        return creditId;
    }

    public void setCreditId(CreditEntity creditId) {
        this.creditId = creditId;
    }

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getAgreement(){
	   	return agreement;
	}
	    
	public void setAgreement(String agreement){
	   	this.agreement=agreement;
	}
	 
	public String getUniquenomer() {
	     return uniquenomer;
	}

	public void setUniquenomer(String uniquenomer) {
	     this.uniquenomer = uniquenomer;
	}
	
	
}
