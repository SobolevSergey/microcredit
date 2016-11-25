package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class ProductMessages extends BaseTransfer<ProductMessagesEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3146246904359835358L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;
	
	public static Constructor<? extends BaseTransfer> getWrapConstructor() {
	   	return wrapConstructor;
	} 	
	static {
	    	try {
				wrapConstructor = ProductMessages.class.getConstructor(ProductMessagesEntity.class);
			} catch (NoSuchMethodException | SecurityException e) {
				wrapConstructor = null;
			}
	}	
	
	protected Reference configType;
	protected Products product;
	
	public ProductMessages(){
		super();
		entity=new ProductMessagesEntity();
	}
	
	public ProductMessages(ProductMessagesEntity entity){
		super(entity);
		product=new Products(entity.getProductId());
		if (entity.getConfigTypeId()!=null){
			configType=new Reference(entity.getConfigTypeId());
		}
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
	
	public String getSignalRef() {
	     return entity.getSignalRef();
	}

	public void setSignalRef(String signalRef) {
	     entity.setSignalRef(signalRef);
	}
	
	public String getDescription() {
	     return entity.getDescription();
	}

	public void setDescription(String description) {
	     entity.setDescription(description);
	}
	
	public String getSubject() {
	     return entity.getSubject();
	}

	public void setSubject(String subject) {
	     entity.setSubject(subject);
	}
	
	public String getBody() {
	     return entity.getBody();
	}

	public void setBody(String body) {
	     entity.setBody(body);
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
	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
