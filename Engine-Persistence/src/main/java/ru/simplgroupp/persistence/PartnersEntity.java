package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Партнеры
 */

@XmlRootElement
public class PartnersEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2087087757478755271L;

    protected Integer txVersion = 0;

    private Integer id;

    /**
     * название
     */
    private String name;

    /**
     * название для отображения
     */
    private String realname;

    /**
     * урл для тестирования
     */
    private String urltest;

    /**
     * урл для работы
     */
    private String urlwork;

    /**
     * код для тестирования
     */
    private String codetest;

    /**
     * код для работы
     */
    private String codework;

    /**
     * логин для тестов
     */
    private String logintest;

    /**
     * логин для работы
     */
    private String loginwork;

    /**
     * пароль для тестов
     */
    private String passwordtest;

    /**
     * пароль для работы
     */
    private String passwordwork;

    /**
     * ид приложения
     */
    private String applicationId;

    /**
     * тестовая группа
     */
    private String groupTest;

    /**
     * рабочая группа
     */
    private String groupWork;

    /**
     * урл для тестовой загрузки
     */
    private String urluploadtest;

    /**
     * урл для рабочей загрузки
     */
    private String urluploadwork;

    /**
     * номер счета
     */
    private String accountnumber;

    /**
     * бик
     */
    private BankEntity bik;

    /**
     * каталог для загрузки
     */
    private String uploadinbox;

    /**
     * каталог для выгрузки
     */
    private String uploadoutbox;

    /**
     * может делать платежи
     */
    private Boolean canMakePayment;

    /**
     * может создавать кредитную историю
     */
    private Boolean canMakeCreditHistory;

    /**
     * может делать скоринг
     */
    private Boolean canMakeScoring;

    /**
     * версия загрузки
     */
    private String uploadVersion;

    /**
     * версия запроса
     */
    private String requestVersion;

    /**
     * дата версии
     */
    private Date dateVersion;

    /**
     * пароль для тестов выгрузки
     */
    private String passwordUploadTest;

    /**
     * пароль для работы выгрузки
     */
    private String passwordUploadWork;
    
    public PartnersEntity() {
    }

    @XmlElement
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getUploadVersion() {
        return uploadVersion;
    }

    public void setUploadVersion(String uploadVersion) {
        this.uploadVersion = uploadVersion;
    }

    @XmlElement
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @XmlElement
    public String getUrltest() {
        return urltest;
    }

    public void setUrltest(String urltest) {
        this.urltest = urltest;
    }

    @XmlElement
    public String getUrlwork() {
        return urlwork;
    }

    public void setUrlwork(String urlwork) {
        this.urlwork = urlwork;
    }

    @XmlElement
    public String getUrluploadtest() {
        return urluploadtest;
    }

    public void setUrluploadtest(String urluploadtest) {
        this.urluploadtest = urluploadtest;
    }

    @XmlElement
    public String getUrluploadwork() {
        return urluploadwork;
    }

    public void setUrluploadwork(String urluploadwork) {
        this.urluploadwork = urluploadwork;
    }

    @XmlElement
    public String getCodetest() {
        return codetest;
    }

    public void setCodetest(String codetest) {
        this.codetest = codetest;
    }

    @XmlElement
    public String getCodework() {
        return codework;
    }

    public void setCodework(String codework) {
        this.codework = codework;
    }

    @XmlElement
    public String getLogintest() {
        return logintest;
    }

    public void setLogintest(String logintest) {
        this.logintest = logintest;
    }

    @XmlElement
    public String getLoginwork() {
        return loginwork;
    }

    public void setLoginwork(String loginwork) {
        this.loginwork = loginwork;
    }

    @XmlElement
    public String getPasswordtest() {
        return passwordtest;
    }

    public void setPasswordtest(String passwordtest) {
        this.passwordtest = passwordtest;
    }

    @XmlElement
    public String getPasswordwork() {
        return passwordwork;
    }

    public void setPasswordwork(String passwordwork) {
        this.passwordwork = passwordwork;
    }

    @XmlElement
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    @XmlElement
    public String getGroupTest() {
        return groupTest;
    }

    public void setGroupTest(String groupTest) {
        this.groupTest = groupTest;
    }

    @XmlElement
    public String getGroupWork() {
        return groupWork;
    }

    public void setGroupWork(String groupWork) {
        this.groupWork = groupWork;
    }

    @XmlElement
    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    @XmlElement
    public BankEntity getBik() {
        return bik;
    }

    public void setBik(BankEntity bik) {
        this.bik = bik;
    }

    @XmlElement
    public String getUploadinbox() {
        return uploadinbox;
    }

    public void setUploadinbox(String uploadinbox) {
        this.uploadinbox = uploadinbox;
    }

    @XmlElement
    public String getUploadoutbox() {
        return uploadoutbox;
    }

    public void setUploadoutbox(String uploadoutbox) {
        this.uploadoutbox = uploadoutbox;
    }

    @XmlElement
    public Boolean getCanMakePayment() {
        return canMakePayment;
    }

    public void setCanMakePayment(Boolean canMakePayment) {
        this.canMakePayment = canMakePayment;
    }

    @XmlElement
    public Boolean getCanMakeCreditHistory() {
        return canMakeCreditHistory;
    }

    public void setCanMakeCreditHistory(Boolean canMakeCreditHistory) {
        this.canMakeCreditHistory = canMakeCreditHistory;
    }

    @XmlElement
    public Boolean getCanMakeScoring() {
        return canMakeScoring;
    }

    public void setCanMakeScoring(Boolean canMakeScoring) {
        this.canMakeScoring = canMakeScoring;
    }

    @XmlElement
    public String getRequestVersion() {
        return requestVersion;
    }

    public void setRequestVersion(String requestVersion) {
        this.requestVersion = requestVersion;
    }

    public Date getDateVersion() {
        return dateVersion;
    }

    public void setDateVersion(Date dateVersion) {
        this.dateVersion = dateVersion;
    }

    public String getPasswordUploadTest() {
		return passwordUploadTest;
	}

	public void setPasswordUploadTest(String passwordUploadTest) {
		this.passwordUploadTest = passwordUploadTest;
	}

	public String getPasswordUploadWork() {
		return passwordUploadWork;
	}

	public void setPasswordUploadWork(String passwordUploadWork) {
		this.passwordUploadWork = passwordUploadWork;
	}

	@Override
    public String toString() {
        return this.realname;
    }

}