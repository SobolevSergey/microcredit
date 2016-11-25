package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.ScoringEntity;
import ru.simplgroupp.persistence.SpouseEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Spouse extends BaseTransfer<SpouseEntity> implements Serializable, Initializable{

	private static final long serialVersionUID = 1L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Spouse.class.getConstructor(SpouseEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }		
	
	public static final int CODE_SPOUSE = 1;
	   
    protected Reference spouseId;
   
    protected PeopleMain peopleMainSpouse;
    protected PeopleMain peopleMain;
    protected Reference typeWork;
    
    public Spouse()
    {
    	super();
    	entity = new SpouseEntity();
    }
    
    public Spouse(SpouseEntity value)
    {
    	super(value);
    	spouseId=new Reference(entity.getSpouseId());
    	peopleMain=new PeopleMain(entity.getPeopleMainId());
    	peopleMainSpouse=new PeopleMain(entity.getPeopleMainSpouseId());
    	if (entity.getTypeworkId() != null) {
			typeWork = new Reference(entity.getTypeworkId());
    	}
    }
    
    public Integer getId() {
        return entity.getId();
    }
    
    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public Reference getSpouseId() {
        return spouseId;
    }

    @XmlElement
    public void setSpouseId(Reference spouseId) {
        this.spouseId=spouseId;
    }
    
    public PeopleMain getPeopleMain() {
        return peopleMain;
    }


    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
    }
    
    public PeopleMain getPeopleMainSpouse() {
        return peopleMainSpouse;
    }

    @XmlElement
    public void setPeopleMainSpouse(PeopleMain peopleMainSpouse) {
        this.peopleMainSpouse=peopleMainSpouse;
    }
    
    public Integer getIsActive() {
        return entity.getIsactive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
        entity.setIsactive(isactive);
    }

    public Date getDatabeg() {
        return entity.getDatabeg();
    }

    @XmlElement
    public void setDatabeg(Date databeg) {
        entity.setDatabeg(databeg);
    }

    public Date getDataend() {
        return entity.getDataend();
    }

    @XmlElement
    public void setDataend(Date dataend) {
        entity.setDataend(dataend);
    }
    
    public  Reference getTypeWork() {
        return typeWork;
    }

    @XmlElement
    public void setTypeWork( Reference typeWork) {
        this.typeWork = typeWork;
    }
    
	@Override
	public void init(Set options) {
		peopleMainSpouse.init(options);	
	}

}
