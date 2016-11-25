package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.ProductConfigEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class ProductConfig extends BaseTransfer<ProductConfigEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5421379707155846262L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
	
	public static Constructor<? extends BaseTransfer> getWrapConstructor() {
	   	return wrapConstructor;
	} 	
	static {
	    	try {
				wrapConstructor = ProductConfig.class.getConstructor(ProductConfigEntity.class);
			} catch (NoSuchMethodException | SecurityException e) {
				wrapConstructor = null;
			}
	}	
	
	protected Reference configType;
	protected Products product;
	
	public ProductConfig(){
		super();
		entity=new ProductConfigEntity();
	}
	
	public ProductConfig(ProductConfigEntity entity){
		super(entity);
		product=new Products(entity.getProductId());
		if (entity.getConfigTypeId()!=null){
			configType=new Reference(entity.getConfigTypeId());
		}
	}
	
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
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
	
	public Integer getIsActive() {
	     return entity.getIsActive();
	}

    public void setIsActive(Integer isactive) {
	    entity.setIsActive(isactive);
	}
  
    public Date getDatabeg() {
        return entity.getDatabeg();
    }
  
    public void setDatabeg(Date databeg) {
        entity.setDatabeg(databeg);
    }

    public Date getDataend() {
        return entity.getDataend();
    }
 
    public void setDataend(Date dataend) {
        entity.setDataend(dataend);
    }
    
    public String getDescription() {
	     return entity.getDescription();
	}

	public void setDescription(String description) {
	     entity.setDescription(description);
	}
	
	public String getDataValue() {
	     return entity.getDataValue();
	}

	public void setDataValue(String dataValue) {
	     entity.setDataValue(dataValue);
	}
	
	public String getDataType() {
	     return entity.getDataType();
	}

	public void setDataType(String dataType) {
	     entity.setDataType(dataType);
	}
	
	public Reference getConfigType() {
		return configType;
	}

	public void setConfigType(Reference configType) {
		this.configType = configType;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	
}
