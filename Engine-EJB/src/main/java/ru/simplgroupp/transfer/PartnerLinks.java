package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.PartnerLinksEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class PartnerLinks extends BaseTransfer<PartnerLinksEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = PartnerLinks.class.getConstructor(PartnerLinksEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
    
	protected Reference referenceId;
	protected Partner partner;
	
	public PartnerLinks(){
		super();
		entity = new PartnerLinksEntity();
	}
	
	public PartnerLinks(PartnerLinksEntity entity){
		super(entity);
		referenceId = new Reference(entity.getReferenceId());
    	partner = new Partner(entity.getPartnersId());
	}
	
	public Integer getId() {
        return entity.getId();
    }
    
	@XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public Integer getIsActive() {
        return entity.getIsActive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
        entity.setIsActive(isactive);
    }
    
    public Reference getReferenceId() {
        return referenceId;
    }

    @XmlElement
    public void setReferenceId(Reference referenceId) {
        this.referenceId = referenceId;
    }

    public Partner getPartner() {
		return partner;
	}
    
    @XmlElement
    public void setPartner(Partner partner) {
        this.partner=partner;
    }
    
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
