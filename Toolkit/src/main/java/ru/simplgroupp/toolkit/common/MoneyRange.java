package ru.simplgroupp.toolkit.common;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

public class MoneyRange  implements Serializable, Range, Cloneable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8580217826343824129L;
	private BigDecimal from;
	private BigDecimal to;
	
	public MoneyRange() {
		super();
	}
	
	public MoneyRange(BigDecimal pfrom, BigDecimal pto) 
	{
		super();
		from = pfrom;
		to = pto;
	}	
	
	@XmlElement
	public BigDecimal getFrom() {
		return from;
	}
	public void setFrom(BigDecimal from) {
		this.from = from;
	}
	
	@XmlElement
	public BigDecimal getTo() {
		return to;
	}
	public void setTo(BigDecimal to) {
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
		MoneyRange mr = new MoneyRange();
		mr.from = from;
		mr.to = to;
		return mr;
	}
}
