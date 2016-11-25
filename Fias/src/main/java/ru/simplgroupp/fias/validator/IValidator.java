package ru.simplgroupp.fias.validator;

import java.util.List;
import java.util.Set;

import ru.simplgroupp.fias.persistence.Address;
import ru.simplgroupp.fias.ejb.IFIASService;

/**
 * Проверка адреса
 * @author irina
 *
 */
public interface IValidator {
	
	/**
	 * Проверяет и исправляет заданный адрес. Если не сказано исправлять ошибки (bCorrect = false), то в список errorList добавляем их перечень. 
	 * Если сказано исправлять ошибки (bCorrect = true), то делаем исправления, а в errorList добавляем только те ошибки, которые не удалось исправить.
	 * @param source
	 * @param kladrService
	 * @param options
	 * @param bCorrect
	 * @param errorList
	 */
	public void validate(Address source, IFIASService kladrService, Set options, boolean bCorrect, List<FiasValidateException> errorList);
}
