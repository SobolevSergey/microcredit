package ru.simplgroupp.fias.validator;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import ru.simplgroupp.fias.persistence.Address;
import ru.simplgroupp.fias.ejb.IFIASService;
import org.apache.commons.lang.StringUtils;

/**
 * Литера может быть только одиночной русской буквой.
 * @author irina
 *
 */
public class ValidatorLiterRussian implements IValidator 
{

	public static final String russianLiter = "абвгдеёжзийклмнопрстуфхцчшщэюя";
	public static final String latinReplace = "ab   e         o pc y x       ";
	
	@Override
	public void validate(Address source, IFIASService kladrService,
			Set options, boolean bCorrect,
			List<FiasValidateException> errorList) 
	{
		if (StringUtils.isEmpty(source.getLiter()))
			return;
		
		String sliter = source.getLiter().trim().toLowerCase();
		source.setLiter(sliter);
		
		if (sliter.length() > 1) {
			if (bCorrect)
				sliter = sliter.substring(0,1);
			else
				errorList.add(new FiasValidateException(FiasValidateException.CODE_LITER_TOO_LONG, "Литера может состоять только из одной буквы"));
		} 
		
		if (! StringUtils.containsOnly(sliter, russianLiter)) {
			if (bCorrect) {
				// если пользователь ввёл похожую латинскую букву, пытаемся заменить
				int n = latinReplace.indexOf(sliter.charAt(0));
				if (latinReplace.charAt(n) == ' ') {
					// нет замены
					sliter = "";
				} else
					sliter = russianLiter.charAt(n) + "";
			} else
				errorList.add(new FiasValidateException(FiasValidateException.CODE_LITER_NON_RUSSIAN, "Литера может состоять только из русской буквы"));
		}
		
		if (bCorrect)
			source.setLiter(sliter);
	
	}

}
