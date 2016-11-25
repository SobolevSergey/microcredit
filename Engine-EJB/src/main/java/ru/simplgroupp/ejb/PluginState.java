package ru.simplgroupp.ejb;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.activiti.engine.runtime.ProcessInstance;

import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.interfaces.ApplicationEventListener;
import ru.simplgroupp.interfaces.Closeable;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal.Mode;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.util.EventKeys;

public class PluginState implements ApplicationEventListener, Closeable {
	protected String currentPacketID;
	protected SSLContext sslContext;
	protected ProcessInstance procPacket = null; // процесс отработки пакетного режима для данного плагина
	protected PluginConfig lastConfig = null;
	protected Map<String,Object> variables = new HashMap<String,Object>(0);
	
	public String getCurrentPacketID() {
		return currentPacketID;
	}
	
	public void resetCurrentPacketID() {
		currentPacketID = UUID.randomUUID().toString();
	}
	
	public SSLContext getSSLContext() {
		return sslContext;
	}
	
	public void setSSLContext(SSLContext con) {
		sslContext = con;
	}

	@Override
	public void firedEvent(String eventName, Map<String, Object> params, RuntimeServices runtimeServices) {
		if (EventKeys.EVENT_PLUGINS_SETTINGS_CHANGED.equals(eventName)) {
			if (params == null || params.containsKey(lastConfig.getPluginName())) {
				// TODO перечитать настройки
			}
		}
		// TODO Auto-generated method stub
		
	}

	public void applyConfig(PluginConfig plc, ActionContext actionContext, RuntimeServices runtimeServices) {
		lastConfig = plc;
		// TODO Auto-generated method stub
		checkPacketProcess(plc, actionContext, runtimeServices);
	}
	
	protected void checkPacketProcess(PluginConfig plc, ActionContext actionContext, RuntimeServices runtimeServices) {
		if (! plc.getIsActive()) {
			return;
		}
		EnumSet<Mode> modes = plc.getPlugin().getModesSupported();
		if (modes.contains(PluginSystemLocal.Mode.PACKET) && PluginSystemLocal.Mode.PACKET.equals(plc.getMode())) {
			try {
				procPacket = runtimeServices.getWorkflowBean().startOrFindPacketProcess(plc, actionContext);
			} catch (WorkflowException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	@Override
	public void close(RuntimeServices runtimeServices, Object... params) {
		if (procPacket != null) {
			// прерываем процесс			
			runtimeServices.getWorkflowBean().endPacketProcess(procPacket.getProcessInstanceId(), lastConfig.getPluginName());
			procPacket = null;			
		}
		runtimeServices.getServBean().unsubscribe(null, this);
		lastConfig = null;
	}

	public PluginConfig getLastConfig() {
		return lastConfig;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

}
