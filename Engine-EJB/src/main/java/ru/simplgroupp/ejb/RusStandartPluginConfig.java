package ru.simplgroupp.ejb;

import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

public class RusStandartPluginConfig extends PluginConfig {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3434638459420761078L;
	/**
	 * рабочий сервис
	 */
	protected boolean useWork = false;
	protected String testServerURL;
	protected String workServerURL;
	/**
	 * запрос сервиса платежной истории
	 */
	protected boolean requestPayment=false;
	/**
	 * запрос действительности паспорта
	 */
	protected boolean requestFMS=false;
	/**
	 * запрос долгов по ФССП
	 */
	protected boolean requestFSSP=false;
	/**
	 * запрос долгов по ФНС
	 */
	protected boolean requestFNS=false;
	/**
	 * запрос верификации
	 */
	protected boolean requestVerify=false;
	/**
	 * запрос скоринга
	 */
	protected boolean requestScoring=false;
	/**
	 * запрос сервиса антифрод
	 */
	protected boolean requestAntifrod=false;
	/**
	 * фейковый xml
	 */
	protected String fakeXmlAnswer;
	/**
	 * дней в кеше	
	 */
	protected int cacheDays=10;
	/**
	 * детализация отчета
	 * 0- максимальная
	 * 3- пустой
	 */
	protected int creditReportDetalization=0;
	/**
	 * сколько повторов при нефатальной ошибке
	 */
	protected int numberRepeats=5;
	
	public RusStandartPluginConfig() {
		super();
	}

	public RusStandartPluginConfig(String plName) {
		super(plName);
	}

	public String getTestServerURL() {
		return testServerURL;
	}

	public void setTestServerURL(String testServerURL) {
		this.testServerURL = testServerURL;
	}

		
	public String getWorkServerURL() {
		return workServerURL;
	}

	public void setWorkServerURL(String workServerURL) {
		this.workServerURL = workServerURL;
	}

	@Override
	public void load(String prefix, Map<String, Object> source) {
		super.load(prefix, source);
		testServerURL = (String) source.get(prefix + ".testServerURL");
		workServerURL = (String) source.get(prefix + ".workServerURL");
		useWork = Utils.booleanFromNumber(source.get(prefix + ".useWork"));
		requestFMS = Utils.booleanFromNumber(source.get(prefix + ".requestFMS"));
		requestFNS = Utils.booleanFromNumber(source.get(prefix + ".requestFNS"));
		requestFSSP = Utils.booleanFromNumber(source.get(prefix + ".requestFSSP"));
		requestVerify = Utils.booleanFromNumber(source.get(prefix + ".requestVerify"));
		requestPayment = Utils.booleanFromNumber(source.get(prefix + ".requestPayment"));
		requestScoring = Utils.booleanFromNumber(source.get(prefix + ".requestScoring"));
		requestAntifrod = Utils.booleanFromNumber(source.get(prefix + ".requestAntifrod"));
		fakeXmlAnswer = (String) source.get(prefix + ".fakeXmlAnswer");
		cacheDays=Convertor.toInteger(source.get(prefix + ".cacheDays"));
		numberRepeats=Convertor.toInteger(source.get(prefix + ".numberRepeats"));
		creditReportDetalization=Convertor.toInteger(source.get(prefix + ".creditReportDetalization"));
	}

	@Override
	public void save(String prefix, Map<String, Object> dest) {
		dest.put(prefix + ".fakeXmlAnswer", fakeXmlAnswer);
		dest.put(prefix + ".testServerURL", testServerURL);
		dest.put(prefix + ".workServerURL", workServerURL);
		dest.put(prefix + ".useWork", (useWork)?1:0);
		dest.put(prefix + ".requestFMS", (requestFMS)?1:0);
		dest.put(prefix + ".requestFNS", (requestFNS)?1:0);
		dest.put(prefix + ".requestFSSP", (requestFSSP)?1:0);
		dest.put(prefix + ".requestVerify", (requestVerify)?1:0);
		dest.put(prefix + ".requestPayment", (requestPayment)?1:0);
		dest.put(prefix + ".requestScoring", (requestScoring)?1:0);
		dest.put(prefix + ".requestAntifrod", (requestAntifrod)?1:0);
		dest.put(prefix + ".cacheDays", cacheDays);
		dest.put(prefix + ".numberRepeats", numberRepeats);
		dest.put(prefix + ".creditReportDetalization", creditReportDetalization);
		super.save(prefix, dest);
	}

	public boolean isUseWork() {
		return useWork;
	}

	public void setUseWork(boolean useWork) {
		this.useWork = useWork;
	}

	public boolean getRequestFMS() {
		return requestFMS;
	}

	public void setRequestFMS(boolean requestFMS) {
		this.requestFMS = requestFMS;
	}
	
	public boolean getRequestFNS() {
		return requestFNS;
	}

	public void setRequestFNS(boolean requestFNS) {
		this.requestFNS = requestFNS;
	}
	
	public boolean getRequestFSSP() {
		return requestFSSP;
	}

	public void setRequestFSSP(boolean requestFSSP) {
		this.requestFSSP = requestFSSP;
	}
	
	public boolean getRequestVerify() {
		return requestVerify;
	}

	public void setRequestVerify(boolean requestVerify) {
		this.requestVerify = requestVerify;
	}
	
	public boolean getRequestPayment() {
		return requestPayment;
	}

	public void setRequestPayment(boolean requestPayment) {
		this.requestPayment = requestPayment;
	}

	public String getFakeXmlAnswer() {
		return fakeXmlAnswer;
	}

	public void setFakeXmlAnswer(String fakeXmlAnswer) {
		this.fakeXmlAnswer = fakeXmlAnswer;
	}

	public boolean getRequestScoring() {
		return requestScoring;
	}

	public void setRequestScoring(boolean requestScoring) {
		this.requestScoring = requestScoring;
	}

	public boolean getRequestAntifrod() {
		return requestAntifrod;
	}

	public void setRequestAntifrod(boolean requestAntifrod) {
		this.requestAntifrod = requestAntifrod;
	}

	public int getCacheDays() {
		return cacheDays;
	}

	public void setCacheDays(int cacheDays) {
		this.cacheDays = cacheDays;
	}

	public int getCreditReportDetalization() {
		return creditReportDetalization;
	}

	public void setCreditReportDetalization(int creditReportDetalization) {
		this.creditReportDetalization = creditReportDetalization;
	}

	public int getNumberRepeats() {
		return numberRepeats;
	}

	public void setNumberRepeats(int numberRepeats) {
		this.numberRepeats = numberRepeats;
	}
	
	
	
}
