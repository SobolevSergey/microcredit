package ru.simplgroupp.ejb.plugins.payment;

import java.util.Map;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

/**
 *
 */
public class QiwiAcquiringPluginConfig extends PluginConfig {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6872012309772610310L;

	private String orderUrl;

    private String statusUrl;

    private String payUrl;

    private String login;

    private String password;

    private String providerId;

    private int connectTimeout;

    private int lifeTime; //храним в часах

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        orderUrl = (String) source.get(prefix + ".orderUrl");
        statusUrl = (String) source.get(prefix + ".statusUrl");
        payUrl = (String) source.get(prefix + ".payUrl");
        login = (String) source.get(prefix + ".login");
        password = (String) source.get(prefix + ".password");
        providerId = (String) source.get(prefix + ".providerId");

        Number number = Utils.safeNumber(source.get(prefix + ".connectTimeout"));
        if (number == null) {
            connectTimeout = 10000; //10 сек
        } else {
            connectTimeout = number.intValue();
        }

        number = Utils.safeNumber(source.get(prefix + ".connectTimeout"));
        if (number == null) {
            lifeTime = 48; //2 дня
        } else {
            lifeTime = number.intValue();
        }
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".orderUrl", orderUrl);
        dest.put(prefix + ".statusUrl", statusUrl);
        dest.put(prefix + ".payUrl", payUrl);
        dest.put(prefix + ".login", login);
        dest.put(prefix + ".password", password);
        dest.put(prefix + ".providerId", providerId);
        dest.put(lifeTime + ".providerId", lifeTime);

        super.save(prefix, dest);
    }


    public String getStatusUrl() {
        return statusUrl;
    }

    public void setStatusUrl(String statusUrl) {
        this.statusUrl = statusUrl;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }
}
