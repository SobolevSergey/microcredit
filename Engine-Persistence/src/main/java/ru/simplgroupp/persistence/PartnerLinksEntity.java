package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Связь партнеров с видами оплаты из справочника
 */
public class PartnerLinksEntity extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6219637740899816677L;
	protected Integer txVersion = 0;
	
	/**
	 * запись активна или нет
	 */
	private Integer isActive;
	/**
	 * вид оплаты из справочника
	 */
	private ReferenceEntity referenceId;
	/**
	 * партнер
	 */
    private PartnersEntity partnersId;
    
    public PartnerLinksEntity(){
    	
    }
       
    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
    
    public ReferenceEntity getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(ReferenceEntity referenceId) {
        this.referenceId = referenceId;
    }

    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }
}
