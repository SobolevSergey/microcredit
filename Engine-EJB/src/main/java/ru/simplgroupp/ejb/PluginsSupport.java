package ru.simplgroupp.ejb;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.ejb.plugins.identity.IdentityBean;
import ru.simplgroupp.ejb.plugins.identity.IdentityPluginConfig;
import ru.simplgroupp.ejb.plugins.payment.*;
import ru.simplgroupp.interfaces.ScoringOkbHunterBeanLocal;
import ru.simplgroupp.interfaces.plugins.identity.IdentityBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.*;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.ScoringEquifaxBeanLocal;
import ru.simplgroupp.interfaces.ScoringNBKIBeanLocal;
import ru.simplgroupp.interfaces.ScoringOkbCaisBeanLocal;
import ru.simplgroupp.interfaces.ScoringOkbIdvBeanLocal;
import ru.simplgroupp.interfaces.ScoringRsBeanLocal;
import ru.simplgroupp.interfaces.ScoringSkbBeanLocal;
import ru.simplgroupp.interfaces.ScoringSociohubBeanLocal;
import ru.simplgroupp.interfaces.VerificationBeanLocal;
import ru.simplgroupp.toolkit.ejb.EJBUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PluginsSupport {

	Logger logger = LoggerFactory.getLogger(PluginsSupport.class);

	public ScoringEquifaxBeanLocal equifax;
	public ScoringOkbCaisBeanLocal okbCais;
	public ScoringOkbIdvBeanLocal okbIdv;
	public ScoringRsBeanLocal rusStandart;
	public ScoringSkbBeanLocal skb;
	public ScoringNBKIBeanLocal nbki;
    public ScoringOkbHunterBeanLocal okbHunter;
	public AlfaBankMarengoBeanLocal alphaMarengo;
	public AlphaAcquiringBeanLocal alphaAcq;
	public ContactAcquiringBeanLocal contactAcq;
	public ContactAcquiringBeanLocal contactAcqPacket;
    public ContactPayBeanLocal contactPay;
    public ContactCancelPayBeanLocal contactCancelPay;
	public Account1CPayBeanLocal account1CPay;
	public Account1CAcquiringBeanLocal account1CAcq;
	public ManualPayBeanLocal manualPay;
	public VerificationBeanLocal verify;
	public QiwiAcquiringBeanLocal qiwiAcquiring;
    public YandexPayBeanLocal yandexPay;
	public YandexIdentificationPayBeanLocal yandexIdentificationPay;
    public WinpayAcquiringBeanLocal winpayAcquiring;
	public WinpayPayBeanLocal winpayPay;
    public YandexAcquiringBeanLocal yandexAcquiring;
    public YandexAcquiringBeanLocal yandexAcqPacket;
    public PayonlineAcquiringBeanLocal payonlineAcq;
    public PayonlinePayBeanLocal payonlinePay;
    public Account1CBeanLocal account1C;
	public IdentityBeanLocal identity;
    public AriusPayBeanLocal ariusPay;
    public AriusAcquiringBeanLocal ariusAcq;


	protected Map<String, PluginConfig> plugins;

	protected static Map<String, Class> localEJBIntfs;
	protected static Map<String, Class> workEJBClasses;
	protected static Map<String, Class> fakeEJBClasses;
	protected static Map<String, Class<? extends PluginConfig>> pluginConfigClasses;
	protected static Map<String, java.lang.reflect.Field> pluginVars;
	
	static {
		workEJBClasses = new HashMap<>(15);
		fakeEJBClasses = new HashMap<>(15);
		localEJBIntfs = new HashMap<>(15);
		pluginConfigClasses = new HashMap<>(15);
		pluginVars = new HashMap<>(15);
		
		registerSystem(ScoringEquifaxBeanLocal.SYSTEM_NAME, ScoringEquifaxBeanLocal.class, ScoringEquifaxBean.class, ScoringEquifaxFakeBean.class, EquifaxPluginConfig.class, "equifax");
		registerSystem(ScoringNBKIBeanLocal.SYSTEM_NAME, ScoringNBKIBeanLocal.class, ScoringNBKIBean.class, ScoringNBKIFakeBean.class, NBKIPluginConfig.class, "nbki");
		registerSystem(ScoringOkbCaisBeanLocal.SYSTEM_NAME, ScoringOkbCaisBeanLocal.class, ScoringOkbCaisBean.class, ScoringOkbCaisFakeBean.class, OkbCaisPluginConfig.class, "okbCais");
		registerSystem(ScoringOkbIdvBeanLocal.SYSTEM_NAME, ScoringOkbIdvBeanLocal.class, ScoringOkbIdvBean.class, ScoringOkbIdvFakeBean.class, OkbIdvPluginConfig.class, "okbIdv");
        registerSystem(ScoringOkbHunterBeanLocal.SYSTEM_NAME, ScoringOkbHunterBeanLocal.class, ScoringOkbHunterBean.class, ScoringOkbHunterFakeBean.class, OkbHunterPluginConfig.class, "okbHunter");
		registerSystem(ScoringRsBeanLocal.SYSTEM_NAME, ScoringRsBeanLocal.class, ScoringRsBean.class, ScoringRsFakeBean.class, RusStandartPluginConfig.class, "rusStandart");
		registerSystem(ScoringSkbBeanLocal.SYSTEM_NAME, ScoringSkbBeanLocal.class, ScoringSkbBean.class, ScoringSkbFakeBean.class, SkbPluginConfig.class, "skb");
		registerSystem(ScoringSociohubBeanLocal.SYSTEM_NAME, ScoringSociohubBeanLocal.class, ScoringSociohubBean.class, ScoringSociohubFakeBean.class, SociohubPluginConfig.class, "sociohub");
		registerSystem(AlfaBankMarengoBeanLocal.SYSTEM_NAME, AlfaBankMarengoBeanLocal.class, AlfaBankMarengoBean.class, AlfaBankMarengoFakeBean.class, AlfaBankMarengoPluginConfig.class, "alphaMarengo");
		registerSystem(AlphaAcquiringBeanLocal.SYSTEM_NAME, AlphaAcquiringBeanLocal.class, AlphaAcquiringBean.class, AlphaAcquiringFakeBean.class, PluginConfig.class, "alphaAcq");
		registerSystem(ContactAcquiringBeanLocal.SYSTEM_NAME, ContactAcquiringBeanLocal.class, ContactAcquiringBean.class, ContactAcquiringFakeBean.class, ContactAcquiringPluginConfig.class, "contactAcq");
		registerSystem(ContactPayBeanLocal.SYSTEM_NAME, ContactPayBeanLocal.class, ContactPayBean.class, ContactPayFakeBean.class, ContactPayPluginConfig.class, "contactPay");
        registerSystem(Account1CPayBeanLocal.SYSTEM_NAME, Account1CPayBeanLocal.class, Account1CPayBean.class, Account1CPayFakeBean.class, PluginConfig.class,"account1CPay");
        registerSystem(Account1CAcquiringBeanLocal.SYSTEM_NAME, Account1CAcquiringBeanLocal.class, Account1CAcquiringBean.class, Account1CAcquiringFakeBean.class, PluginConfig.class,"account1CAcq");
		registerSystem(ManualPayBeanLocal.SYSTEM_NAME, ManualPayBeanLocal.class, ManualPayBean.class, ManualPayFakeBean.class, ManualPluginConfig.class, "manualPay");
		registerSystem(VerificationBeanLocal.SYSTEM_NAME, VerificationBeanLocal.class, VerificationBean.class, VerificationFakeBean.class, VerificationPluginConfig.class, "verify");
        registerSystem(QiwiAcquiringBeanLocal.SYSTEM_NAME, QiwiAcquiringBeanLocal.class, QiwiAcquiringBean.class, QiwiAcquiringFakeBean.class, QiwiAcquiringPluginConfig.class, "qiwiAcquiring");
        registerSystem(QiwiGateBeanLocal.SYSTEM_NAME, QiwiGateBeanLocal.class, QiwiGatePayBean.class, QiwiGateFakeBean.class, PluginConfig.class, "qiwiGatePay");
        registerSystem(YandexAcquiringBeanLocal.SYSTEM_NAME, YandexAcquiringBeanLocal.class,
                YandexAcquiringBean.class, YandexAcquiringFakeBean.class, YandexAcquiringPluginConfig.class, "yandexAcquiring");
        registerSystem(YandexPayBeanLocal.SYSTEM_NAME, YandexPayBeanLocal.class, YandexPayBean.class,
                YandexPayFakeBean.class, YandexPayPluginConfig.class, "yandexPay");
		registerSystem(YandexIdentificationPayBeanLocal.SYSTEM_NAME, YandexIdentificationPayBeanLocal.class,
				YandexIdentificationPayBean.class, YandexIdentificationPayFakeBean.class,
				YandexIdentificationPayPluginConfig.class, "yandexIdentificationPay");
		registerSystem(WinpayAcquiringBeanLocal.SYSTEM_NAME, WinpayAcquiringBeanLocal.class, WinpayAcquiringBean.class,
				WinpayAcquiringFakeBean.class, WinpayAcquiringPluginConfig.class, "winpayAcquiring");
        registerSystem(WinpayPayBeanLocal.SYSTEM_NAME, WinpayPayBeanLocal.class, WinpayPayBean.class,
                WinpayPayFakeBean.class, WinpayPayPluginConfig.class, "winpayPay");
        registerSystem(PayonlineAcquiringBeanLocal.SYSTEM_NAME, PayonlineAcquiringBeanLocal.class, PayonlineAcquiringBean.class, PayonlineAcquiringFakeBean.class, PayonlineAcquiringPluginConfig.class, "payonlineAcq");
        registerSystem(PayonlinePayBeanLocal.SYSTEM_NAME, PayonlinePayBeanLocal.class, PayonlinePayBean
                .class, PayonlinePayFakeBean.class, PayonlinePayPluginConfig.class, "payonlinePay");
        registerSystem(Account1CBeanLocal.SYSTEM_NAME, Account1CBeanLocal.class, Account1CBean.class, Account1CFakeBean.class, PluginConfig.class,"account1C");

        registerSystem(ContactCancelPayBeanLocal.SYSTEM_NAME, ContactCancelPayBeanLocal.class, ContactCancelPayBean.class, ContactCancelPayFakeBean.class, ContactCancelPayPluginConfig.class, "contactCancelPay");


		registerSystem(IdentityBeanLocal.SYSTEM_NAME, IdentityBeanLocal.class, IdentityBean.class, IdentityBean.class, IdentityPluginConfig.class, "identity");


		// добавляем дополнительный плагин Яндекс в пакетном режиме
        //registerSystem(YandexAcquiringBeanLocal.SYSTEM_NAME + "_P1", YandexAcquiringBeanLocal.class,
        //        YandexAcquiringBean.class, YandexAcquiringFakeBean.class, YandexAcquiringPluginConfig.class, "yandexAcqPacket");
        // добавляем дополнительный плагин Контакта в пакетном режиме
        //registerSystem(ContactAcquiringBeanLocal.SYSTEM_NAME, ContactAcquiringBeanLocal.class, ContactAcquiringBean.class, ContactAcquiringFakeBean.class, PluginConfig.class, "contactAcqPacket");
        registerSystem(AriusPayBeanLocal.SYSTEM_NAME, AriusPayBeanLocal.class, AriusPayBean.class, AriusPayFakeBean.class, AriusPayPluginConfig.class, "ariusPay");
        registerSystem(AriusAcquiringBeanLocal.SYSTEM_NAME, AriusAcquiringBeanLocal.class, AriusAcquiringBean.class, AriusAcquiringFakeBean.class, AriusAcquiringPluginConfig.class, "ariusAcq");
	}
	
	static void registerSystem(String sysName, Class intfClass, Class workClass, Class fakeClass, Class<? extends PluginConfig> plcClass, String varName) {
		workEJBClasses.put(sysName, workClass);
		fakeEJBClasses.put(sysName, fakeClass);
		localEJBIntfs.put(sysName, intfClass);
		pluginConfigClasses.put(sysName, plcClass);		
	
		try {
			pluginVars.put(sysName, PluginsSupport.class.getField(varName));
		} catch (NoSuchFieldException | SecurityException e) {
			pluginVars.put(sysName, null);
		}
	}
	
	public PluginsSupport() {
		super();
		
		plugins = new HashMap<>(15);
		for (String sysName: localEJBIntfs.keySet()) {
			PluginConfig plc = null;
			try {
				plc = pluginConfigClasses.get(sysName).newInstance();
				plc.setPluginName(sysName);
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
			if (plc != null) {
				plugins.put(sysName, plc);
			}
		}
	}
	
	/**
	 * Загрузить конфигурацию плагинов
	 * @param prefix
	 * @param source
	 */
	public void load(String prefix, Map<String,Object> source) {
		List<PluginConfig> lstPlugs = new ArrayList<>(plugins.size());
		lstPlugs.addAll(plugins.values());
		Collections.sort(lstPlugs, new Comparator<PluginConfig>() {

			@Override
			public int compare(PluginConfig o1, PluginConfig o2) {
				return (o1.getOrder() - o2.getOrder());
			}});
		
		for (PluginConfig plc: lstPlugs) {
			plc.loadAll(prefix, source);
		}
	}
	
	/**
	 * Сохранить конфигурацию плагинов
	 * @param prefix
	 * @param source
	 */
	public void save(String prefix, Map<String,Object> source) {
		List<PluginConfig> lstPlugs = new ArrayList<>(plugins.size());
		lstPlugs.addAll(plugins.values());
		Collections.sort(lstPlugs, new Comparator<PluginConfig>() {

			@Override
			public int compare(PluginConfig o1, PluginConfig o2) {
				return (o2.getOrder() - o1.getOrder());
			}});		
		
		for (PluginConfig plc: lstPlugs) {
			plc.saveAll(prefix, source);
		}
	}	
	
	public void applyConfigs() {
		for (PluginConfig plc: plugins.values()) {
			applyConfig(plc);
		}
	}
	
	private void applyConfig( PluginConfig plc) {
		String ejbName1;
		if (plc.isUseFake()) {
			ejbName1 = plc.getFakeEJBName();
		} else {
			ejbName1 = plc.getWorkEJBName();
		}
		
		Object aejb = null;
		if (! StringUtils.isBlank(ejbName1)) {
			// пытаемся создать внешнее ejb
			aejb = EJBUtil.findEJB(ejbName1);
		} 
				
		if (aejb == null) {
			// создаём внутреннее ejb
			if (plc.isUseFake()) {
				ejbName1 = fakeEJBClasses.get(plc.getPluginName()).getSimpleName();
			} else {
				ejbName1 = workEJBClasses.get(plc.getPluginName()).getSimpleName();
			}
			
			logger.info("имя ejb -> java:module/" + ejbName1 + "!" + localEJBIntfs.get(plc.getPluginName()).getName());
		
			aejb = EJBUtil.findEJB("java:module/" + ejbName1 + "!" + localEJBIntfs.get(plc.getPluginName()).getName());

		}
		PluginSystemLocal aplg = (PluginSystemLocal) aejb;
		plc.setPlugin(aplg);					
		
		if (ScoringEquifaxBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			equifax = (ScoringEquifaxBeanLocal) aejb;
		} else if (ScoringOkbCaisBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			okbCais = (ScoringOkbCaisBeanLocal) aejb;
		} else if (ScoringOkbIdvBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			okbIdv = (ScoringOkbIdvBeanLocal) aejb;
        } else if (ScoringOkbHunterBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
            okbHunter = (ScoringOkbHunterBeanLocal) aejb;
		} else if (ScoringRsBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			rusStandart = (ScoringRsBeanLocal) aejb;
		}  else if (ScoringSkbBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			skb = (ScoringSkbBeanLocal) aejb;
		}  else if (AlfaBankMarengoBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			alphaMarengo = (AlfaBankMarengoBeanLocal) aejb;
		} else if (AlphaAcquiringBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			alphaAcq = (AlphaAcquiringBeanLocal) aejb;
		} else if (ContactAcquiringBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			contactAcq = (ContactAcquiringBeanLocal) aejb;
        } else if (ContactPayBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			contactPay = (ContactPayBeanLocal) aejb;
        } else if (ContactCancelPayBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			contactCancelPay = (ContactCancelPayBeanLocal) aejb;	
		} else if (Account1CPayBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			account1CPay = (Account1CPayBeanLocal) aejb;
		} else if (Account1CAcquiringBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			account1CAcq = (Account1CAcquiringBeanLocal) aejb;
		} else if (Account1CBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			account1C = (Account1CBeanLocal) aejb;
		} else if (ManualPayBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			manualPay = (ManualPayBeanLocal) aejb;
		} else if (VerificationBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			verify = (VerificationBeanLocal) aejb;
        } else if (QiwiAcquiringBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
            qiwiAcquiring = (QiwiAcquiringBeanLocal) aejb;
        } else if (YandexAcquiringBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
            yandexAcquiring = (YandexAcquiringBeanLocal) aejb;
        } else if (YandexPayBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
            yandexPay = (YandexPayBeanLocal) aejb;
		} else if (YandexIdentificationPayBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			yandexIdentificationPay = (YandexIdentificationPayBeanLocal) aejb;
		} else if (WinpayAcquiringBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			winpayAcquiring = (WinpayAcquiringBeanLocal) aejb;
        } else if (WinpayPayBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			winpayPay = (WinpayPayBeanLocal) aejb;
        } else if (PayonlineAcquiringBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
        	payonlineAcq = (PayonlineAcquiringBeanLocal) aejb;
        } else if (PayonlinePayBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
            payonlinePay = (PayonlinePayBeanLocal) aejb;
        } else if (ScoringNBKIBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
			nbki = (ScoringNBKIBeanLocal) aejb;
		} else if ((YandexAcquiringBeanLocal.SYSTEM_NAME + "_P1").equals(plc.getPluginName())) {
            yandexAcqPacket = (YandexAcquiringBeanLocal) aejb;
        } else if ((ContactAcquiringBeanLocal.SYSTEM_NAME + "_P1").equals(plc.getPluginName())) {
			contactAcqPacket = (ContactAcquiringBeanLocal) aejb;
        } else if ((IdentityBeanLocal.SYSTEM_NAME).equals(plc.getPluginName())) {
			identity = (IdentityBeanLocal) aejb;
		} else if (AriusPayBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
            ariusPay = (AriusPayBeanLocal) aejb;
        } else if (AriusAcquiringBeanLocal.SYSTEM_NAME.equals(plc.getPluginName())) {
            ariusAcq = (AriusAcquiringBeanLocal) aejb;
		}
	}
	
	public PluginSystemLocal getPluginByName(String pluginName) {
		java.lang.reflect.Field fld = pluginVars.get(pluginName);
		if (fld == null) {
			return null;
		}
		
		try {
			return (PluginSystemLocal) fld.get(this);
		} catch (IllegalArgumentException e) {
			logger.error("Не удалось выбрать плагин по названию "+e);
			return null;
		} catch (IllegalAccessException e) {
			logger.error("Не удалось выбрать плагин по названию "+e);
			return null;
		}
	}
	
	public void applyConfig(String name) {
		PluginConfig plc = plugins.get(name);
		if (plc == null) {
			return;
		}
		applyConfig(plc);
	}
	
	public PluginSystemLocal getPlugin(String name) {
		PluginConfig cfg = plugins.get(name);
		if (cfg == null) {
			return null;
		} else {
			return cfg.plugin;
		}
	}
	
	public List<PluginConfig> getPluginConfigs() {
		ArrayList<PluginConfig> lst = new ArrayList<>(plugins.size());
		lst.addAll(plugins.values());
		return lst;
	}
	
	public PluginConfig getPluginConfig(String name) {
		return plugins.get(name);
	}

	public ScoringEquifaxBeanLocal getEquifax() {
		return equifax;
	}

	public ScoringNBKIBeanLocal getNbki(){
		return nbki;
	}
	
	public ScoringOkbCaisBeanLocal getOkbCais() {
		return okbCais;
	}

	public ScoringOkbIdvBeanLocal getOkbIdv() {
		return okbIdv;
	}

    public ScoringOkbHunterBeanLocal getOkbHunter() {
        return okbHunter;
    }

    public ScoringRsBeanLocal getRusStandart() {
		return rusStandart;
	}

	public AlfaBankMarengoBeanLocal getAlphaMarengo() {
		return alphaMarengo;
	}

	public AlphaAcquiringBeanLocal getAlphaAcq() {
		return alphaAcq;
	}

	public ContactAcquiringBeanLocal getContactAcq() {
		return contactAcq;
	}
        
    public ContactPayBeanLocal getContactPay() {
		return contactPay;
	}

	public Account1CPayBeanLocal getAccount1C() {
		return account1CPay;
	}

	public Account1CPayBeanLocal getAccount1CPay() {
		return account1CPay;
	}

	public ManualPayBeanLocal getManualPay() {
		return manualPay;
	}

	public VerificationBeanLocal getVerify() {
		return verify;
	}

    public QiwiAcquiringBeanLocal getQiwiAcquiring() {
        return qiwiAcquiring;
    }

    public YandexAcquiringBeanLocal getYandexAcquiring() {
        return yandexAcquiring;
    }

    public YandexPayBeanLocal getYandexPay() {
        return yandexPay;
    }

	public YandexIdentificationPayBeanLocal getIYandexPayBeanLocal() {
		return yandexIdentificationPay;
	}

    public WinpayAcquiringBeanLocal getWinpayAcquiring() {
        return winpayAcquiring;
    }

	public WinpayPayBeanLocal getWinpayPay() {
		return winpayPay;
	}

    public PayonlineAcquiringBeanLocal getPayonlineAcquiring() {
        return payonlineAcq;
    }

	public ScoringSkbBeanLocal getSkb() {
		return skb;
	}
   
 	public IdentityBeanLocal getIdentity() {
		return identity;
	}
    public AriusPayBeanLocal getAriusPay() {
        return ariusPay;
    }

    public AriusAcquiringBeanLocal getAriusAcq() {
        return ariusAcq;
    }
}

