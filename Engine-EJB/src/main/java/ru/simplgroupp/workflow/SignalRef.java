package ru.simplgroupp.workflow;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Ссылка на сигнал или сообщение в процессе
 * @author irina
 *
 */
public class SignalRef implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5520608952699110871L;
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
	
	public SignalRef() {
		super();
	}
	
	public SignalRef(String procDefKey, String plugName, String aname) {
		super();
		processDefKey = procDefKey;
		pluginName = plugName;
		name = aname;
	}
	
	public boolean isSignal() {
		return (name != null && name.startsWith(ProcessKeys.PREFIX_SIGNAL_SIGNAL));
	}
	
	public boolean isMessage() {
		return (name != null && name.startsWith(ProcessKeys.PREFIX_SIGNAL_MESSAGE));
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
	
	public static SignalRef valueOf(String source) {
		SignalRef ref = new SignalRef();
		String[] vals = source.split(":");
		if (vals.length == 0) {
			return null;
		} else if (vals.length == 1) {
			ref.name = (StringUtils.isBlank(vals[0]))?null:vals[0];
		} else if (vals.length == 3) {
			ref.processDefKey = (StringUtils.isBlank(vals[0]))?null:vals[0];
			ref.pluginName = (StringUtils.isBlank(vals[1]))?null:vals[1];
			ref.name = (StringUtils.isBlank(vals[2]))?null:vals[2];
		}
		return ref;
	}
	
	public static String toString(String procDefKey, String plugName, String aname) {
		SignalRef ref = new SignalRef(procDefKey, plugName, aname);
		return ref.toString();
	}
}
