package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class Requests extends BaseTransfer<RequestsEntity> implements Serializable, Initializable,Identifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6478089529117411542L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Requests.class.getConstructor(RequestsEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final int CALL_SYNC=0;
	public static final int CALL_ASYNC=1;
	
	public static final String TYPE_LOGIN="Login";
    public static final String TYPE_REQUEST="Request";
    public static final String TYPE_ANSWER="Answer";
    public static final String TYPE_LOGOUT="Logout";
    
    //для ОКБ - код возврата
    public static final int RESPONSE_CODE_CAIS_OK=0;
	
    //для Эквифакс - коды возврата
    public static final int RESPONSE_CODE_EQUIFAX_OK=1;
    public static final int RESPONSE_CODE_EQUIFAX_NOT_FOUND=3;
    
    //для НБКИ - код ошибки, что человек не найден
    public static final int ERROR_CODE_NBKI_NOT_FOUND=1;
    
    //статус для запроса информации РС
    public final static int RS_REPORT_DETAILS=0;
    public final static int RS_PAYMENT_INFO=3;
    
    //вид запроса в ОКБ
    public final static int FUNCTION_CAIS=50;
    public final static int FUNCTION_CAIS_CONSUMER=52;
    public final static int FUNCTION_CAIS_EXTENDED=40;
    public final static int FUNCTION_CAIS_SCORING=58;
    //причина запроса
    public final static String REASON_CAIS="02";
    public final static String REASON_CAIS_APPLICATION="01";
    public final static String REASON_CBR="231";
    
    
    private RequestStatus requestStatus;
    private CreditRequest creditRequest;
    protected Partner partner;
    protected PeopleMain peopleMain;
    
    public Requests() {
    	super();
    	entity = new RequestsEntity();
    }
    
    public Requests(RequestsEntity value) {
    	super(value);
    	if (entity.getRequeststatus()!=null) {
    	   requestStatus=new RequestStatus(entity.getRequeststatus());
    	}
    	creditRequest=new CreditRequest(entity.getCreditRequestId());
    	partner=new Partner(entity.getPartnersId());
    	if (entity.getPeopleMainId()!=null){
    	    peopleMain=new PeopleMain(entity.getPeopleMainId());
    	}
    }
      
    public Integer getId(){
    	return entity.getId();
    }
    
    public void setId(Integer id){
    	entity.setId(id);
    }
    
    public String getRequestGuid() {
        return entity.getRequestquid();
    }

    public void setRequestGuid(String requestquid) {
        entity.setRequestquid(requestquid);
    }

    public Integer getRequestNumber() {
        return entity.getRequestNumber();
    }

    public void setRequestNumber(Integer requestNumber) {
        entity.setRequestNumber(requestNumber);
    }
    
    public Date getRequestDate() {
        return entity.getRequestdate();
    }

    public void setRequestDate(Date requestdate) {
        entity.setRequestdate(requestdate);
    }

    public Short getRequestType() {
        return entity.getRequesttype();
    }

    public void setRequestType(Short requesttype) {
        entity.setRequesttype(requesttype);
    }

    public Date getResponseDate() {
        return entity.getResponsedate();
    }

    public void setResponseDate(Date responsedate) {
        entity.setResponsedate(responsedate);
    }

    public byte[] getRequestBody() {
        return entity.getRequestbody();
    }

    public void setRequestBody(byte[] requestbody) {
        entity.setRequestbody(requestbody);
    }

    public byte[] getResponseBody() {
        return entity.getResponsebody();
    }

    public void setResponseBody(byte[] responsebody) {
        entity.setResponsebody(responsebody);
    }

    public String getResponseCode() {
        return entity.getResponsecode();
    }

    public void setResponseCode(String responsecode) {
        entity.setResponsecode(responsecode);
    }

    public String getResponseMessage() {
        return entity.getResponsemessage();
    }

    public void setResponseMessage(String responsemessage) {
        entity.setResponsemessage(responsemessage);
    }

    public String getResponseGuid() {
        return entity.getResponseguid();
    }

    public void setResponseGuid(String responseguid) {
        entity.setResponseguid(responseguid);
    }

    public String getResponseBodyString() {
        return entity.getResponsebodystring();
    }

    public void setResponseBodyString(String responsebodystring) {
        entity.setResponsebodystring(responsebodystring);
    }
    
    public String getSessionGuid() {
        return entity.getSessionguid();
    }

    public void setSessionGuid(String sessionguid) {
        entity.setSessionguid(sessionguid);
    }
    
    public Integer getPackageId(){
    	return entity.getPackageId();
    }
    
    public void setPackageId(Integer packageId){
    	entity.setPackageId(packageId);
    }
    
    public RequestStatus getRequestStatus(){
    	return requestStatus;
    }
    
    public void setRequestStatus(RequestStatus requestStatus){
    	this.requestStatus=requestStatus;
    }
    
    public CreditRequest getCreditRequest(){
    	return creditRequest;
    }
    
    public void setCreditRequest(CreditRequest creditRequest){
    	this.creditRequest=creditRequest;
    }
    
    public Partner getPartner() {
        return partner;
    }


    public void setPartner(Partner partner) {
        this.partner=partner;
    }
    
    public PeopleMain getPeopleMain() {
        return peopleMain;
    }


    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
    }
    
	@Override
	public void init(Set options) {
	    entity.getRequestdate();
	}

	public String getDescription(){
		return entity.getDescription();
	}

	
}
