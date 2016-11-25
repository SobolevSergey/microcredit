package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * списки
 *
 */
public class BusinessListEntity  extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3256455133674701699L;
	protected Integer txVersion = 0;
    /**
     * название
     */
    private String Name;
    /**
     * класс бизнес-объекта
     */
    private String BusinessObjectClass;
    
    /**
     * дополнительные признаки для списка
     */
    private String SubType;
    
    /**
     * создан явно или автоматически (1/0)
     */
    private Integer IsExplicit;
    
    public BusinessListEntity(){
    	
    }
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
    public String getBusinessObjectClass() {
		return BusinessObjectClass;
	}
	public void setBusinessObjectClass(String businessObjectClass) {
		BusinessObjectClass = businessObjectClass;
	}

	public String getSubType() {
		return SubType;
	}

	public void setSubType(String subType) {
		SubType = subType;
	}

	public Integer getIsExplicit() {
		return IsExplicit;
	}

	public void setIsExplicit(Integer isExplicit) {
		IsExplicit = isExplicit;
	}	
}
