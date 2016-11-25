package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * 
 * услуги пунктов Контакта
 *
 */
public class ContactServiceEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Integer txVersion = 0;

	private Integer id;
	private Integer parentId;
	private Integer version;
	private Integer erased;
	private String caption;
	private String comment;
	private String captionLa;
	private String commentLa;
	private Integer canIn;
	private Integer canPay;
	private String csIn;
	private String csInFee;
	private String csPay;

    public ContactServiceEntity() {
    }

    public ContactServiceEntity(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getErased() {
		return erased;
	}

	public void setErased(Integer erased) {
		this.erased = erased;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCaptionLa() {
		return captionLa;
	}

	public void setCaptionLa(String captionLa) {
		this.captionLa = captionLa;
	}

	public String getCommentLa() {
		return commentLa;
	}

	public void setCommentLa(String commentLa) {
		this.commentLa = commentLa;
	}

	public Integer getCanIn() {
		return canIn;
	}

	public void setCanIn(Integer canIn) {
		this.canIn = canIn;
	}

	public Integer getCanPay() {
		return canPay;
	}

	public void setCanPay(Integer canPay) {
		this.canPay = canPay;
	}

	public String getCsIn() {
		return csIn;
	}

	public void setCsIn(String csIn) {
		this.csIn = csIn;
	}

	public String getCsInFee() {
		return csInFee;
	}

	public void setCsInFee(String csInFee) {
		this.csInFee = csInFee;
	}

	public String getCsPay() {
		return csPay;
	}

	public void setCsPay(String csPay) {
		this.csPay = csPay;
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
   	    
   	    ContactServiceEntity cast = (ContactServiceEntity) other;
   	    
   	    if (this.id == null) return false;
   	       
   	    if (cast.getId() == null) return false;       
   	       
   	    if (this.id.intValue() != cast.getId().intValue())
   	    	return false;
    	    
   	    return true;
	}
	
}
