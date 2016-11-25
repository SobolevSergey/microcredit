/**
 * 
 */
package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Информация об устройстве, с котрого подается заявка
 */
public class DeviceInfoEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161291291678269481L;

	/**
	 * default constructor
	 */
	public DeviceInfoEntity() {
	}

	protected Integer txVersion = 0;
	
	/**
	 * заявка
	 */
	private CreditRequestEntity creditRequestId;

	/**
	 * дата получения информации
	 */
	private Date infoDate;
	/**
	 * разрешние экрана по горизонтали
	 */
	private Integer resolutionWidth;
	/**
	 * разрешние экрана по вертикали
	 */
	private Integer resolutionHeight;

	/**
	 * тип операционной системы на устройстве пользователя
	 */
	private String devicePlatform;

	/**
	 * информация о браузере пользователя
	 */
	private String userAgent;

	
	/**
	 * @return the creditRequestId
	 */
	public CreditRequestEntity getCreditRequestId() {
		return creditRequestId;
	}

	/**
	 * @param creditRequestId
	 *            the creditRequestId to set
	 */
	public void setCreditRequestId(CreditRequestEntity creditRequestId) {
		this.creditRequestId = creditRequestId;
	}

	/**
	 * @return the infoDate
	 */
	public Date getInfoDate() {
		return infoDate;
	}

	/**
	 * @param infoDate
	 *            the infoDate to set
	 */
	public void setInfoDate(Date infoDate) {
		this.infoDate = infoDate;
	}

	/**
	 * @return the resolutionWidth
	 */
	public Integer getResolutionWidth() {
		return resolutionWidth;
	}

	/**
	 * @param resolutionWidth
	 *            the resolutionWidth to set
	 */
	public void setResolutionWidth(Integer resolutionWidth) {
		this.resolutionWidth = resolutionWidth;
	}

	/**
	 * @return the resolutionHeight
	 */
	public Integer getResolutionHeight() {
		return resolutionHeight;
	}

	/**
	 * @param resolutionHeight
	 *            the resolutionHeight to set
	 */
	public void setResolutionHeight(Integer resolutionHeight) {
		this.resolutionHeight = resolutionHeight;
	}

	/**
	 * @return the devicePlatform
	 */
	public String getDevicePlatform() {
		return devicePlatform;
	}

	/**
	 * @param devicePlatform
	 *            the devicePlatform to set
	 */
	public void setDevicePlatform(String devicePlatform) {
		this.devicePlatform = devicePlatform;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent
	 *            the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

   

}
