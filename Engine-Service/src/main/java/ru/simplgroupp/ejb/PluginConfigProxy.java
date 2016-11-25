package ru.simplgroupp.ejb;

import java.io.Serializable;

import org.activiti.engine.impl.context.Context;

import ru.simplgroupp.interfaces.ServiceBeanMini;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.workflow.ProcessKeysMini;

public class PluginConfigProxy implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6094630615294814227L;
	
	public String pluginName;
	public String customizationKey;
	
	public PluginConfigProxy() {
		
	}
	
	public PluginConfigProxy(String pluginName, String customKey) {
		this.pluginName = pluginName;
		this.customizationKey = customKey;
	}
	
	private Object readResolve() {
		ServiceBeanMini servBean = (ServiceBeanMini) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeysMini.VAR_SERVICE);
		return servBean.getPluginConfigObject(pluginName, customizationKey);
	}
}
