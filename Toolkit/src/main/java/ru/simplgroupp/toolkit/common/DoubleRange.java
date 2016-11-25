package ru.simplgroupp.toolkit.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class DoubleRange implements Serializable, Range, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3458162870020792857L;
	private Double from;
	private Double to;
	
	public DoubleRange() {
		super();
	}
	
	public DoubleRange(Double from, Double to) {
		super();
		
		this.from = from;
		this.to = to;
	}
	
	@XmlElement
	public Double getFrom() {
		return from;
	}
	public void setFrom(Double from) {
		this.from = from;
	}
	
	@XmlElement
	public Double getTo() {
		return to;
	}
	public void setTo(Double to) {
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
		return new DoubleRange(from, to);
	}
}
