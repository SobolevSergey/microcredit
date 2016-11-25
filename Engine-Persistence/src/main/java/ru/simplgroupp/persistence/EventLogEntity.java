
package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Лог
 */
public class EventLogEntity extends ExtendedBaseEntity implements Serializable {
      
    /**
	 * 
	 */
	private static final long serialVersionUID = 8697101481425984587L;
	
	protected Integer txVersion = 0;
  
    /**
     * время события
     */
    private Date eventtime;
    /**
     * описание события
     */
    private String description;
    /**
     * ip адрес
     */
    private String ipaddress;
    /**
     * browser клиента
     */
    private String browser;
    /**
     * OS клиента
     */
    private String os;
    /**
     * строка заголовка с компьютера клиента
     */
    private String userAgent;
    /**
     * провайдер клиента
     */
    private String provider;
    /**
     * расположение клиента географическое
     */
    private String geoPlace;
    /**
     * вид события по справочнику
     */
    private EventCodeEntity eventcodeid;
    /**
     * тип события по справочнику
     */
    private EventTypeEntity eventtypeid;
    /**
     * кредит
     */
    private CreditEntity creditId;
    /**
     * пользователь
     */
    private UsersEntity userId;

    public EventLogEntity() {
    }

    public Date getEventtime() {
        return eventtime;
    }

    public void setEventtime(Date eventtime) {
        this.eventtime = eventtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public EventCodeEntity getEventcodeid() {
        return eventcodeid;
    }

    public void setEventcodeid(EventCodeEntity eventcodeid) {
        this.eventcodeid = eventcodeid;
    }

    public EventTypeEntity getEventtypeid() {
        return eventtypeid;
    }

    public void setEventtypeid(EventTypeEntity eventtypeid) {
        this.eventtypeid = eventtypeid;
    }
    
    public CreditEntity getCreditId() {
        return creditId;
    }

    public void setCreditId(CreditEntity creditId) {
        this.creditId = creditId;
    }
    
    public UsersEntity getUserId(){
    	return userId;
    }
    
    public void setUserId(UsersEntity userId){
    	this.userId=userId;
    }

    @Override
    public String toString() {
        return "событие - "+eventcodeid.getName()+", время - "+eventtime.toString();
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
    
    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }
    
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public String getGeoPlace() {
        return geoPlace;
    }

    public void setGeoPlace(String geoPlace) {
        this.geoPlace = geoPlace;
    }
    
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
