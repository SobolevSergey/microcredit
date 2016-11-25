package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.RegionsEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class FMSRegion extends BaseTransfer<RegionsEntity> implements Serializable, Initializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5011141605856401120L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = FMSRegion.class.getConstructor(RegionsEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public FMSRegion(){
		super();
		entity = new RegionsEntity();
	}

	public FMSRegion(RegionsEntity value){
		super(value);
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

	 public String getCodereg() {
	     return entity.getCodereg();
	 }

	 @XmlElement
	 public void setCodereg(String codereg) {
	     entity.setCodereg(codereg);
	 }

	 public String getCodeIso() {
	     return entity.getCodeIso();
	 }

	 @XmlElement
	 public void setCodeIso(String codeIso) {
	     entity.setCodeIso(codeIso);
	 }
	 
	 @Override
	 public void init(Set options) {
			
	 }

}
