package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * Суммарная информация по кредиту
 */
public class SummaryEntity implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1312538349170674590L;

	protected Integer txVersion = 0;
	
	private Integer id;
	/**
	 * человек
	 */
	private PeopleMainEntity peopleMainId;
	/**
	 * заявка
	 */
	private CreditRequestEntity creditRequestId;
	/**
	 * партнер
	 */
	private PartnersEntity partnersId;
	/**
	 * значение
	 */
	private String value;
	/**
	 * вид информации по справочнику
	 */
	private RefSummaryEntity fieldRef;
	/**
	 * номер справочника, если информация - справочное значение
	 */
	private Integer refid;
	/**
	 * дата информации
	 */
	private Date summaryDate;
	
    public SummaryEntity(){
		
	}
	
	public SummaryEntity(Integer id){
		this.id=id;
	}
	
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getRefid() {
        return refid;
    }

    public void setRefid(Integer refid) {
        this.refid = refid;
    }
    
    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
       this.creditRequestId = creditRequestId;
    }
    
    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

    public String getValue(){
    	return value;
    }
    
    public void setValue(String value){
    	this.value=value;
    }
    
    public RefSummaryEntity getFieldRef(){
    	return fieldRef;
    }
    
    public void setFieldRef(RefSummaryEntity fieldRef){
    	this.fieldRef=fieldRef;
    }
    
    public Date getSummaryDate() {
		return summaryDate;
	}

	public void setSummaryDate(Date summaryDate) {
		this.summaryDate = summaryDate;
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
	    
	    SummaryEntity cast = (SummaryEntity) other;
	    
	    if (this.id == null) return false;
	       
	    if (cast.getId() == null) return false;       
	       
	    if (this.id.intValue() != cast.getId().intValue())
	    	return false;
	    
	    return true;
   }
}
