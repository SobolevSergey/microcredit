package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.ProductConfigEntity;
import ru.simplgroupp.persistence.ProductRulesEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class ProductRules extends BaseTransfer<ProductRulesEntity> implements Serializable, Initializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1986854213531929141L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
	
	public static Constructor<? extends BaseTransfer> getWrapConstructor() {
	   	return wrapConstructor;
	} 	
	
	static {
	    	try {
				wrapConstructor = ProductRules.class.getConstructor(ProductRulesEntity.class);
			} catch (NoSuchMethodException | SecurityException e) {
				wrapConstructor = null;
			}
	}	
	
	public static final int SCRIPT_JS=1;
	public static final int SCRIPT_KBASE=2;
	
	protected Reference configType;
	protected Products product;
	
	public ProductRules(){
		super();
		entity=new ProductRulesEntity();
	}
	
	public ProductRules(ProductRulesEntity entity){
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
	
	public String getKbase() {
	     return entity.getKbase();
	}

	public void setKbase(String kbase) {
	     entity.setKbase(kbase);
	}
	
	public String getRuleBody() {
	     return entity.getRuleBody();
	}

	public void setRuleBody(String ruleBody) {
	     entity.setRuleBody(ruleBody);
	}
	
	public Integer getScriptType(){
		return entity.getScriptTypeId();
	}
	
	public void setScriptType(Integer scriptType){
		entity.setScriptTypeId(scriptType);
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
