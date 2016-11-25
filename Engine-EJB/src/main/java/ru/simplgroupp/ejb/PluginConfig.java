package ru.simplgroupp.ejb;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.interfaces.PluginConfigReadOnly;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.workflow.ProcessKeys;

public class PluginConfig implements Serializable, PluginConfigReadOnly {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -274500659239370371L;
	
	protected transient PluginSystemLocal plugin = null;
	protected PluginSystemLocal.Mode mode = PluginSystemLocal.Mode.SINGLE;
	protected PluginSystemLocal.ExecutionMode executionMode = PluginSystemLocal.ExecutionMode.AUTOMATIC;
	protected PluginSystemLocal.SyncMode syncMode = PluginSystemLocal.SyncMode.SYNC; 
	protected long packetInterval = 60000;
	protected String packetSchedule;
	protected long asyncInterval = 5*60000;
	protected String asyncSchedule;
	protected long retryInterval = 3*60000;
	protected String retrySchedule;
	protected boolean useFake = false;
	protected String workEJBName;
	protected String fakeEJBName;
	protected String pluginName;
	protected String processName = ProcessKeys.DEF_SUB_STANDART;
	protected String processPacketName = ProcessKeys.PREFIX_DEF_SUB_PACKET;
	protected Partner partner;
	protected String[] propertiesImports = null;
	protected int order = 0;
	public String customizationKey;

	public PluginConfig() {
		super();
	}

	public PluginConfig(String plName) {
		super();
		pluginName = plName;
	}
	
	/**
	 * Инициализируем конфиг начальными значениями
	 * @param runtimeServices
	 */
	public void init(RuntimeServices runtimeServices) {
		
	}
	
	public boolean getIsActive() {
		return (executionMode != null);
	}
	
	public String getModeName() {
		if (PluginSystemLocal.Mode.SINGLE.equals(mode)) {
			return "одиночный режим";
		} else if (PluginSystemLocal.Mode.PACKET.equals(mode)) {
			return "пакетный режим";
		} else {
			return null;
		}
	}
	
	public String getExecutionModeName() {
		if (executionMode == null) {
			return "отключен";
		} else if (PluginSystemLocal.ExecutionMode.AUTOMATIC.equals(executionMode)) {
			return "автоматический режим";
		} else if (PluginSystemLocal.ExecutionMode.MANUAL.equals(executionMode)) {
			return "ручной режим";
		} else {
			return null;
		}
	}
	
	public String getSyncModeName() {
		if (PluginSystemLocal.SyncMode.SYNC.equals(syncMode)) {
			return "синхронные запросы";
		} else if (PluginSystemLocal.SyncMode.ASYNC.equals(syncMode)) {
			return "асинхронные запросы";
		} else {
			return null;
		}
	}
	
	public String getSyncModeAsString() {
		if (syncMode == null) {
			return null;
		} else {
			return syncMode.name();
		}
	}
	
	public String getModeAsString() {
		if (mode == null) {
			return null;
		} else {
			return mode.name();
		}
	}
	
	/**
	 * Загрузить конфигурацию 
	 * @param prefix - не включая имя самого плагина
	 * @param source
	 */	
	public void loadAll(String prefix, Map<String,Object> source) {
		String sprefix = prefix;
		if (sprefix == null) {
			sprefix = "";
		} else {
			sprefix = sprefix + ".";
		}
		load(sprefix + getPluginName(), source);
		loadExtended(sprefix + getPluginName(), source);
		
		if (propertiesImports != null && propertiesImports.length > 0) {
			for (String splug: propertiesImports) {
				loadExtended(sprefix + splug, source);
			}
		}
	}
	
	/**
	 * Загрузить дополнительные значения
	 * @param prefix
	 * @param source
	 */
	public void loadExtended(String prefix, Map<String,Object> source) {
		
	}
	
