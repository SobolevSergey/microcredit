package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * Расписание платежей
 */
public class RepaymentScheduleEntity extends BaseEntity implements Serializable {
  
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6943809216124028061L;
	protected Integer txVersion = 0;
  
    /**
     * дата начала
     */
    private Date databeg;
    /**
     * дата окончания
     */
    private Date dataend;
    /**
     * ставка кредита
     */
    private Double creditstake;
    /**
     * сумма к возврату
     */
    private Double creditsumback;
    /**
     * оставшаяся сумма на текущее время
     */
    private Double creditsum;
    /**
     * причина окончания записи
     */
    private Integer reasonEndId;
    /**
     * активная запись или нет
     */
    private Integer isactive;
    /**
     * кредит
     */
    private CreditEntity creditId;


    public RepaymentScheduleEntity() {
    }

    public Date getDatabeg() {
        return databeg;
    }

    public void setDatabeg(Date databeg) {
        this.databeg = databeg;
    }

    public Date getDataend() {
        return dataend;
    }

    public void setDataend(Date dataend) {
        this.dataend = dataend;
    }

    public Double getCreditstake() {
        return creditstake;
    }

    public void setCreditstake(Double creditstake) {
        this.creditstake = creditstake;
    }

    public Double getCreditsumback() {
        return creditsumback;
    }

    public void setCreditsumback(Double creditsumback) {
        this.creditsumback = creditsumback;
    }

    public Double getCreditsum() {
        return creditsum;
    }

    public void setCreditsum(Double creditsum) {
        this.creditsum = creditsum;
    }
   
    public Integer getReasonEndId() {
        return reasonEndId;
    }

    public void setReasonEndId(Integer reasonEndId) {
        this.reasonEndId = reasonEndId;
    }
  
    public CreditEntity getCreditId() {
        return creditId;
    }

    public void setCreditId(CreditEntity creditId) {
        this.creditId = creditId;
    }
    
    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }
    
}
