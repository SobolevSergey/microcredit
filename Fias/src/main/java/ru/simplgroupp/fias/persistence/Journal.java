package ru.simplgroupp.fias.persistence;

import java.io.Serializable;
import java.util.Date;

public class Journal implements Serializable
{
	private static final long serialVersionUID = -4124943558535278903L;
	
	public static final int STATUS_IN_PROGRESS = 1;
	public static final int STATUS_SUCCESS = 2;
	public static final int STATUS_FAILURE = 3;
	
	public static final int OPER_REPLACE = 1;
	public static final int OPER_UPDATE = 2;
	
	private Integer id;
	private Date dateStart;
	private Integer operationType;
	private Date dateFinish;
	private Integer status;
	private String messageText;
  private Integer versionId;
  private String versionText;

	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	       
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    Journal cast = (Journal) other;
	    
	    if (this.id == null) return false;
	       
	    if (cast.getId() == null) return false;       
	       
	    if (this.id.intValue() != cast.getId().intValue())
	    	return false;
	    
	    return true;
	}
	@Override
	public int hashCode() {
		int _hashCode = 0, aid = 0;
	    if (id != null) aid = id.intValue();
	    _hashCode = 29 * _hashCode + (new Long(aid)).hashCode();
	    return _hashCode;
	}	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Integer getOperationType() {
		return operationType;
	}
	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}
	public Date getDateFinish() {
		return dateFinish;
	}
	public void setDateFinish(Date dateFinish) {
		this.dateFinish = dateFinish;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public Integer getVersionId() {
		return versionId;
	}
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	public String getVersionText() {
		return versionText;
	}
	public void setVersionText(String versionText) {
		this.versionText = versionText;
	}
}
