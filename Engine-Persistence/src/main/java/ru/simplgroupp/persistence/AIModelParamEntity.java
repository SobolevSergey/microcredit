package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.HashCodeBuilder;

import ru.simplgroupp.toolkit.common.Utils;

/**
 * параметры для стратегии
 */
public class AIModelParamEntity extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 162980882260720427L;
	
	protected Integer txVersion = 0;
	
	private AIModelEntity aiModel;
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
	
	private String customKey;
	
	private Integer IsIn;
	
	private Integer IsOut;
	
	public AIModelParamEntity(){
		
	}

	public AIModelEntity getAiModel() {
		return aiModel;
	}

	public void setAiModel(AIModelEntity aiModel) {
		this.aiModel = aiModel;
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

	public String getCustomKey() {
		return customKey;
	}

	public void setCustomKey(String customKey) {
		this.customKey = customKey;
	}

	public Integer getIsIn() {
		return IsIn;
	}

	public void setIsIn(Integer isIn) {
		IsIn = isIn;
	}

	public Integer getIsOut() {
		return IsOut;
	}

	public void setIsOut(Integer isOut) {
		IsOut = isOut;
	}	
	
}
