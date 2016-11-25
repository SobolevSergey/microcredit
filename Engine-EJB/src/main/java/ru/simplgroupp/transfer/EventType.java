package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.EventTypeEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class EventType implements Serializable, Initializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -6373184757799355949L;
	public static final int INFO = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    
    protected EventTypeEntity entity;

    public EventType() {
        super();
        entity = new EventTypeEntity();
    }
    
    public EventType(EventTypeEntity entity) {
        super();
        this.entity = entity;
    }
    
    public EventType(int id) {
        super();
        this.entity = new EventTypeEntity();
        entity.setId(id);
    }    

    @Override
    public void init(Set options) {
        entity.getName();
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

    public String getShortName() {
        return entity.getShortname();
    }

    @XmlElement
    public void setShortName(String shortname) {
        entity.setShortname(shortname);
    }

    public EventTypeEntity getEntity() {
        return entity;
    }
    
}
