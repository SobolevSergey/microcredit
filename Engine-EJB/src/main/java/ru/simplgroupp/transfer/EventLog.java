package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.EventLogEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class EventLog extends BaseTransfer<EventLogEntity> implements Serializable, Initializable,Identifiable {
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5371661073794155234L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = EventLog.class.getConstructor(EventLogEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected EventCode eventCode;
    protected EventType eventType;
    protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Credit credit;
	
	protected Users user;

    public EventLog() {
        super();
        entity = new EventLogEntity();
    }

    public EventLog(EventLogEntity entity) {
        super(entity);
        eventCode = new EventCode(entity.getEventcodeid());
        eventType = new EventType(entity.getEventtypeid());
        if (entity.getPeopleMainId()!=null){
            peopleMain=new PeopleMain(entity.getPeopleMainId());
        }
        if (entity.getCreditRequestId()!=null){
        	creditRequest=new CreditRequest(entity.getCreditRequestId());
        }
        if (entity.getCreditId()!=null){
        	credit=new Credit(entity.getCreditId());
        }
      
        if (entity.getUserId() != null) {
        	user=new Users(entity.getUserId());
        }
    }

    @Override
    public void init(Set options) {
    
    }
    
    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public Date getEventTime() {
        return entity.getEventtime();
    }

    @XmlElement
    public void setEventTime(Date eventtime) {
        entity.setEventtime(eventtime);
    }

    public String getDescription() {
        return entity.getDescription();
    }

    @XmlElement
    public void setDescription(String description) {
        entity.setDescription(description);
    }
    
    public EventCode getEventCode() {
        return eventCode;
    }

    @XmlElement
    public void setEventCode(EventCode eventcode) {
        this.eventCode = eventcode;
    }

    public EventType getEventType() {
        return eventType;
    }

    @XmlElement
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    
    public String getIpaddress() {
        return entity.getIpaddress();
    }

    @XmlElement
    public void setIpaddress(String ipaddress) {
        entity.setIpaddress(ipaddress);
    }    
    
    public String getBrowser() {
        return entity.getBrowser();
    }

    @XmlElement
    public void setBrowser(String browser) {
        entity.setBrowser(browser);
    }    
    
    public String getOs() {
        return entity.getOs();
    }

    @XmlElement
    public void setOs(String os) {
        entity.setOs(os);
    }    
    
    public String getUserAgent() {
        return entity.getUserAgent();
    }

    @XmlElement
    public void setUserAgent(String userAgent) {
        entity.setUserAgent(userAgent);
    }    
    
    public String getProvider() {
        return entity.getProvider();
    }

    @XmlElement
    public void setProvider(String provider) {
        entity.setProvider(provider);
    }    
    
    public String getGeoPlace() {
        return entity.getGeoPlace();
    }

    @XmlElement
    public void setGeoPlace(String geoPlace) {
        entity.setGeoPlace(geoPlace);
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
    
    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit=credit;
    }
    
    public Users getUser() {
    	return user;
    }
    
    @XmlElement
    public void setUser(Users user) {
    	this.user=user;
    }
    
}
