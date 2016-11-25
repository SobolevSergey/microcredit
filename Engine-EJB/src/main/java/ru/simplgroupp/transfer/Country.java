package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.CountryEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Country extends BaseTransfer<CountryEntity> implements Serializable, Initializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4856886547261231236L;
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Country.class.getConstructor(CountryEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

  	public Country() {
        super();
        entity = new CountryEntity();
    }

    public Country(CountryEntity entity) {
        super(entity);
    }

    @Override
    public void init(Set options) {
        entity.getName();
    }
    
    public String getCode() {
        return entity.getCode();
    }

    @XmlElement
    public void setCode(String code) {
        entity.setCode(code);
    }

    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
        entity.setName(name);
    }

    public Integer getCodeInteger() {
        return entity.getCodeinteger();
    }

    @XmlElement
    public void setCodeInteger(Integer codeinteger) {
        entity.setCodeinteger(codeinteger);
    }

    public String getCodeExtend() {
        return entity.getCodeextend();
    }

    @XmlElement
    public void setCodeExtend(String codeextend) {
        entity.setCodeextend(codeextend);
    }
    
}
