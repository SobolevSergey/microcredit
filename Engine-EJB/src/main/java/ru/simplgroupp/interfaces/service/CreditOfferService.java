package ru.simplgroupp.interfaces.service;

import java.util.Date;

import javax.ejb.Local;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.persistence.CreditOfferEntity;
import ru.simplgroupp.transfer.CreditOffer;

@Local
public interface CreditOfferService {
	
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
	CreditOfferEntity saveNewCreditOffer(Integer creditRequestId,Integer peopleMainId,Integer userId,
			Integer offerWayId,Date offerTime,Integer creditDays,Double creditSum,Double creditStake,
			String comment) throws KassaException;

	/**
	 * принять предложение
	 * @param id - id предложения
	 * @return
	 */
	CreditOfferEntity saveAcceptOffer(int id) throws KassaException;
	/**
	 * отказаться от предложения
	 * @param id - id предложения
	 * @return
	 */
	CreditOfferEntity saveDeclineOffer(int id) throws KassaException,WorkflowException;

	/**
	 * Метод отклоняет все предложения у кого accepted == null по ID заявки
	 *
	 * @param creditRequestID ID кредитной заявке
	 */
	void declineAllOffer(Integer creditRequestID);

	/**
	 * Возвращает предложение по заявке на кредит
	 * @param creditRequestID id заявки
	 * @return
     */
	CreditOffer getCreditOfferByCreditRequest(Integer creditRequestID);
}
