package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import ru.simplgroupp.persistence.ReportTypeEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class ReportType extends BaseTransfer<ReportTypeEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7513648154843025652L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = ReportType.class.getConstructor(ReportTypeEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public ReportType() {
		super();
		entity = new ReportTypeEntity();
	}
	
	public ReportType(ReportTypeEntity entity) {
		super(entity);
	}
	
	public Integer getId() {
		return entity.getId();
	}
	
	public void setId(Integer id) {
		entity.setId(id);
	}
	public String getName() {
		return entity.getName();
	}
	
	public void setName(String name) {
		entity.setName(name);
	}
	
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

	
}
