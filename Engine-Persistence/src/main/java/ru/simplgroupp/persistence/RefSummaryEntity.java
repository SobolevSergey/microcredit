package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * Справочник для суммарной информации по кредиту
 */
public class RefSummaryEntity extends BaseEntity implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6926665175956901160L;
	protected Integer txVersion = 0;
	private Integer id;
	/**
	 * название
	 */
	private String name;
	/**
	 * описание
	 */
	private String description;
	/**
	 * активная запись или нет
	 */
	private Integer isActive;
	/**
	 * тип данных
	 */
	private ReferenceEntity datatype;
	
	public RefSummaryEntity(){
		
	}
	
	public RefSummaryEntity(Integer id){
		this.id=id;
	}
	
	public Integer getId() {
        return id;
    }

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName(){
    	return name;
    }
    
    public void setName(String name){
    	this.name=name;
    }
    
    public String getDescription(){
    	return description;
    }
    
    public void setDescription(String description){
    	this.description=description;
    }
    
    public ReferenceEntity getDatatype() {
        return datatype;
    }

    public void setDatatype(ReferenceEntity datatype) {
        this.datatype = datatype;
    }
    
    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
    
  
}
