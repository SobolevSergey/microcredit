package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * вид бизнес-действия
 */
public class BizActionTypeEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 322248106977837607L;

	protected Integer txVersion = 0;
	private Integer id;
	/**
	 * название
	 */
	private String name;
	
	public BizActionTypeEntity(){
		
	}
    public BizActionTypeEntity(Integer id){
		this.id=id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
  
}
