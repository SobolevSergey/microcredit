package ru.simplgroupp.persistence.antifraud;

import java.io.Serializable;
import java.util.Date;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;

/**
 * переходы по страницам
 */
public class AntifraudPageEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4459166811518419576L;
	
	protected Integer txVersion = 0;
	
	public AntifraudPageEntity() {

	}

	private Integer id;

    /**
     * тип странички
     */
    private AntifraudPageTypesEntity type;

    /**
     * дата начала посещения странички
     */
    private Date dateStart;

    /**
     * дата ухода со странички
     */
    private Date dateEnd;
    /**
     * id человека
     */
    private PeopleMainEntity peopleMainId;

    /**
     * id пользователя
     */
    private CreditRequestEntity creditRequestId;

    // id сессии
    String sessionId;

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public AntifraudPageTypesEntity getType() {
        return type;
    }


    public void setType(AntifraudPageTypesEntity type) {
        this.type = type;
    }


    public Date getDateStart() {
        return dateStart;
    }


    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }


    public Date getDateEnd() {
        return dateEnd;
    }


    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
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


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object other) {
    	
    	   if (other == null) return false;
	       
     	    if (other == this) return true;
     	       
     	    if (! (other.getClass() ==  getClass()))
     	    	return false;
     	    
     	    AntifraudPageEntity cast = (AntifraudPageEntity) other;
     	    
     	    if (this.id == null) return false;
     	       
     	    if (cast.getId() == null) return false;       
     	       
     	    if (this.id.intValue() != cast.getId().intValue())
     	    	return false;
     	    
     	    return true;
    }
}
