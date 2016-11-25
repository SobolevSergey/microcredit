package ru.simplgroupp.interfaces;

import java.util.List;
import java.util.Set;

import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.BizAction;

public interface BizActionBeanLocal {

	/**
	 * список бизнес-действий
	 * @param nFirst
	 * @param nCount
	 * @param sorting
	 * @param options
	 * @param bizActionTypeId
	 * @param signalRef
	 * @param businessObjectClass
	 * @param forOne
	 * @param forMany
	 * @param isSystem
	 * @param hasErrors
	 * @param lastStart
	 * @param lastEnd
	 * @param hasProcessDefKey
	 * @param processDefKey
	 * @return
	 */
	public List<BizAction> listBizActions(int nFirst, int nCount,
			SortCriteria[] sorting, Set options, Integer bizActionTypeId,
			String signalRef, String businessObjectClass, Integer forOne,
			Integer forMany, Integer isSystem, Boolean hasErrors,
			DateRange lastStart, DateRange lastEnd, Boolean hasProcessDefKey, String processDefKey);

	/**
	 * кол-во бизнес-действий
	 * @param bizActionTypeId
	 * @param signalRef
	 * @param businessObjectClass
	 * @param forOne
	 * @param forMany
	 * @param isSystem
	 * @param hasErrors
	 * @param lastStart
	 * @param lastEnd
	 * @param hasProcessDefKey
	 * @param processDefKey
	 * @return
	 */
	public int countBizActions(Integer bizActionTypeId, String signalRef,
			String businessObjectClass, Integer forOne, Integer forMany,
			Integer isSystem, Boolean hasErrors, DateRange lastStart,
			DateRange lastEnd, Boolean hasProcessDefKey, String processDefKey);

	/**
	 * генерим hql
	 * @param bizAct - бизнес-действие
	 * @return
	 */
	public String buildHQL(BizActionEntity bizAct);

	String buildHQLOne(BizActionEntity bizAct);

	/**
	 * сохраняем бизнес-действие
	 * @param source - бизнес-действие
	 * @return
	 */
	BizAction saveBizAction(BizAction source);

	String buildHQL(BizActionEntity bizAct, int listId);

	String buildHQLOne(BizActionEntity bizAct, int listId);

	BizAction getBizAction(int aid, Set options);

}
