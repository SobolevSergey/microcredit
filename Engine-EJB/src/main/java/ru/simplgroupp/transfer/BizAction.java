package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.persistence.BizActionEventEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.RulesKeys;

public class BizAction extends BaseTransfer<BizActionEntity> implements Serializable, Identifiable, Initializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3113687710969209733L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = BizAction.class.getConstructor(BizActionEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
    
    public static final int SINGLETON_NO = 0;
    public static final int SINGLETON_OBJECT = 1;
    public static final int SINGLETON_OBJECT_ONE = 2;
    public static final int SINGLETON_ACTION = 3;
    
	public enum Options {
		INIT_EVENTS;
	}    
	
	protected BizActionType bizActionType;
	protected Products product;
	protected Reference configType;
	protected List<Integer> events = new ArrayList<Integer>(5);

	public BizAction() {
		super();
		entity = new BizActionEntity();
	}
	
	public BizAction(BizActionEntity ent) {
		super(ent);
		if (ent.getProductId()!=null){
			product=new Products(ent.getProductId());
		}
		if (ent.getConfigTypeId()!=null){
			configType=new Reference(ent.getConfigTypeId());
		}
		bizActionType = new BizActionType(entity.getBizActionType());
	}
	
	public Integer getId() {
		return entity.getId();
	}
	
	public String getProcessDefKey() {
		return entity.getProcessDefKey();
	}
	
	public String getBusinessObjectClass() {
		return entity.getBusinessObjectClass();
	}
	
	public boolean isBusinessObjectAction() {
		return (entity.getProcessDefKey() == null && entity.getBusinessObjectClass() != null);
	}
	
	public boolean isBusinessProcessAction() {
		return (entity.getProcessDefKey() != null);
	}	
	
	public String getRulePackageName() {
		if (isBusinessProcessAction()) {
			return RulesKeys.BP_ACTIONS_PREFIX + "." + entity.getProcessDefKey() + "." + entity.getSignalRef() + ".biz" + entity.getId().toString();
		} else if (isBusinessObjectAction()) {
			return RulesKeys.BO_ACTIONS_PREFIX + "." + entity.getBusinessObjectClass() + "." + entity.getSignalRef() + ".biz" + entity.getId().toString();
		} else {
			return null;
		}
	}
	
	public String getRuleOptionsName() {
		return RulesKeys.BA_OPTIONS_PREFIX + "." + entity.getBusinessObjectClass() + "." + entity.getSignalRef();
	}
	
	public String getRuleMessagesName() {
		return RulesKeys.BA_MESSAGES_PREFIX + "." + entity.getBusinessObjectClass() + "." + entity.getSignalRef();
	}	
	
	public String getRuleEnabled() {
		return entity.getRuleEnabled();
	}
	
	public String getRuleAction() {
		return entity.getRuleAction();
	}	
	
	public String getRuleStart() {
		return entity.getRuleStart();
	}	
	
	public String getSignalRef() {
		return entity.getSignalRef();
	}
	
	public Integer getForOne() {
		return entity.getForOne();
	}
	
	public Integer getForMany() {
		return entity.getForMany();
	}
	
	public String getAssignee() {
		return entity.getAssignee();
	}
	
	public BizActionType getBizActionType() {
		return bizActionType;
	}
	
	public String getCandidateGroups() {
		return entity.getCandidateGroups();
	}
	
	public String getCandidateUsers() {
		return entity.getCandidateUsers();
	}
	
	public String getDataFilter() {
		return entity.getDataFilter();
	}
	
	public String getDescription() {
		return entity.getDescription();
	}

	public String getErrorCode() {
		return entity.getErrorCode();
	}
	
	public String getErrorMessage() {
		return entity.getErrorMessage();
	}
	
	public Date getLastStart() {
		return entity.getLastStart();
	}
	
	public Date getLastEnd() {
		return entity.getLastEnd();
	}
	
	public Integer getIsActive() {
		return entity.getIsActive();
	}
	
	public Integer getIsRequired() {
		return entity.getIsRequired();
	}
	
	public Integer getIsAtomic() {
		return entity.getIsAtomic();
	}
	
	public Integer getIsSingleton() {
		return entity.getIsSingleton();
	}	
	
	public Integer getIsSystem() {
		return entity.getIsSystem();
	}
	
	public Integer getRetryInterval() {
		return entity.getRetryInterval();
	}
	
	public String getRunProcessDefKey() {
		return entity.getRunProcessDefKey();
	}
	
	public String getSchedule() {
		return entity.getSchedule();
	}
	
	public String getSQLFilter() {
		return entity.getSQLFilter();
	}
	
	public String getPlugin() {
		return entity.getPlugin();
	}
	
	public void setPlugin(String value) {
		entity.setPlugin(value);
	}
	
	public void setAssignee(String assignee) {
		entity.setAssignee(assignee);
	}
	
	public void setBusinessObjectClass(String businessObjectClass) {
		entity.setBusinessObjectClass(businessObjectClass);
	}
	
	public void setCandidateGroups(String candidateGroups) {
		entity.setCandidateGroups(candidateGroups);
	}
	
	public void setCandidateUsers(String candidateUsers) {
		entity.setCandidateUsers(candidateUsers);
	}
	
	public void setDataFilter(String dataFilter) {
		entity.setDataFilter(dataFilter);
	}
	
	public void setDescription(String description) {
		entity.setDescription(description);
	}
	
	public void setErrorCode(String errorCode) {
		entity.setErrorCode(errorCode);
	}
	
	public void setErrorMessage(String errorMessage) {
		entity.setErrorMessage(errorMessage);
	}
	
	public void setForMany(Integer forMany) {
		entity.setForMany(forMany);
	}
	
	public void setForOne(Integer forOne) {
		entity.setForOne(forOne);
	}
	
	public void setIsActive(Integer isActive) {
		entity.setIsActive(isActive);
	}
	
	public void setIsAtomic(Integer isAtomic) {
		entity.setIsAtomic(isAtomic);
	}
	
	public void setIsSingleton(Integer isSingleton) {
		entity.setIsSingleton(isSingleton);
	}	
	
	public void setIsRequired(Integer isRequired) {
		entity.setIsRequired(isRequired);
	}
	
	public void setIsSystem(Integer isSystem) {
		entity.setIsSystem(isSystem);
	}
	
	public void setLastEnd(Date lastEnd) {
		entity.setLastEnd(lastEnd);
	}
	
	public void setLastStart(Date lastStart) {
		entity.setLastStart(lastStart);
	}
	
	public void setProcessDefKey(String processDefKey) {
		entity.setProcessDefKey(processDefKey);
	}
	
	public void setRetryInterval(Integer retryInterval) {
		entity.setRetryInterval(retryInterval);
	}
	
	public void setRuleAction(String ruleAction) {
		entity.setRuleAction(ruleAction);
	}
	
	public void setRuleStart(String ruleAction) {
		entity.setRuleStart(ruleAction);
	}	
	
	public void setRuleEnabled(String ruleEnabled) {
		entity.setRuleEnabled(ruleEnabled);
	}
	
	public void setRunProcessDefKey(String runProcessDefKey) {
		entity.setRunProcessDefKey(runProcessDefKey);
	}
	
	public void setSchedule(String schedule) {
		entity.setSchedule(schedule);
	}
	
	public void setSignalRef(String signalRef) {
		entity.setSignalRef(signalRef);
	}
	
	public void setSQLFilter(String sQLFilter) {
		entity.setSQLFilter(sQLFilter);
	}
	
	public boolean isExecutes() {
		return (entity.getLastStart() != null && entity.getLastEnd() == null);
	}
	
	public String getActiveName() {
		switch (getIsActive()) {
			case ActiveStatus.ACTIVE: return "активная";
			case ActiveStatus.DRAFT: return "черновик";
			case ActiveStatus.ARCHIVE: return "отключена";
		}
		return null;
	}
	
	@Override
	public void init(Set options) {
		Utils.initCollection(entity.getEvents(), options, Options.INIT_EVENTS);
		for (BizActionEventEntity evt: entity.getEvents()) {
			events.add(evt.getEventCode());
		}
	}

	public List<Integer> getEvents() {
		return events;
	}

	public void setEvents(List<Integer> events) {
		this.events = events;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public Reference getConfigType() {
		return configType;
	}

	public void setConfigType(Reference configType) {
		this.configType = configType;
	}
	
	
}
