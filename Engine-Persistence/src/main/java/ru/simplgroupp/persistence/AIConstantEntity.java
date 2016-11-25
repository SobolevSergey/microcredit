package ru.simplgroupp.persistence;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class AIConstantEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6188910564765189109L;
	
	protected Integer txVersion = 0;
	
	/**
	 * к какому пакету правил относится
	 */
	private AIRuleEntity aiRule;
	/**
	 * название
	 */
	private String name;
	/**
	 * тип данных
	 */
	private String dataType;
	/**
	 * значение
	 */
	private String dataValue;
	/**
	 * описание
	 */
	private String description;
	/**
	 * текст
	 */
	private String dataValueText;
	
	public AIConstantEntity(){
		
	}

	public AIRuleEntity getAiRule() {
		return aiRule;
	}

	public void setAiRule(AIRuleEntity aiRule) {
		this.aiRule = aiRule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDataValueText() {
		return dataValueText;
	}

	public void setDataValueText(String dataValueText) {
		this.dataValueText = dataValueText;
	}
	
	public boolean isEmpty() {
		return (StringUtils.isBlank(name) && StringUtils.isBlank(dataType));
	}
	
	public int getIndex() {
		return aiRule.getConstants().indexOf(this);
	}
	
	public String getDataValueString() {
		if (! "C".equals(this.dataType)) {
			return null;
		}
		if (! StringUtils.isEmpty(dataValueText)) {
			return dataValueText;
		} else {
			return dataValue;
		}
	}
	
	public void setDataValueString(String value) {
		if (! "C".equals(this.dataType)) {
			return;
		}		
		if (value.length() > 250) {
			dataValueText = value;
		} else {
			dataValue = value;
		}
	}
	
}
