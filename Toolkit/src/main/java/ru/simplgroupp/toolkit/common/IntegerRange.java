package ru.simplgroupp.toolkit.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class IntegerRange  implements Serializable, Range, Cloneable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -598614060893264988L;
	private Integer from;
	private Integer to;
	
	public IntegerRange() {
		super();
	}
	
	public IntegerRange(Integer pfrom, Integer pto) {
		super();
		from = pfrom;
		to = pto;
	}	

	@XmlElement
	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	@XmlElement
	public Integer getTo() {
		return to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}

	@Override
	public boolean between(Object value) {
		if (value instanceof Number) {
			Number nValue = (Number) value;
			if (from != null && nValue.doubleValue() < from.intValue()) {
				return false;
			}
			if (to != null && nValue.doubleValue() > to.intValue()) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new IntegerRange(from, to);
	}

}
