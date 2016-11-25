package ru.simplgroupp.ejb;

import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

public class SkbPluginConfig extends PluginConfig{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3529442522921192453L;
	protected boolean useWork = true;
	/**
	 * сколько дней в кеше
	 */
	protected int cacheDays=10;
	/**
	 * сколько повторов при нефатальной ошибке
	 */
	protected int numberRepeats=5;
	
	public SkbPluginConfig(){
		super();
	}
	
	public SkbPluginConfig(String plName){
		super(plName);
	}
	
	@Override
	public void load(String prefix, Map<String, Object> source) {
		super.load(prefix, source);
	    useWork=Utils.booleanFromNumber(source.get(prefix + ".useWork"));
	    cacheDays=Convertor.toInteger(source.get(prefix + ".cacheDays"));
	    numberRepeats=Convertor.toInteger(source.get(prefix + ".numberRepeats"));
	}

	@Override
	public void save(String prefix, Map<String, Object> dest) {
		dest.put(prefix + ".cacheDays", cacheDays);
		dest.put(prefix + ".useWork", (useWork)?1:0);
		dest.put(prefix + ".numberRepeats", numberRepeats);
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
	
	
	
}
