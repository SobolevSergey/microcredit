package ru.simplgroupp.persistence;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Банки
 */

  public class BankEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected Integer txVersion = 0;
	/**
	 * бик, ключ
	 */
    private String bik;
    /**
     * название
     */
    private String name;
    /**
     * кор.счет
     */
    private String corAccount;
    /**
     * активная запись или нет
     */
    private Integer isActive;
    /**
     * является банком
     */
    private Integer isBank;
    /**
     * адрес текстом
     */
    private String Address;
    
    public BankEntity() {
		
	}
    
    public BankEntity(String bik) {
		this.bik = bik;
	}

	public String getBik() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik = bik;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getCorAccount() {
        return corAccount;
    }

    public void setCorAccount(String corAccount) {
        this.corAccount = corAccount;
    }
    
    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
    
    public Integer getIsBank() {
        return isBank;
    }

    public void setIsBank(Integer isBank) {
        this.isBank = isBank;
    }
    
    public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getDescription() {
    	if (StringUtils.isNotEmpty(this.bik)&&StringUtils.isNotEmpty(this.name)) {
    		return this.bik+", "+this.name;
    	} else {
    		return this.bik;
    	}
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bik != null ? bik.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
    	if (other == null) return false;
	       
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    BankEntity cast = (BankEntity) other;
	    
	    if (this.bik == null) return false;
	       
	    if (cast.getBik() == null) return false;       
	       
	    if (this.bik.toString() != cast.getBik().toString())
	    	return false;
	    
	    return true;
    }

}
