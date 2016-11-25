package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.RefSummaryEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class RefSummary extends BaseTransfer<RefSummaryEntity> implements Serializable, Initializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8285970765156027728L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = RefSummary.class.getConstructor(RefSummaryEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected Reference datatype;
	
	public RefSummary(){
		super();
		entity = new RefSummaryEntity();
	}
	
	public RefSummary(RefSummaryEntity entity){
		super(entity);
		datatype=new Reference(entity.getDatatype());
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
	 
	 public String getDescription() {
	     return entity.getDescription();
	 }

	 @XmlElement
	 public void setDescription(String description) {
	        entity.setDescription(description);
	 }
	 public Integer getIsActive() {
	        return entity.getIsActive();
	 }

	 @XmlElement
	 public void setIsActive(Integer isactive) {
	        entity.setIsActive(isactive);
	 }
	 
	 public Reference getDataType() {
	        return datatype;
	 }

	 @XmlElement
	public void setDataType(Reference datatype) {
	        this.datatype = datatype;
	}     
	
	@Override
	public void init(Set options) {
		entity.getDescription();
		
	}

}
