package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.codehaus.plexus.util.StringUtils;

import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;

public class PeoplePersonal extends BaseTransfer<PeoplePersonalEntity> implements Serializable, Initializable{

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 227730891377277810L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = PeoplePersonal.class.getConstructor(PeoplePersonalEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public final static int GENDER_MAN=1;
	public final static int GENDER_WOMAN=2;
	
    protected PeopleMain peopleMain;
    protected Country citizen; 
    protected Reference gender;
    protected CreditRequest creditRequest;
    protected Partner partner;
    
    public PeoplePersonal() {
    	super();
    	entity = new PeoplePersonalEntity();
    }
    
    public PeoplePersonal(PeoplePersonalEntity value) {
    	super(value);
    	
    	peopleMain=new PeopleMain(entity.getPeopleMainId());
    	
    	if (entity.getCitizen() == null) {
    		citizen=new Country();
    	} else {
    		citizen=new Country(entity.getCitizen());
    	}
    	if (entity.getGender() == null) {
    		gender = new Reference();
    	} else {
    		gender=new Reference(entity.getGender());
    	}
    	if (entity.getPartnersId() == null) {
    		partner = new Partner();
    	} else {
    		partner = new Partner(entity.getPartnersId());
    	}
    	
    	if (entity.getCreditRequestId()!=null) {
    		creditRequest=new CreditRequest(entity.getCreditRequestId());
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
    
    
    public String getSurname() {
        return entity.getSurname();
    }
    
    @XmlElement
    public void setSurname(String surname) {
        entity.setSurname(surname);
    }

    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
        entity.setName(name);
    }

    public String getMidname() {
        return entity.getMidname();
    }

    @XmlElement
    public void setMidname(String midname) {
        entity.setMidname(midname);
    }

    public String getMaidenname() {
        return entity.getMaidenname();
    }

    @XmlElement
    public void setMaidenname(String maidenname) {
        entity.setMaidenname(maidenname);
    }
    
    public Reference getGender() {
        return gender;
    }

    @XmlElement
    public void setGender(Reference gender) {
        this.gender=gender;
    }

    public Date getBirthDate() {
        return entity.getBirthdate();
    }

    @XmlElement
    public void setBirthDate(Date birthdate) {
        entity.setBirthdate(birthdate);
    }

    public String getBirthPlace() {
        return entity.getBirthplace();
    }

    @XmlElement
    public void setBirthPlace(String birthplace) {
        entity.setBirthplace(birthplace);
    }

    public Integer getIsActive() {
        return entity.getIsactive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
        entity.setIsactive(getIsActive());
    }

    public Boolean getIsUploaded() {
        return entity.getIsUploaded();
    }

    @XmlElement
    public void setIsUploaded(Boolean isUploaded) {
        entity.setIsUploaded(isUploaded);
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

    public Country getCitizen() {
        return citizen;
    }

    @XmlElement
    public void setCitizen(Country citizen) {
        this.citizen=citizen;
    }
    
    public CreditRequest getCreditRequest(){
    	return creditRequest;
    }
    
    public void setCreditRequest(CreditRequest creditRequest){
    	this.creditRequest=creditRequest;
    }
    
	@Override
	public void init(Set options) {
		entity.getSurname();
		
	}

	public Partner getPartner() {
		return partner;
	}

	@XmlElement
	 public void setPartner(Partner partner) {
	        this.partner=partner;
	 }
	 
	public String getDescription(){
		return entity.getDescription();
	}
	
	public Integer getAge(){
		if (this.getBirthDate()==null){
			return 0;
		} else {
		    return DatesUtils.yearsDiffNow(this.getBirthDate());
		}
	}
	
	public Double getAgeDouble(){
		int age=getAge();
		double d=CalcUtils.roundHalfUp((double) age/365.25,2);
		return d;
	}
		
	public String getIO(){
		return entity.getIO();
	}

    public String getFullName() {
        return entity.getFullName();
    }
    
    public String getInitials(){
    	return entity.getInintials();
    }
    
    @Override
    public Boolean equalsContent(PeoplePersonalEntity entity){
    	return  Utils.equalsNull(this.getBirthPlace(), entity.getBirthplace()) 
    			&& Utils.equalsNull(this.getBirthDate(), entity.getBirthdate())
				&& Utils.equalsNull(this.getGender()!=null?this.getGender().getId():null, entity.getGender()!=null?entity.getGender().getId():null)
				&& Utils.equalsNull(this.getMidname(), entity.getMidname()) 
				&& Utils.equalsNull(this.getName(), entity.getName()) 
				&& Utils.equalsNull(this.getSurname(), entity.getSurname());
    }
}