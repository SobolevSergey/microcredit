package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

import java.util.Map;

/**
 * Настройки для payonline
 */
public class PayonlineAcquiringPluginConfig extends PluginConfig {

    private static final long serialVersionUID = 1;

    private String bankCardUrl = "https://secure.payonlinesystem.com/ru/payment/";

    private String verificationCardUrl = "https://secure.payonlinesystem.com/ru/payment/verification/randomamount/";

    private String statusUrl = "https://secure.payonlinesystem.com/payment/search/";

    private String rebillUrl = "https://secure.payonlinesystem.com/payment/transaction/rebill/";

    private String cancelUrl = "https://secure.payonlinesystem.com/payment/transaction/void/";

    private Integer merchantId = 63427;

    private String privateSecurityKey;

    private Integer verificationCardMerchantId;

    private String verificationCardPrivateSecurityKey;

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        bankCardUrl = (String) source.get(prefix + ".bankCardUrl");
        verificationCardUrl = (String) source.get(prefix + ".verificationCardUrl");
        statusUrl = (String) source.get(prefix + ".statusUrl");
        rebillUrl = (String) source.get(prefix + ".rebillUrl");
        cancelUrl = (String) source.get(prefix + ".cancelUrl");
        privateSecurityKey = (String) source.get(prefix + ".privateSecurityKey");
        verificationCardPrivateSecurityKey = (String) source.get(prefix + ".verificationCardPrivateSecurityKey");

        Number n = Utils.safeNumber(source.get(prefix + ".merchantId"));
        merchantId = n == null ? null : n.intValue();

        n = Utils.safeNumber(source.get(prefix + ".verificationCardMerchantId"));
        verificationCardMerchantId = n == null ? null : n.intValue();
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        super.save(prefix, dest);
        dest.put(prefix + ".bankCardUrl", bankCardUrl);
        dest.put(prefix + ".verificationCardUrl", verificationCardUrl);
        dest.put(prefix + ".statusUrl", statusUrl);
        dest.put(prefix + ".rebillUrl", rebillUrl);
        dest.put(prefix + ".cancelUrl", cancelUrl);
        dest.put(prefix + ".merchantId", merchantId);
        dest.put(prefix + ".privateSecurityKey", privateSecurityKey);
        dest.put(prefix + ".verificationCardMerchantId", verificationCardMerchantId);
        dest.put(prefix + ".verificationCardPrivateSecurityKey", verificationCardPrivateSecurityKey);
    }

    public String getBankCardUrl() {
        return bankCardUrl;
    }

    public String getVerificationCardUrl() {
        return verificationCardUrl;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public String getPrivateSecurityKey() {
        return privateSecurityKey;
    }

    public String getStatusUrl() {
        return statusUrl;
    }

    public String getRebillUrl() {
        return rebillUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

	public void setBankCardUrl(String bankCardUrl) {
		this.bankCardUrl = bankCardUrl;
	}

	public void setVerificationCardUrl(String verificationCardUrl) {
		this.verificationCardUrl = verificationCardUrl;
	}

	public void setStatusUrl(String statusUrl) {
		this.statusUrl = statusUrl;
	}

	public void setRebillUrl(String rebillUrl) {
		this.rebillUrl = rebillUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public void setPrivateSecurityKey(String privateSecurityKey) {
		this.privateSecurityKey = privateSecurityKey;
	}

    public Integer getVerificationCardMerchantId() {
        return verificationCardMerchantId;
    }

    public void setVerificationCardMerchantId(Integer verificationCardMerchantId) {
        this.verificationCardMerchantId = verificationCardMerchantId;
    }

    public String getVerificationCardPrivateSecurityKey() {
        return verificationCardPrivateSecurityKey;
    }

    public void setVerificationCardPrivateSecurityKey(String verificationCardPrivateSecurityKey) {
        this.verificationCardPrivateSecurityKey = verificationCardPrivateSecurityKey;
    }
}
