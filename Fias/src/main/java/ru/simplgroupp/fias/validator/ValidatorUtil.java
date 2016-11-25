package ru.simplgroupp.fias.validator;

import java.util.ArrayList;
import java.util.Set;

import ru.simplgroupp.fias.persistence.Address;
import ru.simplgroupp.fias.ejb.IFIASService;

abstract public class ValidatorUtil {
	
	public static final IValidator[] VALS_STANDART_RUS = new IValidator[] { new ValidatorHouseInteger(), new ValidatorLiterRussian() }; 
	
	/**
	 * Проверяет и исправляет адрес.
	 * @param source - адрес для проверки и исправления
	 * @param validators - набор валидаторов
	 * @param kladrService - сервис КЛАДР
	 * @param options - дополнительные настройки для валидаторов
	 * @param bCorrect - true - если исправлять ошибки, false - только информировать
	 * @return - список ошибок
	 */
	public static FiasValidateException[] validate(Address source, IValidator[] validators, IFIASService kladrService, Set options, boolean bCorrect) 
	{
		ArrayList<FiasValidateException> errorList = new ArrayList<FiasValidateException>(5);
		for (IValidator val: validators) {
			val.validate(source, kladrService, options, bCorrect, errorList);
		}
		FiasValidateException[] res = new FiasValidateException[ errorList.size() ];
		if (errorList.size() > 0)
			res = errorList.toArray(res);
		return res;
	}
	
	public static FiasValidateException[] validateStdRus(Address source, IFIASService kladrService, boolean bCorrect)
	{
		return validate(source, VALS_STANDART_RUS, kladrService, null, bCorrect);
	}
	
	/**
	 * Удаляет нецифровые символы
	 * @param source
	 * @return
	 */
	public static final String deleteNonDigits(String source) {
		String dest = "";
		String digits = "0123456789";
		
		for (int i = 0; i < source.length(); i++) {
			char ch = source.charAt(i);
			if (digits.indexOf(ch) >= 0)
				dest = dest + ch;
		}
		
		return dest;
	}
}
