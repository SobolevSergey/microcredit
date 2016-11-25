package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

import java.util.Map;

/**
 * @author sniffl
 */
public class AriusPayPluginConfig extends PluginConfig {

    private static final long serialVersionUID = -292730731665980800L;

    protected String testUrl = "https://sandbox.ariuspay.ru/paynet/api/v2/";
    protected String workUrl = "https://gate.ariuspay.ru/paynet/api/v2/";

    protected String merchantKey = "AD9B8E2C-DEA1-47AA-AAA3-CF594EE4F093";
    protected String testLogin = "mobifinans-sbox";
    protected String workLogin = "somelogin";            //todo выяснить

    protected String endpoint3ds = "848";
    protected String endpointcardreg = "849";
    protected String endpointdeposit2card = "852";
    protected String endpointrecurrent = "851";
    protected Boolean sandbox = true;
    protected Boolean localhost = true;



    protected long intervalWaitClient = 3*24*60*60*1000; // по умолчанию 3 дня

    public AriusPayPluginConfig() {
        super();
    }

    public AriusPayPluginConfig(String plName) {
        super(plName);
    }


    public String getMerchantKey() {
        if(this.merchantKey == null){
            this.merchantKey = "AD9B8E2C-DEA1-47AA-AAA3-CF594EE4F093";
        }
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }

    public String getTestUrl() {
        if(this.testUrl == null){
            this.testUrl = "https://sandbox.ariuspay.ru/paynet/api/v2/";
        }
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

    public String getTestLogin() {
        if(this.testLogin == null){
            this.testLogin = "mobifinans-sbox";
        }
        return testLogin;
    }

    public void setTestLogin(String testLogin) {
        this.testLogin = testLogin;
    }

    public String getWorkLogin() {
        return workLogin;
    }

    public void setWorkLogin(String workLogin) {
        this.workLogin = workLogin;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEndpoint3ds() {
        if(this.endpoint3ds == null){
            this.endpoint3ds = "848";
        }
        return endpoint3ds;
    }

    public void setEndpoint3ds(String endpoint3ds) {
        this.endpoint3ds = endpoint3ds;
    }

    public String getEndpointcardreg() {
        if(this.endpointcardreg == null){
            this.endpointcardreg = "849";
        }
        return endpointcardreg;
    }

    public void setEndpointcardreg(String endpointcardreg) {
        this.endpointcardreg = endpointcardreg;
    }

    public String getEndpointdeposit2card() {
        if(this.endpointdeposit2card == null){
            this.endpointdeposit2card = "852";
        }
        return endpointdeposit2card;
    }

    public void setEndpointdeposit2card(String endpointdeposit2card) {
        this.endpointdeposit2card = endpointdeposit2card;
    }

    public String getEndpointrecurrent() {
        if(this.endpointrecurrent == null){
            this.endpointrecurrent = "851";
        }
        return endpointrecurrent;
    }

    public Boolean getSandbox() {
        if(sandbox == null){
            sandbox = true;
        }
        return sandbox;
    }


    public void setSandbox(Boolean sandbox) {
        this.sandbox = sandbox;
    }

    public Boolean getLocalhost() {
        if(localhost == null){
            localhost = true;
        }
        return localhost;
    }

    public void setLocalhost(Boolean localhost) {
        this.localhost = localhost;
    }

    public void setEndpointrecurrent(String endpointrecurrent) {
        this.endpointrecurrent = endpointrecurrent;
    }

    @Override
	public void load(String prefix, Map<String, Object> source) {
		super.load(prefix, source);
		
	}

	@Override
	public void loadExtended(String prefix, Map<String, Object> source) {
		super.loadExtended(prefix, source);
        merchantKey = (String) source.get(prefix + ".merchantKey");

        testUrl = (String) source.get(prefix + ".testUrl");
        workUrl = (String) source.get(prefix + ".workUrl");

        testLogin = (String) source.get(prefix + ".testLogin");
        workLogin = (String) source.get(prefix + ".workLogin");

        endpoint3ds = (String) source.get(prefix + ".endpoint3ds");
        endpointcardreg = (String) source.get(prefix + ".endpointcardreg");
        endpointdeposit2card = (String) source.get(prefix + ".endpointdeposit2card");
        endpointrecurrent = (String) source.get(prefix + ".endpointrecurrent");


        sandbox = Utils.booleanFromNumber( source.get(prefix + ".sandbox"));
        localhost = Utils.booleanFromNumber( source.get(prefix + ".localhost"));

        Number nn = Utils.safeNumber(source.get(prefix + ".intervalWaitClient"));
        if (nn != null) {
        	intervalWaitClient = nn.longValue();
        }
	}

	@Override
	public void saveExtended(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".merchantKey", merchantKey);
        dest.put(prefix + ".testUrl", testUrl);
        dest.put(prefix + ".workUrl", workUrl);
        dest.put(prefix + ".testLogin", testLogin);
        dest.put(prefix + ".workLogin", workLogin);

        dest.put(prefix + ".endpoint3ds", endpoint3ds);
        dest.put(prefix + ".endpointcardreg", endpointcardreg);
        dest.put(prefix + ".endpointdeposit2card", endpointdeposit2card);
        dest.put(prefix + ".endpointrecurrent", endpointrecurrent);

        dest.put(prefix + ".intervalWaitClient", new Long(intervalWaitClient));
        dest.put(prefix + ".sandbox", (sandbox) ? 1 : 0);
        dest.put(prefix + ".localhost", (localhost) ? 1 : 0);

		super.saveExtended(prefix, dest);
		
	}

	public long getIntervalWaitClient() {
		return intervalWaitClient;
	}

	public void setIntervalWaitClient(long intervalWaitClient) {
		this.intervalWaitClient = intervalWaitClient;
	}


}
