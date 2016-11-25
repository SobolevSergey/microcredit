package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * условия для списков
 */
public class BusinessListCondEntity  extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2551513005231893344L;
	protected Integer txVersion = 0;
  
    /**
     * название
     */
    private String Name;	
    /**
     * список
     */
	private BusinessListEntity list;	
	/**
	 * присоединенный список
	 */
	private BusinessListEntity LinkedList;
	/**
	 * присоединенное условие
	 */
	private BusinessListCondEntity LinkedCond;
	/**
	 * порядок сортировки
	 */
	private Integer OrderNo;
	/**
	 * как соединяем
	 */
	private String JoinCode;
	/**
	 * текст
	 */
	private String Body;
	
    public BusinessListCondEntity(){
    	
    }
    
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public BusinessListEntity getList() {
		return list;
	}

	public void setList(BusinessListEntity list) {
		this.list = list;
	}

	public BusinessListEntity getLinkedList() {
		return LinkedList;
	}

	public void setLinkedList(BusinessListEntity linkedList) {
		LinkedList = linkedList;
	}

	public BusinessListCondEntity getLinkedCond() {
		return LinkedCond;
	}

	public void setLinkedCond(BusinessListCondEntity linkedCond) {
		LinkedCond = linkedCond;
	}

	public Integer getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(Integer orderNo) {
		OrderNo = orderNo;
	}

	public String getJoinCode() {
		return JoinCode;
	}

	public void setJoinCode(String joinCode) {
		JoinCode = joinCode;
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String body) {
		Body = body;
	}	
}
