package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

import java.util.Map;

/**
 * @author sniffl
 */
public class ContactPayPluginConfig extends PluginConfig {

    private static final long serialVersionUID = -292730731665980800L;

    protected boolean useWork = true;
    protected String PPcode = "AFRM";
    protected String testUrl = "https://enter.contact-sys.com:2221/wstrans/wsTrans.exe/soap/ITransmitter";
    protected String workUrl = "https://contactws.contact-sys.com/trans/wsTrans.exe/soap/ITransmitter";
    protected String keyStoreAlias;
    protected String keyStorePassword;
    protected String keyStoreName;
    protected String debetAccount;

    protected String senderFakeDocName;
    protected String senderFakeName;
    protected String senderFakeLastName;
    protected String senderFakeSurName;
    protected String senderFakeZipCode;
    protected String senderFakeRegion;
    protected String senderFakeCity;
    protected String senderFakeAddress;
    protected String senderFakeDocSerNum;
    protected String senderFakeDocdate;
    protected String senderFakeDocWhom;
    protected String senderFakeDocExpDate;
    protected String senderFakeBirthday;
    protected long intervalWaitClient = 3*24*60*60*1000; // по умолчанию 3 дня

    public ContactPayPluginConfig() {
        super();
    }

    public ContactPayPluginConfig(String plName) {
        super(plName);
    }

    public boolean isUseWork() {
        return useWork;
    }

    public void setUseWork(boolean useWork) {
        this.useWork = useWork;
    }

    public String getPPcode() {
        return PPcode;
    }

    public void setPPcode(String PPcode) {
        this.PPcode = PPcode;
    }

