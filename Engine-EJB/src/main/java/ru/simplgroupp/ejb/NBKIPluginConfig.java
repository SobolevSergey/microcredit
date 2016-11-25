package ru.simplgroupp.ejb;

import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
/**
 * конфиг для плагина НБКИ
 */
public class NBKIPluginConfig extends PluginConfig {
	
	private static final long serialVersionUID = 7438039490395150812L;
	private boolean useWork = false;
	/**
	 * сколько дней в кеше
	 */
	private int cacheDays=10;
	/**
	 * запрос скоринга
	 */
	protected boolean requestScoring=false;
	/**
	 * url для КО
	 */
	protected String urlCommon="products/B2BRequestServlet";
	/**
	 * url для КО со скорингом
	 */
	protected String urlScoring="score2";
	/**
	 * используем ли ssl при запросах
	 */
	protected boolean useSSL=false;
	/**
	 * сколько повторов при нефатальной ошибке
	 */
	protected int numberRepeats=5;
	
	public NBKIPluginConfig(){
		super();
	}
	
	public NBKIPluginConfig(String plName){
		super(plName);
	}
	
	@Override
	public void load(String prefix, Map<String, Object> source) {
		super.load(prefix, source);
		if( source.get(prefix + ".useWork") != null ){
		    useWork=Utils.booleanFromNumber(source.get(prefix + ".useWork"));
		}
	    if( source.get(prefix + ".cacheDays") != null ){
	    	cacheDays=Convertor.toInteger(source.get(prefix + ".cacheDays"));
	    }
	    requestScoring = Utils.booleanFromNumber(source.get(prefix + ".requestScoring"));
	    useSSL = Utils.booleanFromNumber(source.get(prefix + ".useSSL"));
	    urlCommon=(String) source.get(prefix+".urlCommon");
	    urlScoring=(String) source.get(prefix+".urlScoring");
	    numberRepeats=Convertor.toInteger(source.get(prefix + ".numberRepeats"));
	}

	@Override
	public void save(String prefix, Map<String, Object> dest) {
		dest.put(prefix + ".useWork", (useWork)?1:0);
		dest.put(prefix + ".cacheDays", cacheDays);
		dest.put(prefix + ".numberRepeats", numberRepeats);
		dest.put(prefix + ".requestScoring", (requestScoring)?1:0);
		dest.put(prefix + ".useSSL", (useSSL)?1:0);
		dest.put(prefix + ".urlCommon", urlCommon);
		dest.put(prefix + ".urlScoring", urlScoring);
		super.save(prefix, dest);
	}

	public boolean isUseWork() {
		return useWork;
	}

	public void setUseWork(boolean useWork) {
		this.useWork = useWork;
	}
	
	public int getCacheDays(){
		return cacheDays;
	}
	
	public void setCacheDays(int cacheDays){
		this.cacheDays = cacheDays;
	}
	
	public boolean getRequestScoring() {
		return requestScoring;
	}

	public void setRequestScoring(boolean requestScoring) {
		this.requestScoring = requestScoring;
	}

	public String getUrlCommon() {
		return urlCommon;
	}

	public void setUrlCommon(String urlCommon) {
		this.urlCommon = urlCommon;
	}

	public String getUrlScoring() {
		return urlScoring;
	}

	public void setUrlScoring(String urlScoring) {
		this.urlScoring = urlScoring;
	}

	public boolean getUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public int getNumberRepeats() {
		return numberRepeats;
	}

	public void setNumberRepeats(int numberRepeats) {
		this.numberRepeats = numberRepeats;
	}

	
}
