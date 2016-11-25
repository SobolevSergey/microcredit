package ru.simplgroupp.persistence;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * разные переменные из стратегии
 */
public class Misc implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9101402492065566667L;
	protected Integer txVersion = 0;
	/**
	 * название класса
	 */
    private String ClassName;
    /**
     * id класса
     */
    private String ClassId;
    /**
     * название переменной
     */
    private String Name;
    /**
     * формат
     */
    private String Format;
    /**
     * значение
     */
    private String Value;
    
    public String getClassName() {
    	return ClassName;
    }
    
    public void setClassName(String ClassName) {
    	this.ClassName=ClassName;
    }
    
    public String getClassId() {
    	return ClassId;
    }
    
    public void setClassId(String ClassId) {
    	this.ClassId=ClassId;
    }
    
    public String getName() {
    	return Name;
    }
    
    public void setName(String Name) {
    	this.Name=Name;
    }
    
    public String getFormat() {
    	return Format;
    }
    
    public void setFormat(String Format) {
    	this.Format=Format;
    }
    
    public String getValue() {
    	return Value;
    }
    
    public void setValue(String Value) {
    	this.Value=Value;
    }
    
    /**
     * эта переменная для заявки или нет
     * @return
     */
    public boolean getForCreditRequest(){
    	if (StringUtils.isNotEmpty(this.ClassName)&&this.ClassName.contains("CreditRequest")){
    		return true;
    	}
    	return false;
    }
}
