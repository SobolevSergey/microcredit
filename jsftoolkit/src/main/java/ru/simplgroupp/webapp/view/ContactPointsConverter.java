package ru.simplgroupp.webapp.view;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.transfer.ContactPoints;

public class ContactPointsConverter implements Converter{

	@EJB
	protected ReferenceBooksLocal refBooks;
	
	String ejbRef="java:global/microcredit/engine-ejb/ReferenceBooks!ru.simplgroupp.interfaces.ReferenceBooksLocal";

	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		checkRefBooks();
		
		int id = Convertor.toInteger(value);
		ContactPoints cp=refBooks.getContactPoint(id);
		return cp;
	
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) {
			return null;
		} else {
			return ((ContactPoints) value).getId().toString();
		}
	}

	private void checkRefBooks() {
		if (refBooks == null) {
			refBooks = (ReferenceBooksLocal) EJBUtil.findEJB(ejbRef);
		}
	}

	public String getEjbRef() {
		return ejbRef;
	}

	public void setEjbRef(String ejbRef) {
		this.ejbRef = ejbRef;
	}
}
