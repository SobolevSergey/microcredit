package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.VerificationEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Verification extends BaseTransfer<VerificationEntity> implements Serializable, Initializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8675962485032907196L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Verification.class.getConstructor(VerificationEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final int CODE_VALIDATION=1;
	public static final int CODE_VERIFICATION=2;
	
    protected VerificationMark verificationMark;
    protected VerificationMark validationMark;
    protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
	
	public Verification(){
		super();
		entity = new VerificationEntity();
	}
	
	public Verification(VerificationEntity entity){
		
		super(entity);
		peopleMain=new PeopleMain(entity.getPeopleMainId());
		partner=new Partner(entity.getPartnersId());
		creditRequest=new CreditRequest(entity.getCreditRequestId());
		
		if (entity.getVerificationMark() != null) {
        	verificationMark = new VerificationMark(entity.getVerificationMark());
        }
		if (entity.getValidationMark() != null) {
        	validationMark = new VerificationMark(entity.getValidationMark());
        }
	}
	
	public Integer getId() {
	     return entity.getId();
	 }

	@XmlElement
	 public void setId(Integer id) {
	     entity.setId(id);
	 }
	 
	public PeopleMain getPeopleMain() {
	      return peopleMain;
	}


	public void setPeopleMain(PeopleMain peopleMain) {
	      this.peopleMain=peopleMain;
	}
	    
	public CreditRequest getCreditRequest() {
	      return creditRequest;
	}


	public void setCreditRequest(CreditRequest creditRequest) {
	      this.creditRequest=creditRequest;
	}
	    
	public Partner getPartner() {
	     return partner;
	}

	@XmlElement
    public void setPartner(Partner partner) {
        this.partner=partner;
    }
	    
    public VerificationMark getVerificationMark(){
    	return verificationMark;
    }
    
    public VerificationMark getValidationMark(){
    	return validationMark;
    }
    
    public String getVerificationText(){
    	return entity.getVerificationText();
    }
    
    @XmlElement
    public void setVerificationText(String verificationText){
    	entity.setVerificationText(verificationText);
    }
        
    public String getValidationText(){
    	return entity.getValidationText();
    }
    
    @XmlElement
    public void setValidationText(String validationText){
    	entity.setValidationText(validationText);
    }
    
    public Double getVerificationScore(){
    	return entity.getVerificationScore();
    }
    
    @XmlElement
    public void setVerificationScore(Double verificationScore){
    	entity.setVerificationScore(verificationScore);
    }
    
    public Double getValidationScore(){
    	return entity.getValidationScore();
    }
    
    @XmlElement
    public void setValidationScore(Double validationScore){
    	entity.setValidationScore(validationScore);
    }
    
    public Integer getVerifyPersonal(){
    	return entity.getVerifyPersonal();
    }
    
    @XmlElement
    public void setVerifyPersonal(Integer verifyPersonal){
    	entity.setVerifyPersonal(verifyPersonal);
    }
    
    public Integer getVerifyDocument(){
    	return entity.getVerifyDocument();
    }
    
    @XmlElement
    public void setVerifyDocument(Integer verifyDocument){
    	entity.setVerifyDocument(verifyDocument);
    }
    
    public Integer getVerifyPhone(){
    	return entity.getVerifyPhone();
    }
    
    @XmlElement
    public void setVerifyPhone(Integer verifyPhone){
    	entity.setVerifyPhone(verifyPhone);
    }
    
    public Integer getVerifyAddress(){
    	return entity.getVerifyAddress();
    }
    
    @XmlElement
    public void setVerifyAddress(Integer verifyAddress){
    	entity.setVerifyAddress(verifyAddress);
    }
    
    public Integer getVerifyInn(){
    	return entity.getVerifyInn();
    }
    
    @XmlElement
    public void setVerifyInn(Integer verifyInn){
    	entity.setVerifyInn(verifyInn);
    }
    
    public Date getVerificationDate(){
    	return entity.getVerificationDate();
    }
    
    @XmlElement
    public void setVerificationDate(Date verificationDate){
    	entity.setVerificationDate(verificationDate);
    }
    
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
