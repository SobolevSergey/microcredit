package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
/**
 * Верификация
 */
public class VerificationEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	 * текст верификации
	 */
	private String verificationText;
	/**
	 * текст валидации
	 */
	private String validationText;
	/**
	 * оценка верификации
	 */
	private Double verificationScore;
	/**
	 * оценка валидации
	 */
	private Double validationScore;
	/**
	 * справочник верификации
	 */
	private VerificationMarkEntity verificationMark;
	/**
	 * справочник валидации
	 */
	private VerificationMarkEntity validationMark;
	/**
	 * верификация ПД
	 */
	private Integer verifyPersonal;
	/**
	 * верификация документа
	 */
	private Integer verifyDocument;
	/**
	 * верификация адреса
	 */
	private Integer verifyAddress;
	/**
	 * верификация телефона
	 */
	private Integer verifyPhone;
	/**
	 * верификация ИНН
	 */
	private Integer verifyInn;
	/**
	 * дата верификации
	 */
	private Date verificationDate;
	
	public VerificationEntity(){
		
	}

	public VerificationEntity(Integer id){
		this.id=id;
	}
	
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getVerificationScore(){
		return verificationScore;
	}
	
	public void setVerificationScore(Double verificationScore){
		this.verificationScore=verificationScore;
	}
	
	public Double getValidationScore(){
		return validationScore;
	}
	
	public void setValidationScore(Double validationScore){
		this.validationScore=validationScore;
	}
	
	public VerificationMarkEntity getVerificationMark(){
		return verificationMark;
	}
		
	public void setVerificationMark(VerificationMarkEntity verificationMark){
		this.verificationMark=verificationMark;
	}
		
	public VerificationMarkEntity getValidationMark(){
		return validationMark;
	}
		
	public void setValidationMark(VerificationMarkEntity validationMark){
		this.validationMark=validationMark;
	}
		
	public String getVerificationText()	{
		return verificationText;
	}
	
	public void setVerificationText(String verificationText){
		this.verificationText=verificationText;
	}
	
	public String getValidationText()	{
		return validationText;
	}
	
	public void setValidationText(String validationText){
		this.validationText=validationText;
	}
	
	
    public Integer getVerifyPersonal() {
		return verifyPersonal;
	}

	public void setVerifyPersonal(Integer verifyPersonal) {
		this.verifyPersonal = verifyPersonal;
	}

	public Integer getVerifyDocument() {
		return verifyDocument;
	}

	public void setVerifyDocument(Integer verifyDocument) {
		this.verifyDocument = verifyDocument;
	}

	public Integer getVerifyAddress() {
		return verifyAddress;
	}

	public void setVerifyAddress(Integer verifyAddress) {
		this.verifyAddress = verifyAddress;
	}

	public Integer getVerifyPhone() {
		return verifyPhone;
	}

	public void setVerifyPhone(Integer verifyPhone) {
		this.verifyPhone = verifyPhone;
	}

	public Integer getVerifyInn() {
		return verifyInn;
	}

	public void setVerifyInn(Integer verifyInn) {
		this.verifyInn = verifyInn;
	}

	public Date getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(Date verificationDate) {
		this.verificationDate = verificationDate;
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
	    
	    VerificationEntity cast = (VerificationEntity) other;
	    
	    if (this.id == null) return false;
	       
	    if (cast.getId() == null) return false;       
	       
	    if (this.id.intValue() != cast.getId().intValue())
	    	return false;
	    
	    return true;
   }
}
