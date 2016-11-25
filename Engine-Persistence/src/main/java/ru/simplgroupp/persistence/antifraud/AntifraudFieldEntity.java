package ru.simplgroupp.persistence.antifraud;

import java.io.Serializable;
import java.util.Date;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;

/**
  * журнал действий
 */
public class AntifraudFieldEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6681439643816645508L;

	
	public AntifraudFieldEntity() {
	}

	protected Integer txVersion = 0;
	private Integer id;

    /**
     * тип поля
     */
    private AntifraudFieldTypesEntity type;

    /**
     * значение до изменения
     */
    private String valueBefore;

    /**
     * значение после изменения
     */
    private String valueAfter;
    /**
     * дата создания
     */
    private Date createAt;
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


    public AntifraudFieldTypesEntity getType() {
        return type;
    }


    public void setType(AntifraudFieldTypesEntity type) {
        this.type = type;
    }

    public String getValueBefore() {
        return valueBefore;
    }

    public void setValueBefore(String valueBefore) {
        this.valueBefore = valueBefore;
    }

    public String getValueAfter() {
        return valueAfter;
    }

    public void setValueAfter(String valueAfter) {
        this.valueAfter = valueAfter;
    }

    public Date getCreateAt() {
        return createAt;
    }


    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
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
     	    
     	    AntifraudFieldEntity cast = (AntifraudFieldEntity) other;
     	    
     	    if (this.id == null) return false;
     	       
     	    if (cast.getId() == null) return false;       
     	       
     	    if (this.id.intValue() != cast.getId().intValue())
     	    	return false;
     	    
     	    return true;
    }
   
}
