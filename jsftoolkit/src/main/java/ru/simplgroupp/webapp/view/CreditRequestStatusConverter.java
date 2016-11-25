package ru.simplgroupp.webapp.view;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.CreditStatusEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.CreditStatus;

public class CreditRequestStatusConverter implements Converter {
	
	@EJB
	protected ReferenceBooksLocal refBooks;

	@Override
	public Object getAsObject(FacesContext facesCtx, UIComponent component, String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}

		int id = Convertor.toInteger(value);
		
		CreditStatusEntity ent = refBooks.getCreditRequestStatus(id); 		
		return new CreditStatus(ent);
	}

	@Override
	public String getAsString(FacesContext facesCtx, UIComponent component, Object value) {
		if (value == null) {
			return null;
		} else {
			return ((CreditStatus) value).getId().toString();
		}
	}

}
