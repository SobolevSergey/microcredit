package ru.simplgroupp.ejb;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.interfaces.ApplicationEventListener;
import ru.simplgroupp.interfaces.Closeable;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.util.EventKeys;
import ru.simplgroupp.workflow.ProcessKeys;

/**
 * Конфигурация, в которой выполняются действия в ActionProcessor
 * @author irina
 *
 */
public class ActionContext implements Serializable, Closeable, ApplicationEventListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6985940135827325790L;
	
	String customizationKey;
	boolean persistent = false;
	transient PluginsSupport plugins;
	transient Map<String, PluginState> pluginStates;
	
	public ActionContext() {
		super();
	}
	
	public ActionContext(String customKey, boolean bPersistent) {
		super();
		customizationKey = customKey;
		persistent = bPersistent;
		plugins = new PluginsSupport();
		pluginStates = new HashMap<String, PluginState>(10);
	}
	
	public PluginState getPluginState(String pluginName) {
		return pluginStates.get(pluginName);
	}

	public String getCustomizationKey() {
		return customizationKey;
	}
	
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }	
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
    
    private Object writeReplace() {
        return new ActionContextProxy(customizationKey, persistent);
    }

	public PluginsSupport getPlugins() {
		return plugins;
	}
	
	protected void initPluginStates(RuntimeServices runtimeServices) {
		for (PluginConfig plc: plugins.getPluginConfigs() ) {
			String actualPrefix = getActualPrefix(plc.getPluginName());
			PluginState pluginState = (PluginState) runtimeServices.getServBean().startStateCached(actualPrefix + ".state");
			if (pluginState == null) {
				pluginState = new PluginState();
				pluginState.resetCurrentPacketID();
				
				runtimeServices.getServBean().putStateCached(actualPrefix + ".state", pluginState);
				pluginState = (PluginState) runtimeServices.getServBean().startStateCached(actualPrefix + ".state");
				pluginState.applyConfig(plc, this, runtimeServices);
				runtimeServices.getServBean().subscribe(EventKeys.EVENT_PLUGINS_SETTINGS_CHANGED, pluginState);
			}			
		}
	}
	
	protected String getActualPrefix(String systemName) {
		String ss = ProcessKeys.PREFIX_PLUGIN + systemName;
		if (! StringUtils.isBlank(customizationKey)) {
			ss = ss + "_" + customizationKey;
		}
		return ss;
	}	

	@Override
	public void close(RuntimeServices runtimeServices, Object... params) {
		for (Map.Entry<String, PluginState> entry: pluginStates.entrySet()) {
			runtimeServices.getServBean().finishStateCached(getActualPrefix(entry.getKey()) + ".state");
		}
	}
	
	@Override
	public void firedEvent(String eventName, Map<String, Object> params, RuntimeServices runtimeServices) {
		if (eventName.equals(EventKeys.EVENT_PLUGINS_SETTINGS_CHANGED)) {
			if (params == null || params.size() == 0) {
				plugins.applyConfigs();
			} else {
				for (String plgName: params.keySet()) {
					plugins.applyConfig(plgName);
				}
			}
		}
	}

	public boolean isPersistent() {
		return persistent;
	}
	
	public PluginConfigProxy getPluginConfigProxy(String pluginName) {
		PluginConfigProxy proxy = new PluginConfigProxy();
		proxy.customizationKey = customizationKey;
		proxy.pluginName = pluginName;
		return proxy;
	}

}
