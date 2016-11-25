package ru.simplgroupp.interfaces;

import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;

public interface ScoringSociohubBeanLocal extends PluginSystemLocal, ScoringBeanLocal {

  public static final String SYSTEM_NAME = "sociohub";
  public static final String SYSTEM_DESCRIPTION = "Запросы к сервису Sociohub";

}
