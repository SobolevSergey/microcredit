package ru.simplgroupp.crypto;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Singleton;

import org.admnkz.common.IOptions;

import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;

@Singleton
@Local(IOptions.class)
public class CryptoOptions implements IOptions {
	
	@EJB
	RulesBeanLocal rulesBean;
	
	@EJB
	ServiceBeanLocal servBean;	
	
	protected Map<String, Object> options;
	
	@PostConstruct
	public void init() {
		options = rulesBean.getCryptoCommonSettings();
//		servBean.subscribe(EventKeys.CRYPTO_COMMON_SETTINGS_CHANGED, this);
	}
	
	@PreDestroy
	public void destroy() {
//		servBean.unsubscribe(EventKeys.CRYPTO_COMMON_SETTINGS_CHANGED, this);
	}

	@Override
	public String getValue(String key) {
		Object aobj = options.get(key);
		if (aobj == null) {
			return null;
		} else {
			return aobj.toString();
		}
	}

}
