package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;




import ru.simplgroupp.persistence.OfficialDocumentsEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class OfficialDocuments extends BaseTransfer<OfficialDocumentsEntity> implements Serializable, Initializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4921416411297873255L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = OfficialDocuments.class.getConstructor(OfficialDocumentsEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
    
    public static final int AGREEMENT_PD=1;
    public static final int OFERTA_CREDIT=2;
    public static final int OFERTA_PROLONG=3;
    public static final int OFERTA_REFINANCE=4;
    
    protected Reference documentType;
    protected PeopleMain peopleMain;
    protected CreditRequest creditRequest;
    protected Credit credit;
    
    public OfficialDocuments() {
		super();
		entity = new OfficialDocumentsEntity();
	}

	public OfficialDocuments(OfficialDocumentsEntity entity) {
		super(entity);
		if (entity.getDocumentTypeId()!=null){
		    documentType = new Reference(entity.getDocumentTypeId());
		}
		peopleMain=new PeopleMain(entity.getPeopleMainId());
		if (entity.getCreditRequestId()!=null){
			creditRequest=new CreditRequest(entity.getCreditRequestId());
		}
		if (entity.getCreditId()!=null){
			credit=new Credit(entity.getCreditId());
		}
	}
	
	 public Integer getId() {
	    return entity.getId();
	 }
 
	 public void setId(Integer id) {
	    entity.setId(id);
	 }
	 
	 public String getDocNumber() {
	    return entity.getDocNumber();
	 }

	 public void setDocNumber(String docNumber) {
	   	entity.setDocNumber(docNumber);
	 }

	 public Date getDocDate() {
        return entity.getDocDate();
     }

	 public void setDocDate(Date docdate) {
    	entity.setDocDate(docdate);
     }

     public Date getDateChange() {
	    return entity.getDateChange();
	 }

	 public void setDateChange(Date dateChange) {
	   	entity.setDateChange(dateChange);
	 }
	 
	 public String getSmsCode() {
	    return entity.getSmsCode();
	 }

	 public void setSmsCode(String smsCode) {
	   	entity.setSmsCode(smsCode);
	 }

	 public Integer getIsActive() {
	    return entity.getIsActive();
	 }

     public void setIsActive(Integer isActive) {
	   	entity.setIsActive(isActive);
	 }

	 public String getDocText() {
	     return entity.getDocText();
	 }

     public void setDocText(String docText) {
	     entity.setDocText(docText);
	 }
     
     public Integer getAnotherId() {
 	    return entity.getAnotherId();
 	 }

     public void setAnotherId(Integer anotherId) {
 	   	entity.setAnotherId(anotherId);
 	 }

	public Reference getDocumentType() {
		return documentType;
	}

	public void setDocumentType(Reference documentType) {
		this.documentType = documentType;
	}

	public PeopleMain getPeopleMain() {
		return peopleMain;
	}

	public void setPeopleMain(PeopleMain peopleMain) {
		this.peopleMain = peopleMain;
	}

	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}
    
	public CreditRequest getCreditRequest() {
		return creditRequest;
	}

	public void setCreditRequest(CreditRequest creditRequest) {
		this.creditRequest = creditRequest;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	@Override
	public Boolean equalsContent(OfficialDocumentsEntity entity){
		return Utils.equalsNull(this.getSmsCode(),entity.getSmsCode())
				&&Utils.equalsNull(this.getDocNumber(),entity.getDocNumber())
				&&Utils.equalsNull(this.getDocText(),entity.getDocText())
				&&Utils.equalsNull(this.getDocumentType()!=null?this.getDocumentType().getId():null,entity.getDocumentTypeId()!=null?entity.getDocumentTypeId():null);
	}
     
}
