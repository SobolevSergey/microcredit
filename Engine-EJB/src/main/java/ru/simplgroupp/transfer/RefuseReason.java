package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.RefuseReasonEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class RefuseReason extends BaseTransfer<RefuseReasonEntity> implements Serializable, Initializable{

	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2713445824699451811L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = RefuseReason.class.getConstructor(RefuseReasonEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }		

    protected Reference reasonId;
	
	public RefuseReason(){
		super();
		entity = new RefuseReasonEntity();
	}
	
	public RefuseReason(RefuseReasonEntity entity){
		super(entity);
		reasonId=new Reference(entity.getReasonId());
	}
	
	 public Integer getId() {
	     return entity.getId();
	 }

	 @XmlElement
	 public void setId(Integer id) {
	     entity.setId(id);
	 }
	
	 public String getNameFull() {
	     return entity.getNameFull();
	 }

	 @XmlElement
	 public void setNameFull(String nameFull) {
	        entity.setNameFull(nameFull);
	 }
	 public String getName() {
	        return entity.getName();
	 }

	 @XmlElement
	 public void setName(String name) {
	        entity.setName(name);
	 } 
	 
	 public Reference getReasonId() {
	        return reasonId;
	 }
	 
	 @XmlElement
	 public void setReasonId(Reference reasonId) {
	        this.reasonId = reasonId;
	 }     
	
	 @Override
	 public void init(Set options) {
	    entity.getNameFull();
	 }
	
	 public String getConstantName() {
		return entity.getConstantName();
	 }
	
	 public void setConstantName(String value) {
		entity.setConstantName(value);
	 }

	 public Integer getForDecision() {
	     return entity.getForDecision();
	 }

	 public void setForDecision(Integer forDecision) {
	     entity.setForDecision(forDecision);
	 }
}
