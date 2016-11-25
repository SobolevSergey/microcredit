package ru.simplgroupp.ejb;

import java.util.Map;

import javax.ejb.EJB;

import ru.simplgroupp.interfaces.ServiceBeanLocal;

abstract public class AbstractPluginBean {
	
	@EJB
	ServiceBeanLocal serviceBean;
	
	abstract public String getSystemName();
	
	public Integer getPartnerId() {
		return null;
	}
	
	public PluginConfig getDebugConfig(String customKey) {
		ActionContext context = serviceBean.createActionContext(customKey, true);
		PluginConfig cfg = context.getPlugins().getPluginConfig(getSystemName());
		return cfg;
	}
	
	public PluginConfig getWorkConfig() {
		ActionContext context = serviceBean.createActionContext(null, true);
		PluginConfig cfg = context.getPlugins().getPluginConfig(getSystemName());
		return cfg;
	}	
	
	public Map<String, Object> getPersistentVariables(String customKey) {
		ActionContext context = serviceBean.createActionContext(null, true);
		return context.getPluginState(getSystemName()).getVariables();
	}
}
