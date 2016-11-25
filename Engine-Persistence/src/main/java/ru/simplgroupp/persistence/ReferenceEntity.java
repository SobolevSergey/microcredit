package ru.simplgroupp.persistence;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Справочники
 */
@XmlRootElement
public class ReferenceEntity implements Serializable {
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1236323686122339118L;

	protected Integer txVersion = 0;

    private Integer id;
    /**
     * значение
     */
    private String name;
    /**
     * символьный код
     */
    private String code;
    /**
     * числовой код
     */
    private Integer codeinteger;
    /**
     * символьный код для маппинга
     */
    private String codemain;
    /**
     * числовой код для маппинга
     */
    private Integer codeintegermain;
    /**
     * активная запись или нет
     */
    private Integer isactive;
   
    /**
     * заголовок справочника
     */
    private RefHeaderEntity refHeaderId;
    /**
     * заголовок справочника для маппинга
     */
    private RefHeaderEntity refHeaderIdMain;
    
    /**
     * Пункт верхнего уровня для иерархических справочников
     */
    private Integer parentId;
        
    public ReferenceEntity() {
    }

    public ReferenceEntity(Integer id) {
        this.id = id;
    }

    @XmlElement
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement
    public Integer getCodeinteger() {
        return codeinteger;
    }

    public void setCodeinteger(Integer codeinteger) {
        this.codeinteger = codeinteger;
    }
    
    @XmlElement
    public String getCodemain() {
        return codemain;
    }

    public void setCodemain(String codemain) {
        this.codemain = codemain;
    }

    @XmlElement
    public Integer getCodeintegermain() {
        return codeintegermain;
    }

    public void setCodeintegermain(Integer codeintegermain) {
        this.codeintegermain = codeintegermain;
    }
    
    @XmlElement
    public Integer getIsactive()
    {
    	return isactive;
    }
    
    public void setIsactive(Integer isactive)
    {
    	this.isactive=isactive;
    }
    
    @XmlElement
    public RefHeaderEntity getRefHeaderId() {
        return refHeaderId;
    }

    public void setRefHeaderId(RefHeaderEntity refHeaderId) {
        this.refHeaderId = refHeaderId;
    }

    @XmlElement
    public RefHeaderEntity getRefHeaderIdMain() {
        return refHeaderIdMain;
    }

    public void setRefHeaderIdMain(RefHeaderEntity refHeaderIdMain) {
        this.refHeaderIdMain = refHeaderIdMain;
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
  	    
  	    ReferenceEntity cast = (ReferenceEntity) other;
  	    
  	    if (this.id == null) return false;
  	       
  	    if (cast.getId() == null) return false;       
  	       
  	    if (this.id.intValue() != cast.getId().intValue())
  	    	return false;
  	    
  	    return true;
    }

    @Override
    public String toString() {
        return this.name;
    }

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
    
}