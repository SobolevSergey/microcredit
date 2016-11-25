package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseAddress extends BaseTransfer<AddressEntity> implements Serializable, Initializable {
    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 702124447507861185L;
	
	public static final int REGISTER_ADDRESS = 0;
	public static final int RESIDENT_ADDRESS = 1;
	public static final int WORKING_ADDRESS = 2;
	public static final int REGISTER_ADDRESS_OKB = 2;
	public static final int RESIDENT_ADDRESS_OKB = 1;
	public static final int WORKING_ADDRESS_OKB = 6;
	public static final int REGISTER_ADDRESS_RS = 1;
	public static final int RESIDENT_ADDRESS_RS = 2;
	public static final String REGISTER_ADDRESS_RS_TEXT = "Адрес регистрации по месту жительства";
	public static final String RESIDENT_ADDRESS_RS_TEXT = "Адрес проживания";
	public static final int IS_SAME=1;
	public static final String COUNTRY_RUSSIA="RU";
	public static final String COUNTRY_RUSSIA_CODE="643";
    public final static String COUNTRY_KAZAKHSTAN = "KZ";
	protected Reference addrType;
	protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
	

    public BaseAddress() {
        super();
        entity = new AddressEntity();
    }
    
    public BaseAddress(AddressEntity entity) {
        super(entity);
        addrType = new Reference(entity.getAddrtype());
        peopleMain=new PeopleMain(entity.getPeopleMainId());
        partner=new Partner(entity.getPartnersId());
        if (entity.getCreditRequestId()!=null){
          creditRequest=new CreditRequest(entity.getCreditRequestId());
        }
    }
    
    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getIndex() {
        return entity.getIndex();
    }

    public void setIndex(String index) {
        entity.setIndex(index);
    }
    
	public boolean isIsSame() {
		if (entity == null || entity.getIsSame() == null) {
			return false;
		} else {
			return new Boolean(entity.getIsSame() == 1);
		}
	}
	
	public boolean isSame() {
		if (entity == null || entity.getIsSame() == null) {
			return false;
		} else {
			return new Boolean(entity.getIsSame() == 1);
		}		
	}
	
	public void setSame(boolean value) {
		if (entity == null) {
			return;
		}

		if (value)
			entity.setIsSame(1);
		else
			entity.setIsSame(0);

	}
	
	
	public void setIsSame(boolean value) {
		if (entity == null) {
			return;
		}

		if (value)
			entity.setIsSame(1);
		else
			entity.setIsSame(0);

	}
    
    public Reference getAddrtype() {
        return addrType;
    }

    public void setAddrType(Reference addrtype) {
        this.addrType = addrtype;
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


    public void setPartner(Partner partner) {
        this.partner=partner;
    }
    
    public Date getDatabeg() {
        return entity.getDatabeg();
    }

    public void setDatabeg(Date databeg) {
        entity.setDatabeg(databeg);
    }

    public Date getDataend() {
        return entity.getDataend();
    }

    public void setDataend(Date dataend) {
        entity.setDataend(dataend);
    }
    
    public Integer getIsActive() {
        return entity.getIsactive();
    }

    public void setIsActive(Integer isactive) {
        entity.setIsactive(isactive);
    }
    
    public String getDescription() {
    	return entity.getDescription();
    }
    
    public String getDescriptionFull() {
    	return entity.getDescriptionFull();
    }
    
    public String getDescriptionFromStreet(){
    	return entity.getDescriptionFromStreet();
    }
    
    public String getDescriptionToStreet(){
    	return entity.getDescriptionToStreet();
    }
    
    public String getDescriptionToCity(){
    	return entity.getDescriptionToCity();
    }
}
