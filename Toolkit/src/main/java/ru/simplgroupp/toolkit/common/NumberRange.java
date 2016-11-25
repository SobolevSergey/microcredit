package ru.simplgroupp.toolkit.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class NumberRange  implements Serializable, Range, Cloneable  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2249672986385485214L;
	private Number from;
	private Number to;
	
	public NumberRange() {
		super();
	}
	
	public NumberRange(Number pfrom, Number pto) {
		super();
		from = pfrom;
		to = pto;
	}	

	@XmlElement
	public Number getFrom() {
		return from;
	}

	public void setFrom(Number from) {
		this.from = from;
	}

	@XmlElement
	public Number getTo() {
		return to;
	}

	public void setTo(Number to) {
		this.to = to;
	}

	@Override
	public boolean between(Object value) {
		if (value instanceof Number) {
			Number nValue = (Number) value;
			if (from != null && nValue.doubleValue() < from.doubleValue()) {
				return false;
			}
			if (to != null && nValue.doubleValue() > to.doubleValue()) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

}