	/**
	 * Загрузить конфигурацию 
	 * @param prefix - включая имя самого плагина
	 * @param source
	 */
	public void load(String prefix, Map<String,Object> source) {
		String smode = (String) source.get(prefix + ".mode");
		if (StringUtils.isBlank(smode)) {
			mode = PluginSystemLocal.Mode.SINGLE;
		} else {
			mode = PluginSystemLocal.Mode.valueOf(smode);
		}
		
		smode = (String) source.get(prefix + ".executionMode");
		if (StringUtils.isBlank(smode)) {
			executionMode = null;
		} else {
			executionMode = PluginSystemLocal.ExecutionMode.valueOf(smode);
		}	
		
		smode = (String) source.get(prefix + ".syncMode");
		if (StringUtils.isBlank(smode)) {
			syncMode = PluginSystemLocal.SyncMode.SYNC;
		} else {
			syncMode = PluginSystemLocal.SyncMode.valueOf(smode);
		}		
		
		Number numPacketInterval = Utils.safeNumber(source.get(prefix + ".packetInterval"));
		if (numPacketInterval == null) {
			packetInterval = 60000;
		} else {
			packetInterval = numPacketInterval.intValue();
		}
		packetSchedule = (String) source.get(prefix + ".packetSchedule"); 
		
		Number numAsyncInterval = Utils.safeNumber(source.get(prefix + ".asyncInterval"));
		if (numAsyncInterval == null) {
			asyncInterval = 60000;
		} else {
			asyncInterval = numAsyncInterval.intValue();
		}	
		asyncSchedule = (String) source.get(prefix + ".asyncSchedule"); 
		
		Number numRetryInterval = Utils.safeNumber(source.get(prefix + ".retryInterval"));
		if (numRetryInterval == null) {
			retryInterval = 120000;
		} else {
			retryInterval = numRetryInterval.intValue();
		}		
		retrySchedule = (String) source.get(prefix + ".retrySchedule"); 
		
		Number numUseFake = Utils.safeNumber(source.get(prefix + ".useFake"));
		if (numUseFake == null) {
			useFake = false;
		} else {
			useFake = (numUseFake.intValue() == 1);
		}
		
		workEJBName = (String) source.get(prefix + ".workEJBName");
		fakeEJBName = (String) source.get(prefix + ".fakeEJBName");
		processName = (String) source.get(prefix + ".processName");
		if (StringUtils.isBlank(processName)) {
			processName = ProcessKeys.DEF_SUB_STANDART;
		}
		processPacketName = (String) source.get(prefix + ".processPacketName");
		if (StringUtils.isBlank(processPacketName)) {
			processPacketName = ProcessKeys.PREFIX_DEF_SUB_PACKET;
		}		
	}
	
	/**
	 * Сохранить конфигурацию
	 * @param prefix - не включая имя самого плагина
	 * @param dest
	 */
	public void saveAll(String prefix, Map<String,Object> dest) {
		String sprefix = prefix;
		if (sprefix == null) {
			sprefix = "";
		} else {
			sprefix = sprefix + ".";
		}
		
		save(sprefix + getPluginName(), dest);
		saveExtended(sprefix + getPluginName(), dest);
		if (propertiesImports != null && propertiesImports.length > 0) {
			for (String splug: propertiesImports) {
				saveExtended(sprefix + splug, dest);
			}
		}
	}
	
	/**
	 * Сохранить конфигурацию
	 * @param prefix - включая имя самого плагина
	 * @param dest
	 */
	public void save(String prefix, Map<String,Object> dest) {
		if (mode == null) {
			dest.put(prefix + ".mode", null);
		} else {
			dest.put(prefix + ".mode", mode.name());
		}
		
		if (executionMode == null) {
			dest.put(prefix + ".executionMode", null);
		} else {
			dest.put(prefix + ".executionMode", executionMode.name());
		}
		
		if (syncMode == null) {
			dest.put(prefix + ".syncMode", null);
		} else {
			dest.put(prefix + ".syncMode", syncMode.name());
		}		
		
		dest.put(prefix + ".packetInterval", new Long(packetInterval));
		dest.put(prefix + ".packetSchedule", packetSchedule);
		dest.put(prefix + ".asyncInterval", new Long(asyncInterval));
		dest.put(prefix + ".asyncSchedule", asyncSchedule);
		dest.put(prefix + ".retryInterval", new Long(retryInterval));
		dest.put(prefix + ".retrySchedule", retrySchedule);
		dest.put(prefix + ".useFake", (useFake)?1:0);
		dest.put(prefix + ".workEJBName", workEJBName);
		dest.put(prefix + ".fakeEJBName", fakeEJBName);
		if (StringUtils.isBlank(processName)) {
			processName = ProcessKeys.DEF_SUB_STANDART;
		}		
		dest.put(prefix + ".processName", processName);
		if (StringUtils.isBlank(processPacketName)) {
			processPacketName = ProcessKeys.PREFIX_DEF_SUB_PACKET;
		}			
		dest.put(prefix + ".processPacketName", processPacketName);
	}
	
