package ru.simplgroupp.dao.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.persistence.CreditOfferEntity;
import ru.simplgroupp.transfer.CreditOffer;

@Local
public interface CreditOfferDAO {

	/**
	 * возвращает предложение других условий по id
	 * @param id - id предложения
	 * @return
	 */
	CreditOfferEntity getCreditOfferEntity(int id);
	/**
	 * сохраняем предложение других условий
	 * @param creditOffer - предложение других условий
	 * @return
	 */
	CreditOfferEntity saveCreditOffer(CreditOfferEntity creditOffer);
	/**
	 * удаляем предложение других условий
	 * @param id - id предложения
	 */
	void deleteCreditOffer(int id);
	/**
	 * новое предложение других условий
	 * @param creditRequestId - id заявки
	 * @param peopleMainId -id человека
	 * @param userId - id пользователя
	 * @param offerWayId - id вида предложения
	 * @param offerTime - время предложения
	 * @param creditDays - дни
	 * @param creditSum - сумма
	 * @param creditStake - ставка
	 * @param comment - комментарий
	 * @return
	 */
	CreditOfferEntity newCreditOffer(Integer creditRequestId,Integer peopleMainId,Integer userId,
			Integer offerWayId,Date offerTime,Integer creditDays,Double creditSum,Double creditStake,
			String comment);
	/**
	 * принять предложение
	 * @param id - id предложения
	 * @return
	 */
	CreditOfferEntity acceptOffer(int id);
	/**
	 * отказаться от предложения
	 * @param id - id предложения
	 * @return
	 */
	CreditOfferEntity declineOffer(int id);
	/**
	 * список предложений других условий по заявке
	 * @param creditRequestId - id заявки
	 * @return
	 */
	List<CreditOfferEntity> findCreditOfferByCreditRequest(Integer creditRequestId);

	/**
	 * список предложений других условий по заявке
	 * @param creditRequestID - id заявки
	 * @return
	 */
	List<CreditOffer> getCreditOfferByCreditRequest(Integer creditRequestID);

	/**
	 * Метод отклоняет все предложения у кого accepted == null по ID заявки
	 *
	 * @param creditRequestID ID кредитной заявке
	 */
	void declineAllOffer(Integer creditRequestID);
}
