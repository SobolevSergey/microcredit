package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Заголовки справочника
 */
@XmlRootElement
public class RefHeaderEntity extends BaseEntity implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8362212948031020381L;

	protected Integer txVersion = 0;

    private Integer id;
    /**
     * название
     */
    private String name;
    /**
     * источник информации
     */
    private PartnersEntity partnersId;
    /**
     * значения справочника
     */
    private List<ReferenceEntity> references=new ArrayList<>(0);
    
    public RefHeaderEntity() {
    }

    public RefHeaderEntity(Integer id) {
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

   public List<ReferenceEntity> getReferences(){
	   return references;
   }
   
   public void setReferences(List<ReferenceEntity> references){
	   this.references=references;
   }
   
   @Override
   public String toString() {
        return this.name;
   }
    
}
