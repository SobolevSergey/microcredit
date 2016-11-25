package ru.simplgroupp.persistence;

import java.io.Serializable;

import ru.simplgroupp.toolkit.common.Identifiable;

/**
 * справочник видов отчета
 */
public class ReportTypeEntity extends BaseEntity implements Serializable, Identifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4548226119597030226L;
	protected Integer txVersion = 0;
	private Integer id;
	private String name;
	
	public ReportTypeEntity(){
		
	}
	
	public ReportTypeEntity(Integer id){
		this.id=id;
	}
	
	public Integer getId() {
		return id;
	}
		
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
  

}
