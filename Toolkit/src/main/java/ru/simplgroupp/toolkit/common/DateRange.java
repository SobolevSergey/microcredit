package ru.simplgroupp.toolkit.common;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class DateRange implements Serializable, Range, Cloneable 
{

	private static final long serialVersionUID = 947075678689788136L;
	
	private Date from;
	private Date to;
	
	public DateRange() {
		super();
	}
	
	public DateRange(Date afrom, Date ato) 
	{
		from = afrom;
		to = ato;
	}

	@XmlElement
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	@XmlElement
	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	@Override
	public boolean between(Object value) {
		if (value instanceof Date) {
			Date dValue = (Date) value;
	        
			if (from != null && dValue.before(from)) {
				return false;
			}
			if (to != null && dValue.after(to)) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	
	public boolean betweenDays(Object value) {
		if (value instanceof Date) {
			Date dValue = (Date) value;
			Calendar c1=Calendar.getInstance();
			if (from!=null){
			  c1.setTime(from);
			}
			Calendar c2=Calendar.getInstance();
			if (to!=null){
			  c2.setTime(to);
			}
			Calendar dvalue=Calendar.getInstance();
			dvalue.setTime(dValue);
			if (from != null && c1.get(Calendar.DAY_OF_MONTH)==dvalue.get(Calendar.DAY_OF_MONTH)
					&& c1.get(Calendar.MONTH)==dvalue.get(Calendar.MONTH)
					&& c1.get(Calendar.YEAR)==dvalue.get(Calendar.YEAR)) {
				return true;
			}
			if (to != null && c2.get(Calendar.DAY_OF_MONTH)==dvalue.get(Calendar.DAY_OF_MONTH)
					&& c2.get(Calendar.MONTH)==dvalue.get(Calendar.MONTH)
					&& c2.get(Calendar.YEAR)==dvalue.get(Calendar.YEAR)) {
				return true;
			}
           
			if (from != null && dValue.before(from)) {
				return false;
			}
			if (to != null && dValue.after(to)) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		DateRange dr = new DateRange(from, to);
		return dr;
	}
}
