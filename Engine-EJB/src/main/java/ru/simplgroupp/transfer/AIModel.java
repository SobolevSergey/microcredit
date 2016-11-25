package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.simplgroupp.persistence.AIModelEntity;
import ru.simplgroupp.persistence.AIModelParamEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.ModelUtils;

public class AIModel extends BaseTransfer<AIModelEntity> implements Serializable, Initializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8719887026691709377L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = AIModel.class.getConstructor(AIModelEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }		
	
	protected Map<String,Object> Params = new HashMap<String,Object>(0);
	protected Products product;
	protected Reference way;
	
	public AIModel() {
		super();
		entity = new AIModelEntity();
	}
	
	public AIModel(AIModelEntity ent) {
		super(ent);
		if (ent.getProductId()!=null){
			product=new Products(ent.getProductId());
		}
		if (ent.getWayId()!=null){
			way = new Reference(ent.getWayId());
		}		
	}	
	
	public boolean getHasParams() {
		return (! entity.getParams().isEmpty());
	}
	
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}
	
	public Map<String,Object> getParams() {
		return Params;
	}
	
	public Integer getId() {
		return entity.getId();
	}

	public String getTarget() {
		return entity.getTarget();
	}
	
	public void setTarget(String avalue) {
		entity.setTarget(avalue);
	}
	
	public Date getDateCreate() {
		return entity.getDateCreate();
	}
	
	public void setDateCreate(Date avalue) {
		entity.setDateCreate(avalue);
	}
	
	public Date getDateChange() {
		return entity.getDateChange();
	}
	
	public void setDateChange(Date avalue) {
		entity.setDateChange(avalue);
	}	
	
	public String getVersion() {
		return entity.getVersion();
	}
	
	public void setVersion(String avalue) {
		entity.setVersion(avalue);
	}
	
	public String getBody() {
		return entity.getBody();
	}
	
	public void setBody(String avalue) {
		entity.setBody(avalue);
	}
	
	public Integer getIsActive() {
		return entity.getIsActive();
	}
	
	public void setIsActive(Integer avalue) {
		entity.setIsActive(avalue);
	}
	
	public String getMimeType() {
		return entity.getMimeType();
	}
	
	public void setMimeType(String avalue) {
		entity.setMimeType(avalue);
	}
	
	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	/**
	 * Является ли модель встроенной в систему и обязательной. 
	 * @return
	 */
	public boolean isBuiltIn() {
		return ModelKeys.TARGET_CREDIT_DECISION.equals(getTarget());
	}
	
	public String getActiveStatusName() {
		switch (getIsActive()) {
			case ActiveStatus.ACTIVE:return "активная"; 
			case ActiveStatus.DRAFT:return "черновик";
			case ActiveStatus.ARCHIVE:return "архивная";
		}
		return null;
	}

	public Reference getWay() {
		return way;
	}

	public void setWay(Reference way) {
		this.way = way;
	}
}
