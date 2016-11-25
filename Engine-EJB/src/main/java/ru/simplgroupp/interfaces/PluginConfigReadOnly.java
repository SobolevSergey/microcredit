package ru.simplgroupp.interfaces;

import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.transfer.Partner;

public interface PluginConfigReadOnly {
	public long getAsyncInterval();
	public PluginSystemLocal.ExecutionMode getExecutionMode();
	public String getExecutionModeName();
	public String getFakeEJBName();
	public boolean getIsActive();
	public PluginSystemLocal.Mode getMode();
	public String getModeName();
	public long getPacketInterval();
	public String getPluginName();
	public String getProcessName();
	public String getProcessPacketName();
	public PluginSystemLocal.SyncMode getSyncMode();
	public String getSyncModeName();
	public String getWorkEJBName();
	public boolean isDefaultProcessName();
	public boolean isUseFake();
	public boolean isWorkEJBInternal();
	public Partner getPartner();
}
