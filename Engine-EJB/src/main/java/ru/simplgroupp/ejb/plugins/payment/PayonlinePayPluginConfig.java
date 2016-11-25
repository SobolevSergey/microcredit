package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

import java.util.Map;

/**
 * Конфигурация для плугина передачи платежей через payonline
 */
public class PayonlinePayPluginConfig extends PluginConfig {

	private static final long serialVersionUID = -292730731665980799L;

    private Integer merchantId;

    private String privateSecurityKey;

    private String refillUrl = "https://secure.payonlinesystem.com/payment/transaction/refill/";

    private String personalInfoUrl = "https://integration.payonlinesystem.com/PersonalInformation/Write/";

    public PayonlinePayPluginConfig() {
        super();
    }

    public PayonlinePayPluginConfig(String plName) {
        super(plName);
    }

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        Number n = Utils.safeNumber(source.get(prefix + ".merchantId"));
        merchantId = n == null ? null : n.intValue();
        privateSecurityKey = (String) source.get(prefix + ".privateSecurityKey");
        refillUrl = (String) source.get(prefix + ".refillUrl");
        personalInfoUrl = (String) source.get(prefix + ".personalInfoUrl");
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".merchantId", merchantId);
        dest.put(prefix + ".privateSecurityKey", privateSecurityKey);
        dest.put(prefix + ".refillUrl", refillUrl);
        dest.put(prefix + ".personalInfoUrl", personalInfoUrl);
        super.save(prefix, dest);
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public String getPrivateSecurityKey() {
        return privateSecurityKey;
    }

    public String getRefillUrl() {
        return refillUrl;
    }

    public String getPersonalInfoUrl() {
        return personalInfoUrl;
    }

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public void setPrivateSecurityKey(String privateSecurityKey) {
		this.privateSecurityKey = privateSecurityKey;
	}

	public void setRefillUrl(String refillUrl) {
		this.refillUrl = refillUrl;
	}

	public void setPersonalInfoUrl(String personalInfoUrl) {
		this.personalInfoUrl = personalInfoUrl;
	}
    
    
}
