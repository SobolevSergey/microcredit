package ru.simplgroupp.transfer;

import java.lang.reflect.Constructor;

import ru.simplgroupp.persistence.AIRuleEntity;

public class AIRule extends BaseTransfer<AIRuleEntity> {

    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 
    static {
    	try {
			wrapConstructor = AIRule.class.getConstructor(AIRuleEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	public AIRule() {
		super();
	}
	
	public AIRule(AIRuleEntity ent) {
		super(ent);
	}
	
	public Integer getId() {
		return entity.getId();
	}
	
	public void setId(Integer id) {
		entity.setId(id);
	}
	
	public String getPackageName() {
		return entity.getPackageName();
	}
	
	public void setPackageName(String packageName) {
		entity.setPackageName(packageName);
	}
	
	public String getBody() {
		return entity.getBody();
	}
	
	public void setBody(String body) {
		entity.setBody(body);
	}
	
	public String getDescription() {
		return entity.getDescription();
	}
	
	public void setDescription(String description) {
		entity.setDescription(description);
	}
	
	public Integer getRuleType() {
		return entity.getRuleType();
	}
	
	public void setRuleType(Integer ruleType) {
		entity.setRuleType(ruleType);		
	}
	
	public String getKBase() {
		return entity.getKbase();
	}
	
	public void setKBase(String kBase) {
		entity.setKbase(kBase);
	}
}
