package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.VerificationEntity;
import ru.simplgroupp.persistence.VerificationMarkEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class VerificationMark extends BaseTransfer<VerificationMarkEntity> implements Serializable, Initializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 761687842064316856L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = VerificationMark.class.getConstructor(VerificationMarkEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final int NO_DATA=1;
	public static final int VERIFY_DOCUMENT=2;
	public static final int VERIFY_PERSONALDATA=3;
	public static final int VERIFY_PERSONALDATA_DOCUMENT=4;
	
	protected Partner partner;
	
	    
	public VerificationMark(){
		super();
		entity = new VerificationMarkEntity();
	}
	
	public VerificationMark(VerificationMarkEntity entity){
		
		super(entity);
		partner=new Partner(entity.getPartnersId());
	}
	
	public Integer getId() {
	     return entity.getId();
	 }

	@XmlElement
	 public void setId(Integer id) {
	     entity.setId(id);
	 }
	 
	 public Partner getPartner() {
	    return partner;
	}

	 @XmlElement
	public void setPartner(Partner partner) {
	    this.partner=partner;
	}
	
	public Double getMarkMin(){
	    return entity.getMarkMin();
	}

	@XmlElement
	public void setMarkMin(Double markMin){
	   	entity.setMarkMin(markMin);
	}
	  
	public Double getMarkMax(){
	    return entity.getMarkMax();
	}
	    
	@XmlElement	
	public void setMarkMax(Double markMax){
	   	entity.setMarkMax(markMax);
	}
	
	public String getName(){
		return entity.getName();
	}
	
	@XmlElement	
	public void setName(String name){
		entity.setName(name);
	}
	
	public Integer getKind(){
		return entity.getKind();
	}
	
	@XmlElement	
	public void setKind(Integer kind){
		entity.setKind(kind);
	}
	
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
