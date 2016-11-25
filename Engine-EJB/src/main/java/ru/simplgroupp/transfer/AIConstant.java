package ru.simplgroupp.transfer;

import java.lang.reflect.Constructor;

import ru.simplgroupp.persistence.AIConstantEntity;

public class AIConstant extends BaseTransfer<AIConstantEntity> {
	
	public static final String TYPE_STRING = "C";
	public static final String TYPE_NUMERIC = "N";
	public static final String TYPE_DATE = "D";
	public static final String TYPE_FLOAT = "F";
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = AIConstant.class.getConstructor(AIConstantEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public AIConstant() {
		super();
	}

	public AIConstant(AIConstantEntity entity) {
		super(entity);
	}

}
