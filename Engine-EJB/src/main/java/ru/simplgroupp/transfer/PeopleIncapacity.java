package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.PeopleIncapacityEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class PeopleIncapacity extends BaseTransfer<PeopleIncapacityEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -377315087832869285L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = PeopleIncapacity.class.getConstructor(PeopleIncapacityEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
    private Reference incapacity;
    private PeopleMain peopleMain;
    protected Partner partner;
    protected CreditRequest creditRequest;
    
    public PeopleIncapacity(){
    	super();
    	entity=new PeopleIncapacityEntity();
    }
    
    public PeopleIncapacity(PeopleIncapacityEntity value){
    	super(value);
    	peopleMain=new PeopleMain(entity.getPeopleMainId());
    	partner = new Partner(entity.getPartnersId());
    	if (entity.getCreditRequestId()!=null) {
    		creditRequest=new CreditRequest(entity.getCreditRequestId());
    	}
    	if (entity.getIncapacityId()!=null){
    		incapacity=new Reference(entity.getIncapacityId());
    	}
    }
    
    public Integer getId() {
        return entity.getId();
    }
    
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public Date getIncapacityDate() {
        return entity.getIncapacityDate();
    }

    @XmlElement
    public void setIncapacityDate(Date incapacityDate) {
        entity.setIncapacityDate(incapacityDate);;
    }

	public Reference getIncapacity() {
		return incapacity;
	}

	public void setIncapacity(Reference incapacity) {
		this.incapacity = incapacity;
	}

	public PeopleMain getPeopleMain() {
		return peopleMain;
	}

	public void setPeopleMain(PeopleMain peopleMain) {
		this.peopleMain = peopleMain;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public CreditRequest getCreditRequest() {
		return creditRequest;
	}

	public void setCreditRequest(CreditRequest creditRequest) {
		this.creditRequest = creditRequest;
	}
    
	@Override
	public void init(Set options) {
			
	}
}
