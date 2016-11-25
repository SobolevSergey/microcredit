package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;

import java.util.Map;

/**
 * Конфигурация для плугина передачи платежей через win-pay
 */
public class WinpayFramePayPluginConfig extends PluginConfig {

    private static final long serialVersionUID = -292730731665980799L;

    private String secretKey;

    private String balanceUrl;

    public WinpayFramePayPluginConfig() {
        super();
    }

    public WinpayFramePayPluginConfig(String plName) {
        super(plName);
    }

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        secretKey = (String) source.get(prefix + ".secretKey");
        balanceUrl = (String) source.get(prefix + ".balanceUrl");
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".secretKey", secretKey);
        dest.put(prefix + ".balanceUrl", balanceUrl);
        super.save(prefix, dest);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBalanceUrl() {
        return balanceUrl;
    }

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setBalanceUrl(String balanceUrl) {
		this.balanceUrl = balanceUrl;
	}
}
