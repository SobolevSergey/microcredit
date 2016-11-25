package ru.simplgroupp.dao.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.persistence.BizActionEventEntity;
import ru.simplgroupp.persistence.BizActionTypeEntity;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.BizActionType;

@Local
public interface BizActionDAO extends ListContainerDAO<BizAction> {

	/**
	 * возвращает бизнес-действие
	 * @param actId - id бизнес-действия
	 * @return
	 */
	BizActionEntity getBizActionEntity(int actId);
	/**
	 * сохраняем бизнес-действие
	 * @param source - бизнес-действие
	 * @return
	 */
	BizActionEntity saveBizActionEntity(BizActionEntity source);

	List<BizActionEntity> listBOActions();

	/**
	 * список действий для процесса
	 * 
	 * @param processDefKey - процесс
	 * @return
	 */
	List<BizActionEntity> listBPActions(String processDefKey);

	/**
	 * список действий для процесса
	 * 
	 * @param processDefKey - процесс
	 * @return
	 */
	List<BizActionEntity> listBPActionsProduct(String processDefKey,Integer productId);
	/**
	 * список действий для всех процессов
	 * @return
	 */
	List<BizActionEntity> listBPActions();

	List<BizActionEventEntity> listBizActionEvents();

	List<BizActionEntity> listBOActions(String businessObjectClass,
			Boolean forOne, Boolean forMany, Integer isSingleton);
    /**
     * список видов бизнес-действий
     * @return
     */
	List<BizActionType> getBizActionTypes();
    /**
     * возвращает действие по бизнес-объекту
     * @param businessObjectClass - класс бизнес-объекта
     * @param signalRef - сигнал
     * @return
     */
	BizActionEntity findBizObjectAction(String businessObjectClass,
			String signalRef);
	/**
     * возвращает действие по бизнес-объекту
     * @param businessObjectClass - класс бизнес-объекта
     * @param signalRef - сигнал
     * @return
     */
	BizActionEntity findBizObjectActionProduct(String businessObjectClass,
			String signalRef,Integer productId);
    /**
     * удаляет бизнес-действие
     * @param source - бизнес-действие
     */
	void removeBizActionEntity(BizActionEntity source);
	 /**
     * удаляет событие для бизнес-действия
     * @param evt - событие для бизнес-действия
     */
	void removeBizActionEventEntity(BizActionEventEntity evt);

	/**
	 * возвращает вид бизнес-действия
	 * @param id - id вида бизнес-действия
	 * @return
	 */
	BizActionTypeEntity getBizActionTypeEntity(int id);
	/**
	 * возвращает вид бизнес-действия
	 * @param id - id вида бизнес-действия
	 * @return
	 */
	BizActionType getBizActionType(int id);

	/**
	 * возвращает бизнес-действие
	 * @param aid - id бизнес-действия
	 * @param options - что инициализируем
	 * @return
	 */
	BizAction getBizAction(int aid, Set options);

	/**
	 * сохраняем бизнес-действие
	 * @param ent - бизнес-действие
	 * @return
	 */
	BizActionEventEntity saveBizActionEventEntity(BizActionEventEntity ent);

	BizActionEntity findProcessAction(String businessObjectClass,
			String procDefKey, String plugin, String signalRef);

	BizActionEntity getBizActionEntityLock(int actId);

	List<BizActionEntity> findBizObjectActions(String signalRef);
	List<BizActionEntity> listBOActions(String businessObjectClass,
			Boolean forOne, Boolean forMany, Integer isSingleton,
			String roleName);

}
