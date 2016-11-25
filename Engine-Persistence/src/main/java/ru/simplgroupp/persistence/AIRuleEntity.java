package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class AIRuleEntity extends BaseEntity implements Serializable, Identifiable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 838710130133418835L;
	
	public static final int RULE_TYPE_STANDART = 1;
	public static final int RULE_TYPE_CONSTANT = 2;
	
	public enum Options {
		INIT_CONTSTANT;
	}
	
	protected Integer txVersion = 0;
	/**
	 * название пакета
	 */
	private String packageName;
	/**
	 * текст правила
	 */
	private String body;
	/**
	 * описание
	 */
	private String description;
	/**
	 * вид правила
	 */
	private Integer ruleType;
	/**
	 * knowledge base
	 */
	private String kbase;
	private List<AIConstantEntity> constants = new ArrayList<>(0); 
	
	public AIRuleEntity(){
		
	}
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public String getKbase() {
		return kbase;
	}

	public void setKbase(String kbase) {
		this.kbase = kbase;
	}

	public List<AIConstantEntity> getConstants() {
		return constants;
	}

	public void setConstants(List<AIConstantEntity> constants) {
		this.constants = constants;
	}

	@Override
	public void init(Set options) {
		Utils.initCollection(getConstants(), options, Options.INIT_CONTSTANT);
		
	}
	
	public boolean isEmptyConstants() {
    	boolean bEmpty = true;
    	for (AIConstantEntity con: constants) {
	    	if (! con.isEmpty()) {
	    		bEmpty = false;
	   			break;
	   		}
	   	}
	   	return bEmpty;
	}
	
	public boolean isStaticConstantRules() {
		return (ruleType == RULE_TYPE_CONSTANT && StringUtils.isBlank(body));
	}
	
}
