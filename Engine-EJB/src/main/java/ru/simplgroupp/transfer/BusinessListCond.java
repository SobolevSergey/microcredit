package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import ru.simplgroupp.persistence.BusinessListCondEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class BusinessListCond extends BaseTransfer<BusinessListCondEntity>  implements Serializable, Initializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8932511205007871596L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = BusinessListCond.class.getConstructor(BusinessListCondEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public enum ListJoinType {		
		UNION, // добавить к списку
		DIFF, // удалить из списка
		INTERSECT; // совпадающие		
	}

	public BusinessListCond() {
		super();
	}

	public BusinessListCond(BusinessListCondEntity entity) {
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
	
	public Integer getOrderNo() {
		return entity.getOrderNo();
	}
	
	public void setOrderNo(Integer orderNo) {
		entity.setOrderNo(orderNo);
	}
	
	public String getBody() {
		return entity.getBody();
	}
	
	public void setBody(String body) {
		entity.setBody(body);
	}
	
	public ListJoinType getJoinType() {
		return ListJoinType.valueOf(entity.getJoinCode());
	}
	
	public void setJoinType(ListJoinType atype) {
		entity.setJoinCode(atype.toString());
	}

	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}
}
