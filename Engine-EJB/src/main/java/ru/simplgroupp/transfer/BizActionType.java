package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import ru.simplgroupp.persistence.BizActionTypeEntity;
import ru.simplgroupp.toolkit.common.Identifiable;

public class BizActionType extends BaseTransfer<BizActionTypeEntity> implements Serializable, Identifiable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6031611329470904501L;
	public static final int TYPE_SEND_EMAIL = 2;
	public static final int TYPE_SEND_SMS = 3;
	public static final int TYPE_APPLICATION = 4;	
	public static final int TYPE_PROCESS = 5;	
	public static final int TYPE_START_FORM = 6;
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = BizActionType.class.getConstructor(BizActionTypeEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public BizActionType() {
		super();
	}

	public BizActionType(BizActionTypeEntity entity) {
		super(entity);
	}
	
	public Integer getId() {
		return entity.getId();
	}
	
	public String getName() {
		return entity.getName();
	}

}
