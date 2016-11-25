package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

import java.util.Map;

/**
 * Настройки для payonline
 */
public class WinpayAcquiringPluginConfig extends PluginConfig {

    private static final long serialVersionUID = 1;

    private String payUrl = "https://fatpay.net/pay";

    private String testUrl = "https://fp.ki-technology.ru/pay";

    private String apiKey;

    private String secretKey;

    private boolean useWork = false;

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        payUrl = (String) source.get(prefix + ".payUrl");
        testUrl = (String) source.get(prefix + ".testUrl");
        apiKey = (String) source.get(prefix + ".apiKey");
        secretKey = (String) source.get(prefix + ".secretKey");
        useWork=Utils.booleanFromNumber(source.get(prefix + ".useWork"));
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        super.save(prefix, dest);
        dest.put(prefix + ".payUrl", payUrl);
        dest.put(prefix + ".testUrl", testUrl);
        dest.put(prefix + ".apiKey", apiKey);
        dest.put(prefix + ".secretKey", secretKey);
        dest.put(prefix + ".useWork", (useWork)?1:0);
    }

    public String getPayUrl() {
        return payUrl;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public boolean isUseWork() {
        return useWork;
    }

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setUseWork(boolean useWork) {
		this.useWork = useWork;
	}
    
}
