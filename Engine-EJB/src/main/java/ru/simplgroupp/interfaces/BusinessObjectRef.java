package ru.simplgroupp.interfaces;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

public class BusinessObjectRef implements Serializable, Comparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1014446323571059298L;
	private String businessObjectClass;
	private Object businessObjectId;
	
	public BusinessObjectRef(String bizClassName, Object bizId) {
		businessObjectClass = bizClassName;
		businessObjectId = bizId;
	}
	
	public String getBusinessObjectClass() {
		return businessObjectClass;
	}
	public void setBusinessObjectClass(String businessObjectClass) {
		this.businessObjectClass = businessObjectClass;
	}
	public Object getBusinessObjectId() {
		return businessObjectId;
	}
	public Integer getIdAsInteger() {
		if (businessObjectId == null) {
			return null;
		}
		if (businessObjectId instanceof Number) {
			return (Convertor.toInteger(businessObjectId));
		} else {
			return null;
		}
	}
	public void setBusinessObjectId(Object businessObjectId) {
		this.businessObjectId = businessObjectId;
	}
	
	public boolean like(BusinessObjectRef other) {
		boolean bRes = false;
		if (other.businessObjectClass == null || other.businessObjectClass.equals("*")) {
			bRes = true;
		} else {
			if (this.businessObjectClass == null || this.businessObjectClass.equals("*")) {
				bRes = true;
			} else {
				bRes = this.businessObjectClass.equals(other.businessObjectClass);
			}
		}
		if (! bRes) {
			return false;
		}
		if (other.businessObjectId == null || other.businessObjectId.toString().equals("*")) {
			return true;
		} else {
			if (this.businessObjectId == null || this.businessObjectId.toString().equals("*")) {
				return true;
			} else {
				return this.businessObjectId.equals(other.businessObjectId);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((businessObjectClass == null) ? 0 : businessObjectClass
						.hashCode());
		result = prime
				* result
				+ ((businessObjectId == null) ? 0 : businessObjectId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		BusinessObjectRef other = (BusinessObjectRef) obj;
		if (businessObjectClass == null) {
			if (other.businessObjectClass != null)
				return false;
		} else if (!businessObjectClass.equals(other.businessObjectClass))
			return false;
		if (businessObjectId == null) {
			if (other.businessObjectId != null)
				return false;
		} else if (!businessObjectId.equals(other.businessObjectId))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object other) {
		if (other == null) {
			return 1;
		}
		
		BusinessObjectRef cast = (BusinessObjectRef) other;
		int nc = this.businessObjectClass.compareTo(cast.businessObjectClass);
		if (nc == 0) {
			return Utils.compareNull(this.businessObjectId, cast.businessObjectId);
		} else {
			return nc;
		}
	}
	
	public static String toString(String bizClassName, Object bizId) {
		return new BusinessObjectRef(bizClassName, bizId).toString();
	}
	
	public static BusinessObjectRef valueOf(String value) {
		String[] ss = value.split(":");
		Object aid = null;
		if (! StringUtils.isBlank(ss[1])) {
			try {
				aid = Convertor.toInteger(ss[1]);
			} catch (Throwable ex) {
				aid = ss[1];
			}
		}
		BusinessObjectRef ref = new BusinessObjectRef(ss[0], aid);
		return ref;
	}
	
	@Override
	public String toString() {
		String ss = businessObjectClass + ":";
		if (businessObjectId != null) {
			ss = ss + businessObjectId.toString();
		}
		return ss;
	}
}
