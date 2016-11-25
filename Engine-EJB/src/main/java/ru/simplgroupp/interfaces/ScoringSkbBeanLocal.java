package ru.simplgroupp.interfaces;

import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;

public interface ScoringSkbBeanLocal extends PluginSystemLocal, ScoringBeanLocal{

	public static final String SYSTEM_NAME = "skb";
	public static final String SYSTEM_DESCRIPTION = "Запросы в СКБ";
}
