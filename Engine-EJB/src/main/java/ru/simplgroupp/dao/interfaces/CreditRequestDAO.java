package ru.simplgroupp.dao.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.transfer.CreditRequest;

@Local
public interface CreditRequestDAO extends ListContainerDAO<CreditRequest> {
	
	/**
	 * возвращаем заявку по id
	 * @param id - id заявки
	 * @param options - что инициализируем
	 * @return
	 */
	CreditRequest getCreditRequest(int id, Set options);

	/**
	 * возвращаем заявку по id
	 * @param id - id заявки
	 * @return
	 */
	CreditRequestEntity getCreditRequestEntity(int id);

	/**
	 * сохраняем заявку
	 * @param creditRequest - заявка
	 */	
	public CreditRequestEntity saveCreditRequest(CreditRequestEntity creditRequest);

	/**
	 * список активных заявок человека
	 * @param peopleMainId - id человека
	 * @return
	 */
	List<CreditRequestEntity> findCreditRequestActive(int peopleMainId,List<Integer> statuses);

	/**
	 * список заявок человека
	 * @param peopleMainId - id человека
	 * @return
	 */
	List<CreditRequestEntity> findCreditRequestsForMan(int peopleMainId);
	/**
	 * список заявок человека от партнера
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @return
	 */
	List<CreditRequestEntity> findPartnersCreditRequests(int peopleMainId,int partnerId);
	
	/**
	 * список активных заявок человека
	 * @param peopleMainId - id человека
	 * @return
	 */
	List<CreditRequest> findCreditRequestActive(int peopleMainId, Set options)
			throws Exception;

	/**
	 * есть ли кредитные заявки для определенного продукта
	 * @param productId - id продукта
	 * @return
	 */
	boolean findCreditRequestsWithProduct(Integer productId);
    /**
     * ищем заявку на подпись
     * @param peopleMainId -id человека
     * @return 
     */
	List<CreditRequestEntity> findCreditRequestWaitingSign(int peopleMainId);
	/**
	 * сохраняем другие сумму и срок
	 * @param creditRequestId - id заявки
	 * @param creditSum - сумма
	 * @param creditDays - срок в днях
	 * @return
	 */
	CreditRequestEntity saveAnotherSumAndDays(Integer creditRequestId,Double creditSum,Integer creditDays);

	/**
	 * сохраняем другие сумму, срок и ставку в заявке
	 *
	 * @param creditRequestId - id заявки
	 * @param creditSum       - сумма
	 * @param creditStake     - ставка
	 * @param creditDays      - срок в днях
	 * @return заявка
	 */
	CreditRequestEntity saveAnotherSumAndDays(Integer creditRequestId, Double creditSum, Double creditStake, Integer creditDays);

    List<CreditRequestEntity> findCreditRequestsToUpload(int partnerId, int requestStatusId);
}
