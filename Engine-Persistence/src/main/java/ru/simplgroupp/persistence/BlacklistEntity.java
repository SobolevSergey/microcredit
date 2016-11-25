
package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;


/**
 * Черные списки
 */
public class BlacklistEntity extends BaseEntity implements Serializable {

   
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5357060066981153758L;
	protected Integer txVersion = 0;
   
    /**
     * дата начала
     */
    private Date databeg;
    /**
     * дата окончания
     */
    private Date dataend;
    /**
     * комментарий
     */
    private String comment;
    /**
     * причина
     */
    private ReferenceEntity reasonId;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    
    public BlacklistEntity() {
    }
    
  
    public Date getDatabeg() {
        return databeg;
    }

    public void setDatabeg(Date databeg) {
        this.databeg = databeg;
    }

    public Date getDataend() {
        return dataend;
    }

    public void setDataend(Date dataend) {
        this.dataend = dataend;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReferenceEntity getReasonId() {
        return reasonId;
    }

    public void setReasonId(ReferenceEntity reasonId) {
        this.reasonId = reasonId;
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

   
}
