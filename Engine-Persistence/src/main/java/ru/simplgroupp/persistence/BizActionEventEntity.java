package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Set;


import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class BizActionEventEntity extends BaseEntity implements Serializable, Initializable, Identifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8146770725561920839L;
	
	public enum Options {
		INIT_BIZACTION;
	}
	
	private BizActionEntity bizAction;
	private Integer eventCode;
	
	
	public BizActionEntity getBizAction() {
		return bizAction;
	}

	public void setBizAction(BizActionEntity bizAction) {
		this.bizAction = bizAction;
	}

	public Integer getEventCode() {
		return eventCode;
	}

	public void setEventCode(Integer eventCode) {
		this.eventCode = eventCode;
	}
  
	@Override
	public void init(Set options) {
		Utils.initRelation(bizAction, options, Options.INIT_BIZACTION);
	}	
}
