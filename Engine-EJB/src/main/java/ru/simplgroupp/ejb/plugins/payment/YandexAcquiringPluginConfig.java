package ru.simplgroupp.ejb.plugins.payment;

import java.util.Map;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

/**
 * Конфигурация для оплаты кредита через yandex
 */
public class YandexAcquiringPluginConfig extends PluginConfig {

    private static final long serialVersionUID = 1;

    private boolean useWork = false;

    private Long shopId;

    private Long scid;

    private String password;

    private String testUrl = "https://demomoney.yandex.ru/eshop.xml";

    private String workUrl = "https://money.yandex.ru/eshop.xml";

    public YandexAcquiringPluginConfig() {
        super();
    }

    public YandexAcquiringPluginConfig(String plName) {
        super(plName);
    }

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        Number n = Utils.safeNumber(source.get(prefix + ".shopId"));
        shopId = n == null ? null : n.longValue();

        n = Utils.safeNumber(source.get(prefix + ".scid"));
        scid = n == null ? null : n.longValue();

        useWork=Utils.booleanFromNumber(source.get(prefix + ".useWork"));

        password = (String) source.get(prefix + ".password");
        testUrl = (String) source.get(prefix + ".testUrl");
        workUrl = (String) source.get(prefix + ".workUrl");
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".useWork", (useWork)?1:0);
        dest.put(prefix + ".shopId", shopId);
        dest.put(prefix + ".scid", scid);
        dest.put(prefix + ".password", password);
        dest.put(prefix + ".testUrl", testUrl);
        dest.put(prefix + ".workUrl", workUrl);
        super.save(prefix, dest);
    }

    public boolean isUseWork() {
        return useWork;
    }

    public Long getShopId() {
        return shopId;
    }

    public Long getScid() {
        return scid;
    }

    public String getPassword() {
        return password;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public String getWorkUrl() {
        return workUrl;
    }

	public void setUseWork(boolean useWork) {
		this.useWork = useWork;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public void setScid(Long scid) {
		this.scid = scid;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public void setWorkUrl(String workUrl) {
		this.workUrl = workUrl;
	}
}
