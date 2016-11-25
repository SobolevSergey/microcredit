package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;

import java.util.Map;

/**
 * Конфигурация для плугина передачи платежей через win-pay
 */
public class WinpayPayPluginConfig extends PluginConfig {

    private static final long serialVersionUID = -292730731665980799L;

    private String secretKey;

    private String payUrl;

    private String statusUrl;

    private String balanceUrl;

    public WinpayPayPluginConfig() {
        super();
    }

    public WinpayPayPluginConfig(String plName) {
        super(plName);
    }

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        secretKey = (String) source.get(prefix + ".secretKey");
        payUrl = (String) source.get(prefix + ".payUrl");
        statusUrl = (String) source.get(prefix + ".statusUrl");
        balanceUrl = (String) source.get(prefix + ".balanceUrl");
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".secretKey", secretKey);
        dest.put(prefix + ".payUrl", payUrl);
        dest.put(prefix + ".statusUrl", statusUrl);
        dest.put(prefix + ".balanceUrl", balanceUrl);
        super.save(prefix, dest);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public String getStatusUrl() {
        return statusUrl;
    }

    public String getBalanceUrl() {
        return balanceUrl;
    }

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public void setStatusUrl(String statusUrl) {
		this.statusUrl = statusUrl;
	}

	public void setBalanceUrl(String balanceUrl) {
		this.balanceUrl = balanceUrl;
	}
}
