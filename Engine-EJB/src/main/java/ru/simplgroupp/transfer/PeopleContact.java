package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.toolkit.common.Initializable;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class PeopleContact extends BaseTransfer<PeopleContactEntity> implements Serializable, Initializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8622021440013248305L;
	
	public static final int CONTACT_EMAIL = 1;
    public static final int CONTACT_CELL_PHONE = 2;
    public static final int CONTACT_HOME_PHONE = 3;
    public static final int CONTACT_WORK_PHONE = 4;
    public static final int CONTACT_HOME_REGISTER_PHONE = 5;
    public static final int NETWORK_VK=6;
    public static final int NETWORK_OK=7;
    public static final int NETWORK_MM=8;
    public static final int NETWORK_FB=9;
    public static final int NETWORK_TT=10;
    public static final int CONTACT_DOPPHONE1 = 12;
    public static final int CONTACT_DOPPHONE2 = 13;
    public static final int CONTACT_OTHER=99;
    
    public static final int CELL_PHONE_RS=1;
    
    public static final int CELL_PHONE_NBKI=4;
    public static final int HOME_PHONE_NBKI=2;
    
    public static final int PHONE_COUNTRY_CODE_RU=7;
    
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = PeopleContact.class.getConstructor(PeopleContactEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
    /**
     * вид контакта
     */
    private Reference contact;
    private PeopleMain peopleMain;
    protected Partner partner;
    protected CreditRequest creditRequest;
    /**
     * регион оператора
     */
    protected FMSRegion regionShort;
    
    public PeopleContact(PeopleContactEntity value) {
        super(value);
    	
    	contact=new Reference(entity.getContactId());
    	peopleMain=new PeopleMain(entity.getPeopleMainId());
    	partner = new Partner(entity.getPartnersId());
    	if (entity.getCreditRequestId()!=null) {
    		creditRequest=new CreditRequest(entity.getCreditRequestId());
    	}
        if (entity.getRegionShort()!=null){
         	regionShort=new FMSRegion(entity.getRegionShort());
        }
    }
    
    public PeopleContact() {
    	super();
    	entity = new PeopleContactEntity();
    }
    
    public Integer getId() {
        return entity.getId();
    }
    
    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public Reference getContact() {
        return contact;
    }

    @XmlElement
    public void setContact(Reference contact) {
        this.contact=contact;
        if (this.contact == null) {
        	entity.setContactId(null);
        } else {
        	entity.setContactId(this.contact.getEntity());
        }
    }

    public String getValue() {
        return entity.getValue();
    }

    @XmlElement
    public void setValue(String value) {
        entity.setValue(value);
    }

    public String getOperator() {
        return entity.getOperator();
    }

    @XmlElement
    public void setOperator(String operator) {
        entity.setOperator(operator);
    }
    
    public String getRegion() {
        return entity.getRegion();
    }

    @XmlElement
    public void setRegion(String region) {
        entity.setRegion(region);
    }
    
    public Integer getIsActive() {
        return entity.getIsactive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
        entity.setIsactive(isactive);
    }

    public Boolean getAvailable() {
    	return entity.getAvailable();
    }
    
    @XmlElement
    public void setAvailable(Boolean available) {
    	entity.setAvailable(available);
    }
    
    public Date getDateactual() {
        return entity.getDateactual();
    }

    @XmlElement
    public void setDateactual(Date dateactual) {
        entity.setDateactual(dateactual);
    }

    public PeopleMain getPeopleMain() {
        return peopleMain;
    }

    @XmlElement
    public void setPartner(Partner partner){
    	this.partner=partner;
    }
    
    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
        if (this.peopleMain == null) {
        	entity.setPeopleMainId(null); 
        } else {
        	entity.setPeopleMainId(peopleMain.getEntity());
        }
    }
    
    public CreditRequest getCreditRequest(){
    	return creditRequest;
    }
    
    public void setCreditRequest(CreditRequest creditRequest){
    	this.creditRequest=creditRequest;
    }
    
       
	@Override
	public void init(Set options) {
	    entity.getValue();
		
	}

	public Partner getPartner() {
		return partner;
	}

	public FMSRegion getRegionShort() {
		return regionShort;
	}

	public void setRegionShort(FMSRegion regionShort) {
		this.regionShort = regionShort;
	}

	
}