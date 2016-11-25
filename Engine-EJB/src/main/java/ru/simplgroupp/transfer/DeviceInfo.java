package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.DeviceInfoEntity;
import ru.simplgroupp.toolkit.common.Initializable;

/**
 *
 */
public class DeviceInfo extends BaseTransfer<DeviceInfoEntity> implements
		Serializable, Initializable {

	private static final long serialVersionUID = 1L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = DeviceInfo.class.getConstructor(DeviceInfoEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected CreditRequest creditRequest;

	/**
	 * default constructor
	 */
	public DeviceInfo() {
		super();
		entity=new DeviceInfoEntity();
	}

	/**
	 * constructor from entity
	 * 
	 * @param entity
	 */
	public DeviceInfo(DeviceInfoEntity entity) {
		super(entity);
		this.creditRequest = new CreditRequest(entity.getCreditRequestId());
	}

	
	@Override
	public void init(Set options) {
		// do nothing
	}

	/**
	 * @return the creditRequest
	 */
	public CreditRequest getCreditRequest() {
		return creditRequest;
	}

	/**
	 * @param creditRequest
	 *            the creditRequest to set
	 */
	public void setCreditRequest(CreditRequest creditRequest) {
		this.creditRequest = creditRequest;
	}

	public String getDevicePlatform() {
		return entity.getDevicePlatform();
	}

	public void setDevicePlatform(String devicePlatform) {
		entity.setDevicePlatform(devicePlatform);
	}

	public String getUserAgent() {
		return entity.getUserAgent();
	}

	public void setUserAgent(String userAgent) {
		entity.setUserAgent(userAgent);
	}

	public Integer getResolutionHeight() {
		return entity.getResolutionHeight();
	}

	public void setResolutionHeight(Integer resolutionHeight) {
		entity.setResolutionHeight(resolutionHeight);
	}

	public Integer getResolutionWidth() {
		return entity.getResolutionWidth();
	}

	public void setResolutionWidth(Integer resolutionWidth) {
		entity.setResolutionWidth(resolutionWidth);
	}

	public Date getInfoDate() {
		return entity.getInfoDate();
	}

	public void setInfoDate(Date infoDate) {
		entity.setInfoDate(infoDate);
	}

	public Integer getId() {
		return entity.getId();
	}

	public void setId(Integer id) {
		entity.setId(id);
	}

	

}
