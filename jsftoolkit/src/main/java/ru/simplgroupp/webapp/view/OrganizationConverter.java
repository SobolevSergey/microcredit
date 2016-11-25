package ru.simplgroupp.webapp.view;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.transfer.Organization;

public class OrganizationConverter implements Converter{

	@EJB
	OrganizationService orgService;
	
	String ejbRef= "java:global/microcredit/engine-ejb/OrganizationServiceImpl!ru.simplgroupp.interfaces.service.OrganizationService";

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		if (StringUtils.isBlank(value)) {
			return null;
		}

		checkOrgService();
		
		int id = Convertor.toInteger(value);
		Organization ref = orgService.findOrganization(id);
		return ref;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) {
			return null;
		} else {
			return ((Organization) value).getId().toString();
		}
	}

	private void checkOrgService() {
		if (orgService == null) {
			orgService = (OrganizationService) EJBUtil.findEJB(ejbRef);
		}
	}

	public String getEjbRef() {
		return ejbRef;
	}

	public void setEjbRef(String ejbRef) {
		this.ejbRef = ejbRef;
	}

}
