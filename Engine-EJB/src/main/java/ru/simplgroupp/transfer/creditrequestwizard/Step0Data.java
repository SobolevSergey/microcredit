package ru.simplgroupp.transfer.creditrequestwizard;

import java.util.Date;

/**
 * Данные шага 0
 */
public class Step0Data {

    private Integer creditDays;

    private Double creditSum;

    private Double creditSumMin;

    private Double creditSumMax;

    private Integer creditDaysMin;

    private Integer creditDaysMax;

    private Double stake;

    private Double stakeMin;

    private Double stakeMax = null;
    //additional day for calc
    private Integer addDays;
    
    private String clientIp;

    private String clientIpAttr;
    
    private Integer clientTimezone;

    /*device info*/
    
    private int resW;
    private int resH;
    private String userAgent;
    private Date infoDate;
    //function for calc stake on client side
    private String percentFunction;
    
    public Integer getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(Integer creditDays) {
        this.creditDays = creditDays;
    }

    public Double getCreditSum() {
        return creditSum;
    }

    public void setCreditsum(Double creditSum) {
        this.creditSum = creditSum;
    }

    public Double getStake() {
        return stake;
    }

    public void setStake(Double stake) {
        this.stake = stake;
    }

    public void setCreditSum(Double creditSum) {
        this.creditSum = creditSum;
    }

    public Double getStakeMin() {
        return stakeMin;
    }

    public void setStakeMin(Double stakeMin) {
        this.stakeMin = stakeMin;
    }

    public Double getStakeMax() {
        return stakeMax;
    }

    public void setStakeMax(Double stakeMax) {
        this.stakeMax = stakeMax;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientIpAttr() {
        return clientIpAttr;
    }

    public void setClientIpAttr(String clientIpAttr) {
        this.clientIpAttr = clientIpAttr;
    }

    public Double getCreditSumMin() {
        return creditSumMin;
    }

    public void setCreditSumMin(Double creditSumMin) {
        this.creditSumMin = creditSumMin;
    }

    public Double getCreditSumMax() {
        return creditSumMax;
    }

    public void setCreditSumMax(Double creditSumMax) {
        this.creditSumMax = creditSumMax;
    }

    public Integer getCreditDaysMin() {
        return creditDaysMin;
    }

    public void setCreditDaysMin(Integer creditDaysMin) {
        this.creditDaysMin = creditDaysMin;
    }

    public Integer getCreditDaysMax() {
        return creditDaysMax;
    }

    public void setCreditDaysMax(Integer creditDaysMax) {
        this.creditDaysMax = creditDaysMax;
    }

	public Integer getClientTimezone() {
		return clientTimezone;
	}

	public void setClientTimezone(Integer clientTimezone) {
		this.clientTimezone = clientTimezone;
	}


	/**
	 * @return the resW
	 */
	public int getResW() {
		return resW;
	}

	/**
	 * @param resW the resW to set
	 */
	public void setResW(int resW) {
		this.resW = resW;
	}

	/**
	 * @return the resH
	 */
	public int getResH() {
		return resH;
	}

	/**
	 * @param resH the resH to set
	 */
	public void setResH(int resH) {
		this.resH = resH;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return the infoDate
	 */
	public Date getInfoDate() {
		return infoDate;
	}

	/**
	 * @param infoDate the infoDate to set
	 */
	public void setInfoDate(Date infoDate) {
		this.infoDate = infoDate;
	}

	public String getPercentFunction() {
		return percentFunction;
	}

	public void setPercentFunction(String percentFunction) {
		this.percentFunction = percentFunction;
	}

	public Integer getAddDays() {
		return addDays;
	}

	public void setAddDays(Integer addDays) {
		this.addDays = addDays;
	}
	
	
}
