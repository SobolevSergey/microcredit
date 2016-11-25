package ru.simplgroupp.toolkit.common;


import java.io.Serializable;

public class NameValuePair implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6071061294206261514L;
	private String name;
	private Object value;
	private Object properties;
	
	public NameValuePair(String aname, Object avalue) {
		name = aname;
		value = avalue;
	}
	
	public NameValuePair(String aname, Object avalue, Object props) {
		name = aname;
		value = avalue;
		properties = props;
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public Object getProperties() {
		return properties;
	}

	public void setProperties(Object properties) {
		this.properties = properties;
	}
	
	
}

