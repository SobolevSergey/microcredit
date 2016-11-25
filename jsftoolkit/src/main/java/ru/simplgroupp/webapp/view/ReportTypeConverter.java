package ru.simplgroupp.webapp.view;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.interfaces.ReportBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.transfer.ReportType;

public class ReportTypeConverter implements Converter{

	@EJB
	protected ReportBeanLocal reportBean;

	String ejbRep= "java:global/microcredit/engine-ejb/ReportBean!ru.simplgroupp.interfaces.ReportBeanLocal";
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}

		checkReportBean();
		
		int id = Convertor.toInteger(value);
		ReportType reportType = reportBean.getReportType(id);
		return reportType;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) {
			return null;
		} else {
			return ((ReportType) value).getId().toString();
		}
	}
	
	private void checkReportBean() {
		if (reportBean == null) {
			reportBean=(ReportBeanLocal) EJBUtil.findEJB(ejbRep);
		}
	}

	public String getEjbRep() {
		return ejbRep;
	}

	public void setEjbRep(String ejbRep) {
		this.ejbRep = ejbRep;
	}

	
}
