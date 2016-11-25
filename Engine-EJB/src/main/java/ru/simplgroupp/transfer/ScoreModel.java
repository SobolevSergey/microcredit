package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.ScoreCronosMainEntity;
import ru.simplgroupp.persistence.ScoreModelEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class ScoreModel extends BaseTransfer<ScoreModelEntity> implements Serializable, Initializable {

	
	private static final long serialVersionUID = 1L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = ScoreModel.class.getConstructor(ScoreModelEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
    protected Partner partner;
    
    public ScoreModel() {
    	super();
    	entity = new ScoreModelEntity();
    }
    
    public ScoreModel(ScoreModelEntity value) {
    	super(value);
    	partner=new Partner(entity.getPartnersId());
    }
    
    public Integer getId() {
        return entity.getId();
    }
    
    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
        entity.setName(name);
    }    
    
    public ScoreModelEntity getEntity() {
    	return entity;
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
	    entity.getName();
		
	}
	

}
