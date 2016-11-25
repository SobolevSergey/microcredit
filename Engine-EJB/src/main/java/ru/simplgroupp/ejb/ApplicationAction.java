package ru.simplgroupp.ejb;

import java.util.Arrays;

import ru.simplgroupp.interfaces.BusinessObjectRef;
import ru.simplgroupp.util.AppUtil;
import ru.simplgroupp.workflow.SignalRef;

public class ApplicationAction {
	private String Id;
	private boolean bExclusive;
	private BusinessObjectRef[] bizRefs;
	private String description;
	
	public SignalRef getAsSignalRef() {
		return SignalRef.valueOf(Id); 
	}
	
	public ApplicationAction(String aid, boolean bExclusive, BusinessObjectRef[] abizRefs) {
		this.Id = aid;
		this.bExclusive = bExclusive;
		
		bizRefs = new BusinessObjectRef[abizRefs.length];
		for (int i = 0; i < abizRefs.length; i++) {
			bizRefs[i] = abizRefs[i];
		}
		Arrays.sort(bizRefs);
	}
	
	public String getId() {
		return Id;
	}
	
	public boolean isExclusive() {
		return bExclusive;
	}
	
	public static int hashCodeOf(String aid, BusinessObjectRef[] abizRefs) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aid == null) ? 0 : aid.hashCode());
		result = prime * result + Arrays.hashCode(abizRefs);
		return result;		
	}

	@Override
	public int hashCode() {
		return AppUtil.calcAppActionHashCode(Id, bizRefs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationAction other = (ApplicationAction) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		if (!Arrays.equals(bizRefs, other.bizRefs))
			return false;
		return true;
	}

	public BusinessObjectRef contains(BusinessObjectRef prmBizRef) {
		for (int i = 0; i < bizRefs.length; i++) {
			if (bizRefs[i].like(prmBizRef)) {
				return bizRefs[i];
			}
		}
		return null;
	}
	
	public boolean contains(BusinessObjectRef[] prmBizRefs, boolean bAny) {
		for (int n = 0; n < prmBizRefs.length; n++) {
			BusinessObjectRef cref = contains(prmBizRefs[n]);
			if (cref == null) {
				if (! bAny) {
					return false;
				}
			} else {
				if (bAny) {
					return true;
				}
			}
		}
		if (bAny) {
			return false;
		} else {
			return true;
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
}
