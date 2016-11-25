package ru.simplgroupp.ejb;

import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

public class OkbHunterPluginConfig extends PluginConfig {
    private static final long serialVersionUID = 1L;

    protected boolean useWork = false;

    protected int numberRepeats = 3;

    protected String changePasswordUrl = "https://nh-test.rb-ei.com/EIHunterManager/passwords.asmx";

    /**
     * сколько дней в кеше
     */
    protected int cacheDays = 10;

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        useWork = Utils.booleanFromNumber(source.get(prefix + ".useWork"));
        numberRepeats = Convertor.toInteger(source.get(prefix + ".numberRepeats"));
        changePasswordUrl = Convertor.toString(source.get(prefix + ".changePasswordUrl"));
        cacheDays = Convertor.toInteger(source.get(prefix + ".cacheDays"));
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".useWork", (useWork) ? 1 : 0);
        dest.put(prefix + ".numberRepeats", numberRepeats);
        dest.put(prefix + ".changePasswordUrl", changePasswordUrl);
        dest.put(prefix + ".cacheDays", cacheDays);
        super.save(prefix, dest);
    }

    public boolean isUseWork() {
        return useWork;
    }

    public void setUseWork(boolean useWork) {
        this.useWork = useWork;
    }

    public int getNumberRepeats() {
        return numberRepeats;
    }

    public void setNumberRepeats(int numberRepeats) {
        this.numberRepeats = numberRepeats;
    }

    public String getChangePasswordUrl() {
        return changePasswordUrl;
    }

    public void setChangePasswordUrl(String changePasswordUrl) {
        this.changePasswordUrl = changePasswordUrl;
    }

    public int getCacheDays() {
        return cacheDays;
    }

    public void setCacheDays(int cacheDays) {
        this.cacheDays = cacheDays;
    }
}
