package ru.simplgroupp.ejb;

import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

public class OkbCaisPluginConfig extends PluginConfig{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1833963468547553235L;
	protected boolean useWork = false;
	/**
	 * сколько дней в кеше
	 */
	protected int cacheDays=10;
	/**
	 * сколько повторов при нефатальной ошибке
	 */
	protected int numberRepeats=5;
	/**
	 * номер функции
	 */
	protected Integer numberFunction=50;
	/**
	 * номер функции
	 */
	protected Integer numberFunctionUpload=21;
	
	public OkbCaisPluginConfig(){
		super();
	}
	
	public OkbCaisPluginConfig(String plName){
		super(plName);
	}
	

	@Override
	public void load(String prefix, Map<String, Object> source) {
		super.load(prefix, source);
		useWork=Utils.booleanFromNumber(source.get(prefix + ".useWork"));
		cacheDays=Convertor.toInteger(source.get(prefix + ".cacheDays"));
		numberRepeats=Convertor.toInteger(source.get(prefix + ".numberRepeats"));
		numberFunction=Convertor.toInteger(source.get(prefix + ".numberFunction"));
		numberFunctionUpload=Convertor.toInteger(source.get(prefix + ".numberFunctionUpload"));
	}

	@Override
	public void save(String prefix, Map<String, Object> dest) {
	
		dest.put(prefix + ".useWork", (useWork)?1:0);
		dest.put(prefix + ".cacheDays", cacheDays);
		dest.put(prefix + ".numberRepeats", numberRepeats);
		dest.put(prefix + ".numberFunction", numberFunction);
		dest.put(prefix + ".numberFunctionUpload", numberFunctionUpload);
		super.save(prefix, dest);
	}

	public boolean isUseWork() {
		return useWork;
	}

	public void setUseWork(boolean useWork) {
		this.useWork = useWork;
	}

	public int getCacheDays() {
		return cacheDays;
	}

	public void setCacheDays(int cacheDays) {
		this.cacheDays = cacheDays;
	}

	public int getNumberRepeats() {
		return numberRepeats;
	}

	public void setNumberRepeats(int numberRepeats) {
		this.numberRepeats = numberRepeats;
	}

	public Integer getNumberFunction() {
		return numberFunction;
	}

	public void setNumberFunction(Integer numberFunction) {
		this.numberFunction = numberFunction;
	}

	public Integer getNumberFunctionUpload() {
		return numberFunctionUpload;
	}

	public void setNumberFunctionUpload(Integer numberFunctionUpload) {
		this.numberFunctionUpload = numberFunctionUpload;
	}
	
	
	
}
