package ru.simplgroupp.workflow;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Ссылка на состояние процесса (задачу, event gateway)
 * @author irina
 *
 */
public class StateRef implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1820978894061390452L;
	/**
	 * название процесса
	 */
	private String processDefKey;
	/**
	 * название плагина
	 */
	private String pluginName;
	/**
	 * название сигнала
	 */
	private String name;
	
	public StateRef() {
		super();
	}
	
	public StateRef(String procDefKey, String plugName, String aname) {
		super();
		processDefKey = procDefKey;
		pluginName = plugName;
		name = aname;
	}
	
	public String getProcessDefKey() {
		return processDefKey;
	}
	public String getPluginName() {
		return pluginName;
	}
	public String getName() {
		return name;
	}
	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return StringUtils.defaultIfBlank(processDefKey, "") + ":" + StringUtils.defaultIfBlank(pluginName, "") + ":" + name; 
	}
	
	public static StateRef valueOf(String source) {
		StateRef ref = new StateRef();
		String[] vals = source.split(":");
		ref.processDefKey = (StringUtils.isBlank(vals[0]))?null:vals[0];
		ref.pluginName = (StringUtils.isBlank(vals[1]))?null:vals[1];
		ref.name = (StringUtils.isBlank(vals[2]))?null:vals[2];
		return ref;
	}
	
	public static String toString(String procDefKey, String plugName, String aname) {
		StateRef ref = new StateRef(procDefKey, plugName, aname);
		return ref.toString();
	}
	
	public void clear() {
		processDefKey = null;
		pluginName = null;
		name = null;
	}
}
