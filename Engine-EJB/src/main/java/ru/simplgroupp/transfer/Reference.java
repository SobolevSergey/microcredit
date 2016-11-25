package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class Reference extends BaseTransfer<ReferenceEntity> implements Serializable, Initializable, Identifiable {
    
    public enum Options {
        INIT_HEADER;
    }

	private static final long serialVersionUID = 1L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Reference.class.getConstructor(ReferenceEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
    public static final int AUTO_EXEC=1;
    public static final int MANUAL_EXEC=2;
    
    private RefHeader refHeader;
    private RefHeader refHeaderMain;
    
	public Reference() {
		super();
		entity = new ReferenceEntity();
	}
	
	public Reference(ReferenceEntity value){
		super(value);
	}
	
	public Integer getId() {
        return entity.getId();
    }
    
	@XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
        entity.setName(name);
    }    
    
    public String getCode() {
        return entity.getCode();
    }

    @XmlElement
    public void setCode(String code) {
        entity.setCode(code);
    }

    public Integer getCodeInteger() {
        return entity.getCodeinteger();
    }

    @XmlElement
    public void setCodeInteger(Integer codeinteger) {
        entity.setCodeinteger(codeinteger);
    }
      
    public String getCodeMain() {
        return entity.getCodemain();
    }

    @XmlElement
    public void setCodeMain(String codemain) {
        entity.setCodemain(codemain);
    }

    public Integer getCodeIntegerMain() {
        return entity.getCodeintegermain();
    }

    @XmlElement
    public void setCodeIntegerMain(Integer codeintegermain) {
        entity.setCodeintegermain(codeintegermain);
    }
    
    public Integer getIsActive() {
    	return entity.getIsactive();
    }
    
    @XmlElement
    public void setIsActive(Integer isactive) {
    	entity.setIsactive(isactive);
    }
    
    public RefHeader getRefHeader() {
    	return refHeader;
    }
    
    public void setRefHeader(RefHeader refHeader) {
    	this.refHeader=refHeader;
    }
    
    public RefHeader getRefHeaderMain() {
    	return refHeaderMain;
    }
    
    public void setRefHeaderMain(RefHeader refHeaderMain) {
    	this.refHeaderMain=refHeaderMain;
    }
    
	@Override
	public void init(Set options) {
	    entity.getName();
	    Utils.initRelation(entity.getRefHeaderId(), options, Options.INIT_HEADER);	
	}

}
