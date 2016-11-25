package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import ru.simplgroupp.persistence.BusinessListEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class BusinessList extends BaseTransfer<BusinessListEntity>  implements Serializable, Initializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2364455637878237716L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = BusinessList.class.getConstructor(BusinessListEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public BusinessList() {
		super();
	}

	public BusinessList(BusinessListEntity entity) {
		super(entity);
	}

	public Integer getId() {
		return entity.getId();
	}
	
	public String getName() {
		return entity.getName();
	}
	
	public void setName(String name) {
		entity.setName(name);
	}
	
	public String getBusinessObjectClass() {
		return entity.getBusinessObjectClass();
	}
	
	public void setBusinessObjectClass(String businessObjectClass) {
		entity.setBusinessObjectClass(businessObjectClass);
	}

	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}
	
	
}
