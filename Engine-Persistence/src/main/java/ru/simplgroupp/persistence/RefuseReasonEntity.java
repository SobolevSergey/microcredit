package ru.simplgroupp.persistence;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Справочник отказа в кредите
 */
@XmlRootElement
public class RefuseReasonEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7983309063045867200L;

	protected Integer txVersion = 0;
	
	private Integer id;
	/**
	 * краткое название
	 */
	private String name;
	/**
	 * полное название
	 */
	private String nameFull;
	/**
	 * причина из справочника
	 */
	private ReferenceEntity reasonId;
	/**
	 * константа для расчета
	 */
	private String constantName;
	/**
	 * используется менеджером для ручного принятия решения
	 */
	private Integer forDecision;
	
    public RefuseReasonEntity(){
		
	}
	
	public RefuseReasonEntity(Integer id){
		this.id=id;
	}
	
	@XmlElement
	public Integer getId() {
        return id;
    }

  
    public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
    public String getName(){
    	return name;
    }
    
    public void setName(String name){
    	this.name=name;
    }
    
    @XmlElement
    public String getNameFull(){
    	return nameFull;
    }
    
    public void setNameFull(String nameFull){
    	this.nameFull=nameFull;
    }
    
    @XmlElement
    public ReferenceEntity getReasonId() {
        return reasonId;
    }

    public void setReasonId(ReferenceEntity reasonId) {
        this.reasonId = reasonId;
    }
    
  	public String getConstantName() {
		return constantName;
	}

	public void setConstantName(String constantName) {
		this.constantName = constantName;
	}
	
	public Integer getForDecision() {
		return forDecision;
	}

	public void setForDecision(Integer forDecision) {
		this.forDecision = forDecision;
	}

	@Override
	public int hashCode() {
	        int hash = 0;
	        hash += (id != null ? id.hashCode() : 0);
	        return hash;
    }

	@Override
	public boolean equals(Object other) {
	        if (other == null) {
	            return false;
	        }

	        if (other == this) {
	            return true;
	        }

	        if (!(other.getClass() == getClass())) {
	            return false;
	        }

	        RefuseReasonEntity entity = (RefuseReasonEntity) other;

	        if (this.id == null) {
	            return false;
	        }

	        if (entity.getId() == null) {
	            return false;
	        }

	        return this.id.equals(entity.getId());
	 }
}
