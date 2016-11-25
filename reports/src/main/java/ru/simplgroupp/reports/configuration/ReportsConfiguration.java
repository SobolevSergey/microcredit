package ru.simplgroupp.reports.configuration;

import javax.annotation.Resource;

/**
 * конфигурация для отчетов. см.файл ejb-jar.xml
 * в него при компиляции подставляется динамическая переменная мавеном @path.reports@(см. pom.xml)
 * которая им берется из файла  properties,соответствующего профилю(например pom.xml)
 */
public class ReportsConfiguration {
    @Resource(name="pathToCompiledReports")
    private String pathToCompiledReports;

    public String getPathToCompiledReports() {
        return pathToCompiledReports;
    }

    public void setPathToCompiledReports(String pathToCompiledReports) {
        this.pathToCompiledReports = pathToCompiledReports;
    }
}
