package ru.simplgroupp.webapp.view;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.transfer.Roles;

public class RolesConverter implements Converter{

	@EJB
	protected UsersBeanLocal usersBean;
	String ejbName="java:global/microcredit/engine-ejb/UsersBean!ru.simplgroupp.interfaces.UsersBeanLocal";
	
	@Override
	public Object getAsObject(FacesContext facesCtx, UIComponent component, String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		checkUsersBean();
		
		int id = Convertor.toInteger(value);
		Roles role=usersBean.getRole(id);
		return role;
	}

	@Override
	public String getAsString(FacesContext facesCtx, UIComponent component, Object value) {
		if (value == null) {
			return null;
		} else {
			return ((Roles) value).getId().toString();
		}
	}

	private void checkUsersBean() {
		if (usersBean == null) {
			usersBean=(UsersBeanLocal) EJBUtil.findEJB(ejbName);
		}
	}

	public String getEjbName() {
		return ejbName;
	}

	public void setEjbRef(String ejbName) {
		this.ejbName = ejbName;
	}
}
