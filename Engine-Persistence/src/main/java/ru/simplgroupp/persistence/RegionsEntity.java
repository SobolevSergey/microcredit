package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * Справочник регионов
 */
public class RegionsEntity implements Serializable {
  
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -945319941307039995L;
	
	protected Integer txVersion = 0;
    /**
     * код региона по октмо
     */
    private String code;
    /**
     * название
     */
    private String name;
    /**
     * автомобильный код региона 
     */
    private String codereg;
    /**
     * код региона по iso
     */
    private String codeIso;
    
    public RegionsEntity() {
    }

    public RegionsEntity(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodereg() {
        return codereg;
    }

    public void setCodereg(String codereg) {
        this.codereg = codereg;
    }

    public String getCodeIso() {
		return codeIso;
	}

	public void setCodeIso(String codeIso) {
		this.codeIso = codeIso;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
    	if (other == null) return false;
	       
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    RegionsEntity cast = (RegionsEntity) other;
	    
	    if (this.code == null) return false;
	       
	    if (cast.getCode() == null) return false;       
	       
	    if (this.code.toString() != cast.getCode().toString())
	    	return false;
	    
	    return true;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
