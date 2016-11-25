package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

import java.util.Map;

/**
 * Конфигурация для выдачи кредита через qiwi
 */
public class QiwiPayPluginConfig extends PluginConfig {

    private static final long serialVersionUID = 1;

    private Long terminalId = 56212L;

    public QiwiPayPluginConfig() {
        super();
    }

    public QiwiPayPluginConfig(String pluginName) {
        super(pluginName);
    }

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        Number n = Utils.safeNumber(source.get(prefix + ".terminalId"));
        terminalId = n == null ? null : n.longValue();
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".terminalId", terminalId);
        super.save(prefix, dest);
    }

    public Long getTerminalId() {
        return terminalId;
    }

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}
}
