package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Заявка на изменение данных кредитной заявки
 */
public class ChangeRequestsEntity extends BaseEntity implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -879834873925367920L;
	protected Integer txVersion = 0;
	 private Integer id;
	 /**
	 * дата заявки
	 */
	 private Date requestDate;
	 /**
	  * описание, что надо поменять
	  */
	 private String description;
	 /**
	  * были ли внесены изменения
	  */
	 private Integer isEdited;
	

	/**
	  * кредитная заявка, куда вносятся изменения
	  */
	 private CreditRequestEntity creditRequestId;
	 /**
	  * пользователь, сделавший запрос
	  */
	 private UsersEntity userId;
	 
	 public ChangeRequestsEntity(){
		 
	 }
	
	 
	 public CreditRequestEntity getCreditRequestId() {
	        return creditRequestId;
	 }

	 public void setCreditRequestId(CreditRequestEntity creditRequestId) {
	        this.creditRequestId = creditRequestId;
	 }
	     
	 public UsersEntity getUserId() {
	    	return userId;
	 }
	    
	 public void setUserId(UsersEntity userId){
	    	this.userId=userId;
	 }
	    
	 public String getDescription() {
	        return description;
	 }

	 public void setDescription(String description) {
	        this.description = description;
	 }
	    
	 public Date getRequestDate() {
	        return requestDate;
	 }

	 public void setRequestDate(Date requestDate) {
	        this.requestDate = requestDate;
	 }
	    
	 public Integer getIsEdited() {
			return isEdited;
	 }
	    
	 public void setIsEdited(Integer isEdited) {
			this.isEdited = isEdited;
	 }
		
	
}
