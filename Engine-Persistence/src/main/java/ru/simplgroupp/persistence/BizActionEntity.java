package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BizActionEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4270893932579959415L;

	public Integer txVersion = 0;
	/**
	 * тип действия
	 */
	private BizActionTypeEntity bizActionType;
	/**
	 * сигнал
	 */
	private String signalRef;
	/**
	 * бизнес-объект
	 */
	private String businessObjectClass;
	/**
	 * описание
	 */
	private String description;
	/**
	 * кому назначено
	 */
	private String assignee;
	/**
	 * группа
	 */
	private String candidateGroups;
	/**
	 * пользователь
	 */
	private String candidateUsers;
	/**
	 * действие для 1 объекта
	 */
	private Integer forOne;
	/**
	 * действие для многих объектов
	 */
	private Integer forMany;
	/**
	 * фильтр sql
	 */
	private String SQLFilter;
	/**
	 * фильтр данных
	 */
	private String dataFilter;
	/**
	 * расписание
	 */
	private String schedule;
	/**
	 * 
	 */
	private String runProcessDefKey;
	/**
	 * событие системное
	 */
	private Integer isSystem;
	/**
	 * интервал для следующей попытки
	 */
	private Integer retryInterval;
	/**
	 * код ошибки
	 */
	private String errorCode;
	/**
	 * сообщение об ошибке
	 */
	private String errorMessage;
	/**
	 * последний раз стартовало
	 */
	private Date lastStart;
	/**
	 * последний раз завершилось
	 */
	private Date lastEnd;
	/**
	 * процесс
	 */
	private String processDefKey;
	/**
	 * правило, когда возможно действие
	 */
	private String ruleEnabled;
	/**
	 * правило для выполнения
	 */
	private String ruleAction;
	/**
	 * правило для стартовой формы
	 */
	private String ruleStart;	
	/**
	 * активное
	 */
	private Integer isActive;
	/**
	 * требуется
	 */
	private Integer isRequired;
	/**
	 * 
	 */
	private Integer isAtomic;
	/**
	 * 
	 */
	private String plugin;
	/**
	 * 
	 */
	private Integer listId;
	/**
	 * 
	 */
	private Integer isSingleton = 1;
	/**
     * продукт
     */
    private ProductsEntity productId;
    /**
     * что кофигурируем (по справочнику)
     */
    private ReferenceEntity configTypeId;
	/**
	 * события
	 */
	private List<BizActionEventEntity> events = new ArrayList<BizActionEventEntity>(0);
	
	private List<BizActionParamEntity> params = new ArrayList<BizActionParamEntity>(0);
	
    public BizActionEntity(){
    	super();
    }
    
  	public BizActionTypeEntity getBizActionType() {
		return bizActionType;
	}

	public void setBizActionType(BizActionTypeEntity bizActionType) {
		this.bizActionType = bizActionType;
	}

	public String getSignalRef() {
		return signalRef;
	}

	public void setSignalRef(String signalRef) {
		this.signalRef = signalRef;
	}

	public String getBusinessObjectClass() {
		return businessObjectClass;
	}

	public void setBusinessObjectClass(String businessObjectClass) {
		this.businessObjectClass = businessObjectClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCandidateGroups() {
		return candidateGroups;
	}

	public void setCandidateGroups(String candidateGroups) {
		this.candidateGroups = candidateGroups;
	}

	public String getCandidateUsers() {
		return candidateUsers;
	}

	public void setCandidateUsers(String candidateUsers) {
		this.candidateUsers = candidateUsers;
	}

	public Integer getForOne() {
		return forOne;
	}

	public void setForOne(Integer forOne) {
		this.forOne = forOne;
	}

	public Integer getForMany() {
		return forMany;
	}

	public void setForMany(Integer forMany) {
		this.forMany = forMany;
	}

	public String getSQLFilter() {
		return SQLFilter;
	}

	public void setSQLFilter(String sQLFilter) {
		SQLFilter = sQLFilter;
	}

	public String getDataFilter() {
		return dataFilter;
	}

	public void setDataFilter(String dataFilter) {
		this.dataFilter = dataFilter;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getRunProcessDefKey() {
		return runProcessDefKey;
	}

	public void setRunProcessDefKey(String runProcessDefKey) {
		this.runProcessDefKey = runProcessDefKey;
	}

	public Integer getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Integer isSystem) {
		this.isSystem = isSystem;
	}

	public Integer getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(Integer retryInterval) {
		this.retryInterval = retryInterval;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Date getLastStart() {
		return lastStart;
	}

	public void setLastStart(Date lastStart) {
		this.lastStart = lastStart;
	}

	public Date getLastEnd() {
		return lastEnd;
	}

	public void setLastEnd(Date lastEnd) {
		this.lastEnd = lastEnd;
	}

	public String getProcessDefKey() {
		return processDefKey;
	}

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}

	public String getRuleEnabled() {
		return ruleEnabled;
	}

	public void setRuleEnabled(String ruleEnabled) {
		this.ruleEnabled = ruleEnabled;
	}

	public String getRuleAction() {
		return ruleAction;
	}

	public void setRuleAction(String ruleAction) {
		this.ruleAction = ruleAction;
	}

	public String getRuleStart() {
		return ruleStart;
	}

	public void setRuleStart(String ruleStart) {
		this.ruleStart = ruleStart;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
	}

	public Integer getIsAtomic() {
		return isAtomic;
	}

	public void setIsAtomic(Integer isAtomic) {
		this.isAtomic = isAtomic;
	}

	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public Integer getListId() {
		return listId;
	}

	public void setListId(Integer listId) {
		this.listId = listId;
	}

	public Integer getIsSingleton() {
		return isSingleton;
	}

	public void setIsSingleton(Integer isSingleton) {
		this.isSingleton = isSingleton;
	}

	public ProductsEntity getProductId() {
		return productId;
	}

	public void setProductId(ProductsEntity productId) {
		this.productId = productId;
	}

	public ReferenceEntity getConfigTypeId() {
		return configTypeId;
	}

	public void setConfigTypeId(ReferenceEntity configTypeId) {
		this.configTypeId = configTypeId;
	}

	public List<BizActionEventEntity> getEvents() {
		return events;
	}

	public void setEvents(List<BizActionEventEntity> events) {
		this.events = events;
	}

	public List<BizActionParamEntity> getParams() {
		return params;
	}

	public void setParams(List<BizActionParamEntity> params) {
		this.params = params;
	}
    
    
	
}
