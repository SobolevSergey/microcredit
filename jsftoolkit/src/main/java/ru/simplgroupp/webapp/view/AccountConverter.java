package ru.simplgroupp.webapp.view;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.AccountDAO;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.transfer.Account;

public class AccountConverter implements Converter {
	
	@EJB
	protected AccountDAO accDAO;
	
	String ejbAcc= "java:global/microcredit/engine-ejb/AccountDAOImpl!ru.simplgroupp.dao.interfaces.AccountDAO";
	
	@Override
	public Object getAsObject(FacesContext facesCtx, UIComponent component, String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
        CheckService();
		int id = Convertor.toInteger(value);
		Account ref = accDAO.getAccount(id, null);
		return ref;
	}

	@Override
	public String getAsString(FacesContext facesCtx, UIComponent component, Object value) {
		if (value == null) {
			return null;
		} else {
			return ((Account) value).getId().toString();
		}
	}
	
	private void CheckService(){
		
		if (accDAO == null) {
			accDAO = (AccountDAO) EJBUtil.findEJB(ejbAcc);
		}
	}

	
	
}
