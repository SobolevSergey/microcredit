package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.KzEgovDataAtsTypeEntity;
import ru.simplgroupp.persistence.KzEgovDataGeonimsTypeEntity;

import javax.ejb.Local;

/**
 * Data Access Object для сущностей
 * Портала Открытые данные Электронного правительства Республики Казахстан
 */
@Local
public interface KzEgovDataDAO {

	/**
	 * Тип административно-территориального устройства по id
	 * @param id - id Тип административно-территориального устройства
	 * @return
	 */
	KzEgovDataAtsTypeEntity getKzEgovDataAtsTypeEntity(long id);

	/**
	 *
	 * Тип составных частей населенных пунктов по id
	 * @param id - id Тип составных частей населенных пунктов
	 * @return
	 */
    KzEgovDataGeonimsTypeEntity getKzEgovDataGeonimsTypeEntity(long id);

}
