package ru.simplgroupp.fias.validator;

import java.util.List;
import java.util.Set;

import ru.simplgroupp.fias.persistence.Address;
import ru.simplgroupp.fias.ejb.IFIASService;
import org.apache.commons.lang.StringUtils;

/**
 * Номер дома должен быть целым числом
 * @author irina
 *
 */
public class ValidatorHouseInteger implements IValidator {

	@Override
	public void validate(Address source, IFIASService fiasService, Set options, boolean bCorrect, List<FiasValidateException> errorList) 
	{
		if (StringUtils.isEmpty(source.getHouse()))
			return;
		
		try {
			Integer ihouse = Integer.parseInt(source.getHouse());
			source.setHouse(ihouse.toString());
		} catch (Exception ex) {
			// содержит некорректные символы, удаляем их
			if (bCorrect) {
				String shouse = ValidatorUtil.deleteNonDigits(source.getHouse());
				source.setHouse(shouse);
			} else
				errorList.add(new FiasValidateException(FiasValidateException.CODE_INVALID_HOUSE, "Номер дома должен содержать только цифры", ex));	
		}
	}

}
