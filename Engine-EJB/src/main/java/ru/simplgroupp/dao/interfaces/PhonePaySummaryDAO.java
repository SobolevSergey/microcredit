package ru.simplgroupp.dao.interfaces;

import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PhonePaySummaryEntity;

@Local
public interface PhonePaySummaryDAO {

	/**
     * ищем записи в phonepaysummary
     *
     * @param pmain   - человек
     * @param cre     - заявка
     * @param partner - партнер
     */
    List<PhonePaySummaryEntity> findPhonePaySummary(PeopleMainEntity pmain, CreditRequestEntity cre, PartnersEntity partner);

    /**
	 * сохраняем суммарную информацию по платежам за телефон
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param paymentType - вид платежа
	 * @param paymentPeriodType - период
	 * @param periodCount - номер в периоде
	 */
	PhonePaySummaryEntity savePhonePaySummary(Integer creditRequestId,Integer peopleMainId,Integer partnerId,
			Integer paymentType,String paymentPeriodType,Integer periodCount);
	
	/**
	 * возвращаем запись в платежах за телефон по id
	 * @param id - id записи
	 * @return
	 */
	PhonePaySummaryEntity getPhonePaySummary(Integer id);
}
