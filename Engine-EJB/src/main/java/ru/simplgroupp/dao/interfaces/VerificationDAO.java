package ru.simplgroupp.dao.interfaces;

import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.VerificationEntity;
import ru.simplgroupp.toolkit.common.DateRange;

@Local
public interface VerificationDAO {

	/**
	 * ищем верификации
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param verificationDate - дата верификации
	 * @return
	 */
	List<VerificationEntity> findVerification(Integer creditRequestId,Integer peopleMainId,
			Integer partnerId,DateRange verificationDate);
	/**
	 * ищем верификации
	 * @param creditRequestId - id заявки
	 * @param peopleMainId - id человека
	 * @param partnerId - id партнера
	 * @param verificationDate - дата верификации
	 * @return
	 */
	VerificationEntity findOneVerification(Integer creditRequestId,Integer peopleMainId,
			Integer partnerId,DateRange verificationDate);
  /**
   * сохраняем данные верификации
   * @param creditRequest - заявка
   * @param peopleMain - человек
   * @param partner - партнер
   * @param verScore - скоринг верификации
   * @param valScore - скоринг валидации
   * @param verifyPersonal - верификация по ПД
   * @param verifyDocument - верификация по документу
   * @param verifyPhone - верификация по телефону
   * @param verifyAddress - верификация по адресу
   * @param verifyInn - верификация по ИНН
   */
  	VerificationEntity saveVerification(CreditRequestEntity creditRequest,PeopleMainEntity peopleMain,PartnersEntity partner,Double verScore,Double valScore,Integer verifyPersonal,Integer verifyDocument,Integer verifyPhone,Integer verifyAddress,Integer verifyInn);
  	/**
  	 * сохраняем верификацию
  	 * @param verification - верификация
  	 * @return
  	 */
  	VerificationEntity saveVerification(VerificationEntity verification);
  
  	/**
  	 * возвращает верификацию по id
  	 * @param verificationId - id верификации
  	 * @return
  	 */
  	VerificationEntity getVerificationEntity(Integer verificationId);
  	/**
  	 * удаляем верификацию
  	 * @param id - id верификации
  	 */
  	void removeVerification(Integer id);
}
