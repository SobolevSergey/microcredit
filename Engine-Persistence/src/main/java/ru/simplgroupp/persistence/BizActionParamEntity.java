package ru.simplgroupp.persistence;

import java.io.Serializable;

public class BizActionParamEntity extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1729753421246747647L;

	protected Integer txVersion = 0;
	
	private BizActionEntity bizAction;	
	private String code;	
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
	 * текст
	 */
	private String dataValueText;
	public BizActionEntity getBizAction() {
		return bizAction;
	}
	public void setBizAction(BizActionEntity bizAction) {
		this.bizAction = bizAction;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getDataValueText() {
		return dataValueText;
	}
	public void setDataValueText(String dataValueText) {
		this.dataValueText = dataValueText;
	}	
}
