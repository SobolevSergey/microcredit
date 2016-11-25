package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * пункты списков
 */
public class BusinessListItemEntity  extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4304614588799914603L;
	protected Integer txVersion = 0;
	/**
	 * список
	 */
	private BusinessListEntity list;
	/**
	 * id объекта
	 */
	private Integer BusinessObjectId;
	
	public BusinessListEntity getList() {
		return list;
	}
	public void setList(BusinessListEntity list) {
		this.list = list;
	}
	public Integer getBusinessObjectId() {
		return BusinessObjectId;
	}
	public void setBusinessObjectId(Integer businessObjectId) {
		BusinessObjectId = businessObjectId;
	}
}