	/**
	 * Сохранить доп.настройки в конфигурацию 
	 * @param prefix
	 * @param dest
	 */
	public void saveExtended(String prefix, Map<String,Object> dest) {
		
	}
	
	public PluginSystemLocal.Mode getMode() {
		return mode;
	}

	public void setMode(PluginSystemLocal.Mode mode) {
		this.mode = mode;
	}

	public long getPacketInterval() {
		return packetInterval;
	}

	public void setPacketInterval(long packetInterval) {
		this.packetInterval = packetInterval;
	}

	public PluginSystemLocal getPlugin() {
		return plugin;
	}

	public void setPlugin(PluginSystemLocal plugin) {
		this.plugin = plugin;
	}

	public boolean isUseFake() {
		return useFake;
	}

	public void setUseFake(boolean useFake) {
		this.useFake = useFake;
	}

	public String getWorkEJBName() {
		return workEJBName;
	}

	public void setWorkEJBName(String workEJBName) {
		this.workEJBName = workEJBName;
	}

	public String getFakeEJBName() {
		return fakeEJBName;
	}

	public void setFakeEJBName(String fakeEJBName) {
		this.fakeEJBName = fakeEJBName;
	}

	public String getPluginName() {
		return pluginName;
	}
	
	public String getPartnerName() {
		return getPluginName();
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	
	public boolean isWorkEJBInternal() {
		return StringUtils.isBlank(workEJBName);
	}

	public PluginSystemLocal.ExecutionMode getExecutionMode() {
		return executionMode;
	}

	public void setExecutionMode(PluginSystemLocal.ExecutionMode executionMode) {
		this.executionMode = executionMode;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public boolean isDefaultProcessName() {
		return (ProcessKeys.DEF_SUB_STANDART.equals(processName));
	}

	public PluginSystemLocal.SyncMode getSyncMode() {
		return syncMode;
	}

	public void setSyncMode(PluginSystemLocal.SyncMode syncMode) {
		this.syncMode = syncMode;
	}

	public long getAsyncInterval() {
		return asyncInterval;
	}

	public void setAsyncInterval(long asyncInterval) {
		this.asyncInterval = asyncInterval;
	}

	public String getProcessPacketName() {
		return processPacketName;
	}

	public void setProcessPacketName(String processPacketName) {
		this.processPacketName = processPacketName;
	}

	public long getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(long retryInterval) {
		this.retryInterval = retryInterval;
	}

	public Partner getPartner() {
		return partner;
	}
	
	public String getExecutionModeAsString() {
		if (this.executionMode == null) {
			return null;
		} else {
			return executionMode.name();
		}
	}
	
	private Object writeReplace() {
		return new PluginConfigProxy(pluginName, customizationKey);
	}

	public String getPacketSchedule() {
		return packetSchedule;
	}

	public void setPacketSchedule(String packetSchedule) {
		this.packetSchedule = packetSchedule;
	}

	public String getAsyncSchedule() {
		return asyncSchedule;
	}

	public void setAsyncSchedule(String asyncSchedule) {
		this.asyncSchedule = asyncSchedule;
	}

	public String getRetrySchedule() {
		return retrySchedule;
	}

	public void setRetrySchedule(String retrySchedule) {
		this.retrySchedule = retrySchedule;
	}

	public int getOrder() {
		return order;
	}
	
}
