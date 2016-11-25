
package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * запросы к партнерам
 */
public class RequestsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Integer txVersion = 0;
    private Integer id;
    /**
     * номер запроса
     */
    private String requestquid;
    /**
     * дата запроса
     */
    private Date requestdate;
    /**
     * тип запроса
     */
    private Short requesttype;
    /**
     * дата ответа
     */
    private Date responsedate;
    /**
     * запрос
     */
    private byte[] requestbody;
    /**
     * ответ
     */
    private byte[] responsebody;
    /**
     * код ответа
     */
    private String responsecode;
    /**
     * сообщение ответа
     */
    private String responsemessage;
    /**
     * ответ строкой
     */
    private String responsebodystring;
    /**
     * номер ответа
     */
    private String responseguid;
    /**
     * номер сессии запроса
     */
    private String sessionguid;
    /**
     * номер пакета
     */
    private Integer packageId;
    /**
     * статус запроса
     */
    private RequestStatusEntity requeststatus;
    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;
    /**
     * источник информации - партнер
     */
    private PartnersEntity partnersId;
    /**
     * номер запроса числовой
     */
    private Integer requestNumber;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    
    public RequestsEntity() {
    }

    public RequestsEntity(Integer id) {
    	this.id=id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(Integer requestNumber) {
		this.requestNumber = requestNumber;
	}

	public String getRequestquid() {
        return requestquid;
    }

    public void setRequestquid(String requestquid) {
        this.requestquid = requestquid;
    }

    public Date getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(Date requestdate) {
        this.requestdate = requestdate;
    }

    public Short getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(Short requesttype) {
        this.requesttype = requesttype;
    }

    public Date getResponsedate() {
        return responsedate;
    }

    public void setResponsedate(Date responsedate) {
        this.responsedate = responsedate;
    }

    public byte[] getRequestbody() {
        return requestbody;
    }

    public void setRequestbody(byte[] requestbody) {
        this.requestbody = requestbody;
    }

    public byte[] getResponsebody() {
        return responsebody;
    }

    public void setResponsebody(byte[] responsebody) {
        this.responsebody = responsebody;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getResponsemessage() {
        return responsemessage;
    }

    public void setResponsemessage(String responsemessage) {
        this.responsemessage = responsemessage;
    }

    public String getResponseguid() {
        return responseguid;
    }

    public void setResponseguid(String responseguid) {
        this.responseguid = responseguid;
    }

    public String getResponsebodystring() {
        return responsebodystring;
    }

    public void setResponsebodystring(String responsebodystring) {
        this.responsebodystring = responsebodystring;
    }
    
    public String getSessionguid() {
        return sessionguid;
    }

    public void setSessionguid(String sessionguid) {
        this.sessionguid = sessionguid;
    }
    
    public RequestStatusEntity getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(RequestStatusEntity requeststatus) {
        this.requeststatus = requeststatus;
    }

    public CreditRequestEntity getCreditRequestId()
    {
    	return creditRequestId;
    }
    
    public void setCreditRequestId(CreditRequestEntity creditRequestId)
    {
    	this.creditRequestId=creditRequestId;
    }
    
    public PartnersEntity getPartnersId() {
        return partnersId;
    }


    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }
    
    public Integer getPackageId() {
    	return packageId;
    }
    
    public void setPackageId(Integer packageId) {
    	this.packageId=packageId;
    }
    
    public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (requestquid != null ? requestquid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
    	if (other == null) return false;
	       
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    RequestsEntity cast = (RequestsEntity) other;
	    
	    if (this.id == null) return false;
	       
	    if (cast.getId() == null) return false;       
	       
	    if (this.id.intValue() != cast.getId().intValue())
	    	return false;
	    
	    return true;
    }

    @Override
    public String toString() {
        return this.requestquid;
    }
    
    public String getDescription(){
    	return "запрос № "+this.getRequestquid()+" от "+this.getRequestdate().toString()+" к партнеру "+this.partnersId.getRealname();
    }
}