    public String getTestUrl() {
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSenderFakeDocName() {
        return senderFakeDocName;
    }

    public void setSenderFakeDocName(String senderFakeDocName) {
        this.senderFakeDocName = senderFakeDocName;
    }

    public String getSenderFakeName() {
        return senderFakeName;
    }

    public void setSenderFakeName(String senderFakeName) {
        this.senderFakeName = senderFakeName;
    }

    public String getSenderFakeLastName() {
        return senderFakeLastName;
    }

    public void setSenderFakeLastName(String senderFakeLastName) {
        this.senderFakeLastName = senderFakeLastName;
    }

    public String getSenderFakeSurName() {
        return senderFakeSurName;
    }

    public void setSenderFakeSurName(String senderFakeSurName) {
        this.senderFakeSurName = senderFakeSurName;
    }

    public String getSenderFakeZipCode() {
        return senderFakeZipCode;
    }

    public void setSenderFakeZipCode(String senderFakeZipCode) {
        this.senderFakeZipCode = senderFakeZipCode;
    }

    public String getSenderFakeRegion() {
        return senderFakeRegion;
    }

    public void setSenderFakeRegion(String senderFakeRegion) {
        this.senderFakeRegion = senderFakeRegion;
    }

    public String getSenderFakeCity() {
        return senderFakeCity;
    }

    public void setSenderFakeCity(String senderFakeCity) {
        this.senderFakeCity = senderFakeCity;
    }

    public String getSenderFakeAddress() {
        return senderFakeAddress;
    }

    public void setSenderFakeAddress(String senderFakeAddress) {
        this.senderFakeAddress = senderFakeAddress;
    }

    public String getSenderFakeDocSerNum() {
        return senderFakeDocSerNum;
    }

    public void setSenderFakeDocSerNum(String senderFakeDocSerNum) {
        this.senderFakeDocSerNum = senderFakeDocSerNum;
    }

    public String getSenderFakeDocdate() {
        return senderFakeDocdate;
    }

    public void setSenderFakeDocdate(String senderFakeDocdate) {
        this.senderFakeDocdate = senderFakeDocdate;
    }

    public String getSenderFakeDocWhom() {
        return senderFakeDocWhom;
    }

    public void setSenderFakeDocWhom(String senderFakeDocWhom) {
        this.senderFakeDocWhom = senderFakeDocWhom;
    }

    public String getSenderFakeDocExpDate() {
        return senderFakeDocExpDate;
    }

    public void setSenderFakeDocExpDate(String senderFakeDocExpDate) {
        this.senderFakeDocExpDate = senderFakeDocExpDate;
    }

    public String getSenderFakeBirthday() {
        return senderFakeBirthday;
    }

    public void setSenderFakeBirthday(String senderFakeBirthday) {
        this.senderFakeBirthday = senderFakeBirthday;
    }

    public String getKeyStoreAlias() {
        return keyStoreAlias;
    }

    public void setKeyStoreAlias(String keyStoreAlias) {
        this.keyStoreAlias = keyStoreAlias;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyStoreName() {
        return keyStoreName;
    }

    public void setKeyStoreName(String keyStoreName) {
        this.keyStoreName = keyStoreName;
    }

    public String getDebetAccount() {
        return debetAccount;
    }

    public void setDebetAccount(String debetAccount) {
        this.debetAccount = debetAccount;
    }
    
	@Override
	public void load(String prefix, Map<String, Object> source) {
		super.load(prefix, source);
		
	}

	@Override
	public void loadExtended(String prefix, Map<String, Object> source) {
		super.loadExtended(prefix, source);
		
		useWork=Utils.booleanFromNumber(source.get(prefix + ".useWork"));
        PPcode = (String) source.get(prefix + ".PPcode");
        testUrl = (String) source.get(prefix + ".testUrl");
        workUrl = (String) source.get(prefix + ".workUrl");


        senderFakeDocName = (String) source.get(prefix + ".senderFakeDocName");
        senderFakeName = (String) source.get(prefix + ".senderFakeName");
        senderFakeLastName = (String) source.get(prefix + ".senderFakeLastName");
        senderFakeSurName = (String) source.get(prefix + ".senderFakeSurName");
        senderFakeZipCode = (String) source.get(prefix + ".senderFakeZipCode");
        senderFakeRegion = (String) source.get(prefix + ".senderFakeRegion");

        senderFakeCity = (String) source.get(prefix + ".senderFakeCity");
        senderFakeAddress = (String) source.get(prefix + ".senderFakeAddress");
        senderFakeDocSerNum = (String) source.get(prefix + ".senderFakeDocSerNum");
        senderFakeDocdate = (String) source.get(prefix + ".senderFakeDocdate");
        senderFakeDocWhom = (String) source.get(prefix + ".senderFakeDocWhom");
        senderFakeDocExpDate = (String) source.get(prefix + ".senderFakeDocExpDate");
        senderFakeBirthday = (String) source.get(prefix + ".senderFakeBirthday");
        keyStorePassword = (String) source.get(prefix + ".keyStorePassword");
        keyStoreAlias = (String) source.get(prefix + ".keyStoreAlias");
        keyStoreName = (String) source.get(prefix + ".keyStoreName");
        debetAccount = (String) source.get(prefix + ".debetAccount");	
        Number nn = Utils.safeNumber(source.get(prefix + ".intervalWaitClient"));
        if (nn != null) {
        	intervalWaitClient = nn.longValue();
        }
	}

	@Override
	public void saveExtended(String prefix, Map<String, Object> dest) {
		
        dest.put(prefix + ".useWork", (useWork) ? 1 : 0);
        dest.put(prefix + ".PPcode", PPcode);
        dest.put(prefix + ".testUrl", testUrl);
        dest.put(prefix + ".workUrl", workUrl);

        dest.put(prefix + ".senderFakeDocName", senderFakeDocName);
        dest.put(prefix + ".senderFakeName", senderFakeName);
        dest.put(prefix + ".senderFakeLastName", senderFakeLastName);
        dest.put(prefix + ".senderFakeSurName", senderFakeSurName);
        dest.put(prefix + ".senderFakeZipCode", senderFakeZipCode);
        dest.put(prefix + ".senderFakeRegion", senderFakeRegion);
        dest.put(prefix + ".senderFakeCity", senderFakeCity);
        dest.put(prefix + ".senderFakeAddress", senderFakeAddress);
        dest.put(prefix + ".senderFakeDocSerNum", senderFakeDocSerNum);
        dest.put(prefix + ".senderFakeDocdate", senderFakeDocdate);
        dest.put(prefix + ".senderFakeDocWhom", senderFakeDocWhom);
        dest.put(prefix + ".senderFakeDocExpDate", senderFakeDocExpDate);
        dest.put(prefix + ".senderFakeBirthday", senderFakeBirthday);
        dest.put(prefix + ".keyStorePassword", keyStorePassword);
        dest.put(prefix + ".keyStoreAlias", keyStoreAlias);
        dest.put(prefix + ".keyStoreName", keyStoreName);
        dest.put(prefix + ".debetAccount", debetAccount);
        dest.put(prefix + ".intervalWaitClient", new Long(intervalWaitClient));

		super.saveExtended(prefix, dest);
		
	}

	public long getIntervalWaitClient() {
		return intervalWaitClient;
	}

	public void setIntervalWaitClient(long intervalWaitClient) {
		this.intervalWaitClient = intervalWaitClient;
	}


}
