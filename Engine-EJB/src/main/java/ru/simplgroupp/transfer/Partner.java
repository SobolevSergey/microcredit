package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class Partner extends BaseTransfer<PartnersEntity> implements Serializable, Initializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -6145734967465872317L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Partner.class.getConstructor(PartnersEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

    public static final int SYSTEM = 1;
    public static final int CRONOS=2;
    public static final int OKB_IDV=3;
    public static final int EQUIFAX = 4;
    public static final int OKB_CAIS=5;
    public static final int CLIENT = 6;
    public static final int VKONTAKTE=7;
    public static final int ODNOKLASSNIKI=8;
    public static final int MOIMIR=9;
    public static final int FACEBOOK=10;
    public static final int TWITTER=11;
    public static final int OKB_HUNTER=12;
    public static final int RSTANDARD=13;
    public static final int UNITELLER=14;
    public static final int ALFABANK=15;
    public static final int QIWI=16;
    public static final int YANDEX=17;
    public static final int CONTACT=18;
    public static final int PAYONLINE=19;
    public static final int WINPAY=20;
    public static final int GOOGLEANALYTICS=21;
    public static final int SKB=22;
    public static final int SOCIOHUB=23;
    public static final int NBKI=24;
    public static final int ARIUS=25;
    
	protected Bank bik;
	 
	public Partner(PartnersEntity value){
		super(value);
		if (entity != null && entity.getBik() != null) {
        	bik=new Bank(entity.getBik());
        }
	}
	
	public Partner(){
		super();
		entity = new PartnersEntity();
	}

	public Partner( int id) {
	    super();
	    entity = new PartnersEntity();
	    entity.setId(id);
	}

	public Integer getId() {
        return entity.getId();
    }
    
	@XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
        entity.setName(name);
    }    
    
    public String getUploadVersion() {
        return entity.getUploadVersion();
    }

    public void setUploadVersion(String uploadVersion) {
        entity.setUploadVersion(uploadVersion);
    }    
    
    public Date getDateVersion() {
        return entity.getDateVersion();
    }

    public void setDateVersion(Date dateVersion) {
        entity.setDateVersion(dateVersion);
    }    
    
    public String getRequestVersion() {
        return entity.getRequestVersion();
    }

    public void setRequestVersion(String requestVersion) {
        entity.setRequestVersion(requestVersion);
    }    
    
    public String getRealName() {
        return entity.getRealname();
    }

    @XmlElement
    public void setRealName(String realname) {
        entity.setRealname(realname);
    }    
    
    public String getUrlTest() {
        return entity.getUrltest();
    }

    public void setUrlTest(String urltest) {
        entity.setUrltest(urltest);
    }
    
    public String getUrlWork() {
        return entity.getUrlwork();
    }

    public void setUrlWork(String urlwork) {
        entity.setUrlwork(urlwork);
    }
    
    public String getUrlUploadTest() {
        return entity.getUrluploadtest();
    }

    public void setUrlUploadTest(String urluploadtest) {
        entity.setUrluploadtest(urluploadtest);
    }
    
    public String getUrlUploadWork() {
        return entity.getUrluploadwork();
    }

    public void setUrlUploadWork(String urluploadwork) {
        entity.setUrluploadwork(urluploadwork);
    }
    
    public String getUploadInbox() {
        return entity.getUploadinbox();
    }

    public void setUploadInbox(String uploadinbox) {
        entity.setUploadinbox(uploadinbox);
    }
    
    public String getUploadOutbox() {
        return entity.getUploadoutbox();
    }

    public void setUploadOutbox(String uploadoutbox) {
        entity.setUploadoutbox(uploadoutbox);
    }
    
    public String getCodeTest() {
        return entity.getCodetest();
    }

    public void setCodeTest(String codetest) {
        entity.setCodetest(codetest);
    }
    
    public String getCodeWork() {
        return entity.getCodework();
    }

    public void setCodeWork(String codework) {
        entity.setCodework(codework);
    }
    
    public String getLoginTest() {
        return entity.getLogintest();
    }

    public void setLoginTest(String logintest) {
        entity.setLogintest(logintest);
    }
    
    public String getLoginWork() {
        return entity.getLoginwork();
    }

    public void setLoginWork(String loginwork) {
        entity.setLoginwork(loginwork);
    }
    
    public String getPasswordTest() {
        return entity.getPasswordtest();
    }

    public void setPasswordTest(String passwordtest) {
        entity.setPasswordtest(passwordtest);
    }
    
    public String getPasswordWork() {
        return entity.getPasswordwork();
    }

    public void setPasswordWork(String passwordwork) {
        entity.setPasswordwork(passwordwork);
    }
    
    public String getPasswordUploadTest() {
        return entity.getPasswordUploadTest();
    }

    public void setPasswordUploadTest(String passwordUploadTest) {
        entity.setPasswordUploadTest(passwordUploadTest);
    }
    
    public String getPasswordUploadWork() {
        return entity.getPasswordUploadWork();
    }

    public void setPasswordUploadWork(String passwordUploadWork) {
        entity.setPasswordUploadWork(passwordUploadWork);
    }
    
    public String getApplicationId() {
    	return entity.getApplicationId();
    }
    
    public void setApplicationId(String applicationId) {
    	entity.setApplicationId(applicationId);
    }
    
    public String getGroupTest() {
    	return entity.getGroupTest();
    }
    
    public void setGroupTest(String groupTest) {
    	entity.setGroupTest(groupTest);
    }
    
    public String getGroupWork() {
    	return entity.getGroupWork();
    }
    
    public void setGroupWork(String groupWork) {
    	entity.setGroupWork(groupWork);
    }
    
    public Bank getBik() {
        return bik;
    }

    public void setBik(Bank bik) {
        this.bik=bik;
    }
    
    public String getAccountNumber() {
        return entity.getAccountnumber();
    }

    public void setAccountNumber(String accountnumber) {
        entity.setAccountnumber(accountnumber);
    }
    
    public Boolean getCanMakePayment(){
    	return entity.getCanMakePayment();
    }
    
    public void setCanMakePayment(Boolean canMakePayment){
    	entity.setCanMakePayment(canMakePayment);
    }
    
    public Boolean getCanMakeCreditHistory(){
    	return entity.getCanMakeCreditHistory();
    }
    
    public void setCanMakeCreditHistory(Boolean canMakeCreditHistory){
    	entity.setCanMakeCreditHistory(canMakeCreditHistory);
    }
    
    public Boolean getCanMakeScoring(){
    	return entity.getCanMakeScoring();
    }
    
    public void setCanMakeScoring(Boolean canMakeScoring){
    	entity.setCanMakeScoring(canMakeScoring);
    }
    
	@Override
	public void init(Set options) {
	    entity.getName();
	}

}