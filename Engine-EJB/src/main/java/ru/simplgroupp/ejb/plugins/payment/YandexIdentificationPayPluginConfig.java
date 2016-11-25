package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

import java.util.Map;

/**
 * Конфигурация для плугина передачи платежей через yandex money
 */
public class YandexIdentificationPayPluginConfig extends PluginConfig {

	private static final long serialVersionUID = -292730731665980799L;

	private boolean useWork = false;

    private String agentId;

    private String testUrl;

    private String workUrl;

    public YandexIdentificationPayPluginConfig() {
        super();
    }

    public YandexIdentificationPayPluginConfig(String plName) {
        super(plName);
    }

    public boolean isUseWork() {
        return useWork;
    }

    public void setUseWork(boolean useWork) {
        this.useWork = useWork;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    public String getWorkUrl() {
        return workUrl;
    }

    public void setWorkUrl(String workUrl) {
        this.workUrl = workUrl;
    }

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        useWork=Utils.booleanFromNumber(source.get(prefix + ".useWork"));
        agentId = (String) source.get(prefix + ".agentId");
        testUrl = (String) source.get(prefix + ".testUrl");
        workUrl = (String) source.get(prefix + ".workUrl");
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".useWork", (useWork)?1:0);
        dest.put(prefix + ".agentId", agentId);
        dest.put(prefix + ".testUrl", testUrl);
        dest.put(prefix + ".workUrl", workUrl);
        super.save(prefix, dest);
    }
}
