package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.toolkit.common.Utils;

import java.util.Map;

/**
 * Конфигурация для оплаты кредита через arius
 */
public class AriusAcquiringPluginConfig extends PluginConfig {

    private static final long serialVersionUID = 1;

    private boolean useWork = false;

    public AriusAcquiringPluginConfig() {
        super();
        this.syncMode = PluginSystemLocal.SyncMode.ASYNC;
    }

    public AriusAcquiringPluginConfig(String plName) {
        super(plName);
        this.syncMode = PluginSystemLocal.SyncMode.ASYNC;
    }

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        useWork=Utils.booleanFromNumber(source.get(prefix + ".useWork"));
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".useWork", (useWork)?1:0);
        super.save(prefix, dest);
    }

    public boolean isUseWork() {
        return useWork;
    }
	public void setUseWork(boolean useWork) {
		this.useWork = useWork;
	}


    @Override
    public PluginSystemLocal.SyncMode getSyncMode() {
        return PluginSystemLocal.SyncMode.ASYNC;
    }
}
